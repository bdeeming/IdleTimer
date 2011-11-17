/**
 * 
 */
package idletimer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import idletimer.ActivityWaypoint.ActivityState;
import idletimer.InputActivityStream.TimedOutException;

/**
 * @author bdeeming
 * 
 */
public class ActivityEventHandler extends Thread {

	InputActivityStream inputActivityStream;

	private final Logger LOGGER = Logger.getLogger(SysActivityMonitor.class
			.getName());

	/**
	 * Create a new event handler thread.
	 * 
	 * @param inputActivityStream
	 *            The input stream of activity events.
	 */
	public ActivityEventHandler(InputActivityStream inputActivityStream) {
		super();
		this.inputActivityStream = inputActivityStream;
	}

	@Override
	public void run() {

		System.out.println("Started timing");
		
		LOGGER.setLevel(Level.ALL);
		
		long totalTime = 0;

		// Start off in the active state
		ActivityWaypoint previousWaypoint = new ActivityWaypoint(
				ActivityState.ACTIVE);

		// Consume the input stream
		while (true) {
			try {

				ActivityWaypoint newWaypoint;
				newWaypoint = inputActivityStream
						.ReadActivityWaypoint(InputActivityStream.WAIT_FOREVER);

				// Check for state transition ACTIVE->IDLE
				if (previousWaypoint.getActivityState() == ActivityState.ACTIVE
						&& newWaypoint.getActivityState() == ActivityState.IDLE) {

					long timeAllotment = newWaypoint.getTime()
							- previousWaypoint.getTime();

					totalTime += timeAllotment;
					
					LOGGER.warning("Whent IDLE at: " + newWaypoint);

				} else if (previousWaypoint.getActivityState() == ActivityState.IDLE
						&& newWaypoint.getActivityState() == ActivityState.ACTIVE) {

					long timeAllotment = newWaypoint.getTime()
							- previousWaypoint.getTime();

					LOGGER.warning("Whent ACTIVE at: " + newWaypoint);
					
					while (true) {
						// Ask user for choice
						String choice = RequestUserTimeChoice(newWaypoint,
								previousWaypoint);

						// Assign their choice appropriately
						if (choice == "keep") {
							totalTime += timeAllotment;
							break;
						} else if (choice == "wipe") {
							// Ignore
							break;
						} else {
							// Try again
							continue;
						}
					}
					
					// Print the total time
					int hours = (int)(totalTime/1000/60/60);
					int mins = (int)((totalTime/1000 - hours * 60)/60);
					System.out.println("Time so far: " + hours + ":" + mins);
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

		System.out.println("Enter choice (keep, wipe): ");
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

		// Buffer
		BufferedActivityStream activityStream = new BufferedActivityStream();

		// Start consumer
		ActivityEventHandler eventHandler = new ActivityEventHandler(
				activityStream);
		eventHandler.start();

		// Start producer
		SysActivityMonitor activityMonitor = new SysActivityMonitor(
				activityStream, checkingRate, idlePeriod);
		activityMonitor.start();

	}
}
