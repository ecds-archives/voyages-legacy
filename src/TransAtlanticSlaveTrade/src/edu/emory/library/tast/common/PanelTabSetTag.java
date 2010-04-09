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

/**
 * Provides tag definition for panel tab set component.
 * The tag can have following attributes:
 * title - title of panel tab set
 * selectedSectionId - id of selected panel tab.
 * For more details please refer to PanelTabTag.
 *
 */
public class PanelTabSetTag extends UIComponentTag {

	private String title;
	private String selectedSectionId;
	
	public String getComponentType() {
		return "PanelTabSet";
	}

	public String getRendererType() {
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		PanelTabSetComponent tabSet = (PanelTabSetComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (title != null) {
			if (isValueReference(title)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(title);
				component.setValueBinding("title", vb);
			} else {
				component.getAttributes().put("title", title);
			}
		}
		
		if (selectedSectionId != null && isValueReference(selectedSectionId))
		{
			ValueBinding vb = app.createValueBinding(selectedSectionId);
			tabSet.setValueBinding("selectedSectionId", vb);
		}
		else
		{
			tabSet.setSelectedSectionId(selectedSectionId);
		}
		
		super.setProperties(component);
	
	}
	
	public String getSelectedSectionId() {
		return selectedSectionId;
	}

	public void setSelectedSectionId(String selectedSectionId) {
		this.selectedSectionId = selectedSectionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
