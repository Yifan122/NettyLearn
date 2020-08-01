package com.yifan.demo.protobuf2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Random random = new Random();
        MyDataInfo.MyMessage message = null;

        for (int i = 0; i < 3; i++) {
            if (random.nextInt(2) == 1) {
                message = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType).setStudent(
                        MyDataInfo.Student.newBuilder().setId(555).setName("LaoWang").build()
                ).build();
            }
        }
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
