package edu.emory.library.tast.common.listing;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * Event that indicates request of show details of selected voyage.
 * @author Pawel Jurczyk
 *
 */
public class ShowDetailsEvent extends FacesEvent {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Voyage ID that will be shown.
	 */
	private Long voyageId = null;
	
	/**
	 * Constructor.
	 * @param uiComponent component invoking event.
	 * @param voyageId	voyage ID
	 */
	public ShowDetailsEvent(UIComponent uiComponent, Long voyageId) {
		super(uiComponent);
		this.voyageId = voyageId;
	}
	
	public boolean isAppropriateListener(FacesListener faceslistener) {
		return false;
	}

	public void processListener(FacesListener faceslistener) {		
	}

	/**
	 * Getter for VoyageID.
	 * @return
	 */
	public Long getVoyageId() {
		return voyageId;
	}

	/**
	 * Setter for VoyageID.
	 * @param voyageId voyageID
	 */
	public void setVoyageId(Long voyageId) {
		this.voyageId = voyageId;
	}
}
