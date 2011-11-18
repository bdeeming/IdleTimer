/**
 * 
 */
package idletimer;

import java.util.Date;

/**
 * @author bdeeming
 * 
 */
public class TaskPrinter extends Thread {

	private Task activeTask;

	/**
	 * @param activeTask
	 */
	public TaskPrinter(Task activeTask) {
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

		System.out.println("Started timing");

		boolean printedStopped = false;
		
		while (true) {
			// Print once per second
			try {
				wait(1 * 1000);
			} catch (InterruptedException e) {
			}

			if (activeTask.IsCurrentlyBeingTimed()) {
				// Erase the line
				System.out
						.print("\r" + activeTask + "           ");
				printedStopped = false;
			} else if (!printedStopped) {
				System.out.println("\nStopped timing at: " + new Date());
				printedStopped = true;
			}
		}
	}
}
