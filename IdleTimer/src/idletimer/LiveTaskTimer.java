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

import java.util.logging.Logger;

import idletimer.ActivityWaypoint.ActivityState;
import idletimer.InputActivityStream.TimedOutException;
import idletimer.ui.CmdLineUi;
import idletimer.ui.TimeAllocationChooser;

/**
 * @author bdeeming
 * 
 */
public class LiveTaskTimer extends Thread {

	private final Logger LOGGER = Logger.getLogger(SysActivityMonitor.class
			.getName());

	// The task currently being timed
	private Task activeTask;

	// Producer of activity events
	private ActivityStateProducer activityProducer;

	// The stream connection between the producer and this thread
	private InputActivityStream inputActivityStream;

	// Conditional so we can tell the diff between a StopTiming request (causes
	// an interrupt) and any other interrupt
	private boolean isRunning;

	private TimeAllocationChooser timeAllocationChooser;

	/**
	 * Create a new live task timer thread.
	 * 
	 * @param activityProducer
	 *            The producer of activity states
	 * @param initialTask
	 *            The task to begin timing with.
	 */
	public LiveTaskTimer(ActivityStateProducer activityProducer,
			InputActivityStream inputActivityStream,
			TimeAllocationChooser timeAllocationUi, Task initialTask) {
		super();

		// Producer of activity events
		this.activityProducer = activityProducer;
		this.inputActivityStream = inputActivityStream;

		// To get user input
		this.timeAllocationChooser = timeAllocationUi;

		// Task that we begin with
		this.activeTask = initialTask;

		// Start off not running
		this.isRunning = false;
	}

	/**
	 * @param newActiveTask
	 *            The activeTask to set
	 */
	synchronized public void SetActiveTask(Task newActiveTask) {
		// Maintain proper task state
		if (this.activeTask.IsCurrentlyBeingTimed()) {
			// Stop timing the current task
			this.activeTask.StopTiming();
			// Continue the timing with the new task
			newActiveTask.StartTiming();
		} else {
			// Not currently timing => state of tasks already match
		}

		// Make new task the active task
		this.activeTask = newActiveTask;
	}

	/**
	 * @return The currently active task.
	 */
	synchronized public Task GetActiveTask() {
		return activeTask;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#start()
	 */
	@Override
	public void start() {
		this.StartTiming();
	}

	public synchronized void StartTiming() {
		this.isRunning = true;

		// Start ourself consuming first
		super.start();

		// Start the activity monitor producing
		activityProducer.StartProducing();
	}

	public synchronized void StopTiming() {

		// Stop the producer
		activityProducer.StopProducing();

		// Stop timing the task
		activeTask.StopTiming();

		// Stop this thread
		this.isRunning = false;

		// Interrupt the read so we don't sit there forever
		this.interrupt();
	}

	public synchronized boolean IsCurrentlyTiming() {
		return isRunning;
	}

	@Override
	public void run() {

		// Start from a clean stream
		inputActivityStream.Clear();

		// Start off in the active state
		ActivityWaypoint previousWaypoint = new ActivityWaypoint(
				ActivityState.ACTIVE);

		synchronized (this) {
			// Start timing the active task
			activeTask.StartTiming();
		}

		// A task to time the idle period with
		Task unallocatedTask = new Task("Idle period", 0.0);

		// Consume the input stream
		while (IsCurrentlyTiming()) {
			try {

				ActivityWaypoint newWaypoint;
				newWaypoint = inputActivityStream
						.ReadActivityWaypoint(InputActivityStream.WAIT_FOREVER);

				// Any task swaps, stops, etc should happen outside of this
				// block
				synchronized (this) {
					// Check for state transition to ACTIVE
					if (newWaypoint.getActivityState() == ActivityState.ACTIVE) {

						if (previousWaypoint.getActivityState() != ActivityState.ACTIVE) {

							// Stop timing the idle period TODO This currently
							// isn't in use - should it used in the user choice
							// request to simplify things? Maybe make it a
							// slightly different type..?
							unallocatedTask.StopTiming(newWaypoint);

							// Ask the user what to do with the idle time
							timeAllocationChooser.RequestUsersChoice(
									activeTask, previousWaypoint, newWaypoint);

							// Assign their choice appropriately
							Task taskToAllocateTo = timeAllocationChooser
									.GetTaskToAllocateTimeTo();
							taskToAllocateTo.AddTime(timeAllocationChooser
									.GetAmountOfTimeToAllocate());

							// Continue timing the chosen task
							SetActiveTask(timeAllocationChooser
									.GetTaskToContinueTimingWith());
							activeTask.StartTiming(newWaypoint);
						}

					} else if (newWaypoint.getActivityState() == ActivityState.IDLE) {

						// Stop timing the main task
						activeTask.StopTiming(newWaypoint);

						// Start timing the idle period from zero
						unallocatedTask = new Task("Idle period", 0.0);
						unallocatedTask.StartTiming(newWaypoint);
					}
				}

				previousWaypoint = newWaypoint;

			} catch (TimedOutException e) {
				// Should never happen because set to wait forever
				LOGGER.fine("TimedOutException was thrown from read when it shouldn't have been");
			} catch (InterruptedException e) {
				// Skip everything except the while conditional checking for
				// running status
			}
		}

	}

	/**
	 * Creates both a live task timer and a sys activity monitor and connects
	 * them together.
	 * 
	 * @param activityCheckingPeriod
	 *            The system activity state is evaluated once every this number
	 *            of seconds.
	 * @param minIdleTime
	 *            The number of seconds that the system must be inactive for to
	 *            be considered IDLE.
	 * @param initialTask
	 *            The task to begin timing with.
	 * @return The new LiveTaskTimer instance.
	 */
	static public LiveTaskTimer CreateSysActivityLiveTimer(
			double activityCheckingPeriod, double minIdlePeriod,
			TimeAllocationChooser timeAllocationUi, Task initialTask) {

		// Buffer to connect the two threads
		BufferedActivityStream activityStream = new BufferedActivityStream();

		// Producer of activity events
		SysActivityMonitor activityProducer = new SysActivityMonitor(
				activityStream, activityCheckingPeriod, minIdlePeriod);

		// Finally the live timer itself
		return new LiveTaskTimer(activityProducer, activityStream,
				timeAllocationUi, initialTask);
	}

	/**
	 * @param args
	 *            Command line args
	 */
	public static void main(String[] args) {

		final double checkingRate = 1.0;
		final double idlePeriod = 5.0 * 60;
		// final double idlePeriod = 5.0;

		// Create a task to time
		Task defaultTask = new Task("Default", 0.0);

		// Create a UI
		CmdLineUi ui = new CmdLineUi();

		// Create the live timer
		LiveTaskTimer liveTimer = CreateSysActivityLiveTimer(checkingRate,
				idlePeriod, ui, defaultTask);

		// Start the live task timer
		liveTimer.StartTiming();

		// Show the task to the user
		ui.DisplayTask(defaultTask);

	}
}
