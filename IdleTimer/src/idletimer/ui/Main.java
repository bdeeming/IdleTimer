package idletimer.ui;

import idletimer.LiveTaskTimer;
import idletimer.Task;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final double checkingRate = 1.0;
		final double idlePeriod = 5.0 * 60;
		// final double idlePeriod = 5.0;

		// Create a task to time
		Task defaultTask = new Task("Default", 0.0);

		// Create a UI
		AllocateIdleTimePopup popUp = new AllocateIdleTimePopup();

		// Create the live timer
		LiveTaskTimer liveTimer = LiveTaskTimer.CreateSysActivityLiveTimer(
				checkingRate, idlePeriod, popUp, defaultTask);

		// Start the live task timer
		liveTimer.StartTiming();

		// Show the task to the user
		CmdLineUi ui = new CmdLineUi();
		ui.DisplayTask(defaultTask);

	}

}
