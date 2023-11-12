package app;

import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JFrame;

import utils.ApplicationTime;
import utils.FrameUpdater;

public abstract class Animation {

	public void start() {
		// open new thread for time measurement
		ApplicationTime applicationTimeThread = new ApplicationTime();
		applicationTimeThread.start();
		FrameUpdater frameUpdater = new FrameUpdater(createFrames(applicationTimeThread));
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(frameUpdater, 100, Constants.TPF);
		// for demonstration of a jerky animation set the third argument to 1000 instead
		// of _0_Constants.TPF
		// After an initial delay of 100 milliseconds, the timer triggers an event every
		// TPF milliseconds
	}

	protected abstract ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread);

}
