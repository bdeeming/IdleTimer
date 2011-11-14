/**
 * 
 */
package idletimer;

/** Output stream for activity event states.
 * @author Ben
 *
 */
public interface OutputActivityStream {
	
	/** Put a new system activity state.
	 * @param newState The state to add to the queue
	 */
	void PutActivityState(ActivityState newState);

}
