package com.sx.tank.utils;

import com.sx.tank.model.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        if (buf.readableBytes() < 8) return;
        buf.markReaderIndex();
        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        if (buf.readableBytes() < length) {
            buf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        Msg msg = (Msg) Class.forName("com.sx.tank.model." + msgType.toString() + "Msg").getDeclaredConstructor().newInstance();
        msg.parse(bytes);
        out.add(msg);
    }
}
