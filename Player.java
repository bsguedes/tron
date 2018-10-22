import java.awt.*;
import java.lang.*;

public class Player {
    public String name;
    Color color;
    public Arena arena;
    byte player_no;

    boolean didsomething = false;

    int x_max;
    int y_max;
    private static final int NORTH = 2;
    private static final int EAST = 1;
    private static final int SOUTH = 0;
    private static final int WEST = 3;

    private static final int CRASH_DELTA = 10;

    private int x0, y0;
    int x1, y1;
    int d;
    private int old_d;
    public boolean crash;
    public int score;

    public Player() {
    }

    public void restart(boolean theOtherGuyCrashed) {
    }

    public int whereDoIGo() {
        return d;
    }

    public void go(int x, int y) {
        x0 = x;
        y0 = y;
        x1 = x0;
        y1 = y0;
        old_d = d = SOUTH;
        crash = false;
        arena.board[x0][y0] = true;
    }

    public void step() {
        if ((d = whereDoIGo()) != old_d) {
            old_d = d;
        }
        crash = markBoard(d);
        if (crash) {
            arena.killPlayer(player_no);
        }
    }

    private void paint(Graphics g) {
        if (crash) {
            g.setColor(Color.red);
            g.drawLine(arena.PLAYER_SIZE *x1 - CRASH_DELTA, arena.PLAYER_SIZE *y1 - CRASH_DELTA, arena.PLAYER_SIZE *x1 + CRASH_DELTA, arena.PLAYER_SIZE *y1 + CRASH_DELTA);
            g.drawLine(arena.PLAYER_SIZE *x1, arena.PLAYER_SIZE *y1 - CRASH_DELTA, arena.PLAYER_SIZE *x1, arena.PLAYER_SIZE *y1 + CRASH_DELTA);
            g.drawLine(arena.PLAYER_SIZE *x1 + CRASH_DELTA, arena.PLAYER_SIZE *y1 - CRASH_DELTA, arena.PLAYER_SIZE *x1 - CRASH_DELTA, arena.PLAYER_SIZE *y1 + CRASH_DELTA);
            g.drawLine(arena.PLAYER_SIZE *x1 - CRASH_DELTA, arena.PLAYER_SIZE *y1, arena.PLAYER_SIZE *x1 + CRASH_DELTA, arena.PLAYER_SIZE *y1);
        } else {
            g.setColor(color);
            g.fillRect(arena.PLAYER_SIZE *x1, arena.PLAYER_SIZE *y1, arena.PLAYER_SIZE, arena.PLAYER_SIZE);
        }
    }

    public void newPos() {
        x0 = x1;
        y0 = y1;
        if (Arena.SHOULD_DRAW) {
            arena.draw(this::paint);
        }
    }


    private boolean markBoard(int direction) {
        boolean r = false;
        switch (direction) {
        case SOUTH:
            y1++;
            if (y1 >= y_max) {
                y1 = 0;
                y0 = y1;
            }
            if (r = arena.board[x1][y1]) {
                break;
            }
            arena.board[x1][y1] = true;
            break;
        case NORTH:
            y1--;
            if (y1 < 0) {
                y1 = y_max - 1;
                y0 = y1;
            }
            if (r = arena.board[x1][y1]) {
                break;
            }
            arena.board[x1][y1] = true;
            break;
        case EAST:
            x1++;
            if (x1 >= x_max) {
                x1 = 0;
                x0 = x1;
            }
            if (r = arena.board[x1][y1]) {
                break;
            }
            arena.board[x1][y1] = true;
            break;
        case WEST:
            x1--;
            if (x1 < 0) {
                x1 = x_max - 1;
                x0 = x1;
            }
            if (r = arena.board[x1][y1]) {
                break;
            }
            arena.board[x1][y1] = true;
            break;
        default:
            System.out.println("UH-OH!");
            break;
        }
        return (r);
    }

    public void tallyWin() {
        score++;
    }
}