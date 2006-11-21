var CheckboxListGlobals = 
{

	initItems: function(items)
	{
		var lookupTable = new Array();
		CheckboxListGlobals.initItemsRecursive(lookupTable, items);
		return lookupTable;
	},
	
	initItemsRecursive: function(lookupTable, items)
	{
		for (var i = 0; i < items.length; i++)
		{
			var item = items[i];
			lookupTable[item.value] = item;
			if (item.subItems && item.subItems.length > 0)
				CheckboxListGlobals.initItemsRecursive(
					lookupTable, item.subItems);
		}
	}

}

function SelectItem(value, htmlElementId, subItems)
{

	this.value = value;
	this.subItems = subItems;
	this.htmlElementId = htmlElementId;
	
	for (var i = 0; i < subItems.length; i++)
		subItems[i].parentItem = this;
	
}

SelectItem.prototype.isChecked = function()
{
	return document.getElementById(this.htmlElementId).checked;
}

SelectItem.prototype.setState = function(checked)
{
	document.getElementById(this.htmlElementId).checked = checked;
}

SelectItem.prototype.allSubitemsChecked = function(onlyChildren)
{
	if (this.subItems)
	{
		for (var i = 0; i < this.subItems.length; i++)
		{
			var subItem = this.subItems[i];
		
			if (!subItem.isChecked())
				return false;
				
			if (!onlyChildren && !subItem.allSubitemsChecked(false))
				return false;
		}
	}
	return true;
}