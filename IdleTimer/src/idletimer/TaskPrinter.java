/**
 * 
 */
package idletimer;

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

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	synchronized public void run() {

		System.out.println("Started timing");
		
		while (true) {
			// Print once per minute
			try {
				wait(1*1000*60);
			} catch (InterruptedException e) {
			}
			
			System.out.println(activeTask);
		}
	}
	
}
