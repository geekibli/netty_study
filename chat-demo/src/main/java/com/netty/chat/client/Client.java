package com.netty.chat.client;


import com.netty.chat.codec.SimpleProtobufDecoder;
import com.netty.chat.codec.SimpleProtobufEncoder;
import com.netty.chat.domain.ChatMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Scanner;

/**
 * @Author gaolei
 * @Date 2022/4/18 下午8:30
 * @Version 1.0
 */
public class Client {


    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO))
                                .addLast(new SimpleProtobufDecoder())
                                .addLast(new SimpleProtobufEncoder());
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect("localhost", 9090).sync();
            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) {
                ChatMsg.Msg.Builder builder = ChatMsg.Msg.newBuilder();
                ChatMsg.Msg msg = builder.setContent(scanner.nextLine())
                        .setSenderId(100L)
                        .setReceiverId(333L)
                        .build();
                channelFuture.channel().writeAndFlush(msg);
            }

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}
