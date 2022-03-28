package com.ibli.netty.demo.event;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.*;
import java.nio.channels.spi.AbstractSelector;

public class SelectionKeyTest {

    private ServerSocketChannel server = null;
    private Selector selector = null;
    int port = 9090;

    public void initServer() throws IOException {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(port));
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void run(){

        server.register(selector, SelectionKey.OP_ACCEPT);

        SelectionKey sk = server.register(selector, 0);
        sk.interestOps(SelectionKey.OP_ACCEPT);

        SelectableChannel channel = sk.channel();
        Selector selector = sk.selector();

        int i = sk.interestOps();


        sk.isAcceptable();
        sk.isConnectable();
        sk.isReadable();
        sk.isWritable();
    }

    public static void main(String[] args) {

    }
}
