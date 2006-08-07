package edu.emory.library.tast.ui.search.stat;

/**
 * Action that memores any parameters that can be used in performAction method.
 * @author Pawel Jurczyk
 *
 */
public abstract class MemorizedAction {
	
	/**
	 * Parameters used in performAction implementation. 
	 */
	protected Object[] params = null;
	
	/**
	 * Constructor.
	 * @param params parameters that will be used in performAction
	 */
	public MemorizedAction(Object[] params) {
		this.params = params;
	}
	
	/**
	 * Method that should perform specific actions using params provided in constructor.
	 *
	 */
	public abstract void performAction();
}
