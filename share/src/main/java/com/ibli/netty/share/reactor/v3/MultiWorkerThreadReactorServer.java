package com.ibli.netty.share.reactor.v3;

import cn.hutool.core.collection.CollectionUtil;
import com.ibli.netty.share.reactor.v1.BasicReactorServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author gaolei
 * @Date 2022/4/22 下午8:32
 * @Version 1.0
 */
public class MultiWorkerThreadReactorServer {

    private final static int PROCESS_NUM = 10;


    public static void start(int port){
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.bind(new InetSocketAddress(port), 128);

            //注册accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT,new BasicReactorServer.Acceptor(selector, serverSocketChannel));

            DispatchHandler[] handlers = new DispatchHandler[PROCESS_NUM];
            for (DispatchHandler h: handlers){
                h = new DispatchHandler();
            }

            int count = 0;

            //阻塞等待就绪事件
            while (selector.select() > 0){
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                //遍历就绪事件
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(key.isAcceptable()){
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        handlers[count ++ % PROCESS_NUM].addChannel(socketChannel);
                    }
                    keys.remove(key);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 读取数据处理
     */
    public static class DispatchHandler{

        private static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() << 1);
        private Selector selector;

        public DispatchHandler() throws IOException {
            selector = Selector.open();
            this.start();
        }

        public void addChannel(SocketChannel socketChannel) throws ClosedChannelException {
            socketChannel.register(selector, SelectionKey.OP_READ);
            this.selector.wakeup();
        }


        public void start() {
            executor.execute(new Runnable() {

                public void run() {
                    while (true){
                        Set<SelectionKey> keys = selector.selectedKeys();
                        if(CollectionUtil.isNotEmpty(keys)){
                            Iterator<SelectionKey> iterator = keys.iterator();
                            if(iterator.hasNext()){
                                SelectionKey key = iterator.next();
                                SocketChannel socketChannel = (SocketChannel)key.channel();
                                iterator.remove();
                                if(key.isReadable()){
                                    try {
                                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                                        int cnt = 0, total = 0;
                                        String msg = "";
                                        do {
                                            cnt = socketChannel.read(buffer);
                                            if (cnt > 0) {
                                                total += cnt;
                                                msg += new String(buffer.array());
                                            }
                                            buffer.clear();
                                        } while (cnt >= buffer.capacity());
                                        System.out.println("read data num:" + total);
                                        System.out.println("recv msg:" + msg);

                                        //回写数据
                                        ByteBuffer sendBuf = ByteBuffer.allocate(msg.getBytes().length + 1);
                                        sendBuf.put(msg.getBytes());
                                        socketChannel.write(sendBuf);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                        if(socketChannel != null){
                                            try {
                                                socketChannel.close();
                                            }catch (Exception ex){
                                                ex.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                keys.remove(key);
                            }
                        }
                    }
                }
            });

        }
    }





    public static void main(String[] args){
        MultiWorkerThreadReactorServer.start(9090);
    }

}

