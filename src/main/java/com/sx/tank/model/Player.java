package com.sx.tank.model;

import com.sx.tank.net.TankWarClient;
import com.sx.tank.service.FireStrategy;
import com.sx.tank.utils.Dir;
import com.sx.tank.utils.Group;
import com.sx.tank.utils.PropertyMgr;
import com.sx.tank.utils.ResourceMgr;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Player extends AbstractGameObject {
    private static final Integer SPEED = 5;

    // 坐标
    private int x, y;

    // 方向
    private Dir dir;

    private boolean moving = false;

    // 前后左右四个方向的布尔值
    private boolean bL, bR, bD, bU;

    private Group group;

    private boolean live = true;

    private UUID id = UUID.randomUUID();

    public Player(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
    }

    public Player() {
    }

    public Player(TankJoinMsg tankJoinMsg) {
        this.x = tankJoinMsg.getX();
        this.y = tankJoinMsg.getY();
        this.dir = tankJoinMsg.getDir();
        this.group = tankJoinMsg.getGroup();
        this.moving = tankJoinMsg.isMoving();
        this.id = tankJoinMsg.getUuid();
    }

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.yellow);
        g.drawString(this.getId().toString(), getX(), getY() - 10);
        g.setColor(c);
        switch (dir) {
            case L:
                g.drawImage(this.group.equals(Group.BAD) ? ResourceMgr.badTankL : ResourceMgr.goodTankL, x, y, null);
                break;
            case U:
                g.drawImage(this.group.equals(Group.BAD) ? ResourceMgr.badTankU : ResourceMgr.goodTankU, x, y, null);
                break;
            case R:
                g.drawImage(this.group.equals(Group.BAD) ? ResourceMgr.badTankR : ResourceMgr.goodTankR, x, y, null);
                break;
            case D:
                g.drawImage(this.group.equals(Group.BAD) ? ResourceMgr.badTankD : ResourceMgr.goodTankD, x, y, null);
                break;
        }

        // 移动
        move();
    }

    public void move() {
        if (!moving) return;

        switch (dir) {
            case L:
                x -= SPEED;
                break;
            case U:
                y -= SPEED;
                break;
            case R:
                x += SPEED;
                break;
            case D:
                y += SPEED;
                break;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
        }
        // 设置方向
        setMainDir();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_SPACE:
                // 按空格键打出子弹
                fire();
                break;
        }
        // 设置方向
        setMainDir();
    }

    private void fire() {
        String fireStrategy = PropertyMgr.get("tankFireStrategy");
        try {
            Class<FireStrategy> clazz = (Class<FireStrategy>) Class.forName("com.sx.tank.service." + fireStrategy);
            FireStrategy fire = clazz.getDeclaredConstructor().newInstance();
            fire.fire(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMainDir() {
        boolean oldMoving = this.moving;
        if (!bU && !bD && !bL && !bR) {
            moving = false;
            TankWarClient.INSTANCE.send(new TankStopMsg(this.id, this.x, this.y));
        } else {
            moving = true;
            if (bU && !bD && !bL && !bR) {
                dir = Dir.U;
            }
            if (!bU && bD && !bL && !bR) {
                dir = Dir.D;
            }
            if (!bU && !bD && bL && !bR) {
                dir = Dir.L;
            }
            if (!bU && !bD && !bL && bR) {
                dir = Dir.R;
            }
        }
        if (this.moving != oldMoving) {
            TankWarClient.INSTANCE.send(new TankMoveOrDirChangeMsg(this.x, this.y, this.dir, this.id));
        }
    }
}
