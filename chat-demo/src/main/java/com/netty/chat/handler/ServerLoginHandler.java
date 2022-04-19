package com.netty.chat.handler;


import com.netty.chat.RegisterTable;
import com.netty.chat.domain.ChatMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


import java.util.Iterator;
import java.util.Map;

/**
 * @Author gaolei
 * @Date 2022/4/19 下午8:03
 * @Version 1.0
 */
public class ServerLoginHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("服务端登录处理器开始执行...");
        System.out.println(msg instanceof ChatMsg.Msg);
        ChatMsg.Msg segment = (ChatMsg.Msg)msg;
        RegisterTable.doRegister(segment.getUserId(), ctx.channel());
//        ctx.channel().pipeline().remove(this);

        Iterator<Map.Entry<String, ChannelHandler>> iterator = ctx.channel().pipeline().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().getKey());
        }

        ctx.write(segment.getUsername() + "登录成功了");
    }
}
