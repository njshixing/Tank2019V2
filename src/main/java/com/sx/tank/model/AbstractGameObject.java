package com.sx.tank.model;

import java.awt.*;
import java.io.Serializable;

public abstract class AbstractGameObject implements Serializable {
    protected abstract void paint(Graphics g);

    protected abstract boolean isLive();
}
