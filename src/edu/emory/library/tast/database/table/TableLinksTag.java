package edu.emory.library.tast.database.table;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class TableLinksTag extends UIComponentTag {

	public String manager;
	
	public String getComponentType() {
		return "TableLinksComponent";
	}

	public String getRendererType() {
		return null;
	}
	
	protected void setProperties(UIComponent component) {

		super.setProperties(component);

		//Setting of properties
		if (manager != null) {
			if (isValueReference(manager)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(manager);
				component.setValueBinding("manager", vb);
			} else {
				component.getAttributes().put("manager", manager);
			}
		}
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

}
