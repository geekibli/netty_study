package com.ibli.netty.share.bio;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author gaolei
 * @Date 2022/4/8 下午8:09
 * @Version 1.0
 */
public class BioServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5154);
        while (true) {
            final Socket accept = serverSocket.accept();    // 阻塞, 系统调用accept函数
            System.out.println("============成功连接了============" + accept.getInetAddress());
            new Thread(new Runnable() {
                public void run() {
                    try {

                        while (true) {
                            // 连接成功之后，读取客户端传来的网络IO对象，输入流
                            InputStream is = accept.getInputStream();

                            // 读取数据流，打印数据
                            // 定义一个缓存数组
                            byte[] bytes = new byte[1024];
                            int len = 0;
                            // 系统调用
                            while (-1 != (len = is.read(bytes))) {   // 阻塞--一直读取流通道，获取流数据，直到发现结束符才会跳出（File文件操作不会堵塞，因为读到最后会遇到EOF的）
                                // 打印客户端发送过来的信息
                                System.out.println("客户端:" + new String(bytes, 0, len));
                                // 设置跳出条件，len长度小于bytes长度,这样就没办法持续读取流通道数据了，因为跳出了
                                if (len < bytes.length) {
                                    break;
                                }
                            }
                            // 向客户端发送信息
                            OutputStream os = accept.getOutputStream();

                            os.write("我收到你的信息了".getBytes());
                        }


                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }).start();
        }
    }


}
