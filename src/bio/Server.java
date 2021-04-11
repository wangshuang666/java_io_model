package bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * bio
 */
public class Server {
    private static int port = 8588;

    public static void main(String[] args) {
        ServerSocket serverSocket =null;
        try {
            serverSocket=new ServerSocket(port);
            System.out.println("服务端启动，端口为：".concat(String.valueOf(port)));
            while (true){
                System.out.println("开始阻塞，等待客户端连接");
                //阻塞 等待客户端连接
                Socket socket = serverSocket.accept();
                System.out.println("接收到一个客户端连接请求");
                InputStream in = socket.getInputStream();
                System.out.println("开始读取客户端交互内容");
                int len = -1;
                byte[] buff = new byte[1024];
                //阻塞 等待客户端发送数据 ，为啥是阻塞的 读取文件流的时候好像不是阻塞的呢
                while ((len=in.read(buff))!=-1){
                    //todo 为啥还多了个1：
                    String str = new String(buff,0,len);
                    System.out.println("读取到客户端输入的内容：".concat(str));
                }
                in.close();
                socket.close();

            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
