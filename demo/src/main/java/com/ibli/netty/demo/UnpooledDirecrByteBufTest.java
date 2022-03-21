package com.ibli.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;

/**
 * @Author gaolei
 * @Date 2022/3/21 下午8:13
 * @Version 1.0
 */
public class UnpooledDirecrByteBufTest {

    public static void main(String[] args) {
        System.setProperty("java.jvm.name", "XXXX");
//        UnpooledDirectByteBuf unpooledDirectByteBuf = (UnpooledDirectByteBuf) UnpooledByteBufAllocator.DEFAULT.directBuffer(9, 100);
        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(9, 100);
        System.err.println(byteBuf);
    }
}
