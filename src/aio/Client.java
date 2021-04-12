package aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;

/**
 *
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入客户端编号");
        int number = scanner.nextInt();
        AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open();
        asynchronousSocketChannel.connect(new InetSocketAddress("127.0.0.1",8585));
        ByteBuffer byteBuffer= ByteBuffer.allocate(1024);
        String str = "";
        while (true){
            System.out.println("客户端编号"+number+"请输入要发送的内容");
            str= scanner.next();
            //客户端输入的内容为什么会带着客户端编号，是因为这里(number + str)
            byte[] bytes = (number + str).getBytes();
            byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            asynchronousSocketChannel.write(byteBuffer);


        }

    }
}
