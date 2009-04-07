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