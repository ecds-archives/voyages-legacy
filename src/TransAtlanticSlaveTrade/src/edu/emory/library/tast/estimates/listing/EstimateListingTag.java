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
package edu.emory.library.tast.estimates.listing;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class EstimateListingTag extends UIComponentTag {

	private String data;
	private String style;
	private String styleClass;
	
	protected void setProperties(UIComponent component) {

		super.setProperties(component);

		//Setting of properties
		if (data != null) {
			if (isValueReference(data)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(data);
				component.setValueBinding("data", vb);
			} else {
				component.getAttributes().put("data", data);
			}
		}
		if (style != null) {
			if (isValueReference(style)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(style);
				component.setValueBinding("style", vb);
			} else {
				component.getAttributes().put("style", style);
			}
		}
		if (styleClass != null) {
			if (isValueReference(styleClass)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(styleClass);
				component.setValueBinding("styleClass", vb);
			} else {
				component.getAttributes().put("styleClass", styleClass);
			}
		}	
		
//		if (component instanceof UITableResultTab && sortChanged != null) {
//			UITableResultTab tab = (UITableResultTab)component;
//			Application app = FacesContext.getCurrentInstance().getApplication();
//			tab.setSortChanged(new MethodBindingImpl(app, sortChanged, new Class[] {SortChangeEvent.class}));
//		}
//		
//		if (component instanceof UITableResultTab && onclick != null) {
//			UITableResultTab tab = (UITableResultTab)component;
//			Application app = FacesContext.getCurrentInstance().getApplication();
//			tab.setShowDetails(new MethodBindingImpl(app, onclick, new Class[] {ShowDetailsEvent.class}));
//		}
	}
	
	public String getComponentType() {
		// TODO Auto-generated method stub
		return "EstimateListing";
	}

	public String getRendererType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
