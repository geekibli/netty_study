package com.geek.netty.base;

public class RequestMessage extends Message<Operation>{

    @Override
    public Class<Operation> getMesssageBodyDecoderClass(int opCode) {
        return null;
    }
}
