package edu.emory.library.tas.web.components.tabs;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class SortChangeEvent extends FacesEvent {

	private static final long serialVersionUID = 1L;
	
	private String attributeSort = null;
	
	public SortChangeEvent(UIComponent uiComponent, String attributeSort) {
		super(uiComponent);
		this.attributeSort = attributeSort;
	}
	
	public boolean isAppropriateListener(FacesListener faceslistener) {
		return false;
	}

	public void processListener(FacesListener faceslistener) {		
	}

	public String getAttributeSort() {
		return attributeSort;
	}

	public void setAttributeSort(String attributeSort) {
		this.attributeSort = attributeSort;
	}

}
