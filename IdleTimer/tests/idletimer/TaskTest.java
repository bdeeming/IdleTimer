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

import java.util.Date;

import org.junit.Test;

public class TaskTest {

	@Test
	public void testAddTime() {
		Task task = new Task("Test", 0.0);
		assertTrue(0.0 == task.GetTotalTime());

		task.AddTime(1.0);
		assertTrue(1.0 == task.GetTotalTime());
	}

	@Test
	public void testStartTimingDate() {
		Task task = new Task("Test", 0.0);
		long time10MsAgo = (new Date()).getTime() - 10;
		task.StartTiming(new Date(time10MsAgo));

		assertEquals(10.0 / 1000.0, task.GetElapsedTime(), 1.0);
		assertEquals(10.0 / 1000.0, task.GetTotalTime(), 1.0);
	}

	@Test
	public void testStartTiming() {
		Task task = new Task("Test", 0.0);

		assertFalse(task.IsCurrentlyBeingTimed());
		task.StartTiming();
		assertTrue(task.IsCurrentlyBeingTimed());

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}

		assertEquals(10.0 / 1000.0, task.GetElapsedTime(), 1.0);
		assertEquals(10.0 / 1000.0, task.GetTotalTime(), 1.0);
	}

	@Test
	public void testStopTiming() {
		Task task = new Task("Test", 0.0);
		long time10MsAgo = (new Date()).getTime() - 10;
		task.StartTiming(new Date(time10MsAgo));
		task.StopTiming();

		assertEquals(0.0, task.GetElapsedTime(), 0.0);
		assertEquals(10.0 / 1000.0, task.GetTotalTime(), 1.0);

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}

		// Shouldn't have changed
		assertEquals(0.0, task.GetElapsedTime(), 0.0);
		assertEquals(10.0 / 1000.0, task.GetTotalTime(), 1.0);
	}

	@Test
	public void testStopTimingDate() {
		Task task = new Task("Test", 0.0);
		task.StartTiming();
		long time10MsIntoFuture = (new Date()).getTime() + 10;
		task.StopTiming(new Date(time10MsIntoFuture));

		assertEquals(0.0, task.GetElapsedTime(), 0.0);
		assertEquals(10.0 / 1000.0, task.GetTotalTime(), 1.0);

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}

		// Shouldn't have changed
		assertEquals(0.0, task.GetElapsedTime(), 0.0);
		assertEquals(10.0 / 1000.0, task.GetTotalTime(), 1.0);

		// Negative time
		Task taskNeg = new Task("Test", 0.0);
		taskNeg.StartTiming();
		long time10MsAgo = (new Date()).getTime() - 10;
		taskNeg.StopTiming(new Date(time10MsAgo));

		assertEquals(0.0, taskNeg.GetElapsedTime(), 0.0);
		assertEquals(10.0 / 1000.0, taskNeg.GetTotalTime(), 1.0);

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}

		// Shouldn't have changed
		assertEquals(0.0, taskNeg.GetElapsedTime(), 0.0);
		assertEquals(10.0 / 1000.0, taskNeg.GetTotalTime(), 1.0);
	}

}
