package com.ibli.netty.share.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author gaolei
 * @Date 2022/4/16 下午2:38
 * @Version 1.0
 */
public class Client {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();

                        pipeline.addLast(new LoggingHandler(LogLevel.INFO))
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2))
                                .addLast(new LengthFieldPrepender(2))
                                .addLast("encoder", new ObjectEncoder())
                                .addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                .addLast(new ClientHandler());
                    }
                });


        try {
            ChannelFuture channelFuture = bootstrap.connect("localhost", 9090).sync();

            //
            channelFuture.channel().writeAndFlush("woshigaolei");


            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }


    }


}
