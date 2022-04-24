package com.io.netty.bio.serversocket;


import com.io.netty.bio.socket.EchoServer;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class BacklogTest {


}


class Client {
    public static void main(String[] args) {
        int cnt = 0;
        List<Socket> sockets = new LinkedList<Socket>();
        while (cnt++ < 10) {
            try {
                Socket socket = new Socket("localhost", 9090);
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
                    OutputStream outputStream = s.getOutputStream();
                    outputStream.write((s.getLocalPort() + "链接server").getBytes());
                    outputStream.flush();
                    //s.close();
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
            ServerSocket serverSocket = new ServerSocket(9090, 2);
            System.out.println("server listen on port:" + 9090);
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
