package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入客户端编号：");
        int no = scanner.nextInt();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",8585));
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String str = "";
        while (true){
            if(!socketChannel.finishConnect()){
                Thread.sleep(100);
                continue;
            }
            System.out.println("客户端"+no+"请输入要发送的内容");
            str = scanner.next();
            if(str.equalsIgnoreCase("quit")){
                break;
            }
            byte[] bytes = (no + ":" + str).getBytes();
             buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            socketChannel.write(buffer);
        }
    }
}
