package com.ibli.netty.demo.im;

public class ImNode {
    /**
     * worker id , zookeeper生成
     */
    private Long id;

    /**
     * 服务端的连接数
     */
    private Integer balance = 0;
    /**
     * 服务端地址
     */
    private String host;
    /**
     * 服务端端口
     */
    private String port;

}
