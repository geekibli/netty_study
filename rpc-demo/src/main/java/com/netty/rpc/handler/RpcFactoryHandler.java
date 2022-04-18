package com.netty.rpc.handler;

import com.netty.rpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author gaolei
 * @Date 2022/4/14 下午12:54
 * @Version 1.0
 */
public class RpcFactoryHandler extends ChannelInboundHandlerAdapter {

    /**
     * 注册中心容器
     */
    private static final ConcurrentHashMap<String, Object> REGISTRY_MAP = new ConcurrentHashMap<String, Object>();

    private List<String> classNameList = new ArrayList<String>();

    public RpcFactoryHandler() {
        // 1、扫描所有需要注册的类
        scannerClass("com.netty.rpc.api.impl");
        // 执行注册
        doRegistry();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result;
        InvokerProtocol request = (InvokerProtocol) msg;
        if (REGISTRY_MAP.containsKey(request.getClassName())) {
            Object provider = REGISTRY_MAP.get(request.getClassName());
            Method method = provider.getClass().getMethod(request.getMethodName(), request.getParams());
            result = method.invoke(provider, request.getValues());
            ctx.write(result);
            ctx.flush();
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void doRegistry() {
        if (classNameList.isEmpty()) {
            return;
        }

        for (String className : classNameList) {
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> i = clazz.getInterfaces()[0];
                REGISTRY_MAP.put(i.getName(), clazz.newInstance());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

    }

    private void scannerClass(String packageName) {
//        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
//        File dir = new File(url.getFile());
//        for (File file : dir.listFiles()) {
//            if (file.isDirectory()) {
//                scannerClass(packageName + "." + file.getName());
//            } else {
//                classNameList.add(packageName + "." + file.getName().replace(".class", "").trim());
//            }
//        }
        classNameList.add(packageName + "." + "UserServiceImpl");
    }

}
