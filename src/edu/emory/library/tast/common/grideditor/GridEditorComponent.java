package edu.emory.library.tast.common.grideditor;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
	
	private boolean extensionsSet = false;
	private Map extensions = null;

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
	
	private String getValueInputPrefix(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_value_" + row;
	}
	
	private String getValueErrorFlagFieldName(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_error_" + row;
	}

	private String getValueErrorMessageFieldName(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_error_msg_" + row;
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
				
				String inputPrefix = getValueInputPrefix(context, columName, rowName);
				String errorFlagFieldName = getValueErrorFlagFieldName(context, columName, rowName);
				String errorMessageFieldName = getValueErrorMessageFieldName(context, columName, rowName);
				
				Value value = adapter.decode(context, inputPrefix, this);
				values.setValue(columName, rowName, value);
				
				if (Boolean.parseBoolean((String) params.get(errorFlagFieldName)))
					value.setErrorMessage((String) params.get(errorMessageFieldName));

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
	
	private void encodeErrorMessage(FacesContext context, ResponseWriter writer, String columnName, String rowName, Value value) throws IOException
	{

		JsfUtils.encodeHiddenInput(this, writer,
				getValueErrorMessageFieldName(context, columnName, rowName),
				value.getErrorMessage());

		writer.startElement("div", this);
		writer.writeAttribute("class", "grid-editor-error", null);
		writer.write(value.getErrorMessage());
		writer.endElement("div");

	}
	
	private void encodeRegJS(FacesContext context, ResponseWriter writer, UIForm form, String mainId) throws IOException
	{

		// JS registration
		StringBuffer regJS = new StringBuffer();
		regJS.append("GridEditorGlobals.registerEditor(new GridEditor(");
		
		// main id
		regJS.append("'").append(mainId).append("', ");
		
		// form name
		regJS.append("'").append(form.getClientId(context)).append("' ");
		
		// fields
		regJS.append(", ");
		regJS.append("[");
		for (int i = 0; i < rows.length; i++)
		{
			Row row = rows[i];
			String rowName = row.getName();
			Adapter adapter = AdapterFactory.getAdapter(row.getType());
			regJS.append(rowName).append(": {");
			for (int j = 0; j < columns.length; j++)
			{
				
				Column column = columns[j];
				String columnName = column.getName();
				Value value = values.getValue(columnName, rowName);
				
				regJS.append(columnName).append(": ");
				adapter.encodeRegJS(
						context,
						regJS,
						this,
						getValueInputPrefix(context, columnName, rowName),
						value,
						column.isReadOnly());

			}
			regJS.append("}");
		}
		regJS.append("]");

		// extensions
		regJS.append(", [");
		int j = 0;
		if (extensions != null) {
		for (Iterator iter = extensions.entrySet().iterator(); iter.hasNext();)
		{
			Entry listEntry = (Entry) iter.next();;
			Extension ext = (Extension) listEntry.getValue();
			String extName = (String) listEntry.getKey();
			if (j > 0) regJS.append(", ");
			regJS.append(extName).append(": ");
			ext.encodeRegJS(regJS);
		}
		}
		regJS.append("]");

		// end js registration
		regJS.append("));");

		// render JS
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);

	}
	
	private void encodeGrid(FacesContext context, ResponseWriter writer, UIForm form, String mainId) throws IOException
	{
		
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
			String rowName = row.getName();
			
			Adapter adapter = AdapterFactory.getAdapter(row.getType());
			
			writer.startElement("tr", this);

			writer.startElement("th", this);
			writer.write(row.getLabel());
			writer.endElement("th");

			for (int j = 0; j < columns.length; j++)
			{
				Column column = columns[j];
				String columnName = column.getName();
				Value value = values.getValue(columnName, rowName);
				
				writer.startElement("td", this);

				adapter.encode(context,
						this,
						mainId,
						form,
						row,
						extensions,
						getValueInputPrefix(context, columnName, rowName),
						value,
						column.isReadOnly());
				
				JsfUtils.encodeHiddenInput(this, writer,
						getValueErrorFlagFieldName(context, columnName, rowName),
						Boolean.toString(value.isError()));

				if (value.isError())
					encodeErrorMessage(context, writer, columnName, rowName, value);
				
				writer.endElement("td");
				
			}
			
			writer.endElement("tr");
			
		}
		
		writer.endElement("table");
		
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
		extensions = getExtensions();
		
		// client id of the grid
		String mainId = getClientId(context);
		
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
		
		// registration JavaScript
		encodeRegJS(context, writer, form, mainId);

		// main grid
		encodeGrid(context, writer, form, mainId);

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

	public Map getExtensions()
	{
		return (Map) JsfUtils.getCompPropObject(this, getFacesContext(),
				"extensions", extensionsSet, extensions);
	}

	public void setExtensions(Map sharedLists)
	{
		extensionsSet = true;
		this.extensions = sharedLists;
	}

}
