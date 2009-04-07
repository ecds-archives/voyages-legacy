var LookupCheckboxListGlobals = 
{

	lists: new Array(),
	
	registerList: function(list)
	{
		this.lists[list.listId] = list;
	},

	quickSearch: function(listId, input)
	{
		var list = LookupCheckboxListGlobals.lists[listId];
		if (list) list.quickSearch(input);
	},
	
	selectAll: function(listId)
	{
		var list = LookupCheckboxListGlobals.lists[listId];
		if (list) list.selectAll();
	},

	deselectAll: function(listId)
	{
		var list = LookupCheckboxListGlobals.lists[listId];
		if (list) list.deselectAll();
	},

	itemToggled: function(listId, input)
	{
		var list = LookupCheckboxListGlobals.lists[listId];
		if (list) list.itemToggled(input);
	},
	
	itemExpandCollapse: function(listId, fullId)
	{
		var list = LookupCheckboxListGlobals.lists[listId];
		if (list) list.itemExpandCollapse(fullId);
	}

}

function LookupCheckboxList(listId, formName, expandedIdsFieldName, idPartsSeparator, idSeparator, autoSelection, items, onChangeHandler)
{
	this.listId = listId;
	this.formName = formName;
	this.expandedIdsFieldName = expandedIdsFieldName;
	this.idPartsSeparator = idPartsSeparator;
	this.idSeparator = idSeparator;
	this.autoSelection = autoSelection;
	this.items = items;
	this.onChangeHandler = onChangeHandler;
}

LookupCheckboxList.prototype.getListItemById = function(id, offset)
{

	var ids = id.split(this.idPartsSeparator);
	var item = this.items[ids[0]];
	
	if (!offset) offset = 0;
	for (var i = 1; i < ids.length - offset; i++)
		item = item.children[ids[i]];

	return item;

}

LookupCheckboxList.prototype.prepareItem = function(item, parent)
{

	item.itemElement = document.getElementById(item.itemElementId);
	item.childrenElement = document.getElementById(item.childrenElementId);
	item.checkbox = document.getElementById(item.checkboxId);
	item.arrowElementId = document.getElementById(item.arrowElementId);
	item.textLowerCase = item.text.toLowerCase();
	item.parent = parent;
	
	if (item.children)
	{
		for (var id in item.children)
		{
			this.prepareItem(item.children[id], item);
		}
	}

}

LookupCheckboxList.prototype.prepare = function()
{

	if (this.prepared)
		return;
		
	var form = document.forms[this.formName];
	this.expandedIdsField = form.elements[this.expandedIdsFieldName];
	
	this.expandedIds = [];
	var expandedIdsArray = this.expandedIdsField.value.split(this.idSeparator);
	for (var i = 0; i < expandedIdsArray.length; i++)
		this.expandedIds[expandedIdsArray[i]] = true;
		
	for (var id in this.items)
		this.prepareItem(this.items[id], null);

	this.prepared = true;

}

LookupCheckboxList.prototype.restoreDefaultTreeState = function(item)
{

	item.itemElement.style.display = "";

	if (item.children)
	{
	
		for (var id in item.children)
			this.restoreDefaultTreeState(item.children[id]);
			
		item.childrenElement.style.display =
			item.expanded ? "" : "none";
			
	}

}

LookupCheckboxList.prototype.quickSearchTree = function(item, searchFor)
{

	var directMatch = item.textLowerCase.indexOf(searchFor) != -1;
	
	var subtreeMatches = 0;
	if (item.children)
		for (var id in item.children)
			if (this.quickSearchTree(item.children[id], searchFor))
				subtreeMatches ++;
	
	item.itemElement.style.display =
		(directMatch || subtreeMatches > 0) ? "" : "none";
	
	if (item.children)
		item.childrenElement.style.display =
			(subtreeMatches > 0) ? "" : "none";
	
	return directMatch || subtreeMatches > 0;
}

LookupCheckboxList.prototype.quickSearch = function(input)
{

	this.prepare();

	var searchFor = input.value.toLowerCase();
	this.searchFor = searchFor;
	
	if (searchFor == "")
	{
		for (var id in this.items)
			this.restoreDefaultTreeState(this.items[id]);
	}
	else
	{
		for (var id in this.items)
			this.quickSearchTree(this.items[id], searchFor);
	}

}

LookupCheckboxList.prototype.selectAll = function()
{
	this.changeSelectionAll(true);
}

LookupCheckboxList.prototype.deselectAll = function()
{
	this.changeSelectionAll(false);
}

LookupCheckboxList.prototype.itemChangeSelectionAll = function(item, state, force)
{

	var match = !this.searchFor || item.textLowerCase.indexOf(this.searchFor) != -1;

	if (this.autoSelection)
	{
		if (force || match)
		{
			item.checkbox.checked = state;
			force = true;
		}
	}
	else
	{
		if (match)
		{
			item.checkbox.checked = state;
		}
	}

	if (item.children)
		for (var id in item.children)
			this.itemChangeSelectionAll(item.children[id], state, force);

}

LookupCheckboxList.prototype.changeSelectionAll = function(state)
{

	this.prepare();

	for (var id in this.items)
		this.itemChangeSelectionAll(this.items[id], state, false);
		
	if (this.onChangeHandler)
		this.onChangeHandler();

}

LookupCheckboxList.prototype.selectItem = function(item, state)
{
	item.checkbox.checked = state;
	if (item.children)
	{
		for (var id in item.children)
		{
			this.selectItem(item.children[id], state);
		}
	}
}

LookupCheckboxList.prototype.itemToggled = function(input)
{

	if (this.autoSelection)
	{
		this.prepare();
		
		var item = this.getListItemById(input.value, 0);
		this.selectItem(item, item.checkbox.checked);
		
		var parent = item.parent;
		while (parent)
		{
			var allSelected = true;
			for (var id in parent.children)
			{
				var child = parent.children[id];
				if (!child.checkbox.checked)
				{
					allSelected = false;
					break;
				}
			}
			if (allSelected)
			{
				parent.checkbox.checked = true;
				parent = parent.parent;
			}
			else
			{
				parent.checkbox.checked = false;
				break;
			}
		}
	}
	
	if (this.onChangeHandler)
		this.onChangeHandler();
	
}

LookupCheckboxList.prototype.itemExpandCollapse = function(fullId)
{

	this.prepare();
	var item = this.getListItemById(fullId, 0);

	if (item.expanded)
	{
		item.expanded = false;
		item.childrenElement.style.display = "none";
		item.arrowElementId.className = "lookup-checkbox-list-item-collapsed";
	}
	else
	{
		item.expanded = true;
		item.childrenElement.style.display = "";
		item.arrowElementId.className = "lookup-checkbox-list-item-expanded";
	}

	this.expandedIds[fullId] = item.expanded;
	var expandedIdsArray = [];
	
	for (var id in this.expandedIds)
	{
		if (this.expandedIds[id])
		{
			expandedIdsArray.push(id);
		}
	}
	
	this.expandedIdsField.value = expandedIdsArray.join(this.idSeparator);

}