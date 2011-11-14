package idletimer;

import static org.junit.Assert.*;
import idletimer.BufferedActivityStream.TimedOutException;

import org.junit.Test;

public class BufferedActivityStreamTest {

	@Test
	public void testReadActivityState() {
		// Create the stream
		BufferedActivityStream stream = new BufferedActivityStream();

		// Put one state on the stream
		stream.PutActivityState(ActivityState.IDLE);

		// Read the state off
		ActivityState firstResultRead = null;
		try {
			firstResultRead = stream.ReadActivityState(10);
		} catch (TimedOutException e) {
			// Catch timed out exception (fail test - shouldn't have blocked)
			fail("ReadActivityState() blocked when it shouldn't have");
		}

		// Check it was the same value
		assertEquals(ActivityState.IDLE, firstResultRead);

		// Read again
		try {
			stream.ReadActivityState(10);
			fail("ReadActivityState() did not block when it should have");
		} catch (TimedOutException e) {
			// Check for the time out exception (pass test - should have
			// blocked)
		}
	}
}
