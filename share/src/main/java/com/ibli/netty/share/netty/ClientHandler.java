package com.ibli.netty.share.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author gaolei
 * @Date 2022/4/16 下午2:55
 * @Version 1.0
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        System.out.println("收到服务端消息" + o);
        ctx.write(o);
    }
}
