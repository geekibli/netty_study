package com.ibli.netty.share.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * @Author gaolei
 * @Date 2022/4/16 下午2:52
 * @Version 1.0
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("revice sss msg: {}" + msg);
        ctx.write(msg);
    }

}
