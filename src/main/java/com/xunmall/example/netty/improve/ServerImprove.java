package com.xunmall.example.netty.improve;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2020/10/23 13:05
 */
public class ServerImprove {

    public static final int BEGIN_PORT = 8000;

    public static final int N_PORT = 8100;

    public static void main(String[] args){
        new ServerImprove().start(BEGIN_PORT,N_PORT);
    }

    public void start(int startPort,int nPort){
        System.out.println("服务端启动中。。。。。。。。。");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childOption(ChannelOption.SO_REUSEADDR,true);
        bootstrap.childHandler(new ConnectionCountHandler());
        for ( int i = 0; i < (nPort - startPort) ; i++){
            final int port = startPort + i ;

            bootstrap.bind(port).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    System.out.println("成功绑定端口：" + port);
                }
            });
        }
        System.out.println("服务端已启动");
    }
}
