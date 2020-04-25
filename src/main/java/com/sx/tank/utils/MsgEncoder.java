package com.sx.tank.utils;

import com.sx.tank.model.TankJoinMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgEncoder extends MessageToByteEncoder<TankJoinMsg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TankJoinMsg msg, ByteBuf out) throws Exception {
        System.out.println("encode:" + msg.toString());
        byte[] bytes = msg.toBytes();
        // 先告知长度
        out.writeInt(bytes.length);
        // 再写内容
        out.writeBytes(bytes);
    }
}
