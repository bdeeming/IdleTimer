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

package idletimer.ui;

import idletimer.LiveTaskTimer;
import idletimer.Task;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final double checkingRate = 1.0;
		final double idlePeriod = 5.0 * 60;
		// final double idlePeriod = 5.0;

		// Create a task to time
		Task defaultTask = new Task("Default", 0.0);

		// Create a UI
		AllocateIdleTimePopup popUp = new AllocateIdleTimePopup();

		// Create the live timer
		LiveTaskTimer liveTimer = LiveTaskTimer.CreateSysActivityLiveTimer(
				checkingRate, idlePeriod, popUp, defaultTask);

		// Start the live task timer
		liveTimer.StartTiming();

		// Show the task to the user
		CmdLineUi ui = new CmdLineUi();
		ui.DisplayTask(defaultTask);

	}

}
