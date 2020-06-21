package com.yifan.demo.netty.groupChant;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChantClient {
    private final int SERVER_PORT;
    private final String SERVER_ADDRESS;

    public GroupChantClient(int server_port, String server_address) {
        SERVER_PORT = server_port;
        SERVER_ADDRESS = server_address;
    }

    public static void main(String[] args) {
        new GroupChantClient(7000, "127.0.0.1").run();
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 客户端使用的不是SeverBootstrap
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast(new GroupChantClientHandler());
                        }
                    });

            System.out.println("Client is ready");
            ChannelFuture channelFuture = bootstrap.connect(SERVER_ADDRESS, SERVER_PORT).sync();

//            channelFuture.channel().writeAndFlush("hhhhhh");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                channelFuture.channel().writeAndFlush(line);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
