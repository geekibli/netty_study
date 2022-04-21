package com.netty.chat.domain;

import io.netty.channel.Channel;

/**
 * @Author gaolei
 * @Date 2022/4/20 下午6:47
 * @Version 1.0
 */

public class ServerSession {

    private Long userId;

    private Channel channel;

    public ServerSession() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
