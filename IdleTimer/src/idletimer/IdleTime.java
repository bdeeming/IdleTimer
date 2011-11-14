/**
 * 
 */
package idletimer;

import com.sun.jna.Platform;

/**
 * @author Ben
 * 
 */
public class IdleTime {

	final static public int milliSecondsPerSecond = 1000;

	/** Get the time in seconds since the last system activity.
	 * @return The time since the last user input activity.
	 * @throws PlatformNotSupportedException
	 */
	public double GetTimeSinceLastActivity() throws PlatformNotSupportedException {
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
