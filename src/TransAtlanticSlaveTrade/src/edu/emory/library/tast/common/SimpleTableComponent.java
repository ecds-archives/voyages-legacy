package edu.emory.library.tast.common;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class SimpleTableComponent extends UIComponentBase
{
	
	private boolean rowsSet = false;
	private SimpleTableCell[][] rows;

	public String getFamily()
	{
		return null;
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// get data from bean
		SimpleTableCell[][] rows = getRows();
		if (rows == null) return; 
		
		// table
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "simple-table", null);

		// render table itfself
		for (int i = 0; i < rows.length; i++)
		{
			SimpleTableCell[] row = rows[i];
			writer.startElement("tr", this);
			for (int j = 0; j < row.length; j++)
			{
				SimpleTableCell cell = row[j];
				if (cell != null)
				{
					writer.startElement("td", this);
					if (cell.getCssStyle() != null) writer.writeAttribute("style", cell.getCssStyle(), null);
					if (cell.getColspan() != 1) writer.writeAttribute("colspan", String.valueOf(cell.getColspan()), null);
					if (cell.getRowspan() != 1) writer.writeAttribute("rowspan", String.valueOf(cell.getRowspan()), null);
					if (cell.getCssClass() != null) writer.writeAttribute("class", cell.getCssClass(), null);
					writer.write(StringUtils.coalesce(cell.getText(), ""));
					writer.endElement("td");
				}
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

	public SimpleTableCell[][] getRows()
	{
		return (SimpleTableCell[][]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"rows", rowsSet, rows);
	}

	public void setRows(SimpleTableCell[][] data)
	{
		rowsSet = true;
		this.rows = data;
	}

}
