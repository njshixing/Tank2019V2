package com.sx.tank.net;

import com.sx.tank.TankFrame;
import com.sx.tank.model.Msg;
import com.sx.tank.model.Player;
import com.sx.tank.model.TankJoinMsg;
import com.sx.tank.utils.MsgDecoder;
import com.sx.tank.utils.MsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TankWarClient {
    public static final TankWarClient INSTANCE = new TankWarClient();
    private Channel channel = null;

    private TankWarClient() {

    }

    public void connect() {
        EventLoopGroup executors = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        try {
            ChannelFuture future = bootstrap.group(executors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            channel = ch;
                            ch.pipeline()
                                    .addLast(new MsgDecoder())
                                    .addLast(new MsgEncoder())
                                    .addLast(new ClientChildHandler());
                        }
                    }).connect("localhost", 12306).sync();
            System.out.println("connected to server");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executors.shutdownGracefully();
        }
    }

    public void send(Msg msg) {
        channel.writeAndFlush(msg);
    }

    public void closeConnection() {
        channel.close();
    }

    private static class ClientChildHandler extends SimpleChannelInboundHandler<Msg> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
            msg.handle();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Player p = TankFrame.INSTANCE.getGm().getPlayer();
            System.out.println(p.getId() + " online");
            ctx.writeAndFlush(new TankJoinMsg(p));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }
    }
}
