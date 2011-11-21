/**
 * 
 */
package idletimer.ui;

import idletimer.Task;

import java.util.Date;

/**
 * Requests to the user for time allocation choices to be made.
 * 
 * @author Ben
 * 
 */
public interface TimeAllocationChooser {

	/**
	 * Presents the user with the time allocation choice.<br>
	 * Displays to user:<br>
	 * 1- The active task details up until the idle period began<br>
	 * 2- The start and end time of the idle period<br>
	 * <br>
	 * It asks the user for:<br \>
	 * 1- If they would like to apply the idle time to the current task, or to a
	 * different one<br>
	 * 2- If they want to keep all, part, or none of the idle time<br>
	 * 3- If part, then how much of it.
	 * 4- If they would like to continue timing the current task, or a different
	 * one.<br>
	 * <br>
	 * Results are stored in this object instance and can be accessed with other
	 * routines.
	 */
	public void RequestUsersChoice(final Task currentTask, final Date idleStartTime,
			final Date idleEndTime);

	/**
	 * Get the total amount of time the user has selected to assign to the
	 * chosen task.
	 * 
	 * @return The time to allocate, in milliseconds.
	 */
	public long GetAmountOfTimeToAllocate();
	
	/** Get the task that the user chose to allocate the time to.
	 * @return The chose task instance.
	 */
	public Task GetTaskToAllocateTimeTo();
	
	/** Get the task the user chose to continue further timing with.
	 * @return The chosen task instance.
	 */
	public Task GetTaskToContinueTimingWith();
}
