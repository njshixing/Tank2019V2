package com.sx.tank.utils;

import com.sx.tank.model.TankJoinMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        System.out.println("aaaa");
        if (buf.readableBytes() < 37) return;
        int length = buf.readInt();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        TankJoinMsg tankJoinMsg = new TankJoinMsg();
        tankJoinMsg.parseBytes(bytes);
        out.add(tankJoinMsg);
    }
}
