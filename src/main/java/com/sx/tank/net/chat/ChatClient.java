package com.sx.tank.net.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

public class ChatClient {
    private Channel channel = null;

    public void connect() {
        EventLoopGroup executors = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(executors);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    channel = ch;
                    ch.pipeline().addLast(new ClientChildHandler());
                }
            });
            ChannelFuture future = bootstrap.connect("localhost", 8888);
            System.out.println("connected to server");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executors.shutdownGracefully();
        }
    }

    public void send(String message) {
        System.out.println("from client:" + message);
        ByteBuf buf = Unpooled.copiedBuffer(message.getBytes());
        channel.writeAndFlush(buf);
    }

    public void closeConnection() {
        send("__bye__");
        channel.close();
    }

    private static class ClientChildHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = null;
            try {
                byteBuf = (ByteBuf) msg;
                ClientFrame.INSTANCE.updateText(byteBuf.toString(StandardCharsets.UTF_8));
            } finally {
                if (null != byteBuf) {
                    // 无论如何都要释放，因为netty的byteBuf使用的是操作系统的内存，jvm通过referenceCount来回收
                    ReferenceCountUtil.release(byteBuf);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }
    }
}
