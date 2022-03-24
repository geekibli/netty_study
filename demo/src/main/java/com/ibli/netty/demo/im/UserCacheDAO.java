package com.ibli.netty.demo.im;

public interface UserCacheDAO {

    void save(UserCache u);

    void remove(String userId, String sessionId);

    void addSession(String userId, SessionCache s);

    UserCache get(String userId);
}
