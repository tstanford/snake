package org.example;

import java.awt.*;

public class TreeBlock extends Block {

    public TreeBlock(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x*10,y*10,10,10);
    }
}
