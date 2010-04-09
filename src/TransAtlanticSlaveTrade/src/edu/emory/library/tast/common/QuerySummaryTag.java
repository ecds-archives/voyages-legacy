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

public class QuerySummaryTag extends UIComponentTag
{
	
	private String items;
	private String noQueryText;

	public String getComponentType()
	{
		return "QuerySummary";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		QuerySummaryComponent querySummary = (QuerySummaryComponent) component;
		
		if (items != null && isValueReference(items))
		{
			ValueBinding vb = app.createValueBinding(items);
			querySummary.setValueBinding("items", vb);
		}
		
		if (noQueryText != null && isValueReference(noQueryText))
		{
			ValueBinding vb = app.createValueBinding(noQueryText);
			querySummary.setValueBinding("noQueryText", vb);
		}
		else
		{
			querySummary.setNoQueryText(noQueryText);
		}

	}

	public String getItems()
	{
		return items;
	}

	public void setItems(String items)
	{
		this.items = items;
	}

	public String getNoQueryText()
	{
		return noQueryText;
	}

	public void setNoQueryText(String noQueryText)
	{
		this.noQueryText = noQueryText;
	}

}