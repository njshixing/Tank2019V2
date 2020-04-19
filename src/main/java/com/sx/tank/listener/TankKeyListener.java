package com.sx.tank.listener;

import com.sx.tank.TankFrame;
import com.sx.tank.model.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TankKeyListener implements KeyListener {
    private Player player;

    public TankKeyListener(Player player) {
        this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_S) TankFrame.INSTANCE.getGm().save();
        else if (key == KeyEvent.VK_L) TankFrame.INSTANCE.getGm().load();
        else player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }
}
