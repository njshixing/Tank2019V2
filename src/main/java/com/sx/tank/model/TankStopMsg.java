package com.sx.tank.model;

import com.sx.tank.TankFrame;
import com.sx.tank.utils.MsgType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class TankStopMsg extends Msg {
    private UUID uuid;
    private int x, y;

    public TankStopMsg() {
    }

    public TankStopMsg(UUID uuid, int x, int y) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        try {
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
            dos.writeInt(x);
            dos.writeInt(y);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            this.uuid = new UUID(dis.readLong(), dis.readLong());
            this.x = dis.readInt();
            this.y = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle() {
        if (this.uuid.equals(TankFrame.INSTANCE.getGm().getPlayer().getId())) {
            return;
        }
        Player p = TankFrame.INSTANCE.getGm().findTankByUuid(this.uuid);
        if (null != p) {
            p.setMoving(false);
            p.setX(x);
            p.setY(y);
        }
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankStop;
    }
}
