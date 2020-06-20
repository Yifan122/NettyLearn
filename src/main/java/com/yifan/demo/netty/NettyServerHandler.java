package com.yifan.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    // 读取信息
    /*
    1. ChannelHandlerContext ctx: 上下文对象，含有管道pipeline，通道channel，地址
    2. Object msg： 就是客户端发送的数据 默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 异步提交task到eventloop中
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(3000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("异步任务结束", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        // 提交定时任务
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("定时5秒执行");
            }
        }, 5000, TimeUnit.MILLISECONDS);


        System.out.println("服务器读取线程" + Thread.currentThread().getName());
        System.out.println("server ctx =" + ctx);

        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端的信息是： " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址： " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, client", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.channel().close();
    }
}
