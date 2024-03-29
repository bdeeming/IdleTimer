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
import idletimer.IdleTime.PlatformNotSupportedException;

/**
 * Thread to monitor a queue up system activity status changes.
 * 
 * @author Ben
 * 
 */
public class SysActivityMonitor extends Thread implements ActivityStateProducer {

	private final Logger LOGGER = Logger.getLogger(SysActivityMonitor.class
			.getName());

	OutputActivityStream outputActivityStream;
	double activityCheckingPeriod;
	double minIdleTime;
	ActivityState lastPublishedActivityState;
	IdleTime idleTime;

	private boolean isRunning;

	/**
	 * Monitors system activity and publishes it as it changes to an output
	 * stream.
	 * 
	 * @param outputActivityStream
	 *            The stream to output activity changes to.
	 * @param activityCheckingPeriod
	 *            The system activity state is evaluated once every this number
	 *            of seconds.
	 * @param minIdleTime
	 *            The number of seconds that the system must be inactive for to
	 *            be considered IDLE.
	 */
	public SysActivityMonitor(OutputActivityStream outputActivityStream,
			double activityCheckingPeriod, double minIdleTime) {

		super();
		this.outputActivityStream = outputActivityStream;
		this.activityCheckingPeriod = activityCheckingPeriod;
		this.minIdleTime = minIdleTime;
		this.lastPublishedActivityState = null;
		this.idleTime = new IdleTime();
		this.isRunning = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		this.StartProducing();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see idletimer.ActivityStateProducer#StartProducing()
	 */
	@Override
	public void StartProducing() {
		this.isRunning = true;
		super.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see idletimer.ActivityStateProducer#StopProducing()
	 */
	@Override
	public void StopProducing() {
		// Gets checked in the main loop before anything more is published
		this.isRunning = false;
	}

	@Override
	public void run() {

		// Convert the period to ms for the sleep
		long activityCheckingPeriod_ms = (long) (this.activityCheckingPeriod * 1000);

		exit: while (true) {

			// Sleep for one period
			try {
				sleep(activityCheckingPeriod_ms);
			} catch (InterruptedException e) {
			}

			// Check if we should stop running
			if (!isRunning) {
				break exit;
			}

			double timeSinceLastActivity = 0;
			try {
				// Get the systems activity state for the period just slept -
				// TODO this needs profiling
				timeSinceLastActivity = idleTime.GetTimeSinceLastActivity();
			} catch (PlatformNotSupportedException e) {
				LOGGER.severe("System activity monitor encontered a fatal error. Your "
						+ "platform is not supported for system activity detection.");
				break exit;
			}

			// Evaluate the system activity state
			PeriodActivityState thisPeriodState = EvaluatePeriodActivityState(timeSinceLastActivity);

			// TODO Log the state to file

			// Convert to the external activity state type
			ActivityWaypoint externalPeriodState = PeriodActivityState
					.ConvertToActivityWaypoint(thisPeriodState);

			// Put it in the output activity stream if its changed
			if (externalPeriodState.getActivityState() != lastPublishedActivityState) {

				// Adjust to the start of inactivity period if we've gone IDLE
				if (externalPeriodState.getActivityState() == ActivityState.IDLE) {
					long minIdleTime_ms = (long) (minIdleTime * 1000);
					long adjustedTime = externalPeriodState.getTime()
							- minIdleTime_ms;
					externalPeriodState.setTime(adjustedTime);
				}

				// Record what we last published
				lastPublishedActivityState = externalPeriodState
						.getActivityState();

				// Publish it
				outputActivityStream.PutActivityWaypoint(externalPeriodState);

			}
		}

		// Maintain proper state
		isRunning = false;

	}

	protected PeriodActivityState EvaluatePeriodActivityState(
			double timeSinceLastActivity) {

		if (timeSinceLastActivity > this.minIdleTime) {
			return PeriodActivityState.IDLE;
		} else if (timeSinceLastActivity > this.activityCheckingPeriod) {
			return PeriodActivityState.INACTIVE;
		} else {
			return PeriodActivityState.ACTIVE;
		}

	}

	/**
	 * A more detailed version of ActivityState to provide more fidelity for
	 * data collection purposes.
	 */
	protected enum PeriodActivityState {
		ACTIVE, INACTIVE, IDLE;

		static public ActivityWaypoint ConvertToActivityWaypoint(
				PeriodActivityState state) {

			switch (state) {
			case IDLE:
				return new ActivityWaypoint(ActivityState.IDLE);
			case INACTIVE:
			case ACTIVE:
			default:
				return new ActivityWaypoint(ActivityState.ACTIVE);
			}

		}
	}

	public static void main(String[] args)
			throws InputActivityStream.TimedOutException {

		BufferedActivityStream activityStream = new BufferedActivityStream();
		SysActivityMonitor activityMonitor = new SysActivityMonitor(
				activityStream, 1.0, 5.0);

		// Start producing state changes
		activityMonitor.start();

		// Consume them
		while (true) {
			try {
				ActivityWaypoint newWaypoint;
				newWaypoint = activityStream
						.ReadActivityWaypoint(InputActivityStream.WAIT_FOREVER);

				System.out.println("Read state: "
						+ newWaypoint.getActivityState() + " at time: "
						+ newWaypoint);
			} catch (InterruptedException e) {
				System.out.println("Was interupted");
			}
		}
	}

}
