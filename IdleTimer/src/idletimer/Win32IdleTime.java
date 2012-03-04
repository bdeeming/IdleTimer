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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.jna.*;
import com.sun.jna.win32.*;

/**
 * Utility method to retrieve the idle time on Windows and sample code to test
 * it. JNA shall be present in your classpath for this to work (and compile).
 */
public class Win32IdleTime {

	public interface Kernel32 extends StdCallLibrary {
		Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32",
				Kernel32.class);

		/**
		 * Retrieves the number of milliseconds that have elapsed since the
		 * system was started.
		 * 
		 * @see http://msdn2.microsoft.com/en-us/library/ms724408.aspx
		 * @return number of milliseconds that have elapsed since the system was
		 *         started.
		 */
		public int GetTickCount();
	};

	public interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

		/**
		 * Contains the time of the last input.
		 * 
		 * @see http 
		 *      ://msdn.microsoft.com/library/default.asp?url=/library/en-us/
		 *      winui/winui/windowsuserinterface/userinput/keyboardinput/
		 *      keyboardinputreference/keyboardinputstructures/lastinputinfo.asp
		 */
		public static class LASTINPUTINFO extends Structure {
			public int cbSize = 8;

			// / Tick count of when the last input event was received.
			public int dwTime;
		}

		/**
		 * Retrieves the time of the last input event.
		 * 
		 * @see http 
		 *      ://msdn.microsoft.com/library/default.asp?url=/library/en-us/
		 *      winui/winui/windowsuserinterface/userinput/keyboardinput/
		 *      keyboardinputreference
		 *      /keyboardinputfunctions/getlastinputinfo.asp
		 * @return time of the last input event, in milliseconds
		 */
		public boolean GetLastInputInfo(LASTINPUTINFO result);
	};

	/**
	 * Get the amount of milliseconds that have elapsed since the last input
	 * event (mouse or keyboard).
	 * 
	 * @return idle time in milliseconds
	 */
	static public long getIdleTimeMillis() {
		User32.LASTINPUTINFO lastInputInfo = new User32.LASTINPUTINFO();
		User32.INSTANCE.GetLastInputInfo(lastInputInfo);
		return Kernel32.INSTANCE.GetTickCount() - lastInputInfo.dwTime;
	}

	enum State {
		UNKNOWN, ONLINE, IDLE, AWAY
	};

	public static void main(String[] args) {
		if (!Platform.isWindows()) {
			System.err.println("ERROR: Only implemented on Windows");
			System.exit(1);
		}
		State state = State.UNKNOWN;
		DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

		for (;;) {
			long idleSec = Win32IdleTime.getIdleTimeMillis() / 1000;

			State newState = idleSec < 1 ? State.ONLINE
					: idleSec > 5 * 60 ? State.AWAY : State.IDLE;

			if (newState != state) {
				state = newState;
				System.out.println(dateFormat.format(new Date()) + " # "
						+ state);
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
			}
		}
	}

}