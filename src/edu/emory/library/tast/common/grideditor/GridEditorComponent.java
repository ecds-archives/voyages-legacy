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

	private static final String NOTE_COLLAPSED = "collapsed";
	private static final String NOTE_EXPANDED = "expanded";
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
		return getClientId(context) + "_" + column + "_" + row + "_value";
	}
	
	private String getValueErrorFlagFieldName(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row + "_error";
	}

	private String getValueErrorMessageFieldName(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row + "_error_msg";
	}

	private String getExpandedGroupsFieldName(FacesContext context)
	{
		return getClientId(context) + "_expanded_groups";
	}

	private String getCellId(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row;
	}

	private String getNoteExpandedStatusFieldName(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row + "_note_status";
	}

	private String getNoteFieldName(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row + "_note";
	}

	private String getNoteEditBoxId(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row + "_note_edit";
	}

	private String getNoteReadBoxId(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row + "_note_read";
	}

	private String getNoteTextDisplayId(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row + "_note_text";
	}

	private String getNoteAddButtonId(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row + "_note_add_button";
	}

	private String getNoteEditButtonId(FacesContext context, String column, String row)
	{
		return getClientId(context) + "_" + column + "_" + row + "_note_edit_button";
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
			String columnName;
			while ((columnName = (String) params.get(getColumnFieldName(context, columnCount++))) != null)
			{

				// field names
				String inputPrefix = getValueInputPrefix(context, columnName, rowName);
				String errorFlagFieldName = getValueErrorFlagFieldName(context, columnName, rowName);
				String errorMessageFieldName = getValueErrorMessageFieldName(context, columnName, rowName);
				String noteFieldName = getNoteFieldName(context, columnName, rowName);
				String noteExpandedStatusFieldName = getNoteExpandedStatusFieldName(context, columnName, rowName);

				// decode value
				Value value = adapter.decode(context, inputPrefix, this);
				if (value == null)
				{
					values = null;
					return;
				}
				
				// add value
				values.setValue(columnName, rowName, value);

				// decode error
				if (params.containsKey(errorFlagFieldName))
				{
					if (Boolean.getBoolean((String) params.get(errorFlagFieldName)))
					{
						String error = (String) params.get(errorMessageFieldName);
						if (error != null)
						{
							value.setErrorMessage(error);
						}
						else
						{
							values = null;
							return;
						}
					}
				}
				else
				{
					values = null;
					return;
				}
				
				// decode note
				if (params.containsKey(noteFieldName) && params.containsKey(noteExpandedStatusFieldName))
				{
					value.setNoteExpanded(NOTE_EXPANDED.equals((String)params.get(noteExpandedStatusFieldName)));
					value.setNote((String)params.get(noteFieldName));
				}

			}

		}
		
		String expandedGroupsStr = (String) params.get(getExpandedGroupsFieldName(context));
		expandedGroups = new HashSet();
		if (!StringUtils.isNullOrEmpty(expandedGroupsStr))
		{
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
	
	private Column getCompateToColumn()
	{
		for (int i = 0; i < columns.length; i++)
		{
			Column column = columns[i];
			if (column.isCompareTo())
			{
				return column;
			}
		}
		return null;
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
						getCellId(context, columnName, rowName),
						value, column.isReadOnly());

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

		// compare to column
		Column compareToColumn = getCompateToColumn();
		regJS.append(", ");
		if (compareToColumn != null)
		{
			regJS.append("'").append(compareToColumn.getName()).append("'");
		}
		else
		{
			regJS.append("null");
		}

		// end js registration
		regJS.append("));");

		// render JS
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);

	}
	
	private List prepareInternalGroups()
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

	private void encodeNoteEditable(FacesContext context, ResponseWriter writer, String mainId, Column column, String rowName, Value value) throws IOException
	{
		
		String columnName = column.getName();
		String noteFieldName = getNoteFieldName(context, columnName, rowName);
		String noteStatusFieldName = getNoteExpandedStatusFieldName(context, columnName, rowName);
		String editBoxId = getNoteEditBoxId(context, columnName, rowName);
		String readBoxId = getNoteReadBoxId(context, columnName, rowName);
		String displayTextId = getNoteTextDisplayId(context, columnName, rowName);
		String addButtonId = getNoteAddButtonId(context, columnName, rowName);
		String editButtonId = getNoteEditButtonId(context, columnName, rowName);
		
		String cssStyleEditMode = value.isNoteExpanded() ? "" : "display: none";
		String cssStyleReadMode = value.isNoteExpanded() ? "display: none" : "";
		
		String cssStyleAddButton = value.hasNote() ? "display: none" : "";
		String cssStyleEditButton = value.hasNote() ? "" : "display: none";

		String onClickEdit = "GridEditorGlobals.editNote(" +
			"'" + mainId + "', " +
			"'" + columnName + "', " +
			"'" + rowName + "')";

		String onClickSave = "GridEditorGlobals.saveNote(" +
			"'" + mainId + "', " +
			"'" + columnName + "', " +
			"'" + rowName + "')";

		JsfUtils.encodeHiddenInput(this, writer,
				noteStatusFieldName,
				value.isNoteExpanded() ? NOTE_EXPANDED : NOTE_COLLAPSED);
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "grid-editor-note", null);

		writer.startElement("div", this);
		writer.writeAttribute("id", editBoxId, null);
		writer.writeAttribute("class", "grid-editor-note-edit", null);
		writer.writeAttribute("style", cssStyleEditMode, null);

		writer.startElement("textarea", this);
		writer.writeAttribute("name", noteFieldName, null);
		writer.writeAttribute("class", "grid-editor-note-box", null);
		writer.write(StringUtils.unNull(value.getNote()));
		writer.endElement("textarea");

		writer.startElement("div", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", "OK", null);
		writer.writeAttribute("onclick", onClickSave, null);
		writer.endElement("input");
		writer.endElement("div");

		writer.endElement("div");

		writer.startElement("div", this);
		writer.writeAttribute("id", readBoxId, null);
		writer.writeAttribute("class", "grid-editor-note-read", null);
		writer.writeAttribute("style", cssStyleReadMode, null);

		writer.startElement("div", this);
		
		writer.startElement("span", this);
		writer.writeAttribute("class", "grid-editor-note-text", null);
		writer.writeAttribute("id", displayTextId, null);
		writer.write(StringUtils.unNull(value.getNote()));
		writer.endElement("span");

		writer.write(" ");

		writer.startElement("span", this);
		writer.writeAttribute("id", addButtonId, null);
		writer.writeAttribute("class", "grid-editor-edit-note-button", null);
		writer.writeAttribute("style", cssStyleAddButton, null);
		writer.writeAttribute("onclick", onClickEdit, null);
		writer.write("Add note");
		writer.endElement("span");

		writer.startElement("span", this);
		writer.writeAttribute("id", editButtonId, null);
		writer.writeAttribute("class", "grid-editor-edit-note-button", null);
		writer.writeAttribute("style", cssStyleEditButton, null);
		writer.writeAttribute("onclick", onClickEdit, null);
		writer.write("Edit note");
		writer.endElement("span");

		writer.endElement("div");

	}
	
	private void encodeNoteReadOnly(FacesContext context, ResponseWriter writer, Value value) throws IOException
	{

		writer.startElement("div", this);
		writer.writeAttribute("class", "grid-editor-note-read-only", null);
		if (value.getNote() != null) writer.write(value.getNote());
		writer.endElement("div");

	}

	private void encodeNote(FacesContext context, ResponseWriter writer, String mainId, Column column, String rowName, Value value) throws IOException
	{
		if (!column.isReadOnly())
			encodeNoteEditable(context, writer, mainId, column, rowName, value);
		else
			encodeNoteReadOnly(context, writer, value);
	}
	
	private void encodeCopyButton(FacesContext context, ResponseWriter writer, String mainId, Column column, String rowName) throws IOException
	{

		String onClickCopy = "GridEditorGlobals.copy(" +
			"'" + mainId + "', " +
			"'" + column.getName() + "', " +
			"'" + column.getCopyToColumn() + "', " +
			"'" + rowName + "')";

		writer.startElement("div", this);
		writer.writeAttribute("class", "grid-editor-copy", null);

		writer.startElement("span", this);
		writer.writeAttribute("class", "grid-editor-copy-button", null);
		writer.writeAttribute("onclick", onClickCopy, null);
		writer.write(column.getCopyToLabel());
		writer.endElement("span");

		writer.endElement("div");

	}
	
	private void encodeGrid(FacesContext context, ResponseWriter writer, UIForm form, String mainId, String mainTableId, List internalGroups) throws IOException
	{
		
		Column compareToColumn = getCompateToColumn();
		
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

					Value compareToValue = null;
					if (compareToColumn != null)
						compareToValue = values.getValue(
								compareToColumn.getName(),
								rowName);

					for (int j = 0; j < columns.length; j++)
					{
						Column column = columns[j];
						String columnName = column.getName();
						Value value = values.getValue(columnName, rowName);

						String cellCssClass = null;
						if (compareToValue != null)
						{
							if (compareToColumn == column)
							{
								cellCssClass = "grid-editor-compared-value";
							}
							else if (compareToValue.equals(value))
							{
								cellCssClass = "grid-editor-same-value";
							}
							else
							{
								cellCssClass = "grid-editor-dist-value";
							}
						}
						else
						{
							cellCssClass = "";
						}
						
						writer.startElement("td", this);
						writer.writeAttribute("class", cellCssClass, null);
						writer.writeAttribute("id", getCellId(context, columnName, rowName), null);
						
						adapter.encode(context,
								this,
								mainId,
								form,
								row,
								column,
								fieldType,
								getValueInputPrefix(context, columnName, rowName),
								value, column.isReadOnly(),
								compareToColumn == column);
						
						JsfUtils.encodeHiddenInput(this, writer,
								getValueErrorFlagFieldName(context, columnName, rowName),
								Boolean.toString(value.isError()));
		
						if (value.isError())
							encodeErrorMessage(context, writer, columnName, rowName, value);
						
						if (column.isCopyToEnabled())
							encodeCopyButton(context, writer, mainId, column, rowName);

						if (row.isNoteEnabled())
							encodeNote(context, writer, mainId, column, rowName, value);
						
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
		List internalGroups = prepareInternalGroups();

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