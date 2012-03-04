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
import idletimer.InputActivityStream.TimedOutException;

import org.junit.Test;

public class BufferedActivityStreamTest {

	@Test
	public void testReadActivityState() {
		// Create the stream
		BufferedActivityStream stream = new BufferedActivityStream();

		// Put one state on the stream
		stream.PutActivityWaypoint(new ActivityWaypoint(ActivityState.IDLE));

		// Read the state off
		ActivityWaypoint firstResultRead = null;
		try {
			firstResultRead = stream.ReadActivityWaypoint(10);
		} catch (TimedOutException e) {
			// Catch timed out exception (fail test - shouldn't have blocked)
			fail("ReadActivityState() blocked when it shouldn't have");
		} catch (InterruptedException e) {
			fail("Should not have thrown interupted exception");
		}

		// Check it was the same value
		assertEquals(ActivityState.IDLE, firstResultRead.getActivityState());

		// Read again
		try {
			stream.ReadActivityWaypoint(10);
			fail("ReadActivityState() did not block when it should have");
		} catch (TimedOutException e) {
			// Check for the time out exception (pass test - should have
			// blocked)
		} catch (InterruptedException e) {
			fail("Should not have thrown interupted exception");
		}
	}

	@Test
	public void testInteruptRead() {
		// Create the stream
		BufferedActivityStream stream = new BufferedActivityStream();

		// Thread to create an interupt
		Thread toInterupt = new Thread() {

			private BufferedActivityStream getsInterupted;

			public synchronized Thread start(
					BufferedActivityStream getsInterupted) {
				this.getsInterupted = getsInterupted;
				super.start();
				return this;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				// Do a read that blocks
				try {
					getsInterupted
							.ReadActivityWaypoint(InputActivityStream.WAIT_FOREVER);
				} catch (TimedOutException e) {
					fail("Shouldn't timeout");
				} catch (InterruptedException e) {
					// Supposed to throw
				}
			}

		}.start(stream);

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			fail();
		}

		toInterupt.interrupt();
	}
}
