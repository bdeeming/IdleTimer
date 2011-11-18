/**
 * 
 */
package idletimer;

import java.util.Date;

/**
 * A single task that the user is timing.
 * 
 * @author bdeeming
 * 
 */
/**
 * @author Ben
 *
 */
/**
 * @author Ben
 * 
 */
public class Task {

	/**
	 * The total time spent on this task.
	 */
	private double totalTime;
	private Date startTime;
	private String name;

	/**
	 * @param name
	 *            The name for this task.
	 * @param totalTime
	 *            The initial total time spent on this task.
	 */
	public Task(String name, double totalTime) {
		super();
		this.totalTime = totalTime;
		this.startTime = null;
		this.name = name;
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

	/**
	 * @param startTime
	 *            The time stamp to start timign from.
	 */
	synchronized public void StartTiming(Date startTime) {
		// Only create a new start time if we were not already started
		if (!this.IsCurrentlyBeingTimed()) {
			this.startTime = startTime;
		}
	}

	/**
	 * Start/resume timing this task at the current time.
	 */
	synchronized public void StartTiming() {
		this.StartTiming(new Date());
	}

	synchronized public boolean IsCurrentlyBeingTimed() {
		return startTime != null;
	}

	/**
	 * Stop timing this task.
	 * 
	 * @param stopTime
	 *            The time at which to effectively stop the timing.
	 */
	synchronized public void StopTiming(Date stopTime) {
		// If we have in fact started timing
		if (this.IsCurrentlyBeingTimed()) {
			// Add the elapsed time to the total
			this.AddTime(GetTimeDifference(startTime, stopTime));
		}

		// Stop timing
		startTime = null;
	}

	/**
	 * Stop timing the task, using the current time stamp as the stop time.
	 */
	synchronized public void StopTiming() {
		this.StopTiming(new Date());
	}

	/**
	 * Get the time since we started timing this task. Excludes the time
	 * accumulated in previous timing intervals.
	 * 
	 * @return
	 */
	synchronized double GetElapsedTime() {
		// If we're not currently timing then simply return zero
		if (!this.IsCurrentlyBeingTimed()) {
			return 0.0;
		} else {
			// Get the amount of time since we started timing
			double elapsedTime = GetTimeDifference(startTime, new Date());

			if (elapsedTime > 0.0) {
				// Convert to seconds
				return elapsedTime;
			} else {
				return 0.0;
			}
		}
	}

	/**
	 * Calculate the time difference between two date objects.
	 * @param start The start date.
	 * @param end The end date.
	 * @return The time difference in seconds.
	 */
	private double GetTimeDifference(Date start, Date end) {
		// Get the amount of time since we started timing
		long elapsedTime_ms = end.getTime() - start.getTime();

		if (elapsedTime_ms > 0.0) {
			// Convert to seconds
			return (double) (elapsedTime_ms) / 1000;
		} else {
			return 0.0;
		}
	}

	/**
	 * If the task is currently being timed, then this total includes the
	 * elapsed time since the timing started in the total.
	 * 
	 * @return The total time in seconds that this task has been running for.
	 */
	synchronized double GetTotalTime() {
		return totalTime + this.GetElapsedTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	synchronized public String toString() {
		// Split it to readable
		int totalHours = (int) (GetTotalTime() / (60 * 60));
		int totalMins = (int) (GetTotalTime() / 60 - (totalHours * 60));
		int totalSec = (int) (GetTotalTime() - (totalHours * 60 * 60 + totalMins * 60));

		int elapsedHours = (int) (GetElapsedTime() / 1000 / 60 / 60);
		int elapsedMins = (int) ((GetElapsedTime() / 1000 - totalHours * 60 * 60) / 60);

		return "Task '" + name + "' [Total time=" + totalHours + ":"
				+ totalMins + ":" + totalSec + "] [Elapsed time: "
				+ +elapsedHours + ":" + elapsedMins + "]";
	}

}
