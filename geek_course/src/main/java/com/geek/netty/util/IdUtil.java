package com.geek.netty.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author gaolei
 * @Date 2022/4/13 下午8:58
 * @Version 1.0
 */
public final class IdUtil {

    private static final AtomicLong IDX = new AtomicLong();

    private IdUtil(){
        //no instance
    }

    public static long nextId(){
        return IDX.incrementAndGet();
    }
}
