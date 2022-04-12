package com.geek.netty.base;

public class MessageHeader {

    private int version = 1;
    private int opCode;
    private long requestId;

    public MessageHeader() {
    }

    public MessageHeader(int version, int opCode, long requestId) {
        this.version = version;
        this.opCode = opCode;
        this.requestId = requestId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
