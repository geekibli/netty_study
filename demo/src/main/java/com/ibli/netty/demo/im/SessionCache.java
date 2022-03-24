package com.ibli.netty.demo.im;

import java.io.Serializable;

public class SessionCache implements Serializable {

    private String userId;

    private String sessionId;

    private ImNode node;
}
