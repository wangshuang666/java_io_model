package bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static int port = 8588;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入客户端编号：");
        String number = scanner.next();
        Socket socket = null;
        try{
            System.out.println("客户端".concat(number).concat("开始连接服务"));
            socket=new Socket("127.0.0.1",port);
            System.out.println("客户端".concat(number).concat("连接成功"));
            OutputStream outputStream = socket.getOutputStream();
            while (true){
                System.out.println("客户端".concat(number).concat("请输入相对服务端说的话(quit表示退出)"));
                String str = scanner.next();
                if(str.equalsIgnoreCase("quit")){
                    break;
                }
                //什么带着客户端编号：，因为这里
                outputStream.write(number.concat(":").concat(str).getBytes());
            }
            System.out.println("客户端".concat(number).concat("连接中断"));
            outputStream.close();


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
