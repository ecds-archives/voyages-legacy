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

public class LookupSelectTag extends UIComponentTag
{
	
	private String sourceId;
	private String selectedValues;

	public String getComponentType()
	{
		return "LookupSelect";
	}
	
	protected void setProperties(UIComponent component)
	{
		
		LookupSelectComponent lookupComponent = (LookupSelectComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (selectedValues != null && isValueReference(selectedValues))
		{
			ValueBinding vb = app.createValueBinding(selectedValues);
			component.setValueBinding("selectedValues", vb);
		}
		
		if (sourceId != null && isValueReference(sourceId))
		{
			ValueBinding vb = app.createValueBinding(sourceId);
			component.setValueBinding("sourceId", vb);
		}
		else
		{
			lookupComponent.setSourceId(sourceId);
		}

	}

	public String getRendererType()
	{
		return null;
	}

	public String getSelectedValues()
	{
		return selectedValues;
	}

	public void setSelectedValues(String selectedIds)
	{
		this.selectedValues = selectedIds;
	}

	public String getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(String sourceId)
	{
		this.sourceId = sourceId;
	}

}