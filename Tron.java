import java.awt.*;

public class Tron extends Frame {
	public static final int NORTH = 2;
	public static final int EAST = 1;
	public static final int SOUTH = 0;
	public static final int WEST = 3;

	public Arena arena;
	private Label statusLabel;
	private Button startButton;
	private Button quitButton;

	public static void main(String args[]) {
		Tron tron = new Tron();
		tron.setTitle("Tron");

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
		StringBuilder text = new StringBuilder();
		for(Player player : Arena.players) {
			text.append(player.name).append(": [").append(player.score).append("] ");
		}
		statusLabel.setText(text.toString());
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
