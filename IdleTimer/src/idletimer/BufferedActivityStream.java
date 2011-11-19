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

	@Override
	synchronized public ActivityWaypoint ReadActivityWaypoint(long timeout)
			throws TimedOutException, InterruptedException {

		// Check if there's anything to read, wait until there is
		while (this.activityStateBuffer.size() <= 0) {
			// Reset so we know what we woke up from
			this.wokeFromNewData = false;
			wait(timeout);

			if (!wokeFromNewData) {
				// Timed out
				throw new TimedOutException("Read activity state timed out");
			}
		}

		// Pop the oldest element
		return this.activityStateBuffer.remove(0);
	}

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

	@Override
	synchronized public void Clear() {
		activityStateBuffer.clear();
	}
}
