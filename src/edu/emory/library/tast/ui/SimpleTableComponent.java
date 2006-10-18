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

	private boolean columnLabelsSet = false;
	private String[] columnLabels;
	
	private boolean rowLabelsSet = false;
	private String[] rowLabels;

	public String getFamily()
	{
		return null;
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// get data from bean
		String[] columnLabels = getColumnLabels();
		String[] rowLabels = getRowLabels();
		String[][] data = getData();
		if (data == null) data = new String[0][0]; 
		
		// nothing to display
		if (columnLabels == null || rowLabels == null)
			return;
		
		// size
		int colCount = columnLabels.length;
		int rowCount = rowLabels.length;
		
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
		for (int j = 0; j < colCount; j++)
		{
			writer.startElement("td", this);
			writer.writeAttribute("class", "simple-table-col-label", null);
			writer.write(columnLabels[j]);
			writer.endElement("td");
		}

		// rows
		for (int i = 0; i < rowCount; i++)
		{
			writer.startElement("tr", this);
			
			// row label
			writer.startElement("td", this);
			writer.writeAttribute("class", "simple-table-row-label", null);
			writer.write(rowLabels[i]);
			writer.endElement("td");

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

	public String[] getColumnLabels()
	{
		return JsfUtils.getCompPropStringArray(this, getFacesContext(),
				"columnLabels", columnLabelsSet, columnLabels);
	}

	public void setColumnLabels(String[] columnLabels)
	{
		columnLabelsSet = true;
		this.columnLabels = columnLabels;
	}

	public String[] getRowLabels()
	{
		return JsfUtils.getCompPropStringArray(this, getFacesContext(),
				"rowLabels", rowLabelsSet, rowLabels);
	}

	public void setRowLabels(String[] rowLabels)
	{
		rowLabelsSet = true;
		this.rowLabels = rowLabels;
	}

}
