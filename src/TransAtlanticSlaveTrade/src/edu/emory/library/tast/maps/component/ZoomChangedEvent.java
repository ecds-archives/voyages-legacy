/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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
