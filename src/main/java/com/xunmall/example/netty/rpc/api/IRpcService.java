package com.xunmall.example.netty.rpc.api;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2020/10/11 15:09
 */
public interface IRpcService {

    int add(int a,int b) ;

    int sub(int a, int b);

    int mult(int a,int b);

    int div(int a,int b);

}
