import java.awt.*;
import java.util.UUID;

public abstract class Block {

    private UUID id = UUID.randomUUID();

    public Block(Block head) {
        this.x = head.getX();
        this.y = head.getY();
    }

    public UUID getId() {
        return id;
    }

    public int x,y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean collidesWith(Block block) {
        if(this.x == block.x && this.y == block.y) {
            return true;
        }
        return false;
    }

    public Block() {
    }

    public abstract void draw(Graphics g );

}
