var CheckboxListGlobals = 
{

	initItems: function(items)
	{
		var lookupTable = new Array();
	},
	
	initItemsRecursive: function(lookupTable, items)
	{
		for (var i = 0; i < items.length; i++)
		{
			var item = items[i];
			lookupTable[item.value] = item;
			if (item.subItems.length > 0)
			{
				CheckboxListGlobals.initItems(lookupTable, item.subItems);
			}
		}
	}

}

function SelectItem(id, subItems, htmlElementId)
{

	this.id = id;
	this.subItems = subItems;
	this.htmlElementId = htmlElementId;
	
	for (var i = 0; i < subItems.length; i++)
		subItems[i].parentItem = this;
	
}
