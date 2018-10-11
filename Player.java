import java.awt.*;
import java.lang.*;
import java.applet.*;

public class Player {
    public String name;
    public Color color;
    public Arena arena;
    public byte player_no;

    public boolean didsomething = false;

    public int x_max;
    public int y_max;
    public static final int NORTH = 2;
    public static final int EAST = 1;
    public static final int SOUTH = 0;
    public static final int WEST = 3;

    public static final int CRASH_DELTA = 10;

    public int x0, y0;
    public int x1, y1;
    public int d;
    public int old_d;
    public boolean crash;
    public int score;

    public Player() {
    }

    public void start() {
    }

    public void stop() {
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
        if (crash)
            arena.state = arena.RESTARTING;
    }

    public void paint(Graphics g) {
        if (crash) {
            g.setColor(Color.red);
            g.drawLine(x1 - CRASH_DELTA, y1 - CRASH_DELTA, x1 + CRASH_DELTA, y1 + CRASH_DELTA);
            g.drawLine(x1, y1 - CRASH_DELTA, x1, y1 + CRASH_DELTA);
            g.drawLine(x1 + CRASH_DELTA, y1 - CRASH_DELTA, x1 - CRASH_DELTA, y1 + CRASH_DELTA);
            g.drawLine(x1 - CRASH_DELTA, y1, x1 + CRASH_DELTA, y1);
        } else {
            g.setColor(color);
            g.drawLine(x0, y0, x1, y1);
        }
    }

    public void newPos() {
        x0 = x1;
        y0 = y1;
        arena.draw((graphics) -> {
            paint(graphics);
        });
    }

    public boolean markBoard(int direction) {
        boolean r = false;
        int i;
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