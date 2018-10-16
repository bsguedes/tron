import java.awt.*;
import java.awt.image.BufferStrategy;
import java.lang.*;
import java.applet.*;
import java.util.Vector;
import java.net.*;
import java.io.*;

public class Arena extends Canvas implements Runnable {
	private final static int SIZE = 64;

	public final static boolean SHOULD_DRAW = true;

	public Tron tron;

	private BufferStrategy bs;

	private Thread conductor;
	public Player player1;
	public Player player2;
	public boolean board[][];
	public boolean clear;
	public boolean startAgain = false;
	private int xmax, ymax;

	public static final int WAITING = 0;
	public static final int RUNNING = 1;
	public static final int RESTARTING = 2;
	public int state;

	public int lastmove;
	public int gen_no;

	public int playerSize = 5;

	public Arena(Tron t) {
		this.setBackground(Color.black);
		this.resize(playerSize * this.SIZE, playerSize * this.SIZE);

		this.conductor = null;
		this.board = null;
		this.state = WAITING;
		this.tron = t;
		this.gen_no = 0;

		this.xmax = SIZE;
		this.ymax = SIZE;

		Player croasonho1 = new CroasonhoRacingTeamPlayer("Croasonho", Color.red, this, this.xmax, this.ymax, (byte) 1);
        Player croasonho2 = new CroasonhoRacingTeamPlayer("Croasonho", Color.red, this, this.xmax, this.ymax, (byte) 2);

		Player random1 = new RandomPlayer("Random", Color.green, this, this.xmax, this.ymax, (byte) 1);
        Player random2 = new RandomPlayer("Random", Color.green, this, this.xmax, this.ymax, (byte) 2);

        Player other1 = new MyOtherPlayer("Other", Color.yellow, this, this.xmax, this.ymax, (byte) 1);
        Player other2 = new MyOtherPlayer("Other", Color.yellow, this, this.xmax, this.ymax, (byte) 2);

        Player circle1 = new CirclePlayer("Circle", Color.white, this, this.xmax, this.ymax, (byte) 1);
        Player circle2 = new CirclePlayer("Circle", Color.white, this, this.xmax, this.ymax, (byte) 2);

		this.player1 = circle1;
		this.player2 = other2;
	}

	public void start() {
		this.player1.crash = false;
		this.player2.crash = false;
		this.clear = true;
		this.repaint();

		if (this.board == null) {
			this.board = new boolean[xmax][ymax];
		}

		this.createBufferStrategy(120);
		this.bs = this.getBufferStrategy();

		if (this.conductor == null) {
			this.conductor = new Thread(this, "Arena");
			this.conductor.start();
		} else {
			this.conductor.resume();
		}
	}

	public void stop() {
		this.conductor.suspend();
	}

	public void startPlayers() {
		this.tron.updateScore();

		this.clearBoard();

		this.player1.go(this.xmax / 4, this.ymax / 2);
		this.player2.go(3 * this.xmax / 4, this.ymax / 2);

		this.state = RUNNING;
		this.lastmove = 0;
	}

	public void run() {
		while (true) {
			switch (state) {
			case RUNNING:
				this.player1.step();
				this.player2.step();
				break;
			case RESTARTING:
				if (this.player1.crash && !this.player2.crash) {
					this.player2.tallyWin();
				} else if (this.player2.crash && !this.player1.crash) {
					this.player1.tallyWin();
				}

				this.player1.restart(this.player2.crash);
				this.player2.restart(this.player1.crash);

				this.state = WAITING;
				this.tron.updateScore();
				if (!SHOULD_DRAW) this.startAgain = true;
				break;
			case WAITING:
				if (this.startAgain) {
					this.startAgain = false;
					this.start();
					this.startPlayers();
				}
				break;
			}

			this.repaint();

			if ( SHOULD_DRAW ) {
				try {
					Thread.sleep(4);
				} catch (InterruptedException e) {
				}
			}

			if (this.player1 != null) {
				this.player1.newPos();
				this.player2.newPos();
			}
		}
	}

	public void draw(DrawLambda callback) {
		Graphics2D graphics = (Graphics2D) this.bs.getDrawGraphics().create();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		callback.draw(graphics);

		graphics.dispose();
		this.bs.show();
	}

	public void update() {
		Graphics graphics = this.bs.getDrawGraphics().create();

		if (this.clear) {
			graphics.clearRect(0, 0, this.size().width, this.size().height);
			this.clear = false;
		}
		if (this.player1 != null) {
			this.player1.paint(graphics);
		}
		if (this.player2 != null) {
			this.player2.paint(graphics);
		}

		graphics.dispose();
		this.bs.show();
	}

	public void clearBoard() {
		int i, j;
		for (i = 0; i < this.xmax; i++)
			for (j = 0; j < this.ymax; j++)
				this.board[i][j] = false;
		this.clear = true;
	}
}
