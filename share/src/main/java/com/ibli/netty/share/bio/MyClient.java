package com.ibli.netty.share.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author gaolei
 * @Date 2022/4/8 下午8:49
 * @Version 1.0
 */
public class MyClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 9000));
            for (int i = 0; i < 10; i++) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write("请求连接...".getBytes());
                outputStream.flush();

                InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024];
                inputStream.read(buffer, 0, inputStream.read());
                System.out.println(new String(buffer));

                Thread.sleep(1000);
            }
    }
}
