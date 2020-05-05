package com.sx.tank.model;

import com.sx.tank.TankFrame;
import com.sx.tank.utils.Dir;
import com.sx.tank.utils.MsgType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class TankMoveOrDirChangeMsg extends Msg {
    private int x, y;
    private Dir dir;
    private UUID uuid;

    public TankMoveOrDirChangeMsg() {
    }

    public TankMoveOrDirChangeMsg(int x, int y, Dir dir, UUID uuid) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.uuid = uuid;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeInt(this.x);
            dos.writeInt(this.y);
            dos.writeInt(dir.ordinal());
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
            dos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
            this.uuid = new UUID(dis.readLong(), dis.readLong());
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
        // 如果是自己
        if (this.uuid.equals(TankFrame.INSTANCE.getGm().getPlayer().getId())) {
            return;
        }
        // 不是自己
        Player p = TankFrame.INSTANCE.getGm().findTankByUuid(this.uuid);
        if (null != p) {
            p.setMoving(true);
            p.setX(this.x);
            p.setY(this.y);
            p.setDir(this.dir);
        }
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankMoveOrDirChange;
    }

    @Override
    public String toString() {
        return "TankMoveOrDirChangeMsg{" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", uuid=" + uuid +
                '}';
    }
}
