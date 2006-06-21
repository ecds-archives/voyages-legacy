package edu.emory.library.tas.web.components.pageScroller;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class ResultPageScrollerTag extends UIComponentTag {

	private String resultFirst;
	private String resultSize;
	private String resultLast;
	
	protected void setProperties(UIComponent component) {

		super.setProperties(component);
		
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
	
	public String getComponentType() {
		return "ResultPageScroller";
	}

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
