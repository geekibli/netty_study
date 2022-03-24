package com.ibli.netty.demo.im;

public interface SessionCacheDAO {

    void save(SessionCache s);

    SessionCache get(String sessionId);

    void remove(String sessionId);
}
