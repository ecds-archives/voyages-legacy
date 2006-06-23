package edu.emory.library.tas.web.components.tabs;

public abstract class MemorizedAction {
	
	protected Object[] params = null;
	
	public MemorizedAction(Object[] params) {
		this.params = params;
	}
	
	public abstract void performAction();
}
