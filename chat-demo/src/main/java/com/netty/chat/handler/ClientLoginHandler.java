package com.netty.chat.handler;

import com.netty.chat.domain.ChatMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author gaolei
 * @Date 2022/4/20 下午6:49
 * @Version 1.0
 */
public class ClientLoginHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("进入.. ClientLoginHandler 。。。");
        ChatMsg.Msg message = (ChatMsg.Msg) msg;
        System.out.println("登录结果：" + message.getContent());
        if (message.getContent().startsWith("login success")) {
            ctx.channel().pipeline().remove(this);
            ctx.channel().pipeline().addLast(new ClientChatHandler());
        }
        ctx.write(msg);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
