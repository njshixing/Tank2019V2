package com.sx.tank.utils;

import java.util.Random;

public enum Dir {
    L, R, U, D;

    private static Random random = new Random();

    public static Dir randomDir() {
        return values()[random.nextInt(values().length)];
    }
}
