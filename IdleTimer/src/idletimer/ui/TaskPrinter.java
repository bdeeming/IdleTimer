/**
 * 
 */
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
