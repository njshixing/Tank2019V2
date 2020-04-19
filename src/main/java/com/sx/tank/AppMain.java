package com.sx.tank;

import com.sx.tank.model.Audio;

public class AppMain {
    public static void main(String[] args) {
        TankFrame.INSTANCE.setVisible(true);
        new Thread(()->new Audio("audio/war1.wav").loop()).start();
        new Thread(()-> {
            for (; ; ) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TankFrame.INSTANCE.repaint();
            }
        }).start();
    }
}
