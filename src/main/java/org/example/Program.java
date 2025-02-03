package org.example;

import javax.swing.*;

public class Program {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new Canvas());
        frame.setTitle("Snake");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
