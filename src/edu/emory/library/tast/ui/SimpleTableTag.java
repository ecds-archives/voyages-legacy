package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class SimpleTableTag extends UIComponentTag
{
	
	private String data;
	private String columns;
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
		
		if (data != null && isValueReference(data))
		{
			ValueBinding vb = app.createValueBinding(data);
			simpleTable.setValueBinding("data", vb);
		}
		
		if (columns != null && isValueReference(columns))
		{
			ValueBinding vb = app.createValueBinding(columns);
			simpleTable.setValueBinding("columns", vb);
		}

		if (rows != null && isValueReference(rows))
		{
			ValueBinding vb = app.createValueBinding(rows);
			simpleTable.setValueBinding("rows", vb);
		}

	}

	public String getColumns()
	{
		return columns;
	}

	public void setColumns(String columnLabels)
	{
		this.columns = columnLabels;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getRows()
	{
		return rows;
	}

	public void setRows(String rowLabels)
	{
		this.rows = rowLabels;
	}
	
	

}
