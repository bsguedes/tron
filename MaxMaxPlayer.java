import java.awt.*;
import java.util.Random;

public class MaxMaxPlayer extends Player {
    private int DEPTH = 10;
    private int[][] enemyBoard;
    private int count = 0;
    private int ENEMY_STRATEGY = 50;

    MaxMaxPlayer(String n, Color c, Arena a, int x, int y, byte number) {
        name = n;
        color = c;
        arena = a;
        x_max = x;
        y_max = y;
        player_no = number;
    }

    @Override
    public void restart(boolean theOtherGuyCrashed) {
        count = 0;
    }

    private int getNormalizedCoordinate(int coord, int max) {
        if (coord < 0) {
            return max - 1;
        } else if (coord > max - 1) {
            return 0;
        }
        return coord;
    }

    private int getScore(boolean[][] board, int depth, int x, int y, int score) {
        x = getNormalizedCoordinate(x, x_max);
        y = getNormalizedCoordinate(y, y_max);
        int x1 = getNormalizedCoordinate(x + 1, x_max);
        int y1 = getNormalizedCoordinate(y + 1, y_max);
        int x2 = getNormalizedCoordinate(x - 1, x_max);
        int y2 = getNormalizedCoordinate(y - 1, y_max);
        if ((board[x][y1] && board[x][y2]) || (board[x1][y] && board[x2][y])) {
            score -= 10;
        }
        score += enemyBoard[x][y];
        if (board[x][y]) {
            return score - 100;
        }
        if (depth == DEPTH) {
            return score + 100;
        }

        board[x][y] = true;
        boolean[][] newboard = new boolean[x_max][y_max];
        for (int i = 0; i < x_max; i++) {
            System.arraycopy(board[i], 0, newboard[i], 0, y_max);
        }

        int scoreLeft = getScore(newboard, depth + 1, x - 1, y, score + 10);
        int scoreRight = getScore(newboard, depth + 1, x + 1, y, score + 10);
        int scoreUp = getScore(newboard, depth + 1, x, y - 1, score + 10);
        int scoreDown = getScore(newboard, depth + 1, x, y + 1, score + 10);
        return Math.max(scoreLeft, Math.max(scoreRight, Math.max(scoreUp, scoreDown)));
    }

    public int whereDoIGo() {
        try {
            Random random = new Random();

            int[][] enemies = arena.getEnemiesCoordinates(this);
            enemyBoard = new int[x_max][y_max];
            int blockSize = 3;
            for (int[] enemy : enemies) {
                int ex = enemy[0];
                int ey = enemy[1];
                for (int i = -blockSize; i < blockSize; i++) {
                    for(int j = -blockSize; j < blockSize; j++) {
                        int nex = getNormalizedCoordinate(ex + i, x_max);
                        int ney = getNormalizedCoordinate(ey + j, y_max);
                        enemyBoard[nex][ney] = 2 * (count < ENEMY_STRATEGY ? -1 : 1);
                    }
                }
            }

            boolean[][] board = new boolean[x_max][y_max];
            for (int i = 0; i < x_max; i++) {
                System.arraycopy(this.arena.board[i], 0, board[i], 0, y_max);
            }

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

            return dir;
        } catch (Exception e) {
            return Tron.EAST;
        }
    }

}