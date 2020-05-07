package com.sx.tank.service;

import com.sx.tank.TankFrame;
import com.sx.tank.model.Bullet;
import com.sx.tank.model.BulletNewMsg;
import com.sx.tank.model.Player;
import com.sx.tank.net.TankWarClient;
import com.sx.tank.utils.ResourceMgr;

public class DefaultFireStrategy implements FireStrategy {
    @Override
    public void fire(Player player) {
        int bX = player.getX() + ResourceMgr.goodTankU.getWidth() / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = player.getY() + ResourceMgr.goodTankU.getHeight() / 2 - ResourceMgr.bulletU.getHeight() / 2;
        Bullet b = new Bullet(player.getId(), bX, bY, player.getDir(), player.getGroup());
        TankFrame.INSTANCE.getGm().addObject(b);
        TankWarClient.INSTANCE.send(new BulletNewMsg(b));
    }
}
