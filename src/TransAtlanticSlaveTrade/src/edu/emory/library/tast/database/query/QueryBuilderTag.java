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
package edu.emory.library.tast.database.query;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * JSP tag for {@link edu.emory.library.tast.database.query.QueryBuilderComponent}.
 * 
 * @author Jan Zich
 * 
 */
public class QueryBuilderTag extends UIComponentTag
{
	
	private String query;
	private String onUpdateTotal;
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		QueryBuilderComponent builder = (QueryBuilderComponent) component;
		
		if (query != null && isValueReference(query))
		{
			ValueBinding vb = app.createValueBinding(query);
			component.setValueBinding("query", vb);
		}
		
		if (onUpdateTotal != null && isValueReference(onUpdateTotal))
		{
			MethodBinding mb = app.createMethodBinding(onUpdateTotal, new Class[] {QueryUpdateTotalEvent.class});
			builder.setOnUpdateTotal(mb);
		}

	}

	public String getComponentType()
	{
		return "QueryBuilder";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public String getOnUpdateTotal()
	{
		return onUpdateTotal;
	}

	public void setOnUpdateTotal(String onUpdateTotal)
	{
		this.onUpdateTotal = onUpdateTotal;
	}

}
