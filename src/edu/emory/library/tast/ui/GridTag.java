package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class GridTag extends UIComponentTag
{
	
	private String rows;
	private String columns;
	private String action;
	private String onOpenRow;

	public String getComponentType()
	{
		return "Grid";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		GridComponent grid = (GridComponent) component;
		
		if (rows != null && isValueReference(rows))
		{
			ValueBinding vb = app.createValueBinding(rows);
			grid.setValueBinding("rows", vb);
		}

		if (columns != null && isValueReference(columns))
		{
			ValueBinding vb = app.createValueBinding(columns);
			grid.setValueBinding("columns", vb);
		}
		
		if (action != null && isValueReference(action))
		{
			MethodBinding mb = app.createMethodBinding(action, null);
			grid.setAction(mb);
		}

		if (onOpenRow != null && isValueReference(onOpenRow))
		{
			MethodBinding mb = app.createMethodBinding(onOpenRow, new Class[] {GridOpenRowEvent.class});
			grid.setOnOpenRow(mb);
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

	public String getColumns()
	{
		return columns;
	}

	public void setColumns(String columns)
	{
		this.columns = columns;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getOnOpenRow()
	{
		return onOpenRow;
	}

	public void setOnOpenRow(String onOpenRow)
	{
		this.onOpenRow = onOpenRow;
	}

}