package com.xunmall.example.netty.rpc.simple;

/**
 * @Author: wangyj03
 * @Description:
 * @Date: 2022/2/9 16:54
 */
public class ServerProvider {

    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        SimpleRpcFramework.export(service,2333);
    }


}
