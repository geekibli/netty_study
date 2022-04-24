package com.io.netty.bio.socket;

import java.io.IOException;
import java.net.*;


public class ExceptionTest {

    public static void UnknownHostExceptionTest() {
        try {
            Socket socket = new Socket("localhost11", 9090);
        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException: e=" + e.getMessage());
        } catch (IOException e) {
            System.out.println("exception: e=" + e.getMessage());
        }
    }

    public static void ConnectExceptionTest() {
        try {
            Socket socket = new Socket("localhost", 8888);
        } catch (ConnectException e) {
            System.out.println("ConnectException: e=" + e.getMessage());
        } catch (IOException e) {
            System.out.println("exception: e=" + e.getMessage());
        }
    }


    public static void SocketTimeoutExceptionTest() {
        try {
            Socket socket = new Socket("localhost", 9090);
        } catch (SocketTimeoutException e) {
            System.out.println("SocketTimeoutException: e=" + e.getMessage());
        } catch (IOException e) {
            System.out.println("exception: e=" + e.getMessage());
        }
    }

    public static void BindExceptionTest() {
        try {
            Socket socket = new Socket("localhost", 9090, InetAddress.getByName("127.0.0.1"), 80);
        } catch (BindException e) {
            System.out.println("BindException: e=" + e.getMessage());
        } catch (IOException e) {
            System.out.println("exception: e=" + e.getMessage());
        }
    }


    public static void main(String[] args) {
        ExceptionTest.UnknownHostExceptionTest();
        ExceptionTest.ConnectExceptionTest();
        ExceptionTest.SocketTimeoutExceptionTest();
        ExceptionTest.BindExceptionTest();
    }
}
