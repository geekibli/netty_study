package com.io.netty.bio.serversocket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhaozhou on 2018/9/28.
 */
public class EchoServerWithJdkPool {
    private final static int PORT = 9999;
    private final static int THREAD_IN_EACH_PROCESSOR = 2;


    public static void main(String[] args) {
        ExecutorService executor = null;
        try {
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * THREAD_IN_EACH_PROCESSOR);
            ServerSocket server = new ServerSocket(PORT, 128);
            System.out.println("server listen on port:" + PORT);
            while (true) {
                Socket client = server.accept();
                System.out.println("receive client connect, localPort=" + client.getLocalPort());
                ClientHandler handler = new ClientHandler(client);
                executor.submit(handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * 客户端读写处理任务
     */
    private static class ClientHandler implements Runnable {

        private Socket client;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            byte[] buf = new byte[1024];
            try {
                while (true) {
                    int cnt = this.client.getInputStream().read(buf, 0, 1023);
                    if (cnt > 0) {
                        System.out.println("receive msg from client:" + new String(buf));
                        this.client.getOutputStream().write(buf, 0, cnt);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!this.client.isClosed()) {
                    try {
                        this.client.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
