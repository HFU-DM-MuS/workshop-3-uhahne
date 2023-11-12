package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import utils.ApplicationTime;

/**
 * This class shall do the following:
 * - Create a window (JFrame) with a grey panel.
 * - 10 red balls are placed randomly on the panel.
 * - Each ball has a velocity and moves around individually.
 * - The balls bounce from the walls of the panel.
 * - The bouncing still works when the window size is changed.
 * TODO: Fix the bugs so that the class does what stated above.
 *
 * @author Hahne
 *
 */
public class MultipleCirclesBouncing extends Animation {

	@Override
	protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
		ArrayList<JFrame> frames = new ArrayList<>();
		// Create main frame (window)
		JFrame frame = new JFrame("Mathematik und Simulation - Workshop #3");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new MultipleCirclesBouncingPanel(applicationTimeThread, 10);
		frame.add(panel);
		frame.pack(); // adjusts size of the JFrame to fit the size of it's components
		frame.setVisible(true);

		frames.add(frame);
		return frames;
	}

}

class MultipleCirclesBouncingPanel extends JPanel {

	// panel has a single time tracking thread associated with it
	private final ApplicationTime thread;

	private final double[] vX;
	private final double[] vY;
	private final double[] currentX;
	private final double[] currentY;
	private double lastFrameTime = 0.0;
	private final int numBalls;

	public MultipleCirclesBouncingPanel(ApplicationTime thread, int numBalls) {
		this.thread = thread;
		this.numBalls = numBalls;
		// initialize random starting position and velocities
		double[] startX = new double[numBalls];
		double[] startY = new double[numBalls];
		vX = new double[numBalls];
		vY = new double[numBalls];
		currentX = new double[numBalls];
		currentY = new double[numBalls];
		// avoid starting positions at the border of the window
		double border = 20.0;
		// needed for the initial velocity range
		double minSpeed = 50.0;
		double maxSpeed = 100.0;
		for (int i = 0; i < numBalls; i++) {

			startX[i] = getRandomValueInRange(border, (double) Constants.WINDOW_HEIGHT - border);
			startY[i] = getRandomValueInRange(border, (double) Constants.WINDOW_WIDTH - border);

			vX[i] = getRandomValueInRange(minSpeed, maxSpeed);
			vY[i] = getRandomValueInRange(minSpeed, maxSpeed);

			currentX[i] = startX[i];
			currentY[i] = startY[i];
		}
	}

	// set this panel's preferred size for auto-sizing the container JFrame
	public Dimension getPreferredSize() {
		return new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
	}

	private double getRandomValueInRange(double minValue, double maxValue) {
		// from Java random API documentation
		// a random factor between 0 and 1 as Math.random() returns only values smaller
		// than 1.0
		double factor = Math.random() / Math.nextDown(1.0);
		// get a value in range [x1,x2]: double x = x1*(1.0 - f) + x2*f;
		return minValue * (1.0 - factor) + maxValue * factor;
	}

	// drawing operations should be done in this method
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

		double time = thread.getTimeInSeconds();
		double deltaTime = time - lastFrameTime;
		lastFrameTime = time;

		for (int i = 1; i < numBalls; i++) {

			currentX[i] = currentX[i] + (vX[i] * deltaTime);
			currentY[i] = currentY[i] + (vY[i] * deltaTime);

			int diameter = 50;
			if (currentX[i] >= Constants.WINDOW_WIDTH - diameter) {
				// Object has hit the right-hand wall
				vX[i] = -vX[i];
				currentX[i] = currentX[i] - 1;

			} else if (currentX[i] <= 0) { // else if to prevent double-checking hence saving performance
				// Object has hit the left-hand wall
				vX[i] = -vX[i];
				currentX[i] = currentX[i] + 1;
			}

			if (currentY[i] >= Constants.WINDOW_HEIGHT) {
				// Object has hit the floor
				vY[i] = -vY[i];
				currentY[i] = currentY[i] - 1;

			} else if (currentY[i] <= 0) {
				// Object has hit the ceiling
				vY[i] = -vY[i];
				currentY[i] = currentY[i] + 1;
			}

			g.setColor(Color.RED);
			g.fillOval((int) currentX[i], (int) currentY[i], diameter, diameter);
		}
	}
}
