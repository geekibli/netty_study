package com.netty.chat.handler;

import com.netty.chat.domain.ChatMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author gaolei
 * @Date 2022/4/18 下午8:41
 * @Version 1.0
 */
public class ServerMsgHandler extends SimpleChannelInboundHandler<ChatMsg.Msg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMsg.Msg msg) throws Exception {

        System.out.println("content : " + msg.getContent());
        System.out.println("sender : " + msg.getSenderId());
        System.out.println("reveive : " + msg.getReceiverId());
        ctx.write(msg);
    }
}
