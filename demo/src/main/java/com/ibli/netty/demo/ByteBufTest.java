package com.ibli.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * @Author gaolei
 * @Date 2022/3/21 下午6:38
 * @Version 1.0
 */
public class ByteBufTest {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        System.err.println("after create  " + buffer.refCnt());

        buffer.retain();
        System.err.println("after retain  " + buffer.refCnt());

        buffer.release();
        System.err.println("after release  " + buffer.refCnt());

        buffer.release();
        System.err.println("after release  " + buffer.refCnt());

        buffer.retain();
        System.err.println("after retain  " + buffer.refCnt());

    }
}
