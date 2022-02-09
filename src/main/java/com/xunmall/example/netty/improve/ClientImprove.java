package com.xunmall.example.netty.improve;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author WangYanjing
 * @description
 * @date 2020/8/25 9:52
 */
public class ClientImprove {

    private static final String SERVER_HOST = "127.0.0.1";

    public static void main(String[] args) {
        new ClientImprove().start(ServerImprove.BEGIN_PORT,ServerImprove.N_PORT);
    }

    public void start(int beginPort, int nPort) {
        System.out.println("客户端启动。。。");
        EventLoopGroup eventExecutor = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventExecutor);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {

            }
        });

        int index = 0;
        int port;
        while (!Thread.interrupted()) {
            port = beginPort + index;
            try {
                ChannelFuture channelFuture = bootstrap.connect(SERVER_HOST, port);
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (!channelFuture.isSuccess()) {
                            System.out.println("连接失败，程序关闭");
                            System.exit(0);
                        }
                    }
                });
                channelFuture.get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (port == nPort) {
                index = 0;
            } else {
                index++;
            }
        }
    }
}
