package com.xunmall.example.netty.rpc.provider;

import com.xunmall.example.netty.rpc.api.IRpcService;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2020/10/11 15:16
 */
public class RpcServiceImpl implements IRpcService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int sub(int a, int b) {
        return a - b;
    }

    @Override
    public int mult(int a, int b) {
        return a * b ;
    }

    @Override
    public int div(int a, int b) {
        return a / b;
    }
}
