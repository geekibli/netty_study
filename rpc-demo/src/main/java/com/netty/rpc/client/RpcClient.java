package com.netty.rpc.client;

import com.netty.rpc.handler.MethodHandler;

import java.lang.reflect.Proxy;

/**
 * @Author gaolei
 * @Date 2022/4/14 下午12:48
 * @Version 1.0
 */
public class RpcClient {

    public static <T> T create(Class<?> clazz) {
        MethodHandler proxy = new MethodHandler(clazz);
        Class<?>[] interfaces = clazz.isInterface() ?
                new Class[]{clazz} :
                clazz.getInterfaces();
        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, proxy);
        return result;
    }

}
