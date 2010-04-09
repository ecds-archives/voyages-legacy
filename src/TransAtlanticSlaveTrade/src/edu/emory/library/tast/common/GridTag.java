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
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class GridTag extends UIComponentTag
{
	
	private String rows;
	private String columns;
	private String action;
	private String onOpenRow;
	private String onColumnClick;

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

		if (onColumnClick != null && isValueReference(onColumnClick))
		{
			MethodBinding mb = app.createMethodBinding(onColumnClick, new Class[] {GridColumnClickEvent.class});
			grid.setOnColumnClick(mb);
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

	public String getOnColumnClick()
	{
		return onColumnClick;
	}

	public void setOnColumnClick(String onColumnClick)
	{
		this.onColumnClick = onColumnClick;
	}

}