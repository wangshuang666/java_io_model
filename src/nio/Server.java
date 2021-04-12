package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 因为
 * SocketChannel accept = serverSocketChannel.accept();
 * 如果没有客户端请求链接，就返回null，所以程序一下子就over了
 * 怎么办呢？循环监听
 *
 */
public class Server {
    public static void main(String[] args) {
        try {
            //创建ServerSocketChannel通道，绑定监听端口为8585
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8585));
            //设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //开始监听
            System.out.println("开始监听.....");
            SocketChannel accept = serverSocketChannel.accept();
            //如果没有人链接，就返回null
            System.out.println("accept:"+(accept));
            if (accept!=null){
                System.out.println("接受到请求...");
                accept.configureBlocking(false);
                //读取数据？
            }
            System.out.println("OVER");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
