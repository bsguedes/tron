import java.awt.*;
import java.lang.*;
import java.applet.*;
import java.util.Random;

public class GuedesBot extends Player {
    public boolean didsomething = false;
    public Random random;

    // Tron.SOUTH, Tron.EAST, Tron.NORTH, Tron.WEST
    public static int[][] offsets = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };



    public boolean isFirstRound = false;
    public int[] enemyPosition;

    private int[] move(int x, int y, int direction){
        int[] dest = new int[] {x, y};
        dest[0] = dest[0] + offsets[direction][0];
        dest[1] = dest[1] + offsets[direction][1];
        if (dest[0] < 0) { dest[0] = x_max - 1; }
        if (dest[1] < 0) { dest[1] = y_max - 1; }
        if (dest[0] == x_max) { dest[0] = 0; }
        if (dest[1] == y_max) { dest[1] = 0; }
        return dest;
    }

    public boolean isDirectionValid(int x, int y, int dir){
        int[] dest = move(x,y,dir);
        return !arena.board[dest[0]][dest[1]];
    }

    private void updateEnemyPosition(){

    }

    public GuedesBot(String n, Color c, Arena a, int x, int y, byte number) {
        name = n;
        color = c;
        arena = a;
        x_max = x;
        y_max = y;
        player_no = number;
        random = new Random();
        isFirstRound = true;

        if (player_no == 1) {
            enemyPosition = new int[]{3 * x / 4, y / 2};
        } else {
            enemyPosition = new int[]{x / 4, y / 2};
        }
    }

    @Override
    public void restart(boolean theOtherGuyCrashed){
        isFirstRound = true;

        if (player_no == 1) {
            enemyPosition = new int[]{3 * x_max / 4, y_max / 2};
        } else {
            enemyPosition = new int[]{x_max / 4, y_max / 2};
        }
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
        if (isFirstRound)
        {
            isFirstRound = false;
        } else {
            updateEnemyPosition();
        }


        int final_direction = -1;
        int direction = 0;
        int range = 20;

        while (final_direction == -1) {
            if (!isObstacleBlocking(this.d, range)) {
                return this.d;
            }

            do {
                direction = random.nextInt(4);

                range--;
                if (range == 0) {
                    return this.d;
                }
            } while (isObstacleBlocking(direction, range));

            final_direction = direction;
        }

        return final_direction;
    }

}
