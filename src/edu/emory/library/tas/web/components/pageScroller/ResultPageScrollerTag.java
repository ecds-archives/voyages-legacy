package edu.emory.library.tas.web.components.pageScroller;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class ResultPageScrollerTag extends UIComponentTag {

	private String resultFirst;
	private String resultSize;
	
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

}
