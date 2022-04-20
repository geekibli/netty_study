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

    private String username;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("服务端登录处理器开始执行...");
        System.out.println(msg instanceof ChatMsg.Msg);
        ChatMsg.Msg segment = (ChatMsg.Msg) msg;
        RegisterTable.doRegister(segment.getUserId(), ctx.channel());
        this.username = segment.getUsername();
        ctx.write(msg);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        String str = "login success ";
        ChatMsg.Msg rsp = ChatMsg.Msg.newBuilder().setContent(str).build();
//        ctx.write(rsp);
        ctx.channel().writeAndFlush(rsp);
        System.out.println("服务端发送登录结果：" + str);
        System.out.println("当前 " + RegisterTable.getOnlineCount() + "在线");
        ctx.channel().pipeline().remove(this);
        ctx.channel().pipeline().addLast(new ServerChatHandler());
    }
}
