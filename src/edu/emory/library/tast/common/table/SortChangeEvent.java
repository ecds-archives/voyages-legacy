package edu.emory.library.tast.common.table;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * Event that indicates change of sorting in result table.
 * @author Pawel Jurczyk
 *
 */
public class SortChangeEvent extends FacesEvent {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Attribute that will be sorted.
	 */
	private String attributeSort = null;
	
	/**
	 * Constructor.
	 * @param uiComponent
	 * @param attributeSort
	 */
	public SortChangeEvent(UIComponent uiComponent, String attributeSort) {
		super(uiComponent);
		this.attributeSort = attributeSort;
	}
	
	public boolean isAppropriateListener(FacesListener faceslistener) {
		return false;
	}

	public void processListener(FacesListener faceslistener) {		
	}

	/**
	 * Getter for sorted attribute name.
	 * @return
	 */
	public String getAttributeSort() {
		return attributeSort;
	}

	/**
	 * Setter for sorted attribute name.
	 * @param attributeSort
	 */
	public void setAttributeSort(String attributeSort) {
		this.attributeSort = attributeSort;
	}

}
