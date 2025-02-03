package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Levels {

    private Random random;
    private int currentLevelIndex = 0;
    private ArrayList<Block[][]> levels;

    public Block[][] reset(){
        currentLevelIndex = 0;
        return getCurrentLevel();
    }

    public Block[][] getCurrentLevel(){
        return levels.get(currentLevelIndex);
    }

    public Block[][] nextLevel(){
        currentLevelIndex = ++currentLevelIndex % levels.size();
        return getCurrentLevel();
    }

    public Levels(int width, int height) {
        currentLevelIndex = 0;
        random = new Random();
        levels = new ArrayList<>();
        var level1 = new WallBlock[height][width];

        for(int i=0;i<60;i++){
            if(i==0 || i == 59) {
                for(int j=0;j<60;j++){
                    level1[i][j] = new WallBlock(j,i);
                }
            } else {
                level1[i][0] = new WallBlock(0,i);
                level1[i][59] = new WallBlock(59,i);
            }
        }

        levels.add(level1);

        var level2 = new Block[height][width];

        for(int y=(int)Math.round((height/10)*0.25); y<(height/10)*0.75; y++) {
            for(int x=(int)Math.round((width/10)*0.25); x<(width/10)*0.75; x++) {
                if(random.nextBoolean()) {
                    level2[y][x] = new WallBlock(x,y);
                } else {
                    level2[y][x] = new TreeBlock(x,y);
                }
            }
        }

        levels.add(level2);

        var level3 = new Block[height][width];
        for(int x=(int)Math.round((width/10)*0.25); x<width; x++) {
            level3[(int)Math.round((width/10)*0.25)][x] = new WallBlock(x,(int)Math.round((height/10)*0.25));
        }
        for(int x=0; x<=(int)Math.round((width/10)*0.75); x++) {
            level3[(int)Math.round((width/10)*0.75)][x] = new WallBlock(x,(int)Math.round((height/10)*0.75));
        }

        levels.add(level3);
    }
}
