package com.ibli.netty.demo.im;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserCache {

    private String userId;

    // session id
    private Map<String, SessionCache> map = new LinkedHashMap<String, SessionCache>(10);
}
