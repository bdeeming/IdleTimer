/**
 * 
 */
package idletimer;

import java.util.Date;

/**
 * The system activity at a given time point.
 * 
 * @author bdeeming
 */
public class ActivityWaypoint extends Date {

	private static final long serialVersionUID = 6926825055644157945L;

	/**
	 * The state of system activity.
	 * 
	 * @author Ben
	 */
	public enum ActivityState {
		ACTIVE, IDLE
	}

	/**
	 * The state of the system at this way point.
	 */
	ActivityState activityState;

	/**
	 * Constructs a activity waypoint with the current time as the time stamp.
	 * 
	 * @param activityState The system activity state at the time this waypoint is created. 
	 */
	public ActivityWaypoint(ActivityState activityState) {
		super();
		this.activityState = activityState;
	}

	public ActivityState getActivityState() {
		return activityState;
	}

}
