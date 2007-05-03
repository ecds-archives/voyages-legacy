package edu.emory.library.tast.common.grideditor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class GridEditorComponent extends UIComponentBase
{

	private boolean rowsSet = false;
	private Row[] rows;
	
	private boolean rowGroupsSet = false;
	private RowGroup[] rowGroups;

	private boolean columnsSet = false;
	private Column[] columns;
	
	private boolean valuesSet = false;
	private Values values = null;

	private boolean fieldTypesSet = false;
	private Map fieldTypes = null;

	private boolean expandedGroupsSet = false;
	private Set expandedGroups = null;
	
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

	private String getExpandedGroupsFieldName(FacesContext context)
	{
		return getClientId(context) + "_expanded_groups";
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
				if (value == null)
				{
					values = null;
					return;
				}
				
				values.setValue(columName, rowName, value);
				
				if (Boolean.parseBoolean((String) params.get(errorFlagFieldName)))
					value.setErrorMessage((String) params.get(errorMessageFieldName));

			}

		}
		
		String expandedGroupsStr = (String) params.get(getExpandedGroupsFieldName(context));
		if (!StringUtils.isNullOrEmpty(expandedGroupsStr))
		{
			expandedGroups = new HashSet();
			String expandedGroupsIndexes[] = expandedGroupsStr.split(",");
			for (int i = 0; i < expandedGroupsIndexes.length; i++)
			{
				expandedGroups.add(new Integer(expandedGroupsIndexes[i]));
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
		
		if (expandedGroups != null)
		{
			ValueBinding vbExpandedGroups = getValueBinding("expandedGroups");
			if (vbExpandedGroups != null) vbExpandedGroups.setValue(context, expandedGroups);
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
	
	private void encodeRegJS(FacesContext context, ResponseWriter writer, UIForm form, String mainId, String mainTableId, List internalGroups) throws IOException
	{

		// JS registration
		StringBuffer regJS = new StringBuffer();
		regJS.append("GridEditorGlobals.registerEditor(new GridEditor(");
		
		// main id
		regJS.append("'").append(mainId).append("'");
		
		// form name
		regJS.append(", ");
		regJS.append("'").append(form.getClientId(context)).append("'");
		
		// main table id
		regJS.append(", ");
		regJS.append("'").append(mainTableId).append("'");

		// hidden field with the list of expanded row groups
		regJS.append(", ");
		regJS.append("'").append(getExpandedGroupsFieldName(context)).append("'");

		// field types
		regJS.append(", {");
		if (fieldTypes != null)
		{
			int j = 0;
			for (Iterator iter = fieldTypes.entrySet().iterator(); iter.hasNext();)
			{
				Entry listEntry = (Entry) iter.next();;
				FieldType fieldType = (FieldType) listEntry.getValue();
				String extName = (String) listEntry.getKey();
				Adapter adapter = AdapterFactory.getAdapter(fieldType.getType());
				if (j > 0) regJS.append(", ");
				regJS.append("'").append(extName).append("': ");
				adapter.createFieldTypeJavaScript(context, regJS, this, fieldType);
				j++;
			}
		}
		regJS.append("}");
		
		// fields
		regJS.append(", ");
		regJS.append("{");
		for (int i = 0; i < rows.length; i++)
		{
			
			Row row = rows[i];
			String rowName = row.getName();
			
			FieldType fieldType = (FieldType) fieldTypes.get(row.getType());
			Adapter adapter = AdapterFactory.getAdapter(fieldType.getType());
			
			if (i > 0) regJS.append(", ");
			
			regJS.append("'").append(rowName).append("': {");
			for (int j = 0; j < columns.length; j++)
			{
				
				Column column = columns[j];
				String columnName = column.getName();
				Value value = values.getValue(columnName, rowName);
				
				if (j > 0) regJS.append(", ");

				regJS.append("'").append(columnName).append("': ");
				adapter.createValueJavaScript(
						context,
						regJS,
						this,
						getValueInputPrefix(context, columnName, rowName),
						row,
						column,
						value,
						column.isReadOnly());

			}
			regJS.append("}");
		}
		regJS.append("}");
		
		// groups
		regJS.append(", ");
		regJS.append("[");
		int rowIndex = 1;
		int i = 0;
		for (Iterator iter = internalGroups.iterator(); iter.hasNext();)
		{
			
			RowGroupInternal internalGroup = (RowGroupInternal) iter.next();
			
			if (internalGroup.rows.size() > 0)
			{
			
				if (i > 0) regJS.append(", ");
				
				regJS.append("new GridEditorRowGroup(");
	
				if (internalGroup.renderTitle) rowIndex++;
				regJS.append(rowIndex);
				
				regJS.append(", ");
				rowIndex += internalGroup.rows.size();
				regJS.append(rowIndex - 1);
				
				regJS.append(", ");
				if (expandedGroups.contains(new Integer(i)))
				{
					regJS.append("true");
				}
				else
				{
					regJS.append("false");
				}
				
				regJS.append(")");
				
				i++;
			
			}
			
		}
		regJS.append("]");

		// end js registration
		regJS.append("));");

		// render JS
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);

	}
	
	private List prepateInternalGroups()
	{
		
		List internalGroups = new ArrayList(); 
		
		// no row groups
		if (rowGroups == null)
		{
			RowGroupInternal fakeGroup = new RowGroupInternal();
			fakeGroup.label = null;
			fakeGroup.renderTitle = false;
			fakeGroup.rows = Arrays.asList(rows);
			internalGroups.add(fakeGroup);
		}
		
		// we have some groups
		else
		{
			
			// given groups
			Map rowGroupMap = new HashMap();
			for (int i = 0; i < rowGroups.length; i++)
			{
				RowGroup row = rowGroups[i];
				RowGroupInternal internalGroup = new RowGroupInternal();
				internalGroup.label = row.getLabel();
				internalGroup.renderTitle = true;
				rowGroupMap.put(row.getName(), internalGroup);
				internalGroups.add(internalGroup);
			}
			
			// special group for unspecified
			RowGroupInternal unspecifiedGroup = new RowGroupInternal();
			unspecifiedGroup.label = "Unspecified";
			unspecifiedGroup.renderTitle = true;
			rowGroupMap.put(null, unspecifiedGroup);
			internalGroups.add(unspecifiedGroup);
			
			// group rows by groupName
			for (int i = 0; i < rows.length; i++)
			{
				Row row = rows[i];
				RowGroupInternal internalGroup = (RowGroupInternal) rowGroupMap.get(row.getGroupName());
				if (internalGroup == null) internalGroup = (RowGroupInternal) rowGroupMap.get(null);
				internalGroup.rows.add(row);
			}
			
		}
		
		return internalGroups;
		
	}
	
	private void encodeGrid(FacesContext context, ResponseWriter writer, UIForm form, String mainId, String mainTableId, List internalGroups) throws IOException
	{
		
		writer.startElement("table", this);
		writer.writeAttribute("id", mainTableId, null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "grid-editor", null);
		
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
		
		int rowGroupIndex = 0;
		for (Iterator iter = internalGroups.iterator(); iter.hasNext();)
		{
			
			RowGroupInternal internalGroup = (RowGroupInternal) iter.next();
			
			if (internalGroup.rows.size() != 0)
			{
				
				boolean groupExpanded = true;
				
				if (internalGroup.renderTitle && internalGroup.label != null)
				{
					
					String onClick = "GridEditorGlobals.toggleRowGroup(" +
							"'" + mainId + "', " +
							"" + rowGroupIndex + ")";
					
					groupExpanded = expandedGroups.contains(new Integer(rowGroupIndex));
					
					writer.startElement("tr", this);
					writer.startElement("th", this);
					writer.writeAttribute("colspan", String.valueOf(columns.length + 1), null);
					writer.writeAttribute("class", "grid-editor-row-group", null);
					writer.writeAttribute("onclick", onClick, null);
					writer.write(internalGroup.label);
					writer.endElement("th");
					writer.endElement("tr");

				}
				
				for (Iterator iterRows = internalGroup.rows.iterator(); iterRows.hasNext();)
				{
					
					Row row = (Row) iterRows.next();
					String rowName = row.getName();
					
					FieldType fieldType = (FieldType) fieldTypes.get(row.getType());
					Adapter adapter = AdapterFactory.getAdapter(fieldType.getType());
					
					writer.startElement("tr", this);
					if (!groupExpanded) writer.writeAttribute("style", "display: none;", null);
		
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
								column,
								fieldType,
								getValueInputPrefix(context, columnName, rowName),
								value, column.isReadOnly());
						
						JsfUtils.encodeHiddenInput(this, writer,
								getValueErrorFlagFieldName(context, columnName, rowName),
								Boolean.toString(value.isError()));
		
						if (value.isError())
							encodeErrorMessage(context, writer, columnName, rowName, value);
						
						writer.endElement("td");
						
					}
					
					writer.endElement("tr");
					
				}
				
				rowGroupIndex++;
				
			}
		
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
		rowGroups = getRowGroups();
		columns = getColumns();
		values = getValues();
		fieldTypes = getFieldTypes();
		expandedGroups = getExpandedGroups();
		if (expandedGroups == null) expandedGroups = new HashSet();
		
		// client id of the grid
		String mainId = getClientId(context);
		String mainTableId = mainId;
		
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
		{

			String userType = rows[i].getType();
			FieldType fieldType = (FieldType) fieldTypes.get(userType);

			if (fieldType == null)
				throw new RuntimeException("No registered field type for: " + userType);
			
			JsfUtils.encodeHiddenInput(this, writer,
					getRowFieldTypeName(context, i),
					fieldType.getType());

		}
		
		// hidden field with the list of expanded row groups
		JsfUtils.encodeHiddenInput(this, writer,
				getExpandedGroupsFieldName(context),
				StringUtils.join(",", expandedGroups));
		
		// prepate a list for rendering
		List internalGroups = prepateInternalGroups();

		// registration JavaScript
		encodeRegJS(context, writer, form, mainId, mainTableId, internalGroups);

		// main grid
		encodeGrid(context, writer, form, mainId, mainTableId, internalGroups);

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

	public Map getFieldTypes()
	{
		return (Map) JsfUtils.getCompPropObject(this, getFacesContext(),
				"fieldTypes", fieldTypesSet, fieldTypes);
	}

	public void setFieldTypes(Map fieldTypes)
	{
		fieldTypesSet = true;
		this.fieldTypes = fieldTypes;
	}

	public RowGroup[] getRowGroups()
	{
		return (RowGroup[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"rowGroups", rowGroupsSet, rowGroups);
	}

	public void setRowGroups(RowGroup[] rowGroups)
	{
		rowGroupsSet = true;
		this.rowGroups = rowGroups;
	}

	public Set getExpandedGroups()
	{
		return (Set) JsfUtils.getCompPropObject(this, getFacesContext(),
				"expandedGroups", expandedGroupsSet, expandedGroups);
	}

	public void setExpandedGroups(Set expandedGroups)
	{
		expandedGroupsSet = true;
		this.expandedGroups = expandedGroups;
	}

}

class RowGroupInternal
{
	public boolean renderTitle;
	public String label;
	public List rows = new ArrayList();
}