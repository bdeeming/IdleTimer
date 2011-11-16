/**
 * 
 */
package idletimer;

import java.util.ArrayList;

/**
 * @author Ben
 * 
 */
public class BufferedActivityStream implements OutputActivityStream,
		InputActivityStream {

	private ArrayList<ActivityWaypoint> activityStateBuffer;
	boolean wokeFromNewData;

	public BufferedActivityStream() {
		super();
		this.activityStateBuffer = new ArrayList<ActivityWaypoint>();
		this.wokeFromNewData = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see idletimer.InputActivityStream#ReadActivityState()
	 */
	@Override
	synchronized public ActivityWaypoint ReadActivityWaypoint(long timeout)
			throws TimedOutException {

		// Check if there's anything to read, wait until there is
		while (this.activityStateBuffer.size() <= 0) {
			try {

				// Reset so we know what we woke up from
				this.wokeFromNewData = false;
				wait(timeout);

				if (!wokeFromNewData) {
					// Timed out
					throw new TimedOutException("Read activity state timed out");
				}

			} catch (InterruptedException e) {
				// Result of data being available possibly
			}
		}

		// Pop the oldest element
		return this.activityStateBuffer.remove(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * idletimer.OutputActivityStream#PutActivityState(idletimer.ActivityState)
	 */
	@Override
	synchronized public void PutActivityWaypoint(ActivityWaypoint newWaypoint) {
		// TODO Add it in chronological placement
		// Add newState to the the buffer
		this.activityStateBuffer.add(newWaypoint);

		// Indicate the reason the consumer is woken
		this.wokeFromNewData = true;

		// Wake up a waiting consumer
		notifyAll();
	}

	public class TimedOutException extends Exception {

		private static final long serialVersionUID = 5798694916302392156L;

		public TimedOutException(String msg) {
			super(msg);
		}
	}

}
