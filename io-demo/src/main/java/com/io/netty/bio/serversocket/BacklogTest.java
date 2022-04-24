package com.io.netty.bio.serversocket;

import com.zhaozhou.demo.bio.socket.EchoServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaozhou on 2018/9/28.
 */
public class BacklogTest {


}


class Client {
    public static void main(String[] args) {
        int cnt = 0;
        List<Socket> sockets = new LinkedList<Socket>();
        while (cnt++ < 10) {
            try {
                Socket socket = new Socket("", 9999);
                System.out.println("connect server success, cnt=" + cnt);
                sockets.add(socket);
            } catch (Exception e) {
                System.out.println("connect server fail, cnt=" + cnt);
                e.getMessage();
            }
        }

        for (Socket s : sockets) {
            try {
                if (s.isConnected()) {
                    s.close();
                }
            } catch (Exception e) {
                e.getMessage();
            }

        }
    }
}


class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999, 2);
            System.out.println("server listen on port:" + 9999);
            Thread.sleep(20000);
            while (true) {
                try {
                    Socket client = serverSocket.accept();
                    System.out.println("receive client connect, localPort=" + client.getPort());
                    new Thread(new EchoServer.HandlerServer(client)).start();
                } catch (Exception e) {
                    System.out.println("client exception,e=" + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("server exception,e=" + e.getMessage());
        }
    }
}
