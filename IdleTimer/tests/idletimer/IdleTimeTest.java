package idletimer;

import static org.junit.Assert.*;
import idletimer.IdleTime.PlatformNotSupportedException;

import org.junit.Test;

public class IdleTimeTest {

	@Test
	public void testGetSeconds() {
		IdleTime idleTime = new IdleTime();
		try {
			assertTrue(idleTime.GetTimeSinceLastActivity() > 0.0);
		} catch (PlatformNotSupportedException e) {
			fail("Threw platform not supported exception");
		}
	}

}
