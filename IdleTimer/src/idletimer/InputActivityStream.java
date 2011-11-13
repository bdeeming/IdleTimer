/**
 * 
 */
package idletimer;

import idletimer.BufferedActivityStream.TimedOutException;

/** Incoming stream of activity state events.
 * @author Ben
 *
 */
public interface InputActivityStream {
	
	/** Consumes the oldest activity state from the queue.
	 * Blocks if no activity states are available.
	 * @param timeout The max amount of time to wait (ms) for a new state.
	 * @return The oldest activity state on the queue.
	 * @throws TimedOutException If timeout is exceeded.
	 */
	public ActivityState ReadActivityState(long timeout) throws TimedOutException;

}
