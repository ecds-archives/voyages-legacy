package edu.emory.library.tast.database.table;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * Tag for VoyageDetail component.
 * @author Pawel Jurczyk
 *
 */
public class DetailTag extends UIComponentTag {

	/**
	 * Data source.
	 */
	private String data;

	/**
	 * Set properties implementation.
	 */
	protected void setProperties(UIComponent component) {

		super.setProperties(component);
		
		//Set value binding for data expression.
		Application app = FacesContext.getCurrentInstance().getApplication();		
		if (data != null && isValueReference(data)) {
			ValueBinding vb = app.createValueBinding(data);
			component.setValueBinding("data", vb);
		} else {
			component.getAttributes().put("data", data);
		}

	}

	/**
	 * Gets component type.
	 */
	public String getComponentType() {
		return "VoyageDetail";
	}

	/**
	 * Gets renderer type (not used specific renderer).
	 */
	public String getRendererType() {
		return null;
	}

	/**
	 * Gets data source string expression.
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * Sets data source string expression.
	 * @param voyageId
	 */
	public void setData(String voyageId) {
		this.data = voyageId;
	}

}
