package edu.emory.library.tas.web.components.tabs;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ShowDetailsEvent extends FacesEvent {
	
	private static final long serialVersionUID = 1L;
	
	private Long voyageId = null;
	
	public ShowDetailsEvent(UIComponent uiComponent, Long voyageId) {
		super(uiComponent);
		this.voyageId = voyageId;
	}
	
	public boolean isAppropriateListener(FacesListener faceslistener) {
		return false;
	}

	public void processListener(FacesListener faceslistener) {		
	}

	public Long getVoyageId() {
		return voyageId;
	}

	public void setVoyageId(Long voyageId) {
		this.voyageId = voyageId;
	}
}
