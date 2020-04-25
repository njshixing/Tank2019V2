package com.sx.tank.net.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.charset.StandardCharsets;

public class ChatServer {
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

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

        ChannelFuture future = b.bind(8888).sync();
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
        String message = buf.toString(StandardCharsets.UTF_8);
        System.out.println(message);
        if ("__bye__".equals(message)) {
            System.out.println(ctx.channel().remoteAddress() + " ready to quit...");
            ChatServer.clients.remove(ctx.channel());
            ReferenceCountUtil.release(msg);
        } else {
            ChatServer.clients.writeAndFlush(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 异常->删除client
        ChatServer.clients.remove(ctx.channel());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 如果连接成功了
        ChatServer.clients.add(ctx.channel());
    }
}
