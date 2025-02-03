package org.example;

import java.awt.*;

public class Apple extends Block {
    public Apple() {
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x*10,y*10,10,10);
    }
}
