package com.netty.chat.codec;

import com.netty.chat.domain.ChatMsg;
import com.netty.chat.domain.ProtoInstant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author gaolei
 * @Date 2022/4/18 下午8:08
 * @Version 1.0
 */
public class SimpleProtobufEncoder extends MessageToByteEncoder<ChatMsg.Msg> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ChatMsg.Msg msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(ProtoInstant.MAGIC_CODE);
        byteBuf.writeShort(ProtoInstant.VERSION_CODE);
        byte[] bytes = msg.toByteArray();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

    }
}
