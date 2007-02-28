package edu.emory.library.tast.ui.images.site;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ShowVoyageEvent extends FacesEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Integer voyageid;
	
	public ShowVoyageEvent(UIComponent uiComponent, Integer voyageid) {
		super(uiComponent);
		this.voyageid = voyageid;
	}
	
	public boolean isAppropriateListener(FacesListener arg0) {
		return false;
	}

	public void processListener(FacesListener arg0) {
	}

	public Integer getVoyageid() {
		return voyageid;
	}

}
