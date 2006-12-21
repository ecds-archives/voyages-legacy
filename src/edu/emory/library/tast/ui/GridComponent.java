package edu.emory.library.tast.ui;

import java.io.IOException;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class GridComponent extends UICommand
{
	
	private boolean rowsSet = false;
	private GridRow[] rows;

	private boolean columnsSet = false;
	private GridColumn[] columns;

	private MethodBinding onOpenRow;

	public String getRendererType()
	{
		return null;
	}
	
	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, onOpenRow);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		onOpenRow = (MethodBinding) restoreAttachedState(context, values[1]);
	}
	
	private String getHiddenFieldNameForToOpen(FacesContext context)
	{
		return getClientId(context) + "_to_open";
	}

	public void decode(FacesContext context)
	{
		
		String toOpenRowId = JsfUtils.getParamString(context,
				getHiddenFieldNameForToOpen(context));
		
		if (!StringUtils.isNullOrEmpty(toOpenRowId))
		{
			queueEvent(new ActionEvent(this));
			queueEvent(new GridOpenRowEvent(this, toOpenRowId));
		}

	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException
	{

		super.broadcast(event);
		
		if (event instanceof GridOpenRowEvent)
			if (onOpenRow != null)
				onOpenRow.invoke(getFacesContext(), new Object[] {event});
	
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		// get data from bean
		GridRow[] rows = getRows();
		GridColumn[] columns = getColumns();
		if (columns == null || columns.length == 0) return; 
		if (rows == null) rows = new GridRow[0];
		int columnsCount = columns.length;
		
		// hidden field for id of the row to be opened
		String rowIdField = getHiddenFieldNameForToOpen(context); 
		JsfUtils.encodeHiddenInput(this, writer, rowIdField);
		
		// table
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "grid", null);
		
		// header
		writer.startElement("tr", this);
		writer.writeAttribute("class", "grid-header", null);
		for (int j = 0; j < columnsCount; j++)
		{
			GridColumn col = columns[j];
			writer.startElement("th", this);
			if (col.hasCssClass()) writer.writeAttribute("class", col.getClass(), null);
			writer.write(col.getHeader());
			writer.endElement("th");
		}
		writer.endElement("tr");

		// data
		for (int i = 0; i < rows.length; i++)
		{
			
			GridRow row = rows[i];
			
			String onClick = JsfUtils.generateSubmitJS(context, form,
					rowIdField, row.getRowId());
			
			writer.startElement("tr", this);
			if (row.hasCssClass()) writer.writeAttribute("class", row.getClass(), null);
			writer.writeAttribute("class", row.getClass(), null);
			writer.writeAttribute("onclick", onClick, null);
			writer.writeAttribute("onmouseover", "this.className = 'focused'", null);
			writer.writeAttribute("onmouseout", "this.className = ''", null);
			
			for (int j = 0; j < columnsCount; j++)
			{
				
				GridColumn col = columns[j];
				String value = row.getNthValue(j);
				
				writer.startElement("td", this);
				if (col.hasCssClass()) writer.writeAttribute("class", col.getClass(), null);
				if (value != null) writer.write(value);
				writer.endElement("td");
				
			}
			writer.endElement("tr");

		}

		// end of table
		writer.endElement("table");
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public GridRow[] getRows()
	{
		return (GridRow[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"rows", rowsSet, rows);
	}

	public void setRows(GridRow[] rows)
	{
		rowsSet = true;
		this.rows = rows;
	}

	public GridColumn[] getColumns()
	{
		return (GridColumn[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"columns", columnsSet, columns);
	}

	public void setColumns(GridColumn[] columns)
	{
		columnsSet = true;
		this.columns = columns;
	}

	public MethodBinding getOnOpenRow()
	{
		return onOpenRow;
	}

	public void setOnOpenRow(MethodBinding onOpenRow)
	{
		this.onOpenRow = onOpenRow;
	}

}