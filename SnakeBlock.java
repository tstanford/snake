import java.awt.*;

public class SnakeBlock extends Block {

    private boolean isDead = false;

    public SnakeBlock(Block head) {
        super(head);
    }

    public SnakeBlock() {
    }

    public void Kill(){
        this.isDead = true;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.isDead ? Color.RED : Color.GREEN);
        g.fillRect(x*10,y*10,10,10);
    }
}
