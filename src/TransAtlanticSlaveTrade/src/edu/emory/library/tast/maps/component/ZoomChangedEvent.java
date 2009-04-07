package edu.emory.library.tast.maps.component;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ZoomChangedEvent extends FacesEvent {

	private static final long serialVersionUID = 1L;
	
	private int zoomLevel;

	public ZoomChangedEvent(UIComponent uiComponent, int newZoom) {
		super(uiComponent);
		this.zoomLevel = newZoom;
	}

	public boolean isAppropriateListener(FacesListener faceslistener) {
		return false;
	}

	public void processListener(FacesListener faceslistener) {
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

}
