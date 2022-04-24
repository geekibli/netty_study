package com.io.netty.bio.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by zhaozhou on 2018/9/28.
 */
public class ConstructorTest {


    /**
     * Socket构造方法不带参数的连接
     *
     * @return
     * @throws IOException
     */
    public static Socket openSocket1() throws IOException {
        System.out.println("test1");
        Socket socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 9999);
        socket.connect(socketAddress, 10000);
        return socket;
    }


    /**
     * 设置host和port的连接测试
     *
     * @param host
     * @param port
     * @return
     * @throws IOException
     */
    public static Socket openSocket2(String host, int port) throws IOException {
        //Socket socket = new Socket(host, port);
        System.out.println("test2");
        Socket socket = new Socket(InetAddress.getByName(host), port);
        return socket;
    }


    /**
     * 设置服务器host和port，设置本地port进行连接
     *
     * @param host
     * @param port
     * @param localPort
     * @return
     * @throws IOException
     */
    public static Socket openSocket3(String host, int port, int localPort) throws IOException {
        System.out.println("test3");
        Socket socket = new Socket(host, port, InetAddress.getByName("127.0.0.1"), 12345);
        return socket;
    }


    /**
     * socket读写测试
     *
     * @param socket
     * @throws IOException
     */
    public static void socketRWTest(Socket socket, String msg) throws IOException {
        socket.getOutputStream().write(msg.getBytes());
        byte[] buf = new byte[1000];
        int cnt = socket.getInputStream().read(buf, 0, 100);
        if (cnt > 0) {
            System.out.println("receive server msg: " + new String(buf));
        } else {
            System.out.println("receive server nothing ");
        }
        socket.close();
    }


    /**
     * Socket不带构造函数的测试
     */
    public static void socketTest1() {
        try {
            Socket socket = ConstructorTest.openSocket1();
            String msg = "Contructor socket() test!";
            ConstructorTest.socketRWTest(socket, msg);
        } catch (Exception e) {
            System.out.println("socket exception, e=" + e.getMessage());
        }
    }

    /**
     * 设置服务器host和端口测试
     */
    public static void socketTest2() {
        try {
            Socket socket = ConstructorTest.openSocket2("127.0.0.1", 9999);
            String msg = "Contructor Socket(String host, int port)  test!";
            ConstructorTest.socketRWTest(socket, msg);
        } catch (Exception e) {
            System.out.println("socket exception, e=" + e.getMessage());
        }
    }


    /**
     * 设置服务器host和端口及本地端口测试
     */
    public static void socketTest3() {
        try {
            Socket socket = ConstructorTest.openSocket3("127.0.0.1", 9999, 7777);
            String msg = "Contructor Socket(InetAddress address, int port, InetAddress localAddr, int localPort) test!";
            ConstructorTest.socketRWTest(socket, msg);
        } catch (Exception e) {
            System.out.println("socket exception, e=" + e.getMessage());
        }
    }


    public static void main(String[] args) {
        ConstructorTest.socketTest1();
        ConstructorTest.socketTest2();
        ConstructorTest.socketTest3();
    }
}
