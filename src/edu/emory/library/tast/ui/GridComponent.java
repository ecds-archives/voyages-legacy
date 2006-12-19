package edu.emory.library.tast.ui;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class GridComponent extends UIComponentBase
{
	
	private boolean rowsSet = false;
	private GridRow[] rows;

	private boolean columnsSet = false;
	private GridColumn[] columns;

	public String getFamily()
	{
		return null;
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// get data from bean
		GridRow[] rows = getRows();
		GridColumn[] columns = getColumns();
		if (columns == null || columns.length == 0) return; 
		if (rows == null) rows = new GridRow[0];
		int columnsCount = columns.length;
		
		// table
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "grid-table", null);
		
		// header
		writer.startElement("tr", this);
		writer.writeAttribute("class", "grid-header", null);
		for (int j = 0; j < columnsCount; j++)
		{
			GridColumn col = columns[j];
			writer.startElement("td", this);
			if (col.hasCssClass()) writer.writeAttribute("class", col.getClass(), null);
			writer.write(col.getHeader());
			writer.endElement("td");
		}
		writer.endElement("tr");

		// data
		for (int i = 0; i < rows.length; i++)
		{
			
			GridRow row = rows[i];
			
			writer.startElement("tr", this);
			if (row.hasCssClass()) writer.writeAttribute("class", row.getClass(), null);
			
			for (int j = 0; j < columnsCount; j++)
			{
				
				GridColumn col = columns[j];
				String value = row.getNthValue(j);
				
				writer.startElement("td", this);
				if (col.hasCssClass()) writer.writeAttribute("class", col.getClass(), null);
				writer.write(value);
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

}