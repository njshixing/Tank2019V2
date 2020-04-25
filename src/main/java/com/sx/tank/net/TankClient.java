package com.sx.tank.net;

import com.sx.tank.TankFrame;
import com.sx.tank.model.TankJoinMsg;
import com.sx.tank.utils.MsgDecoder;
import com.sx.tank.utils.MsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TankClient {
    public static final TankClient INSTANCE = new TankClient();
    private Channel channel = null;

    private TankClient() {

    }

    public void connect() {
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        try {
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    channel = socketChannel;
                    socketChannel.pipeline()
//                            .addLast(new MsgEncoder())
//                            .addLast(new MsgDecoder())
                            .addLast(new MyHandler());
                }
            });

            ChannelFuture future = b.connect("localhost", 12306).sync();
            System.out.println("connected to server");

            //等待关闭
            future.channel().closeFuture().sync();
            System.out.println("go on");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void closeConnection() {

        channel.close();
    }

    public void send(TankJoinMsg msg) {
        channel.writeAndFlush(msg);
    }

    static class MyHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("channelRead...");
            System.out.println(msg.toString());
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelActive...");
            ctx.writeAndFlush(TankFrame.INSTANCE.getGm().getPlayer().getId());
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

//    static class MyHandler extends SimpleChannelInboundHandler<TankJoinMsg> {
//
//        @Override
//        public void channelActive(ChannelHandlerContext ctx) throws Exception {
//            ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getGm().getPlayer()));
//        }
//
//
//        @Override
//        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//            cause.printStackTrace();
//            ctx.close();
//        }
//
//
//        @Override
//        protected void channelRead0(ChannelHandlerContext ctx, TankJoinMsg msg) throws Exception {
//            System.out.println(msg);
////            msg.handle();
//        }
//    }
}
