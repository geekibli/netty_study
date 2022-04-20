package com.ibli.netty.share.nio;

import com.sun.tools.jdi.SocketTransportService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author gaolei
 * @Date 2022/4/20 下午9:23
 * @Version 1.0
 */
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 9090));
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);


        while (selector.isOpen()) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isConnectable()) {
                    SocketChannel socketChannel1 = (SocketChannel) key.channel();
                    socketChannel1.finishConnect();
                    if (socketChannel1.isConnected()) {
                        System.out.println("connect end...");
                    }
                    socketChannel1.configureBlocking(false);
                    socketChannel1.register(selector, SelectionKey.OP_WRITE);
                }

                if (key.isValid() && key.isReadable()) {

                    // 1. 获得TCP协议通信的通道
                    SocketChannel socketChannel1= (SocketChannel) key.channel();
                    // 2. 分配缓存区
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    // 创建字节数组
                    byte[] bytes = new byte[1024 * 2];
                    // 设置下标
                    int index = 0;
                    // 3. 读取数据到缓冲区
                    while (socketChannel.read(buffer) != 0) {
                        // 4. 将缓存区的指针回到初始位置
                        buffer.flip();
                        // 5. 不断读出数据
                        while (buffer.hasRemaining()) {
                            bytes[index++] = buffer.get();
                        }
                        // 读取完毕后，清空缓冲区
                        buffer.clear();
                    }
                    System.out.println(new String(bytes, 0, index, StandardCharsets.UTF_8));
                    // 6. 注册写出业务
                    socketChannel.register(selector,SelectionKey.OP_WRITE);
                }

                if (key.isValid() && key.isWritable()) {

                    SocketChannel socketChannel1 = (SocketChannel) key.channel();

                    String val = "ssssss";

                    byte[] bytes = val.getBytes();
                    ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                    buffer.clear();
                    buffer.put(ByteBuffer.wrap(bytes));
                    buffer.flip();

                    while (buffer.hasRemaining()) {
                        socketChannel1.configureBlocking(false);
                        socketChannel1.write(buffer);
                    }

                    socketChannel1.register(selector, SelectionKey.OP_READ);
                }

            }


        }


    }


}
