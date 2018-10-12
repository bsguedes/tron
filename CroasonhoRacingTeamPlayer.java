import java.awt.*;
import java.lang.*;
import java.applet.*;
import java.util.Random;

public class CroasonhoRacingTeamPlayer extends Player {
    public boolean didsomething = false;
    public Random random;
    public int DEPTH = 11;    
    private int X, Y;
    private int eX, eY;
    private boolean isFirstRound = true;
    private boolean[] enemy = new boolean[4];
    private int player_number;
    private int round_number = 0;

    public CroasonhoRacingTeamPlayer(String n, Color c, Arena a, int x, int y, byte number) {
        name = n;
        color = c;
        arena = a;
        x_max = x;
        y_max = y;
        player_number = number;
        if (number == 1){
            this.X = x / 4;
            this.Y = y / 2;
            this.eX = 3 * x / 4;
            this.eY = y / 2;
        } else {
            this.X = 3 * x / 4;
            this.Y = y / 2;
            this.eX = x / 4;
            this.eY = y / 2;
        }
        player_no = number;
        random = new Random();
    }

    private boolean isObstacleBlocking(boolean[][] board, int direction, int x, int y) {
        try {
            if (direction == Tron.NORTH) {
                if (y <= 0) return (board[x][y_max - 1]);
                return (board[x][y - 1]);
            } else if (direction == Tron.SOUTH) {
                if (y >= y_max - 1) return (board[x][0]);
                return (board[x][y + 1]);
            } else if (direction == Tron.EAST) {
                if (x >= x_max - 1) return (board[0][y]);
                return (board[x + 1][y]);
            } else if (direction == Tron.WEST) {
                if (x <= 0) return (board[x_max - 1][y]);
                return (board[x - 1][y]);
            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    public int[] directions(int index){
        int[] dir = new int[DEPTH];
        for(int i = 0; i < DEPTH; i++){
            dir[i] = (index / (1 << (2 * i))) % 4;
        }
        return dir;
    }

    public int score(boolean[][] board, int x, int y, int ex, int ey, int dir){
        if (y < y_max / 5 && dir == Tron.NORTH) return -(y_max / 5 - y);
        if (y > 4* y_max / 5 && dir == Tron.SOUTH) return -(y_max +(y-y_max));
        if (x < x_max / 5 && dir == Tron.WEST) return -(x_max / 5 - x);
        if (x > 4* x_max / 5 && dir == Tron.EAST) return -(x_max +(x-x_max));
        return 0;
    }

    public int getScore(boolean[][] board, int dir, int depth, int x, int y, int ex, int ey){
        if (x < 0) x = x_max - 1;
        if (x > x_max - 1) x = 0;
        if (y < 0) y = y_max - 1;
        if (y > y_max - 1) y = 0;
        if (depth == DEPTH){
            return score(board, x, y, ex, ey, dir);
        }
        if (depth % 2 == 0) {
            if(isObstacleBlocking(board, dir, x, y)){
                return -100;
            }
            int scoreLeft = getScore(board, Tron.WEST, depth+1, x-1, y, ex, ey);
            int scoreRight = getScore(board, Tron.EAST, depth+1, x+1, y, ex, ey);
            int scoreUp = getScore(board, Tron.NORTH, depth+1, x, y-1, ex, ey);
            int scoreDown = getScore(board, Tron.SOUTH, depth+1, x, y+1, ex, ey);
            return 5 + Math.max(scoreLeft, Math.max(scoreRight, Math.max(scoreUp, scoreDown)));
        } else {
            if(isObstacleBlocking(board, dir, ex, ey)){
                return 100;
            }
            int scoreLeft = getScore(board, Tron.WEST, depth+1, x, y, ex-1, ey);
            int scoreRight = getScore(board, Tron.EAST, depth+1, x, y, ex+1, ey);
            int scoreUp = getScore(board, Tron.NORTH, depth+1, x, y, ex, ey-1);
            int scoreDown = getScore(board, Tron.SOUTH, depth+1, x, y, ex, ey+1);
            return -5 + Math.max(scoreLeft, Math.max(scoreRight, Math.max(scoreUp, scoreDown)));
        }
    }

    @Override
    public void restart(boolean theOtherGuyCrashed){
        isFirstRound = true;
        enemy = new boolean[4];
        if (player_number == 1){
            this.X = x_max / 4;
            this.Y = y_max / 2;
            this.eX = 3 * x_max / 4;
            this.eY = y_max / 2;
        } else {
            this.X = 3 * x_max / 4;
            this.Y = y_max / 2;
            this.eX = x_max / 4;
            this.eY = y_max / 2;
        }
        round_number = random.nextInt(100);
    }

    public int whereDoIGo() {        
        try{
        int[] leafs = new int[(int)Math.pow(4,DEPTH)];
        
        boolean[][] board = new boolean[x_max][y_max];
        for (int i = 0; i < x_max; i++)
            for (int j = 0; j < y_max; j++)
                board[i][j] = this.arena.board[i][j];


        if (!isFirstRound){
            // atualiza adversario
            if (isObstacleBlocking(board, Tron.WEST, eX, eY) && !enemy[Tron.WEST]){
                eX = eX - 1; if (eX < 0) eX = x_max - 1;
            }
            if (isObstacleBlocking(board, Tron.EAST, eX, eY) && !enemy[Tron.EAST]){
                eX = eX + 1; if (eX > x_max - 1) eX = 0;
            }
            if (isObstacleBlocking(board, Tron.NORTH, eX, eY) && !enemy[Tron.NORTH]){
                eY = eY - 1; if (eY < 0) eY = y_max - 1;
            }
            if (isObstacleBlocking(board, Tron.SOUTH, eX, eY) && !enemy[Tron.SOUTH]){
                eY = eY + 1; if (eY > y_max - 1) eY = 0;
            }            
        } else {
            isFirstRound = false;
        }            

        round_number++;

        int scoreUp = getScore(board, Tron.NORTH, 0, X, Y, eX, eY);
        int scoreDown = getScore(board, Tron.SOUTH, 0, X, Y, eX, eY);
        int scoreRight = getScore(board, Tron.EAST, 0, X, Y, eX, eY);
        int scoreLeft = getScore(board, Tron.WEST, 0, X, Y, eX, eY);

        int max = Math.max(scoreLeft, Math.max(scoreRight, Math.max(scoreUp, scoreDown)));
        int dir = Tron.WEST;

if (player_number == 1){        
        if (max == scoreDown) dir = Tron.SOUTH;
        if (max == scoreRight) dir = Tron.EAST;
        if (max == scoreLeft) dir = Tron.WEST;
        if (max == scoreUp) dir = Tron.NORTH;
    } else{        
        if (max == scoreUp) dir = Tron.NORTH;
        if (max == scoreLeft) dir = Tron.WEST;
        if (max == scoreRight) dir = Tron.EAST;        
        if (max == scoreDown) dir = Tron.SOUTH;        
    }

        if (dir == Tron.WEST) {
            X = X - 1; if (X < 0) X = x_max - 1;
        }
        if (dir == Tron.EAST) {
            X = X + 1; if (X > x_max - 1) X = 0;
        }
        if (dir == Tron.NORTH) {
            Y = Y - 1; if (Y < 0) Y = y_max - 1;
        }
        if (dir == Tron.SOUTH) {
            Y = Y + 1; if (Y > y_max - 1) Y = 0;
        }
        board[X][Y] = true;

        enemy[Tron.NORTH] = isObstacleBlocking(board, Tron.NORTH, eX, eY-1);
        enemy[Tron.SOUTH] = isObstacleBlocking(board, Tron.SOUTH, eX, eY+1);
        enemy[Tron.EAST] = isObstacleBlocking(board, Tron.EAST, eX+1, eY);
        enemy[Tron.WEST] = isObstacleBlocking(board, Tron.WEST, eX-1, eY);

        return dir;
    } catch (Exception e){
        System.out.println("LALALALALALA");
        return Tron.EAST;           
    }
    }

}