package com.netty.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gaolei
 * @Date 2022/4/14 下午12:52
 * @Version 1.0
 */
@Data
public class InvokerProtocol implements Serializable {

    // 基于二进制流调用协议

    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 形参
     */
    private Class<?>[] params;
    /**
     * 实参
     */
    private Object[] values;
}
