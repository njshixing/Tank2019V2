package com.sx.tank.model;

import com.sx.tank.utils.Dir;
import lombok.Data;

import java.awt.*;

@Data
public class GameModel {
    private Player player;

    public GameModel() {
        // 初始化游戏里面的物体
        initGameObject();
    }

    private void initGameObject() {
        // 初始化player
        player = new Player(50, 50, Dir.randomDir());
    }

    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.setColor(c);

        // 画出player
        player.paint(g);
    }
}
