package com.yifan.demo.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedBufferDemo {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("C:\\Users\\Yifan\\IdeaProjects\\NettyLearn\\output\\FileChannelOutput.txt", "rw");

        FileChannel channel = file.getChannel();

        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(2, (byte) 'A');
        map.put(4, (byte) 'H');
    }
}
