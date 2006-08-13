/*

	NUMERIC:
	
	typeFieldName,
	fromId,
	dashId,
	toId,
	leId,
	geId,
	eqId

	DATE:
	
	typeFieldName,
	tdFromMonthId,
	tdSlashBetweenStartId,
	tdFromYearId,
	tdDashId,
	tdToMonthId,
	tdSlashBetweenEndId,
	tdToYearId,
	tdLeMonthId,
	tdSlashLeId,
	tdLeYearId,
	tdGeMonthId,
	tdSlashGeId,
	tdGeYearId,
	tdEqMonthId,
	tdSlashEqId,
	tdEqYearId,
	monthFieldName,
	monthTdId,
	monthsTds
	
	LIST:
	

*/

var QueryBuilderGlobals = 
{

	builders: new Array(),
	
	registerBuilder: function(builder)
	{
		this.builders[builder.builderId] = builder;
	},

	updateTotal: function(builderId, delay)
	{
		var builder = this.builders[builderId];
		if (builder) builder.updateTotal(delay);
	},
	
	moveConditionUp: function(builderId, attributeId)
	{
		var builder = this.builders[builderId];
		if (builder) builder.moveConditionUp(attributeId);
	},
	
	moveConditionDown: function(builderId, attributeId)
	{
		var builder = this.builders[builderId];
		if (builder) builder.moveConditionDown(attributeId);
	},
	
	deleteCondition: function(builderId, attributeId)
	{
		var builder = this.builders[builderId];
		if (builder) builder.deleteCondition(attributeId);
	},
	
	changeNumericRangeType: function(builderId, attributeId)
	{
		var builder = this.builders[builderId];
		if (builder) builder.changeNumericRangeType(attributeId);
	},
	
	changeDateRangeType: function(builderId, attributeId)
	{
		var builder = this.builders[builderId];
		if (builder) builder.changeDateRangeType(attributeId);
	},
	
	toggleMonth: function(builderId, attributeId, month)
	{
		var builder = this.builders[builderId];
		if (builder) builder.toggleMonth(attributeId, month);
	},
	
	openList: function(builderId, attributeId)
	{
		var builder = this.builders[builderId];
		if (builder) builder.openList(attributeId);
	},
	
	closeList: function(builderId, attributeId)
	{
		var builder = this.builders[builderId];
		if (builder) builder.closeList(attributeId);
	},
	
	quickSearchList: function(builderId, attributeId, input)
	{
		var builder = this.builders[builderId];
		if (builder) builder.quickSearchList(attributeId, input);
	},
	
	listSelectAll: function(builderId, attributeId)
	{
		var builder = this.builders[builderId];
		if (builder) builder.listSelectAll(attributeId);
	},

	listDeselectAll: function(builderId, attributeId)
	{
		var builder = this.builders[builderId];
		if (builder) builder.listDeselectAll(attributeId);
	},

	listItemToggled: function(builderId, attributeId, input)
	{
		var builder = this.builders[builderId];
		if (builder) builder.listItemToggled(attributeId, input);
	}

}

function QueryBuilder(builderId, formName, attributesFieldName, updateTotalFieldName, conditions)
{
	this.formName = formName;
	this.builderId = builderId;
	this.updateTotalFieldName = updateTotalFieldName;
	this.conditions = conditions;
	this.attributesFieldName = attributesFieldName;
	this.timeoutId = "";
}

QueryBuilder.prototype.updateTotal = function(delay)
{

	if (!ajaxAnywhere) return;
	
	// set to true when submit
	// so that the component can fire an event
	var flagField = document.forms[this.formName].elements[this.updateTotalFieldName];
	
	// delay
	if (delay > 0)
		this.timeoutId = Timer.extendCall(
			this.timeoutId,
			this, "doUpdateExpectedTotal",
			delay);
	
	// immediate
	else
		this.doUpdateExpectedTotal();
		
}

QueryBuilder.prototype.doUpdateExpectedTotal = function()
{
	document.forms[this.formName].elements[this.updateTotalFieldName].value = "true";
	ajaxAnywhere.submitAJAX(null, null);
}

QueryBuilder.prototype.moveConditionUp = function(attributeId)
{
	var cond = document.getElementById(this.conditions[attributeId].conditionDivId);
	var attrListField = document.forms[this.formName].elements[this.attributesFieldName];
	var attrs = attrListField.value.split(',');
	for (var i=0; i<attrs.length; i++)
	{
		if (attrs[i] == attributeId)
		{
			if (i != 0)
			{
				var prevCond = cond.previousSibling;
				var parent = cond.parentNode;
				parent.removeChild(cond);
				parent.insertBefore(cond, prevCond);
				attrs[i] = attrs[i-1];
				attrs[i-1] = attributeId;
				attrListField.value = attrs.join(',');
				if (Scriptaculous)
				{
					Element.setOpacity(cond, 0);
					Effect.Appear(cond, {duration: 0.5});
				}
			}
			return;
		}
	}
}

QueryBuilder.prototype.moveConditionDown = function(attributeId)
{
	var cond = document.getElementById(this.conditions[attributeId].conditionDivId);
	var attrListField = document.forms[this.formName].elements[this.attributesFieldName];
	var attrs = attrListField.value.split(',');
	for (var i=0; i<attrs.length; i++)
	{
		if (attrs[i] == attributeId)
		{
			if (i != attrs.length-1)
			{
				var nextNextCond = cond.nextSibling.nextSibling;
				var parent = cond.parentNode;
				parent.removeChild(cond);
				parent.insertBefore(cond, nextNextCond);
				attrs[i] = attrs[i+1];
				attrs[i+1] = attributeId;
				attrListField.value = attrs.join(',');
				if (Scriptaculous)
				{
					Element.setOpacity(cond, 0);
					Effect.Appear(cond, {duration: 0.5});
				}
			}
			return;
		}
	}
}

QueryBuilder.prototype.deleteCondition = function(attributeId)
{

	var cond = document.getElementById(this.conditions[attributeId].conditionDivId);
	var attrListField = document.forms[this.formName].elements[this.attributesFieldName];
	var attrs = attrListField.value.split(',');
	for (var i=0; i<attrs.length; i++) 
	{
		if (attrs[i] == attributeId)
		{
			attrs.splice(i, 1);
			delete this.conditions[attributeId];
			attrListField.value = attrs.join(',');
			if (Scriptaculous)
			{
				new Effect.Opacity(cond,
				{
					from: 1.0, to: 0.0, duration: 0.5,
					afterFinishInternal: function(effect)
					{
						effect.element.parentNode.removeChild(effect.element);
					}
				});
			}
			else
			{
				cond.parentNode.removeChild(cond);
			}
			this.updateTotal(0);
			return;
		}
	}	
}

QueryBuilder.prototype.changeNumericRangeType = function(attributeId)
{
	var cond = this.conditions[attributeId];
	var type = document.forms[this.formName].elements[cond.typeFieldName].selectedIndex;
	document.getElementById(cond.fromId).style.display = (type == 0) ? '' : 'none';
	document.getElementById(cond.dashId).style.display = (type == 0) ? '' : 'none';
	document.getElementById(cond.toId).style.display = (type == 0) ? '' : 'none';
	document.getElementById(cond.leId).style.display = (type == 1) ? '' : 'none';
	document.getElementById(cond.geId).style.display = (type == 2) ? '' : 'none';
	document.getElementById(cond.eqId).style.display = (type == 3) ? '' : 'none';
	this.updateTotal(0);
}

QueryBuilder.prototype.changeDateRangeType = function(attributeId)
{
	var cond = this.conditions[attributeId];
	var type = document.forms[this.formName].elements[cond.typeFieldName].selectedIndex;
	document.getElementById(cond.tdFromMonthId).style.display = (type == 0) ? '' : 'none';
	document.getElementById(cond.tdSlashBetweenStartId).style.display = (type == 0) ? '' : 'none';
	document.getElementById(cond.tdFromYearId).style.display = (type == 0) ? '' : 'none';
	document.getElementById(cond.tdDashId).style.display = (type == 0) ? '' : 'none';
	document.getElementById(cond.tdToMonthId).style.display = (type == 0) ? '' : 'none';
	document.getElementById(cond.tdSlashBetweenEndId).style.display = (type == 0) ? '' : 'none';
	document.getElementById(cond.tdToYearId).style.display = (type == 0) ? '' : 'none';
	document.getElementById(cond.tdLeMonthId).style.display = (type == 1) ? '' : 'none';
	document.getElementById(cond.tdSlashLeId).style.display = (type == 1) ? '' : 'none';
	document.getElementById(cond.tdLeYearId).style.display = (type == 1) ? '' : 'none';
	document.getElementById(cond.tdGeMonthId).style.display = (type == 2) ? '' : 'none';
	document.getElementById(cond.tdSlashGeId).style.display = (type == 2) ? '' : 'none';
	document.getElementById(cond.tdGeYearId).style.display = (type == 2) ? '' : 'none';
	document.getElementById(cond.tdEqMonthId).style.display = (type == 3) ? '' : 'none';
	document.getElementById(cond.tdSlashEqId).style.display = (type == 3) ? '' : 'none';
	document.getElementById(cond.tdEqYearId).style.display = (type == 3) ? '' : 'none';
	this.updateTotal(0);
}

QueryBuilder.prototype.toggleMonth = function(attributeId, month)
{
	var cond = this.conditions[attributeId];
	var monthInput = document.forms[this.formName].elements[this.monthFieldName];
	var monthTd = document.getElementById(cond.monthsTds[month]);
	if (monthInput.value == "true")
	{
		monthInput.value = "false";
		monthTd.className = 'query-builder-range-month-delected';
	}
	else
	{
		monthInput.value = "true";
		monthTd.className = 'query-builder-range-month-selected';
	}
	this.updateTotal(0);
}

QueryBuilder.prototype.getListItemById = function(cond, id, offset)
{
	var ids = id.split(cond.idSeparator);
	var item = cond.items[ids[0]];
	
	if (!offset) offset = 0;
	for (var i = 1; i < ids.length - offset; i++)
		item = item.children[ids[i]];

	return item;
}

QueryBuilder.prototype.openList = function(attributeId)
{
	var cond = this.conditions[attributeId];
	document.getElementById(cond.showElementId).style.display = "none";
	document.getElementById(cond.editElementId).style.display = "block";
	document.forms[this.formName].elements[cond.stateFieldName].value = "edit";
}

QueryBuilder.prototype.closeList = function(attributeId)
{
	var cond = this.conditions[attributeId];
	document.getElementById(cond.showElementId).style.display = "block";
	document.getElementById(cond.editElementId).style.display = "none";
	document.forms[this.formName].elements[cond.stateFieldName].value = "show";
	
	var names = new Array();
	var allInputs = document.forms[this.formName].getElementsByTagName("input");
	for (var i=0; i<allInputs.length; i++)
	{
		var input = allInputs[i];
		if (input.type == "checkbox" && input.name == cond.itemsField && input.checked)
		{
			names.push(cond.items[input.value].text);
		}
	}
	
	var displayDiv = document.getElementById(cond.showListElementId);
	if (names.length > 0)
	{
		displayDiv.innerHTML = names.join(", ");
	}
	else
	{
		displayDiv.innerHTML = "[<i>nothing selected</i>]";
	}
}

QueryBuilder.prototype.prepareListItem = function(item, parent)
{

	item.itemElement = document.getElementById(item.itemElementId);
	item.childrenElement = document.getElementById(item.childrenElementId);
	item.checkbox = document.getElementById(item.checkboxId);
	item.textLowerCase = item.text.toLowerCase();
	item.parent = parent;
	
	if (item.children)
	{
		for (var id in item.children)
		{
			this.prepareListItem(item.children[id], item);
		}
	}

}

QueryBuilder.prototype.prepareList = function(cond)
{
	if (cond.listPrepared) return;
	for (var id in cond.items)
	{
		this.prepareListItem(cond.items[id], null);
	}
	cond.listPrepared = true;
}

QueryBuilder.prototype.quickSearchTree = function(item, searchFor)
{

	var directMatch = item.textLowerCase.indexOf(searchFor) != -1;
	
	var subtreeMatches = 0;
	if (item.children)
	{
		for (var id in item.children)
		{
			if (this.quickSearchTree(item.children[id], searchFor))
				subtreeMatches ++;
		}
	}
	
	item.itemElement.style.display = (directMatch || subtreeMatches > 0) ? "" : "none";
	item.childrenElement.style.display = (subtreeMatches > 0) ? "" : "none";
	
	return directMatch || subtreeMatches > 0;

}

QueryBuilder.prototype.quickSearchList = function(attributeId, input)
{
	var cond = this.conditions[attributeId];
	this.prepareList(cond);

	var searchFor = input.value.toLowerCase();

	for (var id in cond.items)
	{
		var item = cond.items[id];
		var match = item.textLowerCase.indexOf(searchFor) != -1;
		item.element.style.display = match ? "" : "none";
	}

}

QueryBuilder.prototype.listSelectAll = function(attributeId)
{
	this.listChangeSelectionAll(attributeId, true);
}

QueryBuilder.prototype.listDeselectAll = function(attributeId)
{
	this.listChangeSelectionAll(attributeId, false);
}

QueryBuilder.prototype.listChangeSelectionAll = function(attributeId, state)
{
	var cond = this.conditions[attributeId];
	this.prepareList(cond);

	for (var id in cond.items)
	{
		var item = cond.items[id];
		if (item.element.style.display != "none")
		{
			item.checkbox.checked = state;
		}
	}
	
	this.updateTotal(0);

}

QueryBuilder.prototype.listSelectItem = function(item, state)
{
	listSelectItem.checkbox.checked = state;
	if (item.children)
	{
		for (var i = 0; i < item.children.length; i++)
		{
			this.listSelectItem(item.children[i], state);
		}
	}
}

QueryBuilder.prototype.listItemToggled = function(attributeId, input)
{
	var cond = this.conditions[attributeId];
	if (!cond.selectChildren) false;
	this.prepareList(cond);
	
	var item = this.getListItemById(cond, input.value, 0);
	this.listSelectItem(item, item.checkbox.checked);
	
	var parent = item.parent;
	while (parent)
	{
		var allSelected = true;
		var allDeselected = true;
		for (var i = 0; i < parent.children.length; i++)
		{
			var child = parent.children[i];
			if (!child.checkbox.checked) allSelected = false; else allDeselected = false;
			if (!allSelected && !allDeselected) break;
		}
		if (allSelected)
		{
			parent.checkbox.checked = true;
		}
		else if (allDeselected)
		{
			parent.checkbox.checked = false;
		}
		else
		{
			break;
		}
		parent = parent.parent;
	}
	
}