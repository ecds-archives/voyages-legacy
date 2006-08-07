package edu.emory.library.tast.ui.search.query;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * JSP tag for {@link edu.emory.library.tast.ui.search.query.QueryBuilderComponent}.
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
