package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

public class Canvas extends JPanel implements Runnable, KeyListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private Thread thread;
    private SnakeBlock head;
    private java.util.List<SnakeBlock> tail;
    private java.util.List<Apple> apples;
    private java.util.List<Block> wall;
    private boolean isRunning = false;
    private Direction direction;
    private int ticks;
    private Random random;
    private int score;
    private int snakeSize;
    private boolean hasDied = false;
    private int numberOfApples;
    private Block[][] currentLevel;
    private Levels levels;
    private int currentLevelIndex = 0;

    public Canvas()
    {
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setMinimumSize(new Dimension(WIDTH,HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH,HEIGHT));
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));

        levels = new Levels(WIDTH,HEIGHT);
        start();
    }

    private void newGame(){
        currentLevelIndex = 0;
        direction = Direction.RIGHT;
        numberOfApples = 1;
        ticks=0;
        hasDied = false;
        snakeSize = 20;
        score = 0;
        random = new Random();
        tail = new ArrayList<>();
        apples = new ArrayList<>();
        currentLevel = levels.reset();

        head = new SnakeBlock();
        head.setY(10);
        head.setX(10);

        for(int i=0;i<numberOfApples;i++) {
            Apple apple = new Apple();
            apples.add(apple);
            randomlyPlaceApple(apple);
        }
    }

    private void randomlyPlaceApple(Apple apple){
        do {
            apple.setY(random.nextInt(HEIGHT / 10 - 1));
            apple.setX(random.nextInt(WIDTH / 10 - 1));
        } while(appleIncorrectlyPlaced(apple));
    }

    public void start(){
        isRunning = true;
        thread = new Thread(this);
        thread.start();
        direction = Direction.DOWN;
    }

    public boolean appleIncorrectlyPlaced(Apple apple){

        int x = apple.getX();
        int y = apple.getY();

        if(apple.collidesWith(head)) {
            return true;
        }

        if(currentLevel[y][x] != null) {
            if (apple.collidesWith(currentLevel[y][x]))
                return true;
        }

        for(int i=0; i< tail.size(); i++) {
            if(apple.collidesWith(tail.get(i))) {
                return true;
            }
        }

        return false;
    }

    public void stop(){
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(){
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(hasDied) {
            ticks++;
            if(ticks < 500) {
                head.Kill();
                for(SnakeBlock snakeBlock : tail) {
                    snakeBlock.Kill();
                }
            } else {
                ticks = 0;
                newGame();
            }
            return;
        }

        if(ticks++ >50) {
            ticks=0;
            if (tail.size() > snakeSize) {
                tail.remove(tail.size() - 1);
            }

            tail.add(0, new SnakeBlock(head));

            if (direction == Direction.DOWN)
                head.setY(head.getY() + 1);
            else if (direction == Direction.UP)
                head.setY(head.getY() - 1);
            else if (direction == Direction.LEFT)
                head.setX(head.getX() - 1);
            else if (direction == Direction.RIGHT)
                head.setX(head.getX() + 1);

            for(var apple : apples) {
                if (head.collidesWith(apple)) {
                    score++;
                    randomlyPlaceApple(apple);
                    snakeSize += 2;
                    if(score%10 == 0) {
                        currentLevel = levels.nextLevel();
                        head.setY(10);
                        head.setX(10);
                        direction = Direction.DOWN;
                        tail.clear();
                        for(var a : apples) {
                            randomlyPlaceApple(a);
                        }
                    }
                }
            }

            int headX = head.getX();
            if(headX<0) headX=0;
            if(headX>WIDTH-1) headX=WIDTH-1;
            int headY = head.getY();
            if(headY<0) headY=0;
            if(headY>HEIGHT-1) headX=HEIGHT-1;

            if(currentLevel[headY][headX] != null) {
                if(head.collidesWith(currentLevel[headY][headX])) {
                    hasDied = true;
                    return;
                }
            }

            for(SnakeBlock snakeBlock : tail) {
                if(head.collidesWith(snakeBlock)) {
                    hasDied = true;
                    break;
                }
            }

            if(head.getX() >= WIDTH/10)
                head.setX(0);

            if(head.getX() < 0)
                head.setX(WIDTH/10-1);

            if(head.getY() >= HEIGHT/10)
                head.setY(0);

            if(head.getY() < 0)
                head.setY(HEIGHT/10-1);
        }
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0,0,WIDTH,HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,WIDTH,HEIGHT);

        head.draw(g);

        try {
            for(int wallY = 0;wallY < currentLevel.length; wallY++){
                for(int wallX = 0;wallX < currentLevel[wallY].length; wallX++){
                    if(currentLevel[wallY][wallX] != null){
                        currentLevel[wallY][wallX].draw(g);
                    }
                }
            }

            for(var apple : apples) {
                apple.draw(g);
            }

            for (var block : tail) {
                block.draw(g);
            }
        }
        catch(ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException thrown");
        }

        g.setColor(Color.WHITE);
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
        g.setFont(newFont);
        g.drawString(Integer.toString(score), 15,25);
    }

    @Override
    public void run() {
        newGame();
        while(isRunning) {
            update();
            this.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT && direction != Direction.LEFT)
            direction = Direction.RIGHT;
        if(e.getKeyCode() == KeyEvent.VK_LEFT && direction != Direction.RIGHT)
            direction = Direction.LEFT;
        if(e.getKeyCode() == KeyEvent.VK_UP && direction != Direction.DOWN)
            direction = Direction.UP;
        if(e.getKeyCode() == KeyEvent.VK_DOWN && direction != Direction.UP)
            direction = Direction.DOWN;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
