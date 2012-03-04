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
