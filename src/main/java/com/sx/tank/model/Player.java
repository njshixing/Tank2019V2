package com.sx.tank.model;

import com.mashibing.tank.Group;
import com.mashibing.tank.ResourceMgr;
import com.sx.tank.utils.Dir;
import lombok.Data;

import java.awt.*;

@Data
public class Player {

    /**
     * 坐标
     */
    private int x, y;
    /**
     * 方向
     */
    private Dir dir;

    public Player(int x, int y, Dir dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Player() {
    }

    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.yellow);
        g.setColor(c);

        switch (dir) {
            case L:
                g.drawImage(ResourceMgr.goodTankL, x, y, null);
                break;
            case U:
                g.drawImage(ResourceMgr.goodTankU, x, y, null);
                break;
            case R:
                g.drawImage(ResourceMgr.goodTankR, x, y, null);
                break;
            case D:
                g.drawImage(ResourceMgr.goodTankD, x, y, null);
                break;
        }
    }
}
