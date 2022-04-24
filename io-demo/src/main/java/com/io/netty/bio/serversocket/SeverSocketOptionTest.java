package com.io.netty.bio.serversocket;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

/**
 * Created by zhaozhou on 2018/9/28.
 */
public class SeverSocketOptionTest {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.setSoTimeout(1000);
            SocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
            serverSocket.bind(address);
            serverSocket.accept();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
