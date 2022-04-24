package com.io.netty.bio.serversocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author gaolei
 * @Date 2022/4/24 下午2:08
 * @Version 1.0
 */
public class SimpleClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 9090));

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            OutputStream outputStream = socket.getOutputStream();
            String data = scanner.nextLine();
            outputStream.write(data.getBytes());
            outputStream.flush();


            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            inputStream.read(bytes, 0, 1024);
            System.out.println("客户端收到消息 " + new String(bytes));
        }

    }
}
