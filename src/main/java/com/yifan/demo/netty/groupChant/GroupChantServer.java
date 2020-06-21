package com.yifan.demo.netty.groupChant;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChantServer {
    private final int port;

    public GroupChantServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        new GroupChantServer(7000).run();
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 添加String 的编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            // 添加String 的解码器
                            pipeline.addLast("decoder", new StringDecoder());

                            pipeline.addLast("myHandler", new GroupChantServerHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            // 添加监听器， 对绑定的操作进行监听
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.printf("监听端口 %d 成功\n", port);
                    } else {
                        System.out.printf("监听端口 %d 失败\n", port);
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
