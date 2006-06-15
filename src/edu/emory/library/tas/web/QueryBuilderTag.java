package edu.emory.library.tas.web;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class QueryBuilderTag extends UIComponentTag
{
	
	private String query;
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (query !=null && isValueReference(query))
		{
			ValueBinding vb = app.createValueBinding(query);
			component.setValueBinding("query", vb);
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

}
