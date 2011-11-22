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

	private long RequestUserAmountOfTime(long maxTimeThatCanBeAllocated) {

		// Calendar to use for time extraction
		Calendar cal = Calendar.getInstance();

		// Convert time to ms + set cal
		long time_ms = (long) (maxTimeThatCanBeAllocated * 1000);
		cal.setTimeInMillis(time_ms);

		// Form it into a string
		String readableMaxTime = "";

		// Adjust to count from zero
		int totalTimeDays = cal.get(Calendar.DAY_OF_YEAR) - 1;
		readableMaxTime += totalTimeDays;

		// Epoch starts at 12:00pm
		int totalTimeHours = cal.get(Calendar.HOUR_OF_DAY) - 12;
		readableMaxTime += ":" + totalTimeHours;

		int totalTimeMins = cal.get(Calendar.MINUTE);
		readableMaxTime += ":" + totalTimeMins;

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

				// Accumulate to a ms total
				timeToAllocate = 0;
				timeToAllocate += mins * 60 * 1000;
				timeToAllocate += hours * 60 * 60 * 1000;
				timeToAllocate += days * 24 * 60 * 60 * 1000;
				
				// TODO Check that it doesn't exceed the limit allowed
				
			} else {
				// Try again
				System.out
						.println("Invalid choice, please try again. Format should be M[:H[:D]]");
			}
		}
	}

	@Override
	public long GetAmountOfTimeToAllocate() {
		return this.timeToAllocate;
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
