# NIO实现Reactor多线程模型


相对于第一种单线程的模式来说，在处理业务逻辑，也就是获取到IO的读写事件之后，交由线程池来处理，这样可以减小主reactor的性能开销，从而更专注的做事件分发工作了，从而提升整个应用的吞吐。

<img src="https://oscimg.oschina.net/oscnet/up-c1b53446830b85c1145eedd83dc44b13ddb.png" width=700 height=300>



## 服务端代码

```java
package com.io.netty.reactor;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 多reactor的reactor模式
 */
public class MultiReactorReactorServer {

    public static void start(int port) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.bind(new InetSocketAddress(port), 128);

            //注册accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, new Acceptor(selector, serverSocketChannel));

            //阻塞等待就绪事件
            while (selector.select() > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                //遍历就绪事件
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    Runnable handler = (Runnable) key.attachment();
                    handler.run();
                    keys.remove(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 接受连接处理
     */
    public static class Acceptor implements Runnable {

        private Selector selector;

        private ServerSocketChannel serverSocketChannel;

        public Acceptor(Selector selector, ServerSocketChannel serverSocketChannel) {
            this.selector = selector;
            this.serverSocketChannel = serverSocketChannel;
        }

        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ, new DispatchHandler(socketChannel));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取数据处理
     */
    public static class DispatchHandler implements Runnable {

        private static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() << 1);
        private SocketChannel socketChannel;

        public DispatchHandler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            executor.execute(new ReaderHandler(socketChannel));
        }
    }


    public static class ReaderHandler implements Runnable {
        private SocketChannel socketChannel;

        public ReaderHandler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
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
                System.out.println("current thread: " + Thread.currentThread().getName() + " recv msg:" + msg);

                //回写数据
                ByteBuffer sendBuf = ByteBuffer.allocate(msg.getBytes().length + 1);
                sendBuf.put(msg.getBytes());
                socketChannel.write(sendBuf);

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
    }


    public static void main(String[] args) {
        MultiReactorReactorServer.start(9090);
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


