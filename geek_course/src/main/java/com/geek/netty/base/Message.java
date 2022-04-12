package com.geek.netty.base;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public abstract class Message<T extends MessageBody> {

    private MessageHeader messageHeader;

    private T body;

    public Message() {
    }

    public void encode(ByteBuf byteBuf) {
        byteBuf.writeInt(this.messageHeader.getVersion());
        byteBuf.writeInt(this.messageHeader.getOpCode());
        byteBuf.writeLong(this.messageHeader.getRequestId());
        byteBuf.writeBytes(JSON.toJSONString(body).getBytes(StandardCharsets.UTF_8));
    }

    public abstract Class<T> getMesssageBodyDecoderClass(int opCode);

    public void decode(ByteBuf msg) {
        int version = msg.readInt();
        int opCode = msg.readInt();
        long requestId = msg.readLong();

        this.messageHeader = new MessageHeader(version, opCode, requestId);
        Class<T> clazz = getMesssageBodyDecoderClass(opCode);
        T t = JSON.parseObject(msg.toString(StandardCharsets.UTF_8), clazz);
        this.body = t;
    }


    public Message(MessageHeader messageHeader, T body) {
        this.messageHeader = messageHeader;
        this.body = body;
    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
