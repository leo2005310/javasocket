import org.json.simple.JSONObject;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            System.out.println("服务器开始运行");
            ServerSocket serverSocket = new ServerSocket(9999);
            while(true){
                Socket socket = serverSocket.accept();
                new Thread(new Server_listen(socket)).start();
                new Thread(new Server_send(socket)).start();
            }
        }catch (Exception e){
            e.printStackTrace();


        }

    }
}
class Server_listen implements Runnable{
    private Socket socket;
    Server_listen(Socket socket){
        this.socket = socket;
    }
    @Override//下列方法从父类继承
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while(true){
                System.out.println(ois.readObject());

            }

        }catch (Exception e){
            e.printStackTrace();


        }finally {
            try{socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }

}
class Server_send implements Runnable{
    private Socket socket;
    Server_send(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run(){
        try {ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.print("请输入你想发送的信息");
                String string = scanner.nextLine();//通过string变量读取输入的信息
                JSONObject object = new JSONObject();
                object.put("message", "chat");
                object.put("msg",string);
                oos.writeObject(object);
                oos.flush();//刷新缓冲区
            }


        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
