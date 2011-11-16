/**
 * 
 */
package idletimer;

import java.util.logging.Logger;

import idletimer.ActivityWaypoint.ActivityState;
import idletimer.BufferedActivityStream.TimedOutException;
import idletimer.IdleTime.PlatformNotSupportedException;

/**
 * Thread to monitor a queue up system activity status changes.
 * 
 * @author Ben
 * 
 */
public class SysActivityMonitor extends Thread {

	private final Logger LOGGER = Logger.getLogger(SysActivityMonitor.class
			.getName());

	OutputActivityStream outputActivityStream;
	double activityCheckingPeriod;
	double minIdleTime;
	ActivityState lastPublishedActivityState;
	IdleTime idleTime;

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
	}

	@Override
	public void run() {

		// Convert the period to ms for the sleep
		long activityCheckingPeriod_ms = (long) (this.activityCheckingPeriod * 1000);

		while (true) {

			// Sleep for one period
			try {
				sleep(activityCheckingPeriod_ms);
			} catch (InterruptedException e) {
			}

			double timeSinceLastActivity = 0;
			try {
				// Get the systems activity state for the period just slept -
				// TODO this needs profiling
				timeSinceLastActivity = idleTime.GetTimeSinceLastActivity();
			} catch (PlatformNotSupportedException e) {
				LOGGER.severe("System activity monitor encontered a fatal error. Your "
						+ "platform is not supported for system activity detection.");
				break;
			}

			// Evaluate the system activity state
			PeriodActivityState thisPeriodState = EvaluatePeriodActivityState(timeSinceLastActivity);

			// TODO Log the state to file

			// Convert to the external activity state type
			ActivityWaypoint externalPeriodState = PeriodActivityState
					.ConvertToActivityWaypoint(thisPeriodState);

			// Put it in the output activity stream if its changed
			if (externalPeriodState.getActivityState() != lastPublishedActivityState) {
				lastPublishedActivityState = externalPeriodState
						.getActivityState();
				outputActivityStream.PutActivityWaypoint(externalPeriodState);
			}
		}
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

	public static void main(String[] args) throws TimedOutException {

		BufferedActivityStream activityStream = new BufferedActivityStream();
		SysActivityMonitor activityMonitor = new SysActivityMonitor(
				activityStream, 1.0, 5.0);

		// Start producing state changes
		activityMonitor.start();

		// Consume them
		while (true) {
			ActivityWaypoint newWaypoint = activityStream
					.ReadActivityWaypoint(0);

			System.out.println("Read state: " + newWaypoint.getActivityState()
					+ " at time: " + newWaypoint);
		}
	}

}
