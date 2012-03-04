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

import com.sun.jna.*;
import com.sun.jna.platform.unix.*;

public class LinuxIdleTime {
	/** Definition (incomplete) of the Xext library. */
	interface Xss extends Library {
		Xss INSTANCE = (Xss) Native.loadLibrary("Xss", Xss.class);

		public class XScreenSaverInfo extends Structure {
			public X11.Window window; /* screen saver window */
			public int state; /* ScreenSaver{Off,On,Disabled} */
			public int kind; /* ScreenSaver{Blanked,Internal,External} */
			public NativeLong til_or_since; /* milliseconds */
			public NativeLong idle; /* milliseconds */
			public NativeLong event_mask; /* events */
		}

		XScreenSaverInfo XScreenSaverAllocInfo();

		int XScreenSaverQueryInfo(X11.Display dpy, X11.Drawable drawable,
				XScreenSaverInfo saver_info);
	}

	static public long getIdleTimeMillis() {
		X11.Window win = null;
		Xss.XScreenSaverInfo info = null;
		X11.Display dpy = null;
		final X11 x11 = X11.INSTANCE;
		final Xss xss = Xss.INSTANCE;

		long idlemillis = 0L;
		try {
			dpy = x11.XOpenDisplay(null);
			win = x11.XDefaultRootWindow(dpy);
			info = xss.XScreenSaverAllocInfo();
			xss.XScreenSaverQueryInfo(dpy, win, info);

			idlemillis = info.idle.longValue();
		} finally {
			if (info != null)
				x11.XFree(info.getPointer());
			info = null;

			if (dpy != null)
				x11.XCloseDisplay(dpy);
			dpy = null;
		}
		return idlemillis;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out
				.format("Idle time is: %d", LinuxIdleTime.getIdleTimeMillis());
	}
}
