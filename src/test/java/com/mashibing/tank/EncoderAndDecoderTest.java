package com.mashibing.tank;

import com.sx.tank.model.Tank;
import com.sx.tank.model.TankMessage;
import com.sx.tank.utils.MsgDecoder;
import com.sx.tank.utils.MsgEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class EncoderAndDecoderTest {

    @Test
    public void encoder() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MsgEncoder());
        TankMessage tankMessage = new TankMessage();
        tankMessage.setX(5);
        tankMessage.setY(8);
        ch.writeOutbound(tankMessage);

        ByteBuf buf = ch.readOutbound();

        Assert.assertEquals(5, buf.readInt());
        Assert.assertEquals(8, buf.readInt());
    }

    @Test
    public void decoder() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MsgDecoder());

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(5);
        buf.writeInt(8);

        ch.writeInbound(buf);

        TankMessage message = ch.readInbound();
        Assert.assertEquals(5, message.getX());
        Assert.assertEquals(8, message.getY());
    }
}
