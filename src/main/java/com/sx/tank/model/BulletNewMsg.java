package com.sx.tank.model;

import com.sx.tank.TankFrame;
import com.sx.tank.utils.Dir;
import com.sx.tank.utils.Group;
import com.sx.tank.utils.MsgType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class BulletNewMsg extends Msg {
    private UUID playerId;
    private UUID id;
    private Dir dir;
    private int x, y;
    private Group group;

    public BulletNewMsg() {
    }

    public BulletNewMsg(Bullet bullet) {
        this.playerId = bullet.getPlayerId();
        this.id = bullet.getId();
        this.dir = bullet.getDir();
        this.x = bullet.getX();
        this.y = bullet.getY();
        this.group = bullet.getGroup();
    }

    @Override
    public byte[] toBytes() {
        byte[] bytes = null;
        DataOutputStream dos = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            dos = new DataOutputStream(baos);
            dos.writeLong(playerId.getMostSignificantBits());
            dos.writeLong(playerId.getLeastSignificantBits());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeInt(dir.ordinal());
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(group.ordinal());
            dos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Objects.requireNonNull(dos).close();
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
            playerId = new UUID(dis.readLong(), dis.readLong());
            id = new UUID(dis.readLong(), dis.readLong());
            dir = Dir.values()[dis.readInt()];
            x = dis.readInt();
            y = dis.readInt();
            group = Group.values()[dis.readInt()];
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
        if (this.playerId.equals(TankFrame.INSTANCE.getGm().getPlayer().getId()))
            return;
        Bullet bullet = new Bullet(this.playerId, this.x, this.y, this.dir, this.group);
        bullet.setId(this.id);
        TankFrame.INSTANCE.getGm().addObject(bullet);
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.BulletNew;
    }
}
