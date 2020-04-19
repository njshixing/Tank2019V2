package com.sx.tank.utils;

import com.sx.tank.model.TankMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgEncoder extends MessageToByteEncoder<TankMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TankMessage msg, ByteBuf out) throws Exception {
        System.out.println("encode:" + msg.toString());
        out.writeInt(msg.getX());
        out.writeInt(msg.getY());
    }
}
