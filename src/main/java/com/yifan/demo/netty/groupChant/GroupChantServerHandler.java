package com.yifan.demo.netty.groupChant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class GroupChantServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组， 管理所有的channel
    // GlobalChannelGroup.INSTANCE 是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("客户端新上线：" + ctx.channel().remoteAddress());
        // 该方法可以遍历所有的channel， 并发送消息
        channelGroup.writeAndFlush("客户端新上线：" + ctx.channel().remoteAddress());
        channelGroup.add(ctx.channel());

    }

    // 表示channel处于活跃状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线~");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线~");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
        channelGroup.forEach((channel -> {
            if (channel != ctx.channel()) {
                channel.writeAndFlush("[客户端发送了信息" + ctx.channel().remoteAddress() + "]" + msg);
            } else {
                channel.writeAndFlush("已成功发送信息：" + msg);
            }
        }));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("客户端下线了" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }
}
