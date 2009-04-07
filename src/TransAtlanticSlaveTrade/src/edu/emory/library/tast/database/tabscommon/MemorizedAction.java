package edu.emory.library.tast.database.tabscommon;

/**
 * Action that memores any parameters that can be used in performAction method.
 * This type of action is used in table bean to record operations performed
 * on GUI and gives a possibility of reverting them (cancel feature)
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
