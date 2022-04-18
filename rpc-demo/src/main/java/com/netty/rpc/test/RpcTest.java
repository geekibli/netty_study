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
        System.out.println("test start");
        IUserService service = RpcClient.create(IUserService.class);

        System.out.println("invoke 111");
        String userName = service.getUserName();
        System.out.println("userName : " + userName);

        System.out.println("invoke 333");
        String name = service.setUserName("gaolei");
        System.out.println("name" + name);

    }


}
