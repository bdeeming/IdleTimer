/**
 * 
 */
package idletimer;

/**
 * Output stream for activity event states.
 * 
 * @author Ben
 * 
 */
public interface OutputActivityStream {

	/**
	 * Put a new system activity state.
	 * 
	 * @param newWaypoint
	 *            The waypoint to add to the queue
	 */
	void PutActivityWaypoint(ActivityWaypoint newWaypoint);

}
