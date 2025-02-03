import java.awt.*;

public class WallBlock extends Block {

    public WallBlock(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x*10,y*10,10,10);
    }
}
