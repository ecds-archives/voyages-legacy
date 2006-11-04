package edu.emory.library.tast.ui;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class SimpleTableComponent extends UIComponentBase
{
	
	private boolean dataSet = false;
	private String[][] data;

	private boolean columnsSet = false;
	private SimpleTableLabel[] columns;
	
	private boolean rowsSet = false;
	private SimpleTableLabel[] rows;

	public String getFamily()
	{
		return null;
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// get data from bean
		SimpleTableLabel[] cols = getColumns();
		SimpleTableLabel[] rows = getRows();
		String[][] data = getData();
		if (data == null) data = new String[0][0]; 
		
		// nothing to display
		if (cols == null || rows == null)
			return;
		
		// table
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "simple-table", null);

		// first extra cell
		writer.startElement("td", this);
		writer.writeAttribute("class", "simple-table-top-left-cell", null);
		writer.endElement("td");
		
		// column labels
		int colCount = 0;
		for (int j = 0; j < cols.length; j++)
		{
			SimpleTableLabel col = cols[j];
			int span = col.getSpan();
			writer.startElement("td", this);
			if (span > 1) writer.writeAttribute("colspan", String.valueOf(span), null);
			writer.writeAttribute("class", "simple-table-col-label", null);
			writer.write(col.getLabel());
			writer.endElement("td");
			colCount += span;
		}
		
		// count rows
		int rowCount = 0;
		for (int i = 0; i < rows.length; i++)
		{
			SimpleTableLabel row = rows[i];
			rowCount += row.getSpan();
		}

		// render table itfself
		int k = 0;
		int span = 0;
		for (int i = 0; i < rowCount; i++)
		{
			writer.startElement("tr", this);
			
			// row label
			if (span == 0)
			{
				SimpleTableLabel row = rows[k++];
				span = row.getSpan();
				writer.startElement("td", this);
				if (span > 1) writer.writeAttribute("rowspan", String.valueOf(span), null);
				writer.writeAttribute("class", "simple-table-row-label", null);
				writer.write(row.getLabel());
				writer.endElement("td");
			}
			span--;

			// get data
			String dataRow[] = null;
			if (i < data.length) dataRow = data[i];
			
			// render them
			for (int j = 0; j < colCount; j++)
			{
				writer.startElement("td", this);
				if (dataRow != null && j < dataRow.length)
				{
					writer.write(dataRow[j]);
				}
				writer.endElement("td");
			}
			
			writer.endElement("tr");
		}

		// end table
		writer.endElement("table");
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public String[][] getData()
	{
		return (String[][]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"data", dataSet, data);
	}

	public void setData(String[][] data)
	{
		dataSet = true;
		this.data = data;
	}

	public SimpleTableLabel[] getColumns()
	{
		return (SimpleTableLabel[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"columns", columnsSet, columns);
	}

	public void setColumnLabels(SimpleTableLabel[] columnLabels)
	{
		columnsSet = true;
		this.columns = columnLabels;
	}

	public SimpleTableLabel[] getRows()
	{
		return (SimpleTableLabel[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"rows", rowsSet, rows);
	}

	public void setRowLabels(SimpleTableLabel[] rowLabels)
	{
		rowsSet = true;
		this.rows = rowLabels;
	}

}
