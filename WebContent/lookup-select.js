var LookupSelectGlobals =
{

	lookupSelects: Array(),

	registerLookup: function(lookupSelect)
	{
		this.lookupSelects[lookupSelect.lookupSelectId] = lookupSelect;
	},
	
	removeSelectedItems: function(lookupSelectId)
	{
		var lookupSelect = this.lookupSelects[lookupSelectId];
		if (lookupSelect) lookupSelect.removeSelectedItems();
	},
	
	openLookupWindow: function(lookupSelectId)
	{
		var lookupSelect = this.lookupSelects[lookupSelectId];
		if (lookupSelect) lookupSelect.openLookupWindow();
	},
	
	addItems: function(items)
	{
		var lookupSelect = this.lookupSelects[lookupSelectId];
		if (lookupSelect) lookupSelect.addItems(items);
	}

}

function LookupSelect(lookupSelectId, formName, sourceId, selectFieldName, selectedValuesFieldName)
{
	this.lookupSelectId = lookupSelectId;
	this.formName = formName;
	this.sourceId = sourceId;
	this.selectFieldName = selectFieldName;
	this.selectedValuesFieldName = selectedValuesFieldName;
	EventAttacher.attachOnWindowEvent("load", this, "init");
}

LookupSelect.prototype.init = function()
{
	var form = document.forms[this.formName];
	this.select = form.elements[this.selectName];
	this.selectedValues = form.elements[this.selectedValuesFieldName];
}

LookupSelect.prototype.removeSelectedItems = function()
{
	var options = this.select.options;
	for(var i=0; i<options.length; i++)
	{
		var option = options[i];
		if (option.selected)
		{
			this.select.remove(i);
			i--;
		}
	}
	this.refreshSelectedItemsField();
}

LookupSelect.prototype.openLookupWindow = function()
{
	window.open(
		"lookup.faces?sourceId=" + this.sourceId + "&lookupSelectId=" + this.lookupSelectId,
		"lookup" + lookupSelectId);
}

LookupSelect.prototype.addItems = function(items)
{
	for(var i=0; i<items.length; i++)
	{
		var option = document.createElement("option");
		option.text = item[i].text;
		option.value = item[i].value;
		this.select.add(option, null);
	}
	this.refreshSelectedItemsField();
}

LookupSelect.prototype.refreshSelectedItemsField = function()
{
	var ids = new Array();
	var options = this.select.options;
	for(var i=0; i<options.length; i++)
	{
		var option = options[i];
		ids.push(option.value);
	}
	this.selectedValues.value = ids.join(",");
}