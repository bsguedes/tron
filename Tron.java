import java.awt.*;
import java.lang.*;
import java.applet.*;
import java.util.*;

public class Tron extends Frame {
	public static final int NORTH = 2;
	public static final int EAST = 1;
	public static final int SOUTH = 0;
	public static final int WEST = 3;

	public String idParam;
	public Arena arena;
	public Label statusLabel;
	public Button startButton;
	public Button quitButton;
	public static Random random;

	public int playerOneScore, playerTwoScore;

	public static void main(String args[]) {
		Tron tron = new Tron();
		tron.setTitle("Tron");

		tron.playerOneScore = 0;
		tron.playerTwoScore = 0;

		tron.arena = new Arena(tron);

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		tron.setLayout(layout);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		layout.setConstraints(tron.arena, c);
		tron.add(tron.arena);

		c.gridwidth = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.CENTER;

		tron.startButton = new Button("start");
		c.gridx = 3;
		c.gridy = 1;
		layout.setConstraints(tron.startButton, c);
		tron.add(tron.startButton);

		tron.quitButton = new Button("quit");
		c.gridx = 4;
		c.gridy = 1;
		layout.setConstraints(tron.quitButton, c);
		tron.add(tron.quitButton);

		tron.statusLabel = new Label("Welcome to the first Porto Alegre YEN-sponsored Tron championship.");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 5;
		layout.setConstraints(tron.statusLabel, c);
		tron.add(tron.statusLabel);

		tron.pack();
		tron.show();
		tron.arena.start();
	}

	public void updateScore() {
		playerOneScore = arena.player1.score;
		playerTwoScore = arena.player2.score;
		statusLabel.setText("Player 1: [" + playerOneScore + "]  " + "Player 2: [" + playerTwoScore + "]");
	}

	public void start() {
		arena.start();
	}

	public void stop() {
		arena.stop();
	}

	public void destroy() {
	}

	public boolean handleEvent(Event event) {
		if (event.id == Event.WINDOW_DESTROY) {
			System.exit(1);
			return true;
		}
		return super.handleEvent(event);
	}

	public boolean action(Event evt, Object arg) {
		if (arg.equals("quit")) {
			System.exit(1);
			return true;
		} else if (arg.equals("start")) {
			arena.startAgain = true;
			return true;
		}

		return false;
	}
}
