package edu.emory.library.tas.web.components.tabs;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class DetailTag extends UIComponentTag {

	private String data;

	protected void setProperties(UIComponent component) {

		super.setProperties(component);
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (data != null && isValueReference(data)) {
			ValueBinding vb = app.createValueBinding(data);
			component.setValueBinding("data", vb);
		} else {
			component.getAttributes().put("data", data);
		}

	}

	public String getComponentType() {
		return "VoyageDetail";
	}

	public String getRendererType() {
		return null;
	}

	public String getData() {
		return data;
	}

	public void setData(String voyageId) {
		this.data = voyageId;
	}

}
