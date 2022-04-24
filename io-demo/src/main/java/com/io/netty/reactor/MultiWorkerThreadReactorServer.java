package com.io.netty.reactor;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 多工作线程的reactor模式
 */
public class MultiWorkerThreadReactorServer {

    private final static int PROCESS_NUM = 10;


    public static void start(int port) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.bind(new InetSocketAddress(port), 128);

            //注册accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            DispatchHandler[] handlers = new DispatchHandler[PROCESS_NUM];
            for (int i = 0; i < PROCESS_NUM; i++) {
                handlers[i] = new DispatchHandler(i);
            }

            int count = 0;

            // 阻塞等待就绪事件
            while (selector.select() > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                //遍历就绪事件
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverSocketChannel1.accept();
                        socketChannel.configureBlocking(false);
                        int index = count++ % PROCESS_NUM;
                        handlers[index].addChannel(socketChannel);
                        System.out.println("add addChannel to handler-" + index);
                    }

                    keys.remove(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取数据处理
     */
    public static class DispatchHandler {
        private int id;
        private static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() << 1);
        private Selector selector;

        public DispatchHandler(int id) throws IOException {
            this.id = id;
            selector = Selector.open();
            this.start();
        }

        public void addChannel(SocketChannel socketChannel) throws IOException {
            System.out.println(socketChannel.getRemoteAddress());
            System.out.println(socketChannel.getLocalAddress());
            System.out.println(socketChannel.hashCode());
            socketChannel.register(selector, SelectionKey.OP_READ);
            this.selector.wakeup();
        }


        public void start() {
            System.out.println("handler start ... ");
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (selector.select(5000) == 0) {
                                System.out.println("wait  5 s");
                                continue;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Set<SelectionKey> keys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = keys.iterator();
                        if (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            iterator.remove();
                            if (key.isReadable()) {
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
                                    System.out.println("read data num:" + total + "  handlerId : " + id + " thread name :  " + Thread.currentThread().getName());
                                    System.out.println("recv msg:" + msg);

                                    //回写数据
//                                    ByteBuffer sendBuf = ByteBuffer.allocate(msg.getBytes().length + 1);
                                    ByteBuffer sendBuf = ByteBuffer.allocate(128);
                                    sendBuf.clear();
                                    sendBuf.put("ok ok".getBytes());
                                    sendBuf.flip();
                                    while (sendBuf.hasRemaining()) {
                                        socketChannel.write(sendBuf);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (socketChannel != null) {
                                        try {
                                            socketChannel.close();
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            }
                            keys.remove(key);
                        }
                    }
                }
            });

        }
    }


    public static void main(String[] args) {
        MultiWorkerThreadReactorServer.start(9090);
    }

}
