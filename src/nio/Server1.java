package nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 为什么会阻塞？线程在等待资源，等待客户端输入数据 cpu资源就释放出来了
 * 怎么让线程不阻塞，ServerSocketChannel ServerSocket通道，可读可写
 * 读不到数据怎么办？？？？？？？？？？？？
 */
public class Server1 {
    public static void main(String[] args) {
        try {
            //创建ServerSocketChannel通道，绑定监听端口为8585
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8585));
            //设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //循环监听
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //开始监听
                System.out.println("开始监听.....");
                //在非阻塞模式下，可以立即返回null
                SocketChannel socketChannel = serverSocketChannel.accept();
                System.out.println("socketChannel:" + (socketChannel));
                if (socketChannel != null) {
                    System.out.println("接受到请求...");
                    socketChannel.configureBlocking(false);
                    //非阻塞，一旦read不到数据，直接就进入下一次循环，
                    // SocketChannel socketChannel = serverSocketChannel.accept();
                    //又产生了一个新的socketChannel，原来的socketChannel就找不到了
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len = socketChannel.read(byteBuffer);
                    System.out.println(len);

                }

                System.out.println("OVER");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
