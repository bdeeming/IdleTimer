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

import static org.junit.Assert.*;
import idletimer.ActivityWaypoint.ActivityState;

import org.junit.Test;

public class ActivityWaypointTest {

	@Test
	public void testActivityWaypoint() {
		ActivityWaypoint waypoint = new ActivityWaypoint(ActivityState.ACTIVE);

		assertEquals(ActivityState.ACTIVE, waypoint.getActivityState());
		assertTrue("Timestamp is greater than the Epoch",
				waypoint.getTime() > 0);
	}

}
