package com.netty.rpc.test;

import com.netty.rpc.api.IUserService;
import com.netty.rpc.api.impl.UserServiceImpl;
import com.netty.rpc.client.RpcClient;

import java.sql.SQLOutput;
import java.util.Arrays;

/**
 * @Author gaolei
 * @Date 2022/4/14 下午1:04
 * @Version 1.0
 */
public class RpcTest {


    public static void main(String[] args) {
        IUserService service = RpcClient.create(UserServiceImpl.class);

        String userName = service.getUserName();
        System.out.println("userName : " + userName);

        String name = service.setUserName("gaolei");
        System.out.println("name" + name);

    }




}
