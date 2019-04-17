
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class Life implements MouseListener, Runnable {

	private boolean[][] cells = new boolean[20][20];
	private JFrame frame = new JFrame("Life simulation");
	private GamePanel panel = new GamePanel(cells);
	private ActionListener actionListener;
	Random r = new Random();
	boolean started = false;

	//buttons
	private JButton nextGenerationButton;
	private JButton startButton;
	private JButton stopButton;
	private JButton randomButton;
	private JButton clearButton;

	public Life() {
		frameInitializing();
	}

	private void frameInitializing() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {}

		frame.setPreferredSize(new Dimension(606, 700));

		nextGenerationButton = new JButton("Next generation");
		nextGenerationButton.setBounds(10, 620, 130, 30);
		panel.add(nextGenerationButton);

		startButton = new JButton("Start");
		startButton.setBounds(150, 620, 100, 30);
		panel.add(startButton);

		stopButton = new JButton("Stop");
		stopButton.setBounds(260, 620, 100, 30);
		stopButton.setEnabled(false);
		panel.add(stopButton);

		randomButton = new JButton("Random");
		randomButton.setBounds(370, 620, 100, 30);
		panel.add(randomButton);

		clearButton = new JButton("Clear");
		clearButton.setBounds(480, 620, 100, 30);
		panel.add(clearButton);

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
					Thread t = new Thread(this);
					t.start();
				}
			}
			if (e.getSource().equals(stopButton)) {
				started = false;
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
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void step() {
		boolean[][] nextCells = new boolean[cells.length][cells[0].length];
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				int count = 0;
				if (i > 0 && j > 0 && cells[i - 1][j - 1]) {
					count++;
				}
				if (i > 0 && cells[i - 1][j]) {
					count++;
				}
				if (i > 0 && j < cells[0].length - 1 && cells[i - 1][j + 1]) {
					count++;
				}
				if (j > 0 && cells[i][j - 1]) {
					count++;
				}
				if (j < cells[0].length - 1 && cells[i][j + 1]) {
					count++;
				}
				if (i < cells.length - 1 && j > 0 && cells[i + 1][j - 1]){
					count++;
				}
				if (i < cells.length - 1 && cells[i + 1][j]) {
					count++;
				}
				if (i < cells.length - 1 && j < cells[0].length - 1 && cells[i + 1][j + 1]) {
					count++;
				}

				if (cells[i][j]) {
					if (count == 2 || count == 3) {
						nextCells[i][j] = true;
					} else {
						nextCells[i][j] = false;
					}
				} else {
					if (count == 3) {
						nextCells[i][j] = true;
					} else {
						nextCells[i][j] = false;
					}
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
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!started) {
			int x = e.getX() / (600 / cells[0].length);
			int y = e.getY() / (600 / cells.length);
			if (x < 20 && y < 20) {
				cells[x][y] = !cells[x][y];
				System.out.println(x + " " + y);
				frame.repaint();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
