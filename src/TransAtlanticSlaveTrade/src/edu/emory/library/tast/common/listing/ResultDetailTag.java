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
package edu.emory.library.tast.common.listing;

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
public class ResultDetailTag extends UIComponentTag {

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
		return "ResultDetail";
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
