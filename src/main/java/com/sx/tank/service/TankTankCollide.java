package com.sx.tank.service;

import com.sx.tank.model.AbstractGameObject;
import com.sx.tank.model.Tank;

public class TankTankCollide implements Collide {
    @Override
    public boolean collide(AbstractGameObject o1, AbstractGameObject o2) {
        if (o1 instanceof Tank && o2 instanceof Tank) {
            Tank t1 = (Tank) o1;
            Tank t2 = (Tank) o2;
            if (!t1.isLive() || !t2.isLive()) {
                return true;
            }
            if (t1.getGroup() == t1.getGroup() && t1.getRect().intersects(t2.getRect())) {
                t1.back();
                return false;
            }
        }
        return false;
    }
}
