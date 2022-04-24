
# NIO实现主从Reactor多线程模型

- mainReactor负责监听server socket，用来处理新连接的建立，将建立的socketChannel指定注册给subReactor。

- subReactor维护自己的selector, 基于mainReactor 注册的socketChannel多路分离IO读写事件，读写网 络数据，对业务处理的功能，另其扔给worker线程池来完成。

<img src="https://oscimg.oschina.net/oscnet/up-bf58c270658942f91a1a47176eb0ad944cd.png" width=700 height=300>

## 服务端代码

```java
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

```


## 客户端代码

```java
package com.io.netty.reactor;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ReactorClient {
    private static final int port = 9090;

    private SocketChannel client = null;

    public SocketChannel getClient() {
        return client;
    }

    public void start(int port) {
        try {
            client = SocketChannel.open();
            Selector selector = Selector.open();

            boolean success = client.connect(new InetSocketAddress(port));
            client.configureBlocking(false);
            if (success) {
                client.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);

                while (true) {
                    selector.select();
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();

                        if (key.isConnectable()) {
                            key.interestOps(SelectionKey.OP_READ);
                        }

                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int cnt = ((SocketChannel) key.channel()).read(buffer);
                            if (cnt <= -1) {
                                client.close();
                                break;
                            } else if (cnt > 0) {
                                System.out.println("read count=" + cnt);
                                System.out.println("read data=" + new String(buffer.array()));
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

        final ReactorClient client = new ReactorClient();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.equals("exit")) {
                        break;
                    }

                    try {
                        if (client.getClient().isConnected()) {
                            ByteBuffer buffer = ByteBuffer.allocate(line.getBytes().length + 2);
                            buffer.put(line.getBytes());
                            buffer.flip();
                            client.getClient().write(buffer);
                            System.out.println("write end ...  " + client.getClient().hashCode());
                        } else {
                            System.out.println("还未连接上服务器！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        thread.start();
        client.start(9090);

    }
}

```

