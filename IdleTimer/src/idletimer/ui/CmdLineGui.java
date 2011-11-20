/**
 * 
 */
package idletimer.ui;

import idletimer.ActivityWaypoint;
import idletimer.SysActivityMonitor;
import idletimer.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

	public CmdLineGui() {
		super();
		this.taskPrinter = new TaskPrinter();
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

		// Ask user for choice
		UserAllocationChoice choice = RequestUserTimeChoice(idleStartTime,
				idleEndTime);

		// Assign their choice appropriately
		switch (choice) {
		case KEEP_ALL:
			// Calc the total difference
			this.timeToAllocate = idleEndTime.getTime()
					- idleStartTime.getTime();
			break;
		case KEEP_PART:
			// Ask the user how much
			this.timeToAllocate = RequestUserAmountOfTime();
			break;
		case KEEP_NONE:
			// Set to zero
			this.timeToAllocate = 0;
			break;
		default:

		}

	}

	private enum UserAllocationChoice {
		KEEP_ALL, KEEP_PART, KEEP_NONE
	}

	private UserAllocationChoice RequestUserTimeChoice(Date startTime,
			Date endTime) {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				System.in));

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

	private long RequestUserAmountOfTime() {
		System.out.println("How much of the time should be allocated?");

		while (true) {
			// TODO Implement
//			String choice = null;
//			try {
//				choice = inputReader.readLine();
//			} catch (IOException e) {
//				LOGGER.severe("Error while reading user IO from cmd line. Exiting.");
//				System.out.println("IO error while trying to "
//						+ "read your choice! Exiting...");
//				System.exit(1);
//			}
//
//			if (choice.equalsIgnoreCase("a") || choice.equalsIgnoreCase("all")) {
//				return UserAllocationChoice.KEEP_ALL;
//			} else if (choice.equalsIgnoreCase("p")
//					|| choice.equalsIgnoreCase("part")) {
//				return UserAllocationChoice.KEEP_PART;
//			} else if (choice.equalsIgnoreCase("n")
//					|| choice.equalsIgnoreCase("none")) {
//				return UserAllocationChoice.KEEP_NONE;
//			} else {
//				// Try again
//				System.out.println("Invalid choice, please try again");
//			}
		}
	}

	@Override
	public long GetAmountOfTimeToAllocate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Task GetTaskToAllocateTimeTo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task GetTaskToContinueTimingWith() {
		// TODO Auto-generated method stub
		return null;
	}

}
