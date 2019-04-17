import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

	private boolean[][] cells;
	private int width, height;

	public GamePanel(boolean[][] cells) {
		this.cells = cells;
		panelInitializing();
	}

	private void panelInitializing() {
		this.setPreferredSize(new Dimension(706, 700));
		this.setLayout(null);
	}

	public void setCells(boolean[][] cells) {
		this.cells = cells;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		width = 700 / cells[0].length;
		height = 700 / cells.length;

		g.setColor(Color.BLUE);
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				if (cells[i][j]) {
					g.fillRect(i * width, j * height, width, height);
				}
			}
		}

		g.setColor(Color.BLACK);

		for (int x = 0; x < cells[0].length; x++) {
			g.drawLine(x * width, 0, x * width, 700);
		}
		for (int y = 0; y < cells.length; y++) {
			g.drawLine(0, y * height, 700, y * height);
		}

		g.drawRect(0, 0, 700, 700);
	}
}
