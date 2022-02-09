package com.xunmall.example.netty.rpc.provider;

import com.xunmall.example.netty.rpc.api.IRpcHelloService;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2020/10/11 15:15
 */
public class RpcHelloServiceImpl implements IRpcHelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name +"!";
    }
}
