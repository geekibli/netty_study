package com.ibli.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;

/**
 * @Author gaolei
 * @Date 2022/3/22 下午7:52
 * @Version 1.0
 */
public class UnpooledDirectByteBufTest {

    public static void main(String[] args) {
        System.setProperty("io.netty.Unsafe", "true");
        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(9, 100);
        System.out.println(byteBuf);
    }
}
