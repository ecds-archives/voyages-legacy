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
package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.el.MethodBindingImpl;

import edu.emory.library.tast.common.listing.ClickEvent;


public class SimpleButtonTag extends UIComponentTag {

	private String style;
	private String styleClass;
	private String id;
	private String action;
	
	
	protected void setProperties(UIComponent component) {

		super.setProperties(component);

		//Setting of properties
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
		if (id != null) {
			if (isValueReference(id)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(id);
				component.setValueBinding("id", vb);
			} else {
				component.getAttributes().put("id", id);
			}
		}
		if (component instanceof SimpleButtonComponent && action != null) {
			SimpleButtonComponent tab = (SimpleButtonComponent)component;
			Application app = FacesContext.getCurrentInstance().getApplication();
			tab.setAction(new MethodBindingImpl(app, action, new Class[] {ClickEvent.class}));
		}
		
	}
	
	public String getComponentType() {
		return "SimpleButton";
	}

	public String getRendererType() {
		return null;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
