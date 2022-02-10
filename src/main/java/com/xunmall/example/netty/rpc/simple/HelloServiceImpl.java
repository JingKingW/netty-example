package com.xunmall.example.netty.rpc.simple;

/**
 * @Author: wangyj03
 * @Description:
 * @Date: 2022/2/9 16:54
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "You man Hello,I am " + name;
    }
}
