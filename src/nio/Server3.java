package nio;


import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

/**
 * 让轮训器去阻塞，轮训器判断是否有请求时间发生，是否有读数据的事件发生，是否有写数据的事件发生
 * 如果都没有，轮训器就阻塞等待，让出cpu资源
 * 任何一个事件发生（请求，读，写），轮训器就被打通，这时主线程开始处理事件
 * 存在的问题，一个cpu8核如何合理利用
 */
public class Server3 {
    public static void main(String[] args) {
        try {
            //创建ServerSocketChannel通道，绑定监听端口为8585
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8585));
            //设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //创建一个多路复用器
            Selector selector = Selector.open();
            //注册选择器，设置选择器的操作类型,将serverSocketChannel注册到selector中，监听的事件是ACCEPT
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //循环监听
            while (true){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("开始监听.....");
                //选择事件这里会阻塞，如果没有任何事件处理该方法处理阻塞状态
                int select = selector.select();
                //取出所有可以处理的事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    System.out.println("处理事件长度为"+selectionKeys.size());
                    SelectionKey next = iterator.next();
                    //删除key防止重复处理
                    iterator.remove();
                    if(next.isReadable()){
                        System.out.println("读请求事件");
                        SocketChannel channel =(SocketChannel) next.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int read = channel.read(byteBuffer);
                        System.out.println(new String(byteBuffer.array(),0,read));
                    }else if(next.isAcceptable()){
                        System.out.println("监听事件");
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    }else if(next.isWritable()){
                        System.out.println("写数据事件");
                    }
                }

                System.out.println("OVER");
            }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
