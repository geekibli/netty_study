package com.ibli.netty.share.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @Author gaolei
 * @Date 2022/4/22 下午7:53
 * @Version 1.0
 */
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9090));

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            System.out.println(socketChannel.finishConnect());
            String nextLine = scanner.nextLine();
            byte[] bytes = nextLine.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.clear();
            buffer.put(ByteBuffer.wrap(bytes));
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
        }


    }
}
