var GridEditorGlobals = 
{
	
	gridEditors: new Array(),
	
	registerEditor: function(gridEditor)
	{
		GridEditorGlobals.gridEditors[gridEditor.gridEditorId] = gridEditor;
	},
	
	getEditorField: function(gridEditorId, rowName, columnName)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (!gridEditor) return null;
		return gridEditor.getField(rowName, columnName);
	},

	listItemSelected: function(gridEditorId, rowName, columnName, depth, invokeCompare)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (gridEditor) gridEditor.listItemSelected(rowName, columnName, depth, invokeCompare);
	},
	
	toggleRowGroup: function(gridEditorId, rowGroupIndex)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (gridEditor) gridEditor.toggleRowGroup(rowGroupIndex);
	},
	
	editNote: function(gridEditorId, columnName, rowName)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (gridEditor) gridEditor.editNote(columnName, rowName);
	},

	saveNote: function(gridEditorId, columnName, rowName)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (gridEditor) gridEditor.saveNote(columnName, rowName);
	},
	
	copy: function(gridEditorId, srcColumnName, dstColumnName, rowName)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (gridEditor) gridEditor.copy(srcColumnName, dstColumnName, rowName);
	},
	
	copyRow: function(gridEditorId, columnName, srcRowName, dstRowName)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (gridEditor) gridEditor.copyRow(columnName, srcRowName, dstRowName);
	},

	compare: function(gridEditorId, rowName, columnName)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (gridEditor) {
			gridEditor.compare(rowName, columnName);
		}	
	},
	
	compareRow: function(gridEditorId, rowName, columnName)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (gridEditor) gridEditor.compareRow(rowName, columnName);
	}

}

/********************************************
* constructors
*********************************************/

function GridEditor(gridEditorId, formName, mainTableId, expandedGroupsFieldName, fieldTypes, fields, rowGroups, compareToColumn, compareToRows)
{
	this.gridEditorId = gridEditorId;
	this.mainTableId = mainTableId;
	this.formName = formName;
	this.fieldTypes = fieldTypes;
	this.fields = fields;
	this.rowGroups = rowGroups;
	this.expandedGroupsFieldName = expandedGroupsFieldName;
	this.compareToColumn = compareToColumn;
	this.compareToRows = compareToRows;
}

function GridEditorListFieldType(list)
{
	this.list = list;
}

function GridEditorListItem(value, text, subItems)
{
	this.value = value;
	this.text = text;
	this.subItems = subItems;
}

function GridEditorTextbox(cellId, inputName)
{
	this.cellId = cellId;
	this.inputName = inputName;
}

function GridEditorTextarea(cellId, inputName)
{
	this.cellId = cellId;
	this.inputName = inputName;
}

function GridEditorDate(cellId, yearInputName, monthInputName, dayInputName)
{
	this.cellId = cellId;
	this.yearInputName = yearInputName;
	this.monthInputName = monthInputName;
	this.dayInputName = dayInputName;
}

function GridEditorList(cellId, fieldTypeName, selectsNamePrefix, depthFieldName)
{
	this.cellId = cellId;
	this.fieldTypeName = fieldTypeName;
	this.selectsNamePrefix = selectsNamePrefix;
	this.depthFieldName = depthFieldName;
}

function GridEditorRowGroup(firstRowIndex, lastRowIndex, expanded)
{
	this.firstRowIndex = firstRowIndex;
	this.lastRowIndex = lastRowIndex;
	this.expanded = expanded;
}

/********************************************
* GridEditor object
*********************************************/

GridEditor.prototype.getNoteExpandedStatusFieldName = function(column, row)
{
	return this.gridEditorId + "_" + column + "_" + row + "_note_status";
}

GridEditor.prototype.getNoteFieldName = function(column, row)
{
	return this.gridEditorId + "_" + column + "_" + row + "_note";
}

GridEditor.prototype.getNoteEditBoxId = function(column, row)
{
	return this.gridEditorId + "_" + column + "_" + row + "_note_edit";
}

GridEditor.prototype.getNoteReadBoxId = function(column, row)
{
	return this.gridEditorId + "_" + column + "_" + row + "_note_read";
}

GridEditor.prototype.getNoteTextDisplayId = function(column, row)
{
	return this.gridEditorId + "_" + column + "_" + row + "_note_text";
}

GridEditor.prototype.getNoteAddButtonId = function(column, row)
{
	return this.gridEditorId + "_" + column + "_" + row + "_note_add_button";
}

GridEditor.prototype.getNoteEditButtonId = function(column, row)
{
	return this.gridEditorId + "_" + column + "_" + row + "_note_edit_button";
}

GridEditor.prototype.getField = function(rowName, columnName)
{
	var row = this.fields[rowName];
	if (!row) return null;
	return row[columnName];
}

GridEditor.prototype.listItemSelected = function(rowName, columnName, depth, invokeCompare)
{
	var field = this.getField(rowName, columnName);
	if (field)
	{
		field.itemSelected(this, depth, invokeCompare);
		if (invokeCompare) {
			if (this.compareToColumn) {
				this.compare(rowName, columnName);
			} else {
				this.compareRow(rowName, columnName);
			}
		}
	}
}

GridEditor.prototype.toggleRowGroup = function(rowGroupIndex)
{

	var rowGroup = this.rowGroups[rowGroupIndex];
	rowGroup.expanded = !rowGroup.expanded;

	var mainTable = document.getElementById(this.mainTableId);	
	for (var i = rowGroup.firstRowIndex; i <= rowGroup.lastRowIndex; i++)
		mainTable.rows[i].style.display = rowGroup.expanded ? "" : "none";

	var expandedRowsIndexes = new Array();
	for (var i = 0; i < this.rowGroups.length; i++)
	{
		if (this.rowGroups[i].expanded)
		{
			expandedRowsIndexes.push(i);		
		}
	}
	
	document.forms[this.formName].elements[this.expandedGroupsFieldName].value =
		expandedRowsIndexes.join(",");
	
}

GridEditor.prototype.changeNoteStatus = function(columnName, rowName, expanded)
{

	var statusFieldName = this.getNoteExpandedStatusFieldName(columnName, rowName);
	var noteFieldName = this.getNoteFieldName(columnName, rowName);
	var editBoxId = this.getNoteEditBoxId(columnName, rowName);
	var readBoxId = this.getNoteReadBoxId(columnName, rowName);
	var textDisplayId = this.getNoteTextDisplayId(columnName, rowName);
	var addButtonId = this.getNoteAddButtonId(columnName, rowName);
	var editButtonId = this.getNoteEditButtonId(columnName, rowName);
	
	var frm = document.forms[this.formName];

	var readBox = document.getElementById(readBoxId);
	var editBox = document.getElementById(editBoxId);
	
	if (expanded)
	{
	
		readBox.style.display = "none";
		editBox.style.display = "block";

		frm.elements[statusFieldName].value = "expanded";
	
	}
	else
	{

		readBox.style.display = "block";
		editBox.style.display = "none";

		frm.elements[statusFieldName].value = "collapsed";

		var note = frm.elements[noteFieldName].value;
		var isEmpty = note.match(/^\s*$/);
		
		var textDisplay = document.getElementById(textDisplayId);
		var addButton = document.getElementById(addButtonId);
		var editButton = document.getElementById(editButtonId);

		if (isEmpty)
		{
			textDisplay.innerHTML = "";
			textDisplay.style.display = "none";
			addButton.style.display = "";
			editButton.style.display = "none";
		}
		else
		{
			textDisplay.innerHTML = note;
			textDisplay.style.display = "";
			addButton.style.display = "none";
			editButton.style.display = "";
		}

	}

}

GridEditor.prototype.editNote = function(columnName, rowName)
{
	this.changeNoteStatus(columnName, rowName, true);
}

GridEditor.prototype.saveNote = function(columnName, rowName)
{
	this.changeNoteStatus(columnName, rowName, false);
}

GridEditor.prototype.copy = function(srcColumnName, dstColumnName, rowName)
{

	var row = this.fields[rowName];
	if (!row) return;

	var srcField = row[srcColumnName];
	var dstField = row[dstColumnName];
	if (!srcField || !dstField) return;
	
	var value = srcField.getValue(this);
	dstField.setValue(this, value);
	
	if (this.compareToColumn)
		this.compare(rowName, this.compareToColumn);

}

GridEditor.prototype.copyRow = function(columnName, srcRowName, dstRowName)
{

	var srcRow = this.fields[srcRowName];
	if (!srcRow) return;

	var dstRow = this.fields[dstRowName];
	if (!dstRow) return;

	var srcField = srcRow[columnName];
	var dstField = dstRow[columnName];
	if (!srcField || !dstField) return;
	
	var value = srcField.getValue(this);
	dstField.setValue(this, value);
	
	if (this.compareToRows)
		this.compareRow(dstRowName, columnName);

}

GridEditor.prototype.compare = function(rowName, columnName)
{

	if (!this.compareToColumn) {
		this.compareRow(rowName, columnName);
	} else {
		var value = this.fields[rowName][columnName].getValue(this);
	
		var rowFields = this.fields[rowName];
		for (c in rowFields)
		{
			if (c != columnName)
			{
				var cell = document.getElementById(rowFields[c].cellId);
				if (rowFields[c].compareTo(this, value))
				{
					cell.className = "grid-editor-same-value";
				}
				else
				{
					cell.className = "grid-editor-dist-value";
				}
			}
		}
	}
}

GridEditor.prototype.compareRow = function(rowName, columnName)
{
	var value = this.fields[rowName][columnName].getValue(this);
	var rowsToCheck = this.compareToRows[rowName];
	for (row in rowsToCheck) {
		var rowFields = this.fields[rowsToCheck[row]];
		var cell = document.getElementById(rowFields[columnName].cellId);
		if (rowFields[columnName].compareTo(this, value))
		{
			cell.className = "grid-editor-same-value";
		}
		else
		{
			cell.className = "grid-editor-dist-value";
		}
	}
}

/********************************************
* GridEditorTextbox object
*********************************************/

GridEditorTextbox.prototype.getValue = function(grid)
{
	return document.forms[grid.formName].elements[this.inputName].value;
}

GridEditorTextbox.prototype.setValue = function(grid, value)
{
	document.forms[grid.formName].elements[this.inputName].value = value;
}

GridEditorTextbox.prototype.compareTo = function(grid, value)
{
	return value == this.getValue(grid);
}

/********************************************
* GridEditorTextarea object
*********************************************/

GridEditorTextarea.prototype.getValue = function(grid)
{
	return StringUtils.splitByLinesAndRemoveEmpty(document.forms[grid.formName].elements[this.inputName].value);
}

GridEditorTextarea.prototype.setValue = function(grid, values)
{
	document.forms[grid.formName].elements[this.inputName].value = values.join("\n");
}

GridEditorTextarea.prototype.compareTo = function(grid, values)
{
	return StringUtils.compareArrays(values, this.getValue(grid));
}

/********************************************
* GridEditorDate object
*********************************************/

GridEditorDate.prototype.getValue = function(grid)
{
	var frm = document.forms[grid.formName];
	return {
		year: frm.elements[this.yearInputName].value.replace(/^0+/, ""),
		month: frm.elements[this.monthInputName].value.replace(/^0+/, ""),
		day: frm.elements[this.dayInputName].value.replace(/^0+/, "") };
}

GridEditorDate.prototype.setValue = function(grid, value)
{
	var frm = document.forms[grid.formName];
	frm.elements[this.yearInputName].value = value.year;
	frm.elements[this.monthInputName].value = value.month;
	frm.elements[this.dayInputName].value = value.day;
}

GridEditorDate.prototype.compareTo = function(grid, value)
{
	var thisValue = this.getValue(grid);
	return thisValue.year == value.year && thisValue.month == value.month && thisValue.day == value.day;
}

/********************************************
* GridEditorList object
*********************************************/

GridEditorList.prototype.getValue = function(grid)
{
	
	var frm = document.forms[grid.formName];
	var depth = parseInt(frm.elements[this.depthFieldName].value);

	var values = new Array();	
	for (var i = 0; i < depth; i++)
	{
		var sel = frm.elements[this.selectsNamePrefix + i];
		values[i] = sel.value;
	}
	
	return values;

}

GridEditorList.prototype.setValue = function(grid, values)
{

	var frm = document.forms[grid.formName];
	var nextItems = grid.fieldTypes[this.fieldTypeName].list;
	
	var i = 0;
	while (nextItems && nextItems.length != 0)
	{
	
		var items = nextItems;

		var sel = frm.elements[this.selectsNamePrefix + i];;
		sel.style.display = "";
		
		ElementUtils.removeAllOptions(sel);

		for (var k = 0; k < items.length; k++)
			ElementUtils.addOption(sel,
				items[k].value,
				items[k].text);
				
		nextItems = null;

		for (var j = 0; j < items.length; j++)
		{
			if (items[j].value == values[i])
			{
				nextItems = items[j].subItems;
				sel.selectedIndex = j;
			}
		}
		
		i++;

	}
	
	frm.elements[this.depthFieldName].value = i;
	
	var sel = frm.elements[this.selectsNamePrefix + i];
	while (sel)
	{
		sel.style.display = "none";
		sel = frm.elements[this.selectsNamePrefix + (i++)];
	}

}

GridEditorList.prototype.compareTo = function(grid, values)
{
	var thisValues = this.getValue(grid);
	if (thisValues.length != values.length)
	{
		return false;
	}
	else
	{
		var n = thisValues.length;
		for (var i = 0; i < n; i++)
		{
			if (thisValues[i] != values[i])
			{
				return false;
			}
		}
	}
	return true;
}

GridEditorList.prototype.itemSelected = function(grid, depth)
{

	var form = document.forms[grid.formName];
	var i = 0;
	
	var items = grid.fieldTypes[this.fieldTypeName].list;
	while (i <= depth)
	{
		items = items[form.elements[this.selectsNamePrefix + i].selectedIndex].subItems;
		i++;
	}
	
	var sel = form.elements[this.selectsNamePrefix + i];

	while (items.length > 0 && sel)
	{
		
		sel.style.display = "";
		
		ElementUtils.removeAllOptions(sel);
		
		for (var j = 0; j < items.length; j++)
			ElementUtils.addOption(sel,
				items[j].value,
				items[j].text);
		
		i++;
		sel = form.elements[this.selectsNamePrefix + i];
		
		if (items[0].subItems.length > 0)
		{
			items = items[0].subItems;	
		}
		else
		{
			break;
		}

	}
	
	form.elements[this.depthFieldName].value = i;

	while (sel)
	{
		sel.style.display = "none";
		sel = form.elements[this.selectsNamePrefix + (++i)];
	}

}
