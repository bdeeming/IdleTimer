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

import idletimer.Task;

/**
 * Methods for displaying a task to the user.
 * 
 * @author Ben
 * 
 */
public interface TaskDisplayer {

	/**
	 * Displays the task to the user.<br>
	 * This is a non-blocking method.
	 * 
	 * @param taskToDisplay
	 *            The task to display to the user.
	 */
	public void DisplayTask(Task taskToDisplay);

	/**
	 * Stop displaying the task to the user.
	 */
	public void StopDisplayingTask();

}
