package edu.emory.library.tas.web.components.tabs;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ClickEvent extends FacesEvent {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 * @param uiComponent
	 * @param attributeSort
	 */
	public ClickEvent(UIComponent uiComponent) {
		super(uiComponent);
	}
	
	public boolean isAppropriateListener(FacesListener faceslistener) {
		return false;
	}

	public void processListener(FacesListener faceslistener) {		
	}
}
