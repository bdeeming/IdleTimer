/**
 * 
 */
package idletimer.ui;

import idletimer.Task;

/**
 * Methods for displaying a task to the user.
 * 
 * @author Ben
 * 
 */
public interface TaskDisplayer {

	/**
	 * Displays the task to the user.<br>
	 * This is a non-blocking method.
	 * 
	 * @param taskToDisplay
	 *            The task to display to the user.
	 */
	public void DisplayTask(Task taskToDisplay);

	/**
	 * Stop displaying the task to the user.
	 */
	public void StopDisplayingTask();

}
