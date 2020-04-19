package com.sx.tank.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        // 负责接客
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        // 负责服务
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        // Server启动辅助类
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        // 异步 netty 帮我们内部处理了accept的过程
        b.channel(NioServerSocketChannel.class);
        // 定义处理客户端传过来的数据类
        b.childHandler(new MyChannelHandler());

        ChannelFuture future = b.bind(8888);
        // 等待关闭
        future.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}

class MyChannelHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new MyChildChannelHandler());
    }
}

class MyChildChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(buf.toString());
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}