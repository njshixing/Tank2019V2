package com.sx.tank.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup clientGroup = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        b.group(clientGroup);
        b.channel(NioSocketChannel.class);
        b.handler(new ClientChannelHandler());
        ChannelFuture future = b.connect("localhost", 8888).sync();
        future.channel().closeFuture().sync();
        clientGroup.shutdownGracefully();
    }

    private static class ClientChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ClientChildHandler());
        }
    }

    private static class ClientChildHandler extends ChannelInboundHandlerAdapter {
        // 第一次连接到server
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ByteBuf buf = Unpooled.copiedBuffer("hello".getBytes());
            ctx.writeAndFlush(buf);
        }

        // 读取信息
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println(byteBuf.toString(StandardCharsets.UTF_8));
        }

        // 异常处理
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
