package com.xunmall.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author wangyanjing
 * @date 2020/8/5 13:58
 */
public class NettyClient {

    private int port = 9000;

    private String host = "localhost";

    public NettyClient(String host,int port){
        this.host = host;
        this.port = port;
        start();
    }

    private void start(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(host,port);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new NettyClientHandler());
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
            if (channelFuture.isSuccess()){
                System.out.println("connect server success");
            }
            channelFuture.channel().closeFuture().sync();
        }catch(Exception ex){
            eventLoopGroup.shutdownGracefully();
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new NettyClient("localhost",9000);
    }
}
