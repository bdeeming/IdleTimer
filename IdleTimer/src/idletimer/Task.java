/**
 * 
 */
package idletimer;

/**
 * A single task that the user is timing.
 * 
 * @author bdeeming
 * 
 */
public class Task {

	/**
	 * The total time spent on this task.
	 */
	private double totalTime;

	/**
	 * @param totalTime
	 *            The initial total time spent on this task.
	 */
	public Task(double totalTime) {
		super();
		this.totalTime = totalTime;
	}

	/**
	 * Increment the amount of time spent on this task.
	 * 
	 * @param timeToAdd
	 *            The time in seconds to add to the task.
	 */
	synchronized public void AddTime(double timeToAdd) {
		totalTime += timeToAdd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	synchronized public String toString() {
		// Print the total time
		int hours = (int) (totalTime / 1000 / 60 / 60);
		int mins = (int) ((totalTime / 1000 - hours * 60 * 60) / 60);
		return "Task [totalTime="  + hours + ":" + mins + "]";
	}

}
