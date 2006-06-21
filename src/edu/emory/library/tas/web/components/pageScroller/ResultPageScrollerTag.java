package edu.emory.library.tas.web.components.pageScroller;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * Tag that is used to split results into pages.
 * 
 * @author Pawel Jurczyk
 *
 */
public class ResultPageScrollerTag extends UIComponentTag {

	private String resultFirst;		//Rowid of current first row back-bean binding
	private String resultSize;		//# of rows per page back-bean binding
	private String resultLast;		//Rowid of current last row back-bean binding
	
	/**
	 * Overriden method from supertype.
	 */
	protected void setProperties(UIComponent component) {

		super.setProperties(component);
		
		//Set mapping of resultFirst
		if (resultFirst != null) {
            if (isValueReference(resultFirst)) {
                ValueBinding vb =
                    getFacesContext().getApplication().
                    createValueBinding(resultFirst);
                component.setValueBinding("resultFirst", vb);
            } else {
                component.getAttributes().put("resultFirst", resultFirst);
            }
		}
		
		//Set mapping of resultLast
		if (resultLast != null) {
            if (isValueReference(resultFirst)) {
                ValueBinding vb =
                    getFacesContext().getApplication().
                    createValueBinding(resultLast);
                component.setValueBinding("resultLast", vb);
            } else {
                component.getAttributes().put("resultLast", resultLast);
            }
		}
		
		//Set mapping of resultSize		
		if (resultSize != null) {
            if (isValueReference(resultSize)) {
                ValueBinding vb =
                    getFacesContext().getApplication().
                    createValueBinding(resultSize);
                component.setValueBinding("resultSize", vb);
            } else {
                component.getAttributes().put("resultSize", resultSize);
            }
		}
    
	}
	
	/**
	 * Gets component type
	 */
	public String getComponentType() {
		return "ResultPageScroller";
	}

	////////////////////// GETTERS - SETTERS for TAG attributes //////////////////////
	
	public String getRendererType() {
		return null;
	}

	public String getResultFirst() {
		return resultFirst;
	}

	public void setResultFirst(String resultFirst) {
		this.resultFirst = resultFirst;
	}

	public String getResultSize() {
		return resultSize;
	}

	public void setResultSize(String resultSize) {
		this.resultSize = resultSize;
	}

	public String getResultLast() {
		return resultLast;
	}

	public void setResultLast(String resultLast) {
		this.resultLast = resultLast;
	}

}
