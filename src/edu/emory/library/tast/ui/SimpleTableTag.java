package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentBodyTag;

public class SimpleTableTag extends UIComponentBodyTag
{
	
	private String data;
	private String columnLabels;
	private String rowLabels;

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
		
		if (columnLabels != null && isValueReference(columnLabels))
		{
			ValueBinding vb = app.createValueBinding(columnLabels);
			simpleTable.setValueBinding("columnLabels", vb);
		}

		if (rowLabels != null && isValueReference(rowLabels))
		{
			ValueBinding vb = app.createValueBinding(rowLabels);
			simpleTable.setValueBinding("rowLabels", vb);
		}

	}

	public String getColumnLabels()
	{
		return columnLabels;
	}

	public void setColumnLabels(String columnLabels)
	{
		this.columnLabels = columnLabels;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getRowLabels()
	{
		return rowLabels;
	}

	public void setRowLabels(String rowLabels)
	{
		this.rowLabels = rowLabels;
	}
	
	

}
