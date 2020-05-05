package com.sx.tank.utils;

import com.sx.tank.model.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgEncoder extends MessageToByteEncoder<Msg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getMsgType().ordinal());
        byte[] bytes = msg.toBytes();
        // 先告知长度
        out.writeInt(bytes.length);
        // 再写内容
        out.writeBytes(bytes);
    }
}
