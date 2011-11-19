/**
 * 
 */
package idletimer;

/**
 * Incoming stream of activity state events.
 * 
 * @author Ben
 * 
 */
public interface InputActivityStream {

	/**
	 * Can be provided as a timeout value to indicate that we should never
	 * timeout.
	 */
	final long WAIT_FOREVER = 0;

	/**
	 * Consumes the oldest activity state from the queue. Blocks if no activity
	 * states are available.
	 * 
	 * @param timeout
	 *            The max amount of time to wait (ms) for a new state.
	 * @return The oldest activity waypoint on the queue.
	 * @throws TimedOutException
	 *             If timeout is exceeded.
	 * @throws InterruptedException
	 *             If wait is interrupted
	 */
	public ActivityWaypoint ReadActivityWaypoint(long timeout)
			throws TimedOutException, InterruptedException;

	/**
	 * Remove all items on the stream.
	 */
	public void Clear();

	public class TimedOutException extends Exception {

		private static final long serialVersionUID = 5798694916302392156L;

		public TimedOutException(String msg) {
			super(msg);
		}
	}

}
