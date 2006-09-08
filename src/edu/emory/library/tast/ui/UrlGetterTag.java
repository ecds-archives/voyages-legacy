package edu.emory.library.tast.ui;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class UrlGetterTag extends UIComponentTag {

	private String id;
	private String param;
	private String value;
	
	protected void setProperties(UIComponent component) {

		super.setProperties(component);

		//Setting of properties
		if (id != null) {
			if (isValueReference(id)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(id);
				component.setValueBinding("id", vb);
			} else {
				component.getAttributes().put("id", id);
			}
		}
		
		if (param != null) {
			if (isValueReference(param)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(param);
				component.setValueBinding("param", vb);
			} else {
				component.getAttributes().put("param", param);
			}
		}
		
		if (value != null) {
			if (isValueReference(value)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(value);
				component.setValueBinding("value", vb);
			} else {
				component.getAttributes().put("value", value);
			}
		}
	}
	
	public String getComponentType() {
		return "UrlGetterComponent";
	}

	public String getRendererType() {
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
