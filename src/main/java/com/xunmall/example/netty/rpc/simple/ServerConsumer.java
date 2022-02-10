package com.xunmall.example.netty.rpc.simple;

/**
 * @Author: wangyj03
 * @Description:
 * @Date: 2022/2/9 17:01
 */
public class ServerConsumer {
    public static void main(String[] args) throws Exception {
        HelloService helloService = SimpleRpcFramework.refer(HelloService.class, "127.0.0.1", 2333);
        String tome = helloService.hello("tome");
        System.out.println(tome);
    }
}
