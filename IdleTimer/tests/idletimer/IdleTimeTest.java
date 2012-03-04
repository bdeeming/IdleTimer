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
