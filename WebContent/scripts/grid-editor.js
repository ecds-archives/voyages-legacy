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

	listItemSelected: function(gridEditorId, rowName, columnName, depth)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (gridEditor) gridEditor.listItemSelected(rowName, columnName, depth);
	},
	
	toggleRowGroup: function(gridEditorId, rowGroupIndex)
	{
		var gridEditor = GridEditorGlobals.gridEditors[gridEditorId];
		if (gridEditor) gridEditor.toggleRowGroup(rowGroupIndex);
	}

}

/********************************************
* constructors
*********************************************/

function GridEditor(gridEditorId, formName, mainTableId, expandedGroupsFieldName, fieldTypes, fields, rowGroups)
{
	this.gridEditorId = gridEditorId;
	this.mainTableId = mainTableId;
	this.formName = formName;
	this.fieldTypes = fieldTypes;
	this.fields = fields;
	this.rowGroups = rowGroups;
	this.expandedGroupsFieldName = expandedGroupsFieldName;
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

function GridEditorTextbox()
{
}

function GridEditorDate()
{
}

function GridEditorList(fieldTypeName, selectsNamePrefix, depthFieldName)
{
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

GridEditor.prototype.getField = function(rowName, columnName)
{
	var row = this.fields[rowName];
	if (!row) return null;
	return row[columnName];
}

GridEditor.prototype.listItemSelected = function(rowName, columnName, depth)
{
	var field = this.getField(rowName, columnName);
	if (field) field.itemSelected(this, depth);
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

/********************************************
* GridEditorList object
*********************************************/

GridEditorList.prototype.itemSelected = function(gridEditor, depth)
{

	var form = document.forms[gridEditor.formName];
	var i = 0;
	
	var items = gridEditor.fieldTypes[this.fieldTypeName].list;
	while (i <= depth)
	{
		items = items[form.elements[this.selectsNamePrefix + i].selectedIndex].subItems;
		i++;
	}
	
	var sel = form.elements[this.selectsNamePrefix + i];

	while (items.length > 0 && sel)
	{
		
		sel.style.display = "";
		
		while (sel.length > 0)
			sel.remove(0);
		
		for (var j = 0; j < items.length; j++)
		{
		
			var opt = document.createElement("option");
			opt.value = items[j].value;
			opt.text = items[j].text;
		
			try
			{
			    sel.add(opt, null);
			}
			catch(ex)
			{
				sel.add(opt);
			}

		}
		
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
