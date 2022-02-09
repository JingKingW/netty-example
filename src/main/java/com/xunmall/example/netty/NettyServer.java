package com.xunmall.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author wangyanjing
 * @date 2020/8/5 11:17
 */
public class NettyServer {

    private int prot;

    public NettyServer(int port){
        this.prot = port;
        bind();
    }

    private void bind(){
        //1: 创建BossGroup和WorkerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //2:创建ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            //3：设置channel和option
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline channelPipeline = socketChannel.pipeline();
                    //定义MessageDecoder,用来解码server接收消息并处理
                    channelPipeline.addLast("decoder", new MessageDecoder());
                }
            });
            //4:设置绑定端口号并启动
            ChannelFuture channelFuture = bootstrap.bind(prot).sync();
            if (channelFuture.isSuccess()){
                System.out.println("NettyServer start sucess, port:" + this.prot);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyServer(9000);
    }


}
