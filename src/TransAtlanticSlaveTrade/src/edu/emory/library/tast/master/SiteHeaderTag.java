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
package edu.emory.library.tast.master;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class SiteHeaderTag extends UIComponentTag
{
	
	private String activeSectionId;

	public String getComponentType()
	{
		return "SiteHeader";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		SiteHeaderComponent menu = (SiteHeaderComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (activeSectionId != null && isValueReference(activeSectionId))
		{
			ValueBinding vb = app.createValueBinding(activeSectionId);
			menu.setValueBinding("activeSectionId", vb);
		}
		else
		{
			menu.setActiveSectionId(activeSectionId);
		}

	}

	public String getActiveSectionId()
	{
		return activeSectionId;
	}

	public void setActiveSectionId(String activeSectionId)
	{
		this.activeSectionId = activeSectionId;
	}

}
