package com.yifan.demo.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    // 读取信息
    /*
    1. ChannelHandlerContext ctx: 上下文对象，含有管道pipeline，通道channel，地址
    2. Object msg： 就是客户端发送的数据 默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        StudentPOJO.Student student = (StudentPOJO.Student) msg;
        System.out.println("Client message: " + student.getId() + ", " + student.getName());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.channel().close();
    }
}
