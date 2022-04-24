package com.io.netty.preactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class Preactor {

    private final static int port = 9090;

    public static void start() throws IOException {

        AsynchronousServerSocketChannel channel = null;
        try {
            AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(10));
            channel = AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress(port), 128);
            System.out.println("服务器已启动，端口号：" + port);

            channel.accept(null, new Acceptor());
            CountDownLatch latch = new CountDownLatch(1);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        try {

            Preactor.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static class Acceptor implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

        public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                result.read(buffer, buffer, new ReadHandler(result));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
            exc.printStackTrace();
        }
    }


    public static class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

        private AsynchronousSocketChannel socketChannel;

        public ReadHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        public void completed(Integer result, ByteBuffer attachment) {
            try {
                attachment.flip();
                String msg = new String(attachment.array());
                System.out.println("recv client msg:" + msg);
                socketChannel.write(attachment, attachment, new WritterHandler(socketChannel));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void failed(Throwable exc, ByteBuffer attachment) {
            exc.printStackTrace();
            try {
                socketChannel.close();
                ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static class WritterHandler implements CompletionHandler<Integer, ByteBuffer> {

        private AsynchronousSocketChannel socketChannel;

        public WritterHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        public void completed(Integer result, ByteBuffer attachment) {
            try {
                attachment.clear();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                socketChannel.read(buffer, buffer, new ReadHandler(socketChannel));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void failed(Throwable exc, ByteBuffer attachment) {
            exc.printStackTrace();
            try {
                socketChannel.close();
                ;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
