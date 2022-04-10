package com.ibli.netty.share.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author gaolei
 * @Date 2022/4/8 下午8:19
 * @Version 1.0
 */
public class BioClient {


    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 5154);
        OutputStream os = socket.getOutputStream();
        for (int i = 0; i < 10; i++) {

            Thread.sleep(1000);
            os.write(("服务我来链接你了..." + i + "").getBytes());
            os.flush();

            InputStream is = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len = is.read(bytes);

            // 打印
            System.out.println("服务器端：" + new String(bytes, 0, len));
        }


    }
}
