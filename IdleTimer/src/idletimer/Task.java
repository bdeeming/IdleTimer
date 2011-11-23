/**
 * 
 */
package idletimer;

import java.util.Calendar;
import java.util.Date;

/**
 * A single task that may be timed.<br>
 * Allows time to be accumulated and periodically stored.<br>
 * The total time and elapsed time is able to be retrieved ad-hoc at any point
 * in time.<br>
 * All methods are thread safe.
 * 
 * @author bdeeming
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
	 * Create a new task instance.
	 * 
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
	 * 
	 * @param start
	 *            The start date.
	 * @param end
	 *            The end date.
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

		// Convert the two time stamps into strings
		String totalTimeString = TimeToString(GetTotalTime());
		String elapsedTimeString = TimeToString(GetElapsedTime());

		return "Task '" + name + "' [Total time= " + totalTimeString
				+ "] [Elapsed time: " + elapsedTimeString + "]";
	}

	private String TimeToString(double seconds) {
		// Calendar to use for time extraction
		Calendar cal = Calendar.getInstance();

		// Convert time to ms + set cal
		long time_ms = (long) (seconds * 1000);
		cal.setTimeInMillis(time_ms);

		// Form it into a string
		String timeString = "";

		// Adjust to count from zero
		int totalTimeDays = cal.get(Calendar.DAY_OF_YEAR) - 1;
		timeString += totalTimeDays;

		// Epoch starts at 12:00pm
		int totalTimeHours = cal.get(Calendar.HOUR_OF_DAY) - 12; 
		timeString += ":" + totalTimeHours;

		int totalTimeMins = cal.get(Calendar.MINUTE);
		timeString += ":" + totalTimeMins;

		int totalTimeSeconds = cal.get(Calendar.SECOND);
		timeString += ":" + totalTimeSeconds;
		
		return timeString;
	}

	public String GetName() {
		return name;
	}

}
