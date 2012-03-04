/**********************************************************************
 * Copyright (c) 2011 Benjamin Deeming.
 * 
 * This file is part of IdleTimer.
 * 
 * IdleTimer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * IdleTimer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with IdleTimer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *********************************************************************/

package idletimer;

import java.util.Date;

/**
 * The system activity at a given time point.
 */
public class ActivityWaypoint extends Date {

	private static final long serialVersionUID = 6926825055644157945L;

	/**
	 * The state of system activity.
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
	 * @param activityState
	 *            The system activity state at the time this waypoint is
	 *            created.
	 */
	public ActivityWaypoint(ActivityState activityState) {
		super();
		this.activityState = activityState;
	}

	public ActivityState getActivityState() {
		return activityState;
	}

}
