package com.sx.tank.utils;

import com.sx.tank.model.TankMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        System.out.println("decode:");
        if (buf.readableBytes() < 8) return;
        TankMessage tankMessage = new TankMessage();
        tankMessage.setX(buf.readInt());
        tankMessage.setY(buf.readInt());
        out.add(tankMessage);
    }
}
