package com.ibli.netty.share.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author gaolei
 * @Date 2022/4/8 下午8:45
 * @Version 1.0
 */
public class MyServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(9000));
        Socket accept = serverSocket.accept();
        while (true) {

            InputStream inputStream = accept.getInputStream();

            byte[] buf = new byte[1024];

            inputStream.read(buf, 0, inputStream.read());

            String inputStr = new String(buf);
            System.out.println("收到客户端信息：" + inputStr);

            OutputStream outputStream = accept.getOutputStream();
            String outStr = "我是服务端，欢迎你！";
            outputStream.write(outStr.getBytes());
            outputStream.flush();
        }

    }
}
