var CheckboxListGlobals = 
{

	checkboxLists: new Array(),

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
	},
	
	changeStateAll: function(checkboxListId, state)
	{
		var checkboxList = CheckboxListGlobals.checkboxLists[checkboxListId];
		if (checkboxList)
		{
			for (var i = 0; i < checkboxList.items.length; i++)
			{
				checkboxList.items[i].setStateRecursively(state);
			}
		}
	},

	checkAll: function(checkboxListId)
	{
		CheckboxListGlobals.changeStateAll(checkboxListId, true);
	},
	
	uncheckAll: function(checkboxListId)
	{
		CheckboxListGlobals.changeStateAll(checkboxListId, false);
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

SelectItem.prototype.setState = function(state)
{
	document.getElementById(this.htmlElementId).checked = state;
}

SelectItem.prototype.setStateRecursively = function(state)
{
	this.setState(state);
	if (this.subItems)
	{
		for (var i = 0; i < this.subItems.length; i++)
		{
			this.subItems[i].setStateRecursively(state);
		}
	}
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

