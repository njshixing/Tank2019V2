package com.sx.tank.service;

import com.sx.tank.model.AbstractGameObject;
import com.sx.tank.utils.PropertyMgr;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 责任链模式
 *
 * @author shixing
 */
public class CollideChain implements Collide {
    List<Collide> collideList = new ArrayList<>();

    public CollideChain() {
        String[] colliders = PropertyMgr.get("colliders").split(",");
        for (String collider : colliders) {
            try {
                Class clazz = Class.forName("com.sx.tank.service." + collider);
                Collide collide = (Collide) clazz.getConstructor().newInstance();
                collideList.add(collide);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean collide(AbstractGameObject o1, AbstractGameObject o2) {
        for (Collide collide : collideList) {
            System.out.println(collideList.size());
            if (!collide.collide(o1, o2)) {
                return false;
            }
        }
        return true;
    }
}
