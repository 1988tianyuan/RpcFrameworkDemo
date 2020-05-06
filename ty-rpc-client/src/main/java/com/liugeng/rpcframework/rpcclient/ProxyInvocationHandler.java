package com.liugeng.rpcframework.rpcclient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import com.liugeng.rpcframework.rpcclient.client.RpcClient;
import com.liugeng.rpcframework.rpcprotocal.model.RpcRequestPacket;
import com.liugeng.rpcframework.rpcprotocal.model.RpcResponsePacket;

public class ProxyInvocationHandler implements InvocationHandler {

    private final RpcClient rpcClient;

    public ProxyInvocationHandler(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequestPacket requestPacket = new RpcRequestPacket();
        requestPacket.setRequestId(UUID.randomUUID().toString()); 
        requestPacket.setClassName(method.getDeclaringClass().getName());
        requestPacket.setMethodName(method.getName());
        requestPacket.setParamTypes(method.getParameterTypes());
        requestPacket.setParams(args);
        RpcResponsePacket responsePacket = rpcClient.send(requestPacket);
        return responsePacket.getResult();
    }
}
