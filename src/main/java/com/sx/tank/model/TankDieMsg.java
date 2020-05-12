package com.sx.tank.model;

import com.sx.tank.TankFrame;
import com.sx.tank.utils.MsgType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class TankDieMsg extends Msg {
    private UUID uuid;
    private UUID bulletId;

    public TankDieMsg() {
    }

    public TankDieMsg(UUID uuid, UUID bulletId) {
        this.uuid = uuid;
        this.bulletId = bulletId;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;

        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);

            dos.writeLong(bulletId.getMostSignificantBits());
            dos.writeLong(bulletId.getLeastSignificantBits());

            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());

            dos.flush();
            bytes = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (dos != null)
                    dos.close();
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
            this.bulletId = new UUID(dis.readLong(), dis.readLong());
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
        Bullet b = TankFrame.INSTANCE.getGm().findBulletByUUID(bulletId);
        if (b != null) {
            b.die();
        }
        System.out.println("==================================");
        System.out.println(uuid);
        System.out.println(TankFrame.INSTANCE.getGm().getPlayer().getId());
        System.out.println("==================================");
        if (this.uuid.equals(TankFrame.INSTANCE.getGm().getPlayer().getId())) {
            System.out.println("i am die");
            TankFrame.INSTANCE.getGm().getPlayer().die();
        } else {
            Player p = TankFrame.INSTANCE.getGm().findTankByUuid(this.uuid);
            if (null != p) {
                p.die();
            }
        }
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankDie;
    }
}
