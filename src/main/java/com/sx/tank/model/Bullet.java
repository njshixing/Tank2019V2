package com.sx.tank.model;

import com.sx.tank.TankFrame;
import com.sx.tank.utils.Dir;
import com.sx.tank.utils.Group;
import com.sx.tank.utils.ResourceMgr;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Bullet extends AbstractGameObject {
    public static final int W = ResourceMgr.bulletU.getWidth();
    public static final int H = ResourceMgr.bulletU.getHeight();
    private static final Integer SPEED = 6;
    private UUID playerId;
    private int x, y;
    private Dir dir;
    private Group group;
    private UUID id = UUID.randomUUID();
    private boolean live;
    private Rectangle rect;

    public Bullet(UUID playerId, int x, int y, Dir dir, Group group) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.live = true;
        rect = new Rectangle(x, y, W, H);
    }

    @Override
    protected void paint(Graphics g) {
        switch (dir) {
            case L:
                g.drawImage(ResourceMgr.bulletL, x, y, null);
                break;
            case U:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case R:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
            case D:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
        }
        move();
    }

    private void move() {
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
        rect.x = x;
        rect.y = y;

        // 边缘检测
        boundsCheck();
    }

    private void boundsCheck() {
        if (x < 0 || y < 30 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT) {
            live = false;
        }
    }

    @Override
    public boolean isLive() {
        return live;
    }

    public void die() {
        this.setLive(false);
    }
}
