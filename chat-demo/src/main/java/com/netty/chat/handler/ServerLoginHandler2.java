package com.netty.chat.handler;


import com.netty.chat.RegisterTable;
import com.netty.chat.domain.ChatMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Iterator;
import java.util.Map;

/**
 * @Author gaolei
 * @Date 2022/4/19 下午8:03
 * @Version 1.0
 */
public class ServerLoginHandler2 extends SimpleChannelInboundHandler<ChatMsg.Msg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMsg.Msg msg) {
        System.out.println("服务端登录处理器开始执行...");

        RegisterTable.doRegister(msg.getUserId(), ctx.channel());
//        ctx.channel().pipeline().remove(this);

        Iterator<Map.Entry<String, ChannelHandler>> iterator = ctx.channel().pipeline().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().getKey());
        }
        ctx.write(msg.getUsername() + "登录成功了");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ssss..." + msg.toString());
        super.channelRead(ctx, msg);
    }
}
