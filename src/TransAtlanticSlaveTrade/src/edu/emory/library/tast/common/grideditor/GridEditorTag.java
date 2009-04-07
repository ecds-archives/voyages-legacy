package edu.emory.library.tast.common.grideditor;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class GridEditorTag extends UIComponentTag
{
	
	private String rows;
	private String columns;
	private String values;
	private String fieldTypes;
	private String rowGroups;
	private String expandedGroups;
	private String onColumnAction;

	public String getOnColumnAction()
	{
		return onColumnAction;
	}

	public void setOnColumnAction(String onColumnAction)
	{
		this.onColumnAction = onColumnAction;
	}

	public String getComponentType()
	{
		return "GridEditor";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{

		GridEditorComponent gridEditor = (GridEditorComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (rows != null && isValueReference(rows))
		{
			ValueBinding vb = app.createValueBinding(rows);
			gridEditor.setValueBinding("rows", vb);
		}
		
		if (columns != null && isValueReference(columns))
		{
			ValueBinding vb = app.createValueBinding(columns);
			gridEditor.setValueBinding("columns", vb);
		}

		if (values != null && isValueReference(values))
		{
			ValueBinding vb = app.createValueBinding(values);
			gridEditor.setValueBinding("values", vb);
		}
	
		if (fieldTypes != null && isValueReference(fieldTypes))
		{
			ValueBinding vb = app.createValueBinding(fieldTypes);
			gridEditor.setValueBinding("fieldTypes", vb);
		}

		if (rowGroups != null && isValueReference(rowGroups))
		{
			ValueBinding vb = app.createValueBinding(rowGroups);
			gridEditor.setValueBinding("rowGroups", vb);
		}
		
		if (expandedGroups != null && isValueReference(expandedGroups))
		{
			ValueBinding vb = app.createValueBinding(expandedGroups);
			gridEditor.setValueBinding("expandedGroups", vb);
		}
		
		if (onColumnAction != null && isValueReference(onColumnAction))
		{
			MethodBinding mb = app.createMethodBinding(onColumnAction, new Class[] {ColumnActionEvent.class});
			gridEditor.setOnColumnAction(mb);
		}

	}

	public String getColumns()
	{
		return columns;
	}

	public void setColumns(String columns)
	{
		this.columns = columns;
	}

	public String getRows()
	{
		return rows;
	}

	public void setRows(String rows)
	{
		this.rows = rows;
	}

	public String getValues()
	{
		return values;
	}

	public void setValues(String values)
	{
		this.values = values;
	}

	public String getFieldTypes()
	{
		return fieldTypes;
	}

	public void setFieldTypes(String extensions)
	{
		this.fieldTypes = extensions;
	}

	public String getRowGroups()
	{
		return rowGroups;
	}

	public void setRowGroups(String rowGroups)
	{
		this.rowGroups = rowGroups;
	}

	public String getExpandedGroups()
	{
		return expandedGroups;
	}

	public void setExpandedGroups(String expandedRows)
	{
		this.expandedGroups = expandedRows;
	}

}
