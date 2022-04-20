package com.ibli.netty.share.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author gaolei
 * @Date 2022/4/20 下午9:11
 * @Version 1.0
 */
public class Server {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9090));
        serverSocketChannel.configureBlocking(false)
                .register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverSocketChannel1.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("client connect ..." + socketChannel.getLocalAddress().toString());
                    }
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        byte[] array = buffer.array();
                        System.out.println("client" + new String(array));
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_WRITE);
                    }
                    if (key.isWritable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        String val = "楚河瑞党务";
                        byte[] bytes = val.getBytes();
                        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                        buffer.clear();
                        buffer.put(ByteBuffer.wrap(bytes));
                        buffer.flip();
                        socketChannel.configureBlocking(false);
                        while (buffer.hasRemaining()) {
                            socketChannel.write(buffer);
                        }
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                }


            }


        }


    }
}
