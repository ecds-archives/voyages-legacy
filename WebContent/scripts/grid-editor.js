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
	}

}

/********************************************
* constructors
*********************************************/

function GridEditor(gridEditorId, formName, fields, extensions)
{
	this.gridEditorId = gridEditorId;
	this.formName = formName;
	this.fields = fields;
	this.extensions = extensions;
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

function GridEditorList(sharedListName, selectsNamePrefix, depthFieldName)
{
	this.sharedListName = sharedListName;
	this.selectsNamePrefix = selectsNamePrefix;
	this.depthFieldName = depthFieldName;
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

/********************************************
* GridEditorList object
*********************************************/

GridEditorList.prototype.itemSelected = function(gridEditor, depth)
{

	var form = document.forms[gridEditor.formName];
	var i = 0;
	
	var items = gridEditor.extensions[this.sharedListName];
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

	while (i < this.selectsNames.length)
	{
		var sel = form.elements[this.selectsNames[i]];
		sel.style.display = "none";
		i++;
	}

}
