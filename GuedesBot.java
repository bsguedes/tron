import java.awt.*;
import java.lang.*;
import java.applet.*;
import java.util.Random;

public class GuedesBot extends Player {
    public boolean didsomething = false;
    public Random random;

    // Tron.SOUTH, Tron.EAST, Tron.NORTH, Tron.WEST
    public static int[][] offsets = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
    public int firstMove = -1;
    public boolean isClockwise = false;

    int[] move(int x, int y, int direction){
        int[] dest = new int[] {x, y};
        dest[0] = dest[0] + offsets[direction][0];
        dest[1] = dest[1] + offsets[direction][1];
        if (dest[0] < 0) { dest[0] = x_max - 1; }
        if (dest[1] < 0) { dest[1] = y_max - 1; }
        if (dest[0] == x_max) { dest[0] = 0; }
        if (dest[1] == y_max) { dest[1] = 0; }
        return dest;
    }

    boolean isDirectionValid(boolean[][] board, int x, int y, int dir){
        int[] dest = move(x,y,dir);
        return !board[dest[0]][dest[1]];
    }

    public GuedesBot(String n, Color c, Arena a, int x, int y, byte number) {
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

    boolean isObstacleBlocking(boolean[][] board, int direction, int range) {
        int x = this.x1;
        int y = this.y1;
        return isObstacleBlocking(board, direction, range, x, y);
    }

    boolean isObstacleBlocking(boolean[][] board, int direction, int range, int x, int y) {
        try {
            if (direction == Tron.NORTH) {
                for (int ty = 1; ty <= range; ty++) {
                    int delta = y - ty;
                    if (delta < 0) delta += y_max;
                    if (board[x][delta])
                        return true;
                }
            } else if (direction == Tron.SOUTH) {
                for (int ty = 1; ty <= range; ty++) {
                    int delta = y + ty;
                    if (delta >= y_max) delta -= y_max;
                    if (board[x][delta])
                        return true;
                }
            } else if (direction == Tron.EAST) {
                for (int tx = 1; tx <= range; tx++) {
                    int delta = x + tx;
                    if (delta >= x_max) delta -= x_max;
                    if (board[delta][y])
                        return true;
                }
            } else if (direction == Tron.WEST) {
                for (int tx = 1; tx <= range; tx++) {
                    int delta = x - tx;
                    if (delta < 0) delta += x_max;
                    if (board[delta][y])
                        return true;
                }
            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    boolean[][] copyBoard(boolean[][] board){
        boolean[][] b = new boolean[x_max][y_max];
        for (int i = 0; i < x_max ; i++) {
            for (int j = 0; j < y_max; j++) {
                b[i][j] = board[i][j];
            }
        }
        return b;
    }

    int[] shuffle(){
        int[] a = new int[4];
        int delta = random.nextInt(8);
        for (int i = 0; i < 4; i++) {
            a[i] = (i + delta) & 3;
        }
        for (int i = 0; i < 3; i++) {
            int j = random.nextInt(4);
            int k = random.nextInt(4);
            int b = a[j];
            a[j] = a[k];
            a[k] = b;
        }
        return a;
    }

    int floodFill(boolean[][] board, int[] pos) {
        if (!board[pos[0]][pos[1]]) {
            board[pos[0]][pos[1]] = true;
            return 1 + floodFill(board, move(pos[0], pos[1], 0)) +
                    floodFill(board, move(pos[0], pos[1], 1)) +
                    floodFill(board, move(pos[0], pos[1], 2)) +
                    floodFill(board, move(pos[0], pos[1], 3));
        }
        return 0;
    }

    boolean shouldFloodFill(boolean[][] b, int x, int y, int[] out) {
        int count = 0;
        boolean[] validDirections = new boolean[4];
        for (int i = 0; i < 4; i++) {
            validDirections[i] = isDirectionValid(b, x, y, i);
            if (validDirections[i]) {
                count++;
            }
        }
        if (count <= 1) return false;
        if (count == 2 || count == 3) {
            int[] fillLength = new int[4];
            for (int i = 0; i < 4; i++) {
                if (validDirections[i]) {
                    fillLength[i] = floodFill(copyBoard(b), move(x, y, i));
                }
            }
            int max = 0;
            for (int i = 0; i < 4; i++) {
                if (fillLength[i] > max) {
                    max = fillLength[i];
                    out[0] = i;
                }
            }
            int c = 0;
            for(int i = 0; i < 4; i++){
                if (max == fillLength[i]){
                    c++;
                } else {
                    validDirections[i] = false;
                }
            }
            if (c >= count) return false;
            return true;
        } else {
            System.out.println("error");
            return false;
        }
    }

    public int whereDoIGo() {
        int final_direction = -1;
        int direction;
        boolean isCrash = false;
        int[] directions = shuffle();

        boolean[][] board = copyBoard(this.arena.board);
        int[] ff = new int[1];

        if (firstMove == -1 && shouldFloodFill(board, this.x1, this.y1, ff)) {
            final_direction = ff[0];
        }
        else if (firstMove == -1) {
            direction = this.d;
            if (isObstacleBlocking(board, isClockwise ? ((--direction + 4) & 3) : (++direction & 3), 1)){
                if (!isObstacleBlocking(board, this.d, 1)){
                    final_direction = this.d;
                }
                else {
                    for (int i = 0; i < 4; i++){
                        int rd = directions[i];
                        if (!isObstacleBlocking(board, rd, 1)){
                            final_direction = rd;
                            break;
                        }
                        final_direction = this.d;
                        isCrash = true;
                    }
                }
            } else {
                if (!isObstacleBlocking(board, (direction + 4) & 3, 1)){
                    final_direction = (direction + 4) & 3;
                }
                else {
                    for (int i = 0; i < 4; i++){
                        int rd = directions[i];
                        if (!isObstacleBlocking(board, rd, 1)){
                            final_direction = rd;
                            break;
                        }
                        final_direction = this.d;
                        isCrash = true;
                    }
                }
            }
        } else {
            final_direction = firstMove;
            firstMove = -1;
        }

        return final_direction;
    }

}
