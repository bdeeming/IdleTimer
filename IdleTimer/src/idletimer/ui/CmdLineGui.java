/**
 * 
 */
package idletimer.ui;

import idletimer.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * A command line version of the idle timer GUI.
 * 
 * @author Ben
 * 
 */
public class CmdLineGui implements TimeAllocationChooser, TaskDisplayer {

	private final Logger LOGGER = Logger.getLogger(CmdLineGui.class.getName());

	private TaskPrinter taskPrinter;
	private long timeToAllocate;
	private BufferedReader inputReader;
	private Task originalTask;

	public CmdLineGui() {
		super();
		this.taskPrinter = new TaskPrinter();
		// For user input
		this.inputReader = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override
	public synchronized void DisplayTask(Task taskToDisplay) {
		// Start it running
		this.taskPrinter.StartPrintingTask(taskToDisplay);
	}

	@Override
	public synchronized void StopDisplayingTask() {
		// Stop it
		this.taskPrinter.StopPrintingTask();
	}

	@Override
	public void RequestUsersChoice(Task currentTask, Date idleStartTime,
			Date idleEndTime) {

		// Keep the task that the request was made with
		this.originalTask = currentTask;

		// Display the necessary details
		DisplayCurrentTaskDetails(currentTask);
		DisplayIdlePeriodDetails(idleStartTime, idleEndTime);

		// Ask user for choice
		UserAllocationChoice choice = RequestUserTimeChoice(idleStartTime,
				idleEndTime);

		// Assign their choice appropriately
		switch (choice) {
		default:
			LOGGER.severe("Got an invalid time allocation state. "
					+ "Defaulting to keeping all of the idle time.");
		case KEEP_ALL:
			// Calc the total difference
			this.timeToAllocate = idleEndTime.getTime()
					- idleStartTime.getTime();
			break;
		case KEEP_PART:
			// Ask the user how much
			long maxTimeThatCanBeAllocated = idleEndTime.getTime()
					- idleStartTime.getTime();
			this.timeToAllocate = RequestUserAmountOfTime(maxTimeThatCanBeAllocated);
			break;
		case KEEP_NONE:
			// Set to zero
			this.timeToAllocate = 0;
			break;
		}
	}

	private enum UserAllocationChoice {
		KEEP_ALL, KEEP_PART, KEEP_NONE
	}

	private void DisplayCurrentTaskDetails(Task taskToDisplay) {
		// TODO Display the current tasks details (total committed time, etc)

	}

	private void DisplayIdlePeriodDetails(Date idlePeriodStart,
			Date idlePeriodEnd) {
		// TODO Display the current tasks details (total committed time, etc)

	}

	private UserAllocationChoice RequestUserTimeChoice(Date startTime,
			Date endTime) {

		System.out.println("Would you like to keep all of the idle time, "
				+ "part of the idle time, or none of it?");

		while (true) {
			System.out.print("Enter choice (all, part, none): ");
			String choice = null;
			try {
				choice = inputReader.readLine();
			} catch (IOException e) {
				LOGGER.severe("Error while reading user IO from cmd line. Exiting.");
				System.out.println("IO error while trying to "
						+ "read your choice! Exiting...");
				System.exit(1);
			}

			if (choice.equalsIgnoreCase("a") || choice.equalsIgnoreCase("all")) {
				return UserAllocationChoice.KEEP_ALL;
			} else if (choice.equalsIgnoreCase("p")
					|| choice.equalsIgnoreCase("part")) {
				return UserAllocationChoice.KEEP_PART;
			} else if (choice.equalsIgnoreCase("n")
					|| choice.equalsIgnoreCase("none")) {
				return UserAllocationChoice.KEEP_NONE;
			} else {
				// Try again
				System.out.println("Invalid choice, please try again");
			}
		}
	}

	/**
	 * @param maxTimeThatCanBeAllocated_ms
	 *            The maximum amount of time that is allowed to be allocated.
	 * @return The amount of time entered by the user
	 */
	private long RequestUserAmountOfTime(long maxTimeThatCanBeAllocated_ms) {

		final long ONE_MIN_MS = 60 * 1000;
		
		// Calendar to use for time extraction
		Calendar cal = Calendar.getInstance();

		// Convert time to ms + set cal
		cal.setTimeInMillis(maxTimeThatCanBeAllocated_ms);

		// Form it into a string
		String readableMaxTime = "";

		// Adjust to count from zero
		int totalTimeDays = cal.get(Calendar.DAY_OF_YEAR) - 1;
		readableMaxTime += totalTimeDays + " days ";

		// Epoch starts at 12:00pm
		int totalTimeHours = cal.get(Calendar.HOUR_OF_DAY) - 12;
		readableMaxTime += totalTimeHours + " hours ";

		int totalTimeMins = cal.get(Calendar.MINUTE);
		// If max time is less than 1 min then display 1 min
		if (maxTimeThatCanBeAllocated_ms < ONE_MIN_MS) {
			totalTimeMins = 1;
		}
		readableMaxTime += totalTimeMins + " mins";

		System.out.println("How much of the " + readableMaxTime
				+ " should be allocated? (M[:H[:D]]) ");

		while (true) {
			String userInput = null;
			try {
				userInput = inputReader.readLine();
			} catch (IOException e) {
				LOGGER.severe("Error while reading user IO from cmd line. Exiting.");
				System.out.println("IO error while trying to "
						+ "read your input! Exiting...");
				System.exit(1);
			}

			// Check its a number
			if (userInput.matches("(\\d{1,2})(\\:(\\d{1,2})){0,2}")) {
				String timeValues[] = userInput.split(":");

				// Convert to longs
				long mins = (timeValues.length > 0) ? new Long(timeValues[0])
						: 0;
				long hours = (timeValues.length > 1) ? new Long(timeValues[1])
						: 0;
				long days = (timeValues.length > 2) ? new Long(timeValues[2])
						: 0;

				// Accumulate to a local total (ms)
				long timeToAllocate = 0;
				timeToAllocate += mins * 60 * 1000;
				timeToAllocate += hours * 60 * 60 * 1000;
				timeToAllocate += days * 24 * 60 * 60 * 1000;

				// Check that it doesn't exceed the limit allowed
				if (timeToAllocate <= maxTimeThatCanBeAllocated_ms) {
					return timeToAllocate;
				} else if (maxTimeThatCanBeAllocated_ms < ONE_MIN_MS
						&& timeToAllocate == ONE_MIN_MS) {
					// Return the max (actually less than one min, but keeps
					// interface tidy without secs)
					return maxTimeThatCanBeAllocated_ms;
				} else {
					System.out.println("The time that you entered was "
							+ "larger than the idle period. Please try again.");
				}

			} else {
				// Try again
				System.out
						.println("Invalid choice, please try again. Format should be M[:H[:D]]");
			}
		}
	}

	@Override
	public double GetAmountOfTimeToAllocate() {
		return ((double)(this.timeToAllocate)) / 1000.0;
	}

	@Override
	public Task GetTaskToAllocateTimeTo() {
		// TODO Full function not due until v2.0
		return this.originalTask;
	}

	@Override
	public Task GetTaskToContinueTimingWith() {
		// TODO Full function not due until v2.0
		return this.originalTask;
	}

}
