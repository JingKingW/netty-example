package com.xunmall.example.netty.rpc.consumer;

import com.xunmall.example.netty.rpc.api.IRpcHelloService;
import com.xunmall.example.netty.rpc.api.IRpcService;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2020/10/11 16:51
 */
public class RpcConsumer {

    public static void main(String[] args) {
        IRpcHelloService rpcHelloService = RpcProxy.create(IRpcHelloService.class);
        System.out.println(rpcHelloService.hello("Tom Teacher"));

        IRpcService rpcService = RpcProxy.create(IRpcService.class);
        System.out.println(rpcService.add(2, 4));
    }

}
