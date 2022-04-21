package com.netty.chat.handler;

import com.netty.chat.RegisterTable;
import com.netty.chat.domain.ChatMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

/**
 * @Author gaolei
 * @Date 2022/4/20 下午7:48
 * @Version 1.0
 */
public class ServerChatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ChatMsg.Msg message = (ChatMsg.Msg) msg;
        if (message.getReceiverId() == 0) {
            super.channelRead(ctx, msg);
            return;
        }

        long receiverId = message.getReceiverId();

        if (receiverId == -1) {
            System.out.println(message.getSenderId() + " 发送群聊消息：  " + message.getContent());
            List<Channel> all = RegisterTable.getAll();
            for (Channel channel : all) {
                channel.writeAndFlush(message);
            }
        } else {
            System.out.println(message.getSenderId() + " 发送私聊消息：  " + message.getContent());
            Channel channel = RegisterTable.getChannel(receiverId);
            channel.writeAndFlush(message);
        }
    }
}
