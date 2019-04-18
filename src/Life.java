
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Life implements MouseListener, Runnable {

	private boolean[][] cells = new boolean[30][30];
	private JFrame frame = new JFrame("Life simulation");
	private GamePanel panel = new GamePanel(cells);
	private ActionListener actionListener;
	private boolean started = false;
	private int speed = 700;

	private JButton nextGenerationButton;
	private JButton startButton;
	private JButton stopButton;
	private JButton randomButton;
	private JButton clearButton;

	Random r = new Random();

	public Life() {
		frameInitializing();
	}

	private void frameInitializing() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}

		frame.setPreferredSize(new Dimension(606, 700));

		nextGenerationButton = new JButton("Next generation");
		nextGenerationButton.setBounds(10, 610, 110, 30);
		panel.add(nextGenerationButton);

		startButton = new JButton("Start");
		startButton.setBounds(130, 610, 70, 30);
		panel.add(startButton);

		stopButton = new JButton("Stop");
		stopButton.setBounds(210, 610, 70, 30);
		stopButton.setEnabled(false);
		panel.add(stopButton);

		randomButton = new JButton("Random");
		randomButton.setBounds(290, 610, 80, 30);
		panel.add(randomButton);

		clearButton = new JButton("Clear");
		clearButton.setBounds(380, 610, 70, 30);
		panel.add(clearButton);

		JLabel speedLabel = new JLabel("Speed:");
		speedLabel.setBounds(460, 610, 100, 30);
		panel.add(speedLabel);

		JSlider speedSlider = new JSlider(0, 950, 300);
		speedSlider.setBounds(500, 610, 90, 30);
		panel.add(speedSlider);
		speedSlider.addChangeListener(e -> {
			int value = ((JSlider)e.getSource()).getValue();
			speed = 1000 - value;
		});

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Field size");
		JMenuItem smallMenu = new JMenuItem("Small");
		JMenuItem mediumMenu = new JMenuItem("Medium");
		JMenuItem bigMenu = new JMenuItem("Big");
		menu.add(smallMenu);
		menu.add(mediumMenu);
		menu.add(bigMenu);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);

		smallMenu.addActionListener(e -> {
			cells = new boolean[20][20];
			panel.setCells(cells);
			frame.repaint();
		});
		mediumMenu.addActionListener(e -> {
			cells = new boolean[30][30];
			panel.setCells(cells);
			frame.repaint();
		});
		bigMenu.addActionListener(e -> {
			cells = new boolean[40][40];
			panel.setCells(cells);
			frame.repaint();
		});

		actionListener = e -> {
			if (e.getSource().equals(nextGenerationButton)) {
				step();
			}
			if (e.getSource().equals(startButton)) {
				if (!started) {
					started = true;
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
					randomButton.setEnabled(false);
					clearButton.setEnabled(false);
					nextGenerationButton.setEnabled(false);
					menu.setEnabled(false);
					Thread t = new Thread(this);
					t.start();
				}
			}
			if (e.getSource().equals(stopButton)) {
				started = false;
				menu.setEnabled(true);
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
				randomButton.setEnabled(true);
				clearButton.setEnabled(true);
				nextGenerationButton.setEnabled(true);
			}
			if (e.getSource().equals(randomButton)) {
				random();
			}
			if (e.getSource().equals(clearButton)) {
				clear();
			}
		};

		nextGenerationButton.addActionListener(actionListener);
		startButton.addActionListener(actionListener);
		stopButton.addActionListener(actionListener);
		randomButton.addActionListener(actionListener);
		clearButton.addActionListener(actionListener);

		panel.addMouseListener(this);

		frame.getContentPane().add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void run() {
		while (started) {
			step();
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void step() {
		boolean[][] nextCells = new boolean[cells.length][cells[0].length];
		int len = cells.length;
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				int count = 0;
				if (i > 0 && j > 0 && cells[i - 1][j - 1]) {
					count++;
				}
				if (i > 0 && cells[i - 1][j]) {
					count++;
				}
				if (i > 0 && j < len - 1 && cells[i - 1][j + 1]) {
					count++;
				}
				if (j > 0 && cells[i][j - 1]) {
					count++;
				}
				if (j < len - 1 && cells[i][j + 1]) {
					count++;
				}
				if (i < len - 1 && j > 0 && cells[i + 1][j - 1]){
					count++;
				}
				if (i < len - 1 && cells[i + 1][j]) {
					count++;
				}
				if (i < len - 1 && j < len - 1 && cells[i + 1][j + 1]) {
					count++;
				}

				if (cells[i][j]) {
					nextCells[i][j] = count == 2 || count == 3;
				} else {
					nextCells[i][j] = count == 3;
				}
			}
		}
		cells = nextCells;
		panel.setCells(nextCells);
		frame.repaint();
	}

	private void random() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				cells[i][j] = r.nextBoolean();
				panel.setCells(cells);
				frame.repaint();
			}
		}
	}

	private void clear() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				cells[i][j] = false;
				panel.setCells(cells);
				frame.repaint();
			}
		}
	}

	public static void main(String[] args) {
		new Life();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!started) {
			int x = e.getX() / (600 / cells[0].length);
			int y = e.getY() / (600 / cells.length);
			if (x < cells[0].length && y < cells.length) {
				cells[x][y] = !cells[x][y];
				frame.repaint();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
