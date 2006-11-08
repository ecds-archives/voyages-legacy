package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class SimpleTableTag extends UIComponentTag
{
	
	private String rows;

	public String getComponentType()
	{
		return "SimpleTable";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		SimpleTableComponent simpleTable = (SimpleTableComponent) component;
		
		if (rows != null && isValueReference(rows))
		{
			ValueBinding vb = app.createValueBinding(rows);
			simpleTable.setValueBinding("rows", vb);
		}

	}

	public String getRows()
	{
		return rows;
	}

	public void setRows(String data)
	{
		this.rows = data;
	}

}