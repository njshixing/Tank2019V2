package com.sx.tank.model;

import com.sx.tank.utils.Dir;
import com.sx.tank.utils.Group;
import lombok.Data;

@Data
public class TankMessage {
    private int x, y;
//    private Dir dir;
//    private Group group;
//    private String uuid;

    @Override
    public String toString() {
        return "TankMessage{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
