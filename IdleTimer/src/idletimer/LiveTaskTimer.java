/**
 * 
 */
package idletimer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import idletimer.ActivityWaypoint.ActivityState;
import idletimer.InputActivityStream.TimedOutException;

/**
 * @author bdeeming
 * 
 */
public class LiveTaskTimer extends Thread {

	InputActivityStream inputActivityStream;

	private final Logger LOGGER = Logger.getLogger(SysActivityMonitor.class
			.getName());

	private Task activeTask;

	/**
	 * Create a new event handler thread.
	 * 
	 * @param inputActivityStream
	 *            The input stream of activity events.
	 */
	public LiveTaskTimer(InputActivityStream inputActivityStream) {
		super();
		this.inputActivityStream = inputActivityStream;
		this.activeTask = new Task("Default", 0.0);
	}

	/**
	 * @param newActiveTask
	 *            The activeTask to set
	 */
	synchronized public void setActiveTask(Task newActiveTask) {
		// Maintain proper task state
		if (this.activeTask.IsCurrentlyBeingTimed()) {
			// Stop timing the current task
			this.activeTask.StopTiming();
			// Continue the timing with the new task
			newActiveTask.StartTiming();
		} else {
			// Not currently timing => state of tasks already match
		}

		// Make new task the active task
		this.activeTask = newActiveTask;
	}

	/**
	 * @return The currently active task.
	 */
	synchronized public Task GetActiveTask() {
		return activeTask;
	}

	@Override
	public void run() {

		// Start off in the active state
		ActivityWaypoint previousWaypoint = new ActivityWaypoint(
				ActivityState.ACTIVE);

		// Start timing the active task
		activeTask.StartTiming();

		// A task to time the idle period with
		Task unallocatedTask = new Task("Idle period", 0.0);

		// Consume the input stream
		while (true) {
			try {

				ActivityWaypoint newWaypoint;
				newWaypoint = inputActivityStream
						.ReadActivityWaypoint(InputActivityStream.WAIT_FOREVER);

				// Synch to protect against task being swapped out
				synchronized (this) {
					// Check for state transition to ACTIVE
					if (newWaypoint.getActivityState() == ActivityState.ACTIVE) {

						if (previousWaypoint.getActivityState() != ActivityState.ACTIVE) {

							// Stop timing the idle period
							unallocatedTask.StopTiming(newWaypoint);

							// Ask the user if they want to keep the idle time
							while (true) {
								// Ask user for choice
								String choice = RequestUserTimeChoice(
										newWaypoint, previousWaypoint);

								// Assign their choice appropriately
								if (choice == "keep") {
									System.out
											.println("Adding idle period to the total: "
													+ unallocatedTask);
									activeTask.AddTime(unallocatedTask
											.GetTotalTime());
									break;
								} else if (choice == "wipe") {
									// Ignore
									break;
								} else {
									// Try again
									continue;
								}
							}

							// Continue timing the main task
							activeTask.StartTiming(newWaypoint);
						}

					} else if (newWaypoint.getActivityState() == ActivityState.IDLE) {

						// Stop timing the main task
						activeTask.StopTiming(newWaypoint);

						// Start timing the idle period from zero
						unallocatedTask = new Task("Idle period", 0.0);
						unallocatedTask.StartTiming(newWaypoint);
					}
				}

				previousWaypoint = newWaypoint;

			} catch (TimedOutException e) {
				// Should never happen because set to wait forever
				LOGGER.fine("TimedOutException was thrown from read when it shouldn't have been");
			}
		}

	}

	public String RequestUserTimeChoice(ActivityWaypoint curWaypoint,
			ActivityWaypoint previousWaypoint) {
		BufferedReader console = new BufferedReader(new InputStreamReader(
				System.in));

		System.out.print("Enter choice (keep, wipe): ");
		String choice = null;
		try {
			choice = console.readLine();
		} catch (IOException e) {
			System.out
					.println("IO error trying to read your choice! Exiting...");
			System.exit(1);
		}

		if (choice.equalsIgnoreCase("k")) {
			choice = "keep";
		} else if (choice.equalsIgnoreCase("w")) {
			choice = "wipe";
		}

		return choice.toLowerCase();
	}

	/**
	 * @param args
	 *            Command line args
	 */
	public static void main(String[] args) {

		final double checkingRate = 1.0;
		final double idlePeriod = 5.0 * 60;
		// final double idlePeriod = 5.0;

		// Buffer
		BufferedActivityStream activityStream = new BufferedActivityStream();

		// Start consumer
		LiveTaskTimer eventHandler = new LiveTaskTimer(activityStream);
		eventHandler.start();

		// Start producer
		SysActivityMonitor activityMonitor = new SysActivityMonitor(
				activityStream, checkingRate, idlePeriod);
		activityMonitor.start();

		// Start a task printer
		Task activeTask = eventHandler.GetActiveTask();
		TaskPrinter printer = new TaskPrinter(activeTask);
		printer.start();

	}
}
