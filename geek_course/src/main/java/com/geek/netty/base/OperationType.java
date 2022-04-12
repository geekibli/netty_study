package com.geek.netty.base;

public enum OperationType {

    // TODO

    AUTH(1, Operation.class, OperationResult.class);

    private int opCode;
    private Class<? extends Operation> operationClass;
    private Class<? extends OperationResult> operationResultClass;

    OperationType(int opCode, Class<? extends Operation> operationClass, Class<? extends OperationResult> operationResultClass) {
        this.opCode = opCode;
        this.operationClass = operationClass;
        this.operationResultClass = operationResultClass;
    }

    OperationType() {
    }


    public int getOpCode() {
        return opCode;
    }

    public Class<? extends Operation> getOperationClass() {
        return operationClass;
    }

    public Class<? extends OperationResult> getOperationResultClass() {
        return operationResultClass;
    }
}
