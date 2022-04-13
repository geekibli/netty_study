package com.geek.netty.base.auth;


import com.geek.netty.base.OperationResult;

public class AuthOperationResult extends OperationResult {

    private final boolean passAuth;

    public AuthOperationResult(boolean passAuth) {
        this.passAuth = passAuth;
    }
}
