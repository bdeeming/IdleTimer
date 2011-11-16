package idletimer;

import static org.junit.Assert.*;
import idletimer.ActivityWaypoint.ActivityState;

import org.junit.Test;

public class ActivityWaypointTest {

	@Test
	public void testActivityWaypoint() {
		ActivityWaypoint waypoint = new ActivityWaypoint(ActivityState.ACTIVE);
		
		assertEquals(ActivityState.ACTIVE, waypoint.getActivityState());
		assertTrue("Timestamp is greater than the Epoch", waypoint.getTime() > 0);
	}

}
