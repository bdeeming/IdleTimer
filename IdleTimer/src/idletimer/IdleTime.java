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

import com.sun.jna.Platform;

/**
 * @author Ben
 * 
 */
public class IdleTime {

	final static public int milliSecondsPerSecond = 1000;

	/**
	 * Get the time in seconds since the last system activity.
	 * 
	 * @return The time since the last user input activity.
	 * @throws PlatformNotSupportedException
	 */
	public double GetTimeSinceLastActivity()
			throws PlatformNotSupportedException {
		double idleSeconds = 0;

		if (Platform.isLinux()) {
			idleSeconds = (double) LinuxIdleTime.getIdleTimeMillis()
					/ milliSecondsPerSecond;
		} else if (Platform.isWindows()) {
			idleSeconds = (double) Win32IdleTime.getIdleTimeMillis()
					/ milliSecondsPerSecond;
		} else {
			throw new PlatformNotSupportedException(
					"Your platform is not supported");
		}

		return idleSeconds;
	}

	public class PlatformNotSupportedException extends Exception {

		private static final long serialVersionUID = 292778019356097665L;

		public PlatformNotSupportedException() {
			super();
		}

		public PlatformNotSupportedException(String message, Throwable cause) {
			super(message, cause);
		}

		public PlatformNotSupportedException(String message) {
			super(message);
		}

		public PlatformNotSupportedException(Throwable cause) {
			super(cause);
		}
	}
}
