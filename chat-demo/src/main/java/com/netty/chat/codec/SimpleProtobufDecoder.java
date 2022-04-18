package com.netty.chat.codec;

import com.netty.chat.domain.ChatMsg;
import com.netty.chat.domain.ProtoInstant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author gaolei
 * @Date 2022/4/18 下午8:14
 * @Version 1.0
 */
public class SimpleProtobufDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        short magic = byteBuf.readShort();
        if (magic != ProtoInstant.MAGIC_CODE) {
            throw new RuntimeException("魔数不对...");
        }
        short version = byteBuf.readShort();
        if (version != ProtoInstant.VERSION_CODE) {
            throw new RuntimeException("版本不对...");
        }

        int length = byteBuf.readInt();
        if (length < 0) {
            // 非法数据，关闭连接
            ctx.close();
        }

        // 读到的消息体长度如果小于传送过来的消息长度
        if (length > byteBuf.readableBytes()) {
            // 重置读取位置
            byteBuf.resetReaderIndex();
        }

        byte[] array = new byte[length];
        byteBuf.readBytes(array, 0, length);

        ChatMsg.Msg msg = ChatMsg.Msg.parseFrom(array);
        list.add(msg);
    }
}
