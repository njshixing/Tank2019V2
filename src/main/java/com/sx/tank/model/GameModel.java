package com.sx.tank.model;

import com.sx.tank.TankFrame;
import com.sx.tank.service.CollideChain;
import com.sx.tank.utils.Dir;
import com.sx.tank.utils.Group;
import lombok.Data;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
public class GameModel implements Serializable {
    private Player player;
    private List<AbstractGameObject> objects;
    private CollideChain collideChain = new CollideChain();

    private Random r = new Random();

    public GameModel() {
        // 初始化游戏里面的物体
        initGameObject();
    }

    private void initGameObject() {
        // 初始化player
        player = new Player(50 + r.nextInt(700), 50 + r.nextInt(600), Dir.randomDir(), Group.values()[r.nextInt(Group.values().length)]);
        objects = new ArrayList<>();
    }

    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("objects:" + objects.size(), 10, 50);
        g.setColor(c);
        player.paint(g);

        for (int i = 0; i < objects.size(); i++) {
            AbstractGameObject object = objects.get(i);
            if (!object.isLive()) {
                objects.remove(object);
                break;
            }
        }

        for (int i = 0; i < objects.size(); i++) {
            AbstractGameObject go1 = objects.get(i);
            for (int j = 0; j < objects.size(); j++) {
                AbstractGameObject go2 = objects.get(j);
                collideChain.collide(go1, go2);
            }
            if (objects.get(i).isLive()) {
                objects.get(i).paint(g);
            }
        }
    }

    public void addObject(AbstractGameObject obj) {
        objects.add(obj);
    }

    public void save() {
        ObjectOutputStream objectOutputStream = null;
        try {
            FileOutputStream fos = new FileOutputStream(new File("F:/workspace/tank.dat"));
            objectOutputStream = new ObjectOutputStream(fos);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load() {
        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(new File("F:/workspace/tank.dat"));
            ois = new ObjectInputStream(fis);
            TankFrame.INSTANCE.setGm((GameModel) ois.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != ois) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Player findTankByUuid(UUID uuid) {
        for (AbstractGameObject object : objects) {
            if (object instanceof Player) {
                Player p = (Player) object;
                if (p.getId().equals(uuid)) {
                    return p;
                }
            }
        }
        return null;
    }

    public Bullet findBulletByUUID(UUID bulletId) {
        for (AbstractGameObject o : objects) {
            if (o instanceof Bullet) {
                Bullet b = (Bullet) o;
                if (bulletId.equals(b.getId())) return b;
            }
        }

        return null;
    }
}
