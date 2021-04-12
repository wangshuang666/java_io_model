package aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 异步非阻塞
 */
public class Server1 {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        final AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
        asynchronousServerSocketChannel.bind(new InetSocketAddress("127.0.0.1",8585));

            System.out.println("开始监听");
           asynchronousServerSocketChannel.accept(null,
                    new CompletionHandler<AsynchronousSocketChannel, Void>() {
                        @Override
                        public void completed(AsynchronousSocketChannel result, Void attachment) {
                            //继续监听
                            asynchronousServerSocketChannel.accept(null,this);
                            //接收到新的客户端连接时调用，result就是和客户端的连接对话，此时通过result和客户端进行通信
                            System.out.println("accept completed");
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            while (result.isOpen()){
                                Future<Integer> read = result.read(byteBuffer);
                                try {
                                    Integer len = read.get();
                                    if(len>0){
                                        try {
                                            System.out.println(new String(byteBuffer.array(),0,len,"utf-8"));
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        byteBuffer.clear();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void failed(Throwable exc, Void attachment) {
                            System.out.println("accept失败时回调");
                        }
                    });
           //让程序阻塞，也说明了是异步的
           System.in.read();

    }
}
