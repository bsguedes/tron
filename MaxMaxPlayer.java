import java.awt.*;
import java.lang.*;
import java.applet.*;
import java.util.Random;

public class MaxMaxPlayer extends Player {
    public Random random;
    public int DEPTH = 10;

    private int count = 0;

    public MaxMaxPlayer(String n, Color c, Arena a, int x, int y, byte number) {
        name = n;
        color = c;
        arena = a;
        x_max = x;
        y_max = y;
        player_no = number;
        random = new Random();
    }

    @Override
    public void restart(boolean theOtherGuyCrashed) {
        count = 0;
    }

    public int getScore(boolean[][] board, int depth, int x, int y, int score) {
        int bonus = count < 300 ? 1 : -1;
        if (x < 0) {
            score += 5 * bonus;
            x = x_max - 1;
        }
        if (x > x_max - 1) {
            score += 5 * bonus;
            x = 0;
        }
        if (y < 0) {
            score += 5 * bonus;
            y = y_max - 1;
        }
        if (y > y_max - 1) {
            score += 5 * bonus;
            y = 0;
        }
        if (depth == DEPTH) {
            return score + 150;
        }
        if (board[x][y]) {
            return score - 100;
        }

        board[x][y] = true;
        boolean[][] newboard = new boolean[x_max][y_max];
        for (int i = 0; i < x_max; i++) {
            for (int j = 0; j < y_max; j++) {
                newboard[i][j] = board[i][j];
            }
        }

        int scoreLeft = getScore(newboard, depth + 1, x - 1, y, score + 5);
        int scoreRight = getScore(newboard, depth + 1, x + 1, y, score + 5);
        int scoreUp = getScore(newboard, depth + 1, x, y - 1, score + 5);
        int scoreDown = getScore(newboard, depth + 1, x, y + 1, score + 5);
        return Math.max(scoreLeft, Math.max(scoreRight, Math.max(scoreUp, scoreDown)));
    }

    public int whereDoIGo() {
        try {

            boolean[][] board = new boolean[x_max][y_max];
            for (int i = 0; i < x_max; i++)
                for (int j = 0; j < y_max; j++)
                    board[i][j] = this.arena.board[i][j];

            Random random = new Random();

            int scoreUp = getScore(board, 0, x1, y1 - 1, 0) + random.nextInt(4);
            int scoreDown = getScore(board, 0, x1, y1 + 1, 0) + random.nextInt(4);
            int scoreRight = getScore(board, 0, x1 + 1, y1, 0) + random.nextInt(4);
            int scoreLeft = getScore(board, 0, x1 - 1, y1, 0) + random.nextInt(4);

            int max = Math.max(scoreLeft, Math.max(scoreRight, Math.max(scoreUp, scoreDown)));
            int dir = Tron.WEST;

            if (max == scoreDown) dir = Tron.SOUTH;
            if (max == scoreRight) dir = Tron.EAST;
            if (max == scoreLeft) dir = Tron.WEST;
            if (max == scoreUp) dir = Tron.NORTH;

            count++;
            System.out.println(count);

            return dir;
        } catch (Exception e) {
            System.out.println("LALALALALALA");
            return Tron.EAST;
        }
    }

}