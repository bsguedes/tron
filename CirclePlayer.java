import java.awt.*;
import java.lang.*;
import java.applet.*;
import java.util.Random;

public class CirclePlayer extends Player {
    public boolean didsomething = false;
    public Random random;
    public boolean isClockwise = true;
    public int firstMove = -1;

    public CirclePlayer(String n, Color c, Arena a, int x, int y, byte number) {
        name = n;
        color = c;
        arena = a;
        x_max = x;
        y_max = y;
        player_no = number;
        random = new Random();
        isClockwise = random.nextInt(2) == 0;  
        firstMove = random.nextInt(4);     
    }

    @Override
    public void restart(boolean theOtherGuyCrashed){
        isClockwise = random.nextInt(2) == 0;   
        firstMove = random.nextInt(4);     
    }

    private boolean isObstacleBlocking(int direction, int range) {
        int x = this.x1;
        int y = this.y1;

        try {
            if (direction == Tron.NORTH) {
                for (int ty = 1; ty <= range; ty++) {
                    int delta = y - ty;
                    if (delta < 0) delta += y_max;
                    if (this.arena.board[x][delta])
                        return true;
                }
            } else if (direction == Tron.SOUTH) {
                for (int ty = 1; ty <= range; ty++) {
                    int delta = y + ty;
                    if (delta >= y_max) delta -= y_max;
                    if (this.arena.board[x][delta])
                        return true;
                }
            } else if (direction == Tron.EAST) {
                for (int tx = 1; tx <= range; tx++) {
                    int delta = x + tx;
                    if (delta >= x_max) delta -= x_max;
                    if (this.arena.board[delta][y])
                        return true;
                }
            } else if (direction == Tron.WEST) {
                for (int tx = 1; tx <= range; tx++) {
                    int delta = x - tx;
                    if (delta < 0) delta += x_max;
                    if (this.arena.board[delta][y])
                        return true;
                }
            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    public int whereDoIGo() {
        int final_direction = -1;
        int direction = 0;

       /* if (random.nextInt(100) == 0){
            isClockwise = !isClockwise;
        }*/
        
        if (firstMove == -1) {
            direction = this.d;            
            if (isObstacleBlocking(isClockwise ? ((--direction + 4) % 4) : (++direction % 4), 1)){
                if (!isObstacleBlocking(this.d, 1)){
                    final_direction = this.d;
                }
                else {
                    for (int i = 0; i < 4; i++){
                        if (!isObstacleBlocking(i, 1)){
                            final_direction = i;
                            break;
                        }
                        final_direction = this.d;
                    }                    
                }
            } else {
                if (!isObstacleBlocking((direction + 4) % 4, 1)){
                    final_direction = (direction + 4) % 4;
                }
                else {
                    for (int i = 0; i < 4; i++){
                        if (!isObstacleBlocking(i, 1)){
                            final_direction = i;
                            break;
                        }
                    }
                    final_direction = this.d;
                }
            }
        } else {
            final_direction = firstMove;
            firstMove = -1;            
        }

        
        return final_direction;
    }

}
