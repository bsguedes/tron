import java.awt.*;
import java.lang.*;
import java.applet.*;
import java.util.Random;

public class RandomPlayer extends Player {
    public boolean didsomething = false;
    public Random random;

    public RandomPlayer(String n, Color c, Arena a, int x, int y, byte number) {
        name = n;
        color = c;
        arena = a;
        x_max = x;
        y_max = y;
        player_no = number;
        random = new Random();
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
