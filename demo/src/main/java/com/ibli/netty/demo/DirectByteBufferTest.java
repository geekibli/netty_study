package com.ibli.netty.demo;

import sun.misc.VM;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/**
 * @Author gaolei
 * @Date 2022/3/21 下午8:22
 * @Version 1.0
 */
public class DirectByteBufferTest {

    public static void main(String[] args) {


        ByteBuffer buffer = ByteBuffer.allocateDirect(20);

        for (byte i = 0; i < 5; i++) {
            buffer.put(i);
        }

        buffer.flip();

        for (byte i = 0; i < 5; i++) {
            System.err.println(i);
        }


        long l = VM.maxDirectMemory();
        System.err.println(l / 1024 / 1024);

        long l1 = Runtime.getRuntime().maxMemory();
        System.err.println(l1 / 1024/ 1024);
    }
}
