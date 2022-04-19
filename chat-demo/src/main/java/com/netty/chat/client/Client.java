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
                String input = scanner.nextLine();
                String[] split = input.split(":");
                // login:1:zhangsan:20
                // login:2:lisi:30
                // login:3:wangwu:30
                if (input.startsWith("login:")) {

                    ChatMsg.Msg.Builder builder = ChatMsg.Msg.newBuilder();
                    builder.setUserId(Long.parseLong(split[1]));
                    builder.setUsername(split[2]);
                    builder.setAge(Integer.parseInt(split[3]));
                    ChatMsg.Msg msg = builder.build();
                    channelFuture.channel().writeAndFlush(msg);
                    System.out.println("客户端：登录消息发送成功");
                }

                // chat:1:2:hello
                // chat:1:3:hello
                // chat:1:all:hello
                if (input.startsWith("chat:")) {
                    ChatMsg.Msg.Builder builder = ChatMsg.Msg.newBuilder();
                    ChatMsg.Msg msg = builder.setSenderId(Long.parseLong(split[1]))
                            .setReceiverId(Long.parseLong(split[2]))
                            .setContent(split[3])
                            .build();
                    channelFuture.channel().writeAndFlush(msg);
                    System.out.println("客户端：聊天消息发送成功");
                }
            }

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}
