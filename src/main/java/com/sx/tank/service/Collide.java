package com.sx.tank.service;

import com.sx.tank.model.AbstractGameObject;

import java.io.Serializable;

public interface Collide extends Serializable {

    /**
     * 两个物体碰撞逻辑
     *
     * @param o1 o1
     * @param o2 o2
     * @return boolean
     */
    boolean collide(AbstractGameObject o1, AbstractGameObject o2);
}
