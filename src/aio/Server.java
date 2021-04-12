package aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 异步阻塞
 * 除第一个连接外，其他的会阻塞
 */
public class Server {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
        asynchronousServerSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8585));
        while (true) {
            System.out.println("开始监听");
            Future<AsynchronousSocketChannel> future = asynchronousServerSocketChannel.accept();
            //阻塞
            AsynchronousSocketChannel asynchronousSocketChannel = future.get();
            System.out.println("接收到请求。。。");
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            while (asynchronousServerSocketChannel.isOpen()) {
                //阻塞
                Future<Integer> read = asynchronousSocketChannel.read(allocate);
                Integer len = read.get();
                if (len > 0) {
                    System.out.println("读取到数据" + new String(allocate.array(), 0, len));
                    allocate.clear();
                }
            }
        }
    }
}
