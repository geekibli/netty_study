package com.netty.chat.handler;

import com.netty.chat.domain.ChatMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author gaolei
 * @Date 2022/4/20 下午7:53
 * @Version 1.0
 */
public class ClientChatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChatMsg.Msg message = (ChatMsg.Msg) msg;
        if (message.getReceiverId() == 0) {
            super.channelRead(ctx, msg);
            return;
        }

        System.out.println("收到消息： " + message.getContent() + "   来自用户: " + message.getSenderId());
    }
}
