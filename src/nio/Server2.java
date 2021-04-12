package nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 自己用java实现了一个多路复用
 * 存在的弊端
 * 1.SocketChannel accept = serverSocketChannel.accept();一直站着资源
 * 如果一直没有请求进来， while (true)则一直循环，浪费资源
 * 2.：10000个人打开了百度，只有10个人在活动，
 *  for (int i = 0; i < socketChannels.size(); i++)
 *  循环10000次，只有10个有用请求要交互，浪费资源
 */
public class Server2 {
    public static void main(String[] args) {
        //用来存储SocketChannel对象
        List<SocketChannel> socketChannels = new ArrayList<>();
        try {
            //创建ServerSocketChannel通道，绑定监听端口为8585
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8585));
            //设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //循环监听
            while (true){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //开始监听
                System.out.println("开始监听.....");
                //在非阻塞模式下，可以立即返回null
                SocketChannel accept = serverSocketChannel.accept();
                System.out.println("accept:"+(accept));
                if (accept!=null){
                    System.out.println("接受到请求...");
                    accept.configureBlocking(false);
                    //把所有的请求放在一个集合中
                    socketChannels.add(accept);
                    System.out.println("socketChannels:size"+socketChannels.size());
                }
                //每次遍历所有的socketChannels，看看能不能读取数据
                for (int i = 0; i < socketChannels.size(); i++) {
                    //和socket是一样
                    SocketChannel sc = socketChannels.get(i);
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    try {
                        //读到多少字节 可以知道是否是阻塞状态
                        int read = sc.read(buffer);
                        if(read>0){
                            buffer.flip();
                            String str = new String(buffer.array(), 0, buffer.limit());
                            System.out.println("监听到的数据为"+str);
                            //但是如果集合中的事件特别难，该模式不适用
                        }
                    }catch (Exception e){
                        System.out.println(e.getLocalizedMessage());
                        socketChannels.remove(sc);
                    }

                }
                System.out.println("OVER");
            }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
