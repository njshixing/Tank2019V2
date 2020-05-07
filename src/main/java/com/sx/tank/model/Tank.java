package com.sx.tank.model;

import com.sx.tank.TankFrame;
import com.sx.tank.utils.Dir;
import com.sx.tank.utils.Group;
import com.sx.tank.utils.ResourceMgr;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Tank extends AbstractGameObject {
    private static final Integer SPEED = 5;

    // 坐标
    private int x, y;

    // 上一步的坐标
    private int oldX, oldY;

    // 坦克的高度和宽度
    private int width, height;

    // 方向
    private Dir dir;

    private boolean moving = true;

    // 前后左右四个方向的布尔值
    private boolean bL, bR, bD, bU;

    private Group group;
    private UUID id = UUID.randomUUID();

    private boolean live = true;
    private Random r = new Random();

    private Rectangle rect;

    public Tank(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.dir = dir;
        this.group = group;
        this.width = ResourceMgr.goodTankU.getWidth();
        this.height = ResourceMgr.goodTankU.getHeight();
        this.rect = new Rectangle(x, y, width, height);
    }

    public Tank() {
    }

    @Override
    public void paint(Graphics g) {
        if (!this.isLive()) {
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.yellow);
        g.setColor(c);
        switch (dir) {
            case L:
                g.drawImage(ResourceMgr.badTankL, x, y, null);
                break;
            case U:
                g.drawImage(ResourceMgr.badTankU, x, y, null);
                break;
            case R:
                g.drawImage(ResourceMgr.badTankR, x, y, null);
                break;
            case D:
                g.drawImage(ResourceMgr.badTankD, x, y, null);
                break;
        }

        // 移动
        move();

        rect.x = x;
        rect.y = y;

        if (r.nextInt(100) > 90) {
            this.dir = Dir.randomDir();
        }

        if (r.nextInt(100) > 90) {
            fire();
        }
    }

    public void move() {
        if (!moving) return;

        oldX = x;
        oldY = y;

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
        // 边界检查
        boundsCheck();
    }

    private void boundsCheck() {
        if (x < 0 || y < 30 || x + width > TankFrame.GAME_WIDTH || y + height > TankFrame.GAME_HEIGHT) {
            // 回退一步
            this.back();
        }
    }

    public void back() {
        this.x = oldX;
        this.y = oldY;
    }

    private void fire() {
        int bX = x + width / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = y + height / 2 - ResourceMgr.bulletU.getHeight() / 2;
        Bullet b = new Bullet(id, bX, bY, getDir(), getGroup());
        TankFrame.INSTANCE.getGm().addObject(b);
    }


    public void die() {
        this.setLive(false);
        TankFrame.INSTANCE.getGm().addObject(new Explode(x, y));
    }
}
