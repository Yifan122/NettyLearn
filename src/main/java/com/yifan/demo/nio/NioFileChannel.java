package com.yifan.demo.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer, 可以让文件直接在堆外内存中进行修改
 */
public class NioFileChannel {
    public static void main(String[] args) {
        String str = "Hello, Yifan";

        try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Yifan\\IdeaProjects\\NettyLearn\\output\\FileChannelOutput.txt")) {
            FileChannel fileChannel = fileOutputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            buffer.clear();
            buffer.put(str.getBytes());

            buffer.flip();

            fileChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
