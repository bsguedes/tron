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
        //boo = new boolean[64][64];
    }

    private boolean hasThreeLockedNeighbors(boolean[][] board){
        int x = this.x1;
        int y = this.y1;
        int count = 0;
        for (int i = 0; i < 4; i++) {
            count += isObstacleBlocking(board, i, 1, x, y) ? 1 : 0;
        }        
        return count >= 3;
    }

    private boolean hasThreeLockedNeighbors(boolean[][] board, int x, int y){
        if (board[x][y]) return true;
        int count = 0;
        for (int i = 0; i < 4; i++) {
            count += isObstacleBlocking(board, i, 1, x, y) ? 1 : 0;
        }        
        //if (count == 3 && Math.abs(x - this.x1) <= 1 && Math.abs(y - this.y1) <= 1 && x != this.x1 && y != this.y1) return false;        
        return count >= 3;
    }

    private boolean isObstacleBlocking(boolean[][] board, int direction, int range) {
        int x = this.x1;
        int y = this.y1;
        return isObstacleBlocking(board, direction, range, x, y);
    }

    private boolean isObstacleBlocking(boolean[][] board, int direction, int range, int x, int y) {
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

    public int[] shuffle(){
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

   // int pi,pj;
   // boolean[][] boo = new boolean[64][64];
    
    public int whereDoIGo() {          
        int final_direction = -1;
        int direction = 0;

        int[] directions = shuffle();

       /* if (random.nextInt(100) == 0){
            isClockwise = !isClockwise;
        }*/

        boolean[][] board = new boolean[x_max][y_max];
        for (int i = 0; i < x_max ; i++) {
            for (int j = 0; j < y_max; j++) {
                board[i][j] = this.arena.board[i][j];                                
            }
        }    

        int deadEnds = 0;
        do
        {
            deadEnds = 0;
            for (int i = 0; i < x_max ; i++) {
                for (int j = 0; j < y_max; j++) {                            
                    if (hasThreeLockedNeighbors(board, i, j) && !board[i][j]) {                         
                       // pi = i; pj = j;
                        deadEnds++;
                        board[i][j] = true;
                      //  if (!boo[pi][pj]){
                      //      boo[pi][pj] = true;
                      //      arena.draw((graphics) -> {                                
                      //          graphics.setColor(Color.orange);
                      //          graphics.fillRect(5 * pi, 5 * pj, 5 , 5 );
                      //      });
                      //  }
                    }                    
                }
            }
        } while (deadEnds > 0);

        boolean crash = false;

        if (hasThreeLockedNeighbors(board)){
            for (int i = 0; i < 4; i++){
                int rd = directions[i];
                if (!isObstacleBlocking(board, rd, 1)){
                    //System.out.println("here");
                    return rd;                    
                }                
            } 
        }
        
        if (firstMove == -1) {
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
                        crash = true;
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
                        crash = true;
                    }                    
                }
            }
        } else {
            final_direction = firstMove;
            firstMove = -1;            
        }

        if (crash){            
            board = new boolean[x_max][y_max];
            for (int i = 0; i < x_max ; i++) {
                for (int j = 0; j < y_max; j++) {
                    board[i][j] = this.arena.board[i][j];                                
                }
            }
            if (firstMove == -1) {
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
                            crash = true;
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
                            crash = true;
                        }                    
                    }
                }
            } else {
                final_direction = firstMove;
                firstMove = -1;            
            }
        }
        return final_direction;
    }
}
