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
import java.util.stream.Collectors;

@Data
public class GameModel implements Serializable {
    private Player player;
    private List<AbstractGameObject> objectList;
    private CollideChain collideChain;

    public GameModel() {
        objectList = new ArrayList<>();
        collideChain = new CollideChain();
        // 初始化游戏里面的物体
        initGameObject();
    }

    private void initGameObject() {
        // 初始化player
        player = new Player(50, 50, Dir.randomDir(), Group.GOOD);
        // 敌人坦克
        int tankCount = new Random().nextInt(5) + 3;
        for (int i = 0; i < tankCount; i++) {
            objectList.add(new Tank(100 + new Random().nextInt(100), 100, Dir.randomDir(), Group.BAD));
        }
    }

    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.setColor(c);
        g.drawString("objects:" + objectList.size(), 10, 50);
        g.setColor(c);
        System.out.println("objects:" + objectList.size());
        // 去除die的物体
        objectList = objectList.stream().filter(AbstractGameObject::isLive).collect(Collectors.toList());

        // 画出player
        player.paint(g);

        for (int i = 0; i < objectList.size(); i++) {
            AbstractGameObject object = objectList.get(i);
            if (!object.isLive()) {
                objectList.remove(object);
                break;
            }
        }

        for (int i = 0; i < objectList.size(); i++) {
            AbstractGameObject go1 = objectList.get(i);
            for (int j = 0; j < objectList.size(); j++) {
                AbstractGameObject go2 = objectList.get(j);
                collideChain.collide(go1, go2);
            }
            if (objectList.get(i).isLive()) {
                objectList.get(i).paint(g);
            }
        }
    }

    public void addObject(AbstractGameObject obj) {
        objectList.add(obj);
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
}
