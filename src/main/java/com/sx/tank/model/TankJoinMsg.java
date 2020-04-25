package com.sx.tank.model;

import com.sx.tank.TankFrame;
import com.sx.tank.net.TankWarClient;
import com.sx.tank.utils.Dir;
import com.sx.tank.utils.Group;
import lombok.Data;

import java.io.*;
import java.util.UUID;

@Data
public class TankJoinMsg {
    private int x, y;
    private Dir dir;
    private Group group;
    private UUID uuid;
    private boolean moving;

    public TankJoinMsg() {
    }

    public TankJoinMsg(Player p) {
        this.x = p.getX();
        this.y = p.getY();
        this.dir = p.getDir();
        this.moving = p.isMoving();
        this.group = p.getGroup();
        this.uuid = p.getId();
    }

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
            dos.writeBoolean(moving);
            dos.writeInt(group.ordinal());
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

    public void parseBytes(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
            this.moving = dis.readBoolean();
            this.group = Group.values()[dis.readInt()];
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
    public String toString() {
        return "TankMessage{" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", group=" + group +
                ", uuid='" + uuid + '\'' +
                ", moving=" + moving +
                '}';
    }

    public void handle() {
        if (this.uuid.equals(TankFrame.INSTANCE.getGm().getPlayer().getId())) {
            return;
        }
        if (TankFrame.INSTANCE.getGm().findTankByUuid(this.uuid) != null) {
            return;
        }

        Player player = new Player(this);
        TankFrame.INSTANCE.getGm().addObject(player);
        TankWarClient.INSTANCE.send(new TankJoinMsg(TankFrame.INSTANCE.getGm().getPlayer()));
    }
}
