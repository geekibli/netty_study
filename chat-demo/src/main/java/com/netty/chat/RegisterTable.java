package com.netty.chat;



import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author gaolei
 * @Date 2022/4/19 下午8:02
 * @Version 1.0
 */
public class RegisterTable {

    public static final Map<Long, Channel> online_channel_map = new ConcurrentHashMap<Long, Channel>();

    public static void doRegister(Long key, Channel channel) {
        online_channel_map.put(key, channel);
        System.out.println(key + "添加到注册表...");
    }


    public static Channel getChannel(Long userId) {
        return online_channel_map.get(userId);
    }


    public static int getOnlineCount() {
        return online_channel_map.size();
    }

    public static List<Channel> getAll(){
        return online_channel_map.values().stream().collect(Collectors.toList());
    }
}
