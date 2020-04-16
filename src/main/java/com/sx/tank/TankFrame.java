package com.sx.tank;


import com.sx.tank.model.GameModel;

import java.awt.*;

public class TankFrame extends Frame {
    public static final TankFrame INSTANCE = new TankFrame();
    public static final int GAME_WIDTH = 1024, GAME_HEIGHT = 768;
    private Image offScreenImage = null;

    private GameModel gameModel;

    private TankFrame() {
        this.setTitle("tank war");
        this.setLocation(400, 100);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        gameModel = new GameModel();
//        this.addKeyListener(new com.mashibing.tank.TankFrame.TankKeyListener());
    }

    @Override
    public void paint(Graphics g) {
        gameModel.paint(g);
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }
}
