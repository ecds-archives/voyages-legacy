var RecordEditorGlobals =
{

	editors: Array(),
	
	registerEditor: function(editor)
	{
		RecordEditorGlobals.editors[editor.editorId] = editor;
	},
	
	dropdownValueChanged: function(editorId, name, index)
	{
		var editor = RecordEditorGlobals.editors[editorId];
		if (editor)
		{
			var dropdowns = editor.fieldsById[name];
			if (dropdowns)
			{
				dropdowns.dropdownValueChanged(index);
			}
		}
	}

}

function RecordEditor(editorId, formName, lists, fields)
{

	this.editorId = editorId;
	this.formName = formName;
	this.lists = lists;
	this.fields = fields;
	
	this.listsById = new Array();
	for (var i = 0; i < this.lists.length; i++)
	{
		this.listsById[this.lists[i].listId] = lists[i];
	}

	this.fieldsById = new Array();
	for (var i = 0; i < this.fields.length; i++)
	{
		this.fields[i].init(this);
		this.fieldsById[this.fields[i].name] = fields[i];
	}
	
}

function RecordEditorList(listId, items)
{
	this.listId = listId;
	this.items = items;
}

function ListItem(value, parentValue, Text)
{
	this.value = value;
	this.parentValue = parentValue;
	this.text = Text;
}

/////////////////////////////////////////////////////////////

function RecordEditorTexbox(name)
{
	this.name = name;
}

RecordEditorTexbox.prototype.init = function(editor)
{
	this.editor = editor;
}

/////////////////////////////////////////////////////////////

function RecordEditorDate(name)
{
	this.name = name;
}

RecordEditorDate.prototype.init = function(editor)
{
	this.editor = editor;
}

/////////////////////////////////////////////////////////////

function RecordEditorCheckbox(name)
{
	this.name = name;
}

RecordEditorCheckbox.prototype.init = function(editor)
{
	this.editor = editor;
}

/////////////////////////////////////////////////////////////

function RecordEditorDropdowns(name, selectNames, listIds)
{
	this.name = name;
	this.count = selectNames.length;
	this.selectNames = selectNames;
	this.listIds = listIds;
}

RecordEditorDropdowns.prototype.init = function(editor)
{

	this.editor = editor;

	this.lists = new Array();
	for (var i = 0; i < this.listIds; i++)
		this.lists.push(
			editor.listsById[this.listIds[i]]);

}

RecordEditorDropdowns.prototype.dropdownValueChanged = function(index)
{

	var frm = document.forms[this.editor.formName];
	var sel = frm.elements[this.selectNames[index]];
	var parentValue = sel.value;
	
	for (var i = index+1; i < this.count; i++)
	{
	
		sel = frm.elements[this.selectNames[i]];
		ElementUtils.deleteAllChildren(sel);
		
		var list = this.editor.listsById[this.listIds[i]];
		var nextParentValue = null;
		var itemsCount = 0;
		
		for (var j = 0; j < list.items.length; j++)
		{
			var listItem = list.items[j];
			if (listItem.parentValue == parentValue || listItem.parentValue == null)
			{
				if (itemsCount == 0) nextParentValue = listItem.value;
				ElementUtils.addOption(sel, listItem.value, listItem.text);
				itemsCount++;
			}
		}
		
		parentValue = nextParentValue;

	}
	
}