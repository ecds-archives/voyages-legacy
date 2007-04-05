package edu.emory.library.tast.common.grideditor;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

public class GridEditorComponent extends UIComponentBase
{

	private boolean rowsSet = false;
	private Row[] rows;
	
	private boolean columnsSet = false;
	private Column[] columns;
	
	private boolean valuesSet = false;
	private Values values = null;

	public String getFamily()
	{
		return null;
	}
	
	private String getRowFieldName(FacesContext context, int rowIndex)
	{
		return getClientId(context) + "_row_name_" + rowIndex;
	}
	
	private String getRowFieldTypeName(FacesContext context, int rowIndex)
	{
		return getClientId(context) + "_row_type_" + rowIndex;
	}

	private String getColumnFieldName(FacesContext context, int columnIndex)
	{
		return getClientId(context) + "_column_name_" + columnIndex;
	}
	
	private String getCellInputPrefix(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row;
	}
	
	public void decode(FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();
		
		values = new Values();
		
		int rowCount = 0;
		String rowName;
		while ((rowName = (String) params.get(getRowFieldName(context, rowCount))) != null)
		{
			
			String rowType = (String) params.get(getRowFieldTypeName(context, rowCount));
			if (rowType == null) throw new RuntimeException("missing row type for row " + rowName);
			rowCount++;

			Adapter adapter = AdapterFactory.getAdapter(rowType);
			
			int columnCount = 0;
			String columName;
			while ((columName = (String) params.get(getColumnFieldName(context, columnCount++))) != null)
			{
				
				values.setValue(columName, rowName,
						adapter.decode(context,
								getCellInputPrefix(context, columName, rowName), this));

			}

		}
		
	}
	
	public void processUpdates(FacesContext context)
	{
		
		if (values != null)
		{
			ValueBinding vbValues = getValueBinding("values");
			if (vbValues != null) vbValues.setValue(context, values);
		}
		
	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		// get data from bean
		rows = getRows();
		columns = getColumns();
		values = getValues();
		
		// hidden fields with column names
		for (int i = 0; i < columns.length; i++)
			JsfUtils.encodeHiddenInput(this, writer,
					getColumnFieldName(context, i),
					columns[i].getName());
		
		// hidden fields with row names
		for (int i = 0; i < rows.length; i++)
			JsfUtils.encodeHiddenInput(this, writer,
					getRowFieldName(context, i),
					rows[i].getName());
		
		// hidden fields with row types
		for (int i = 0; i < rows.length; i++)
			JsfUtils.encodeHiddenInput(this, writer,
					getRowFieldTypeName(context, i),
					rows[i].getType());

		writer.startElement("table", this);
		writer.writeAttribute("border", "1", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "5", null);
		
		writer.startElement("tr", this);

		writer.startElement("th", this);
		writer.endElement("th");

		for (int i = 0; i < columns.length; i++)
		{
			writer.startElement("th", this);
			writer.write(columns[i].getLabel());
			writer.endElement("th");
		}

		writer.endElement("tr");

		for (int i = 0; i < rows.length; i++)
		{
			
			Row row = rows[i];
			Adapter adapter = AdapterFactory.getAdapter(row.getType());
			
			writer.startElement("tr", this);

			writer.startElement("th", this);
			writer.write(row.getLabel());
			writer.endElement("th");

			for (int j = 0; j < columns.length; j++)
			{
				Column column = columns[j];
				
				writer.startElement("td", this);
				
				adapter.encode(context, this, form,
						getCellInputPrefix(context, column.getName(), row.getName()),
						values.getValue(column.getName(), row.getName()), false);
				
				writer.endElement("td");
				
			}
			
			writer.endElement("tr");
			
		}
		
		writer.endElement("table");

	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public Column[] getColumns()
	{
		return (Column[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"columns", columnsSet, columns);
	}

	public void setColumns(Column[] columns)
	{
		columnsSet = true;
		this.columns = columns;
	}

	public Row[] getRows()
	{
		return (Row[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"rows", rowsSet, rows);
	}

	public void setRows(Row[] rows)
	{
		rowsSet = true;
		this.rows = rows;
	}

	public Values getValues()
	{
		return (Values) JsfUtils.getCompPropObject(this, getFacesContext(),
				"values", valuesSet, values);
	}

	public void setValues(Values values)
	{
		valuesSet = true;
		this.values = values;
	}

}
