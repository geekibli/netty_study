package com.ibli.netty.demo.channel;

import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class AttributeMapTest {
    private final static AttributeKey<User> USER_KEY = AttributeKey.valueOf("USER_KEY");
    public static void main(String[] args) {
        NioServerSocketChannel channel = new NioServerSocketChannel();
        channel.attr(USER_KEY).set(new User("gaolei",10));

        Attribute<User> userKey = channel.attr(USER_KEY);
        User user = userKey.get();
    }
}
