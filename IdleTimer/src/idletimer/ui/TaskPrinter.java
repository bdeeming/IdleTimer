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

import java.util.Date;

/**
 * @author bdeeming
 * 
 */
public class TaskPrinter {

	private PrintingThread currentPrintingThread;

	public void StartPrintingTask(Task taskToPrint) {
		// Create a thread to do the printing
		this.currentPrintingThread = new PrintingThread(taskToPrint);

		// Start it running
		this.currentPrintingThread.start();
	}

	public void StopPrintingTask() {
		// TODO Implement
	}

	private class PrintingThread extends Thread {

		private Task activeTask;

		/**
		 * @param activeTask
		 */
		public PrintingThread(Task activeTask) {
			super();
			this.activeTask = activeTask;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		synchronized public void run() {

			System.out.println("Showing task '" + activeTask.GetName() + "'");

			boolean printedStopped = false;

			while (true) {
				// Print once per second
				try {
					wait(1 * 1000);
				} catch (InterruptedException e) {
				}

				if (activeTask.IsCurrentlyBeingTimed()) {
					// Erase the line
					System.out.print("\r" + activeTask + "           ");
					printedStopped = false;
				} else if (!printedStopped) {
					System.out.println("\nStopped timing at: " + new Date());
					printedStopped = true;
				}
			}
		}
	}
}
