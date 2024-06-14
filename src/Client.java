import org.json.simple.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static boolean connects_state=false;//初始值为负值，在主函数中调用connect()函数进行连接
    private static ObjectOutputStream oos;
    public static void main(String[] args) {
        while(!connects_state) {
            connect();
            try{
                Thread.sleep(3000);
            } catch (Exception e){
                e.printStackTrace();

            }        }


    }
    private static void connect() {
         try {
             socket = new Socket("127.0.0.1", 9999);
             connects_state=true;
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             new Thread(new Client_listen(socket,ois)).start();
             new Thread(new Client_send(socket,oos)).start();
             new Thread(new Client_heart(socket,oos)).start();//线程不一定能成功启动，故使用try

         }catch (Exception e) {
             e.printStackTrace();
             connects_state=false;
         }
    }
    public static void reconnect(){
        while(!connects_state) {
            System.out.println("正在尝试重新连接");
            connect();
        }
        try {
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }



    }


}
class Client_listen implements Runnable{
    private Socket socket;
    private ObjectInputStream ois;
    Client_listen(Socket socket, ObjectInputStream ois) {//开启客户端监听
        this.socket = socket;
        this.ois = ois;
    }//初始化传入数据
    @Override
    public void run() {
        try {
            while(true){
                System.out.println(ois.readObject());//不断读取接收到的信息
            }

        }catch (Exception e) {
            e.printStackTrace();
        }



    }

}
class Client_send implements Runnable{
    private Socket socket;
    private ObjectOutputStream oos;
    Client_send(Socket socket,ObjectOutputStream oos) {
        this.socket = socket;
        this.oos = oos;
    }
    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.println("请输入你要发送的信息");
                String string = scanner.nextLine();
                JSONObject object = new JSONObject();
                object.put("message", "chat");
                object.put("msg",string);
                oos.writeObject(object);
                oos.flush();//刷新

            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class Client_heart implements Runnable{
    private Socket socket;
    private ObjectOutputStream oos;
    Client_heart(Socket socket,ObjectOutputStream oos) {
        this.socket = socket;
        this.oos = oos;

    }
    @Override
    public void run() {
        try {
            System.out.println("心跳包线程已启动.......");

            while(true){
                Thread.sleep(5000);
                JSONObject object = new JSONObject();
                object.put("message", "heart");
                object.put("msg","心跳包");
                oos.writeObject(object);
                oos.flush();//刷新

            }

        }catch (Exception e) {
            e.printStackTrace();
        }


    }
}