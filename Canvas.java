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
    private boolean isRunning = false;
    private Direction direction;
    private int ticks = 0;
    private Apple apple;
    private Random random;
    private int score = 0;
    private int snakeSize = 20;
    private boolean hasDied = false;

    public Canvas()
    {
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setMinimumSize(new Dimension(WIDTH,HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH,HEIGHT));
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));

        start();
    }

    private void newGame(){
        hasDied = false;
        snakeSize = 20;
        score = 0;
        random = new Random();
        tail = new ArrayList<>();

        head = new SnakeBlock();
        head.setY(10);
        head.setX(10);

        apple = new Apple();
        randomlyPlaceApple();
    }

    private void randomlyPlaceApple(){
        apple.setY(random.nextInt(HEIGHT/10-1));
        apple.setX(random.nextInt(WIDTH/10-1));
    }

    public void start(){
        isRunning = true;
        thread = new Thread(this);
        thread.start();
        direction = Direction.DOWN;
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

        if(ticks++ > 50) {
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

            if(head.collidesWith(apple)) {
                score++;
                randomlyPlaceApple();
                snakeSize+=5;
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

        apple.draw(g);
        head.draw(g);

        try {
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
