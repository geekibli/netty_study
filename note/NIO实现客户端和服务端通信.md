# NIO实现客户端和服务端通信


## 服务端

```java
package com.io.netty.nio.serverSocketChannel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioEchoServer {

    private static final int SELECTOR_TIMEOUT = 1000;

    public void start(int port) {
        try {
            //打开服务socket
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            //打开selector
            Selector selector = Selector.open();

            //服务监听port端口，配置为非阻塞模式
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            //将channel注册到selector中，并监听accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            long cnt = 1;
            while (true) {
                if (selector.select(SELECTOR_TIMEOUT) == 0) {
                    System.out.println("wait " + (cnt++) + "s");
                    continue;
                }
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    try {
                        if (key.isAcceptable()) {
                            SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
                            client.configureBlocking(false);

                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            client.register(selector, SelectionKey.OP_READ, buffer);
                            System.out.println("connect accept!!");
                        }

                        if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            buffer.compact();
                            int count = client.read(buffer);
                            if (count <= -1) {
                                client.close();
                            } else if (count > 0) {
                                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                                System.out.println("read count=" + count);
                                System.out.println("read data= " + new String(buffer.array()));
                            }
                        }


                        if (key.isValid() && key.isWritable()) {
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            buffer.flip();
                            ((SocketChannel) key.channel()).write(buffer);
                            if (!buffer.hasRemaining()) {
                                key.interestOps(SelectionKey.OP_READ);
                            }
                            buffer.compact();
                        }
                    } catch (Exception e) {
                        ((SocketChannel) key.channel()).close();
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        try {
            new NioEchoServer().start(9090);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


```

## 客户端

```java
package com.io.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class NioEchoClient {
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

        final NioEchoClient client = new NioEchoClient();

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