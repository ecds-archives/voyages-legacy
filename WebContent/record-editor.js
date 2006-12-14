var RecordEditorGlobals =
{

	editors: Array(),
	
	registerEditor: function(editor)
	{
		RecordEditorGlobals.editors[editor.editorId] = editor;
	}

}

function RecordEditor(editorId, formName, lists)
{
	this.editorId = editorId;
	this.formName = formName;
	this.lists = lists;
}

function ListItem(value, parentValue, Text)
{
	this.value = value;
	this.parentValue = parentValue;
	this.text = Text;
}