package com.netty.rpc.api.impl;

import com.netty.rpc.api.IUserService;

import java.util.UUID;

/**
 * @Author gaolei
 * @Date 2022/4/14 下午12:44
 * @Version 1.0
 */
public class UserServiceImpl implements IUserService {

    public String getUserName() {
        return UUID.randomUUID().toString();
    }

    public String setUserName(String name) {
        return name + " is updated!";
    }
}
