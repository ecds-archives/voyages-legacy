var CheckboxListExpandableGlobals = 
{

	registerCheckboxList: function(checkboxList)
	{
		CheckboxListGlobals.checkboxLists[checkboxList.checkboxListId] = checkboxList;
	},
	
	collexp: function(checkboxListId, value, imageElementId, subitemsElementId)
	{
		var checkboxList = CheckboxListGlobals.checkboxLists[checkboxListId];
		if (checkboxList) checkboxList.collexp(value, imageElementId, subitemsElementId);
	}

}

function CheckboxListExpandable(
	checkboxListId,
	formName,
	expandedValuesFieldName,
	expandedImageUrl,
	collapsedImageUrl,
	items)
{
	this.checkboxListId = checkboxListId;
	this.formName = formName;
	this.expandedValuesFieldName = expandedValuesFieldName;
	this.expandedImageUrl = expandedImageUrl;
	this.collapsedImageUrl = collapsedImageUrl;
	this.items = items;
}

CheckboxListExpandable.prototype.precomputeExpandedValues = function()
{
	if (!this.expandedValues)
	{
		var frm = document.forms[this.formName];
		var expandedValuesField = frm.elements[this.expandedValuesFieldName];
		var expandedValues = expandedValuesField.value.split(",");
		this.expandedValues = new Array();
		for (var i = 0; i < expandedValues.length; i++)
		{
			this.expandedValues[expandedValues[i]] = true;
		}
	}
}

CheckboxListExpandable.prototype.saveExpandedValues = function()
{
	var expandedValues = new Array();
	for (value in this.expandedValues)
	{
		if (this.expandedValues[value])
		{
			expandedValues.push(value);
		}
	}
	var frm = document.forms[this.formName];
	var expandedValuesField = frm.elements[this.expandedValuesFieldName];
	expandedValuesField.value = expandedValues.join(",");
}

CheckboxListExpandable.prototype.collexp = function(value, imageElementId, subitemsElementId)
{

	var img = document.getElementById(imageElementId);
	var el = document.getElementById(subitemsElementId);
	
	this.precomputeExpandedValues();
	
	if (this.expandedValues[value])
	{
		this.expandedValues[value] = false;
		img.src = this.collapsedImageUrl;
		el.style.display = "none";
	}
	else
	{
		this.expandedValues[value] = true;
		img.src = this.expandedImageUrl;
		el.style.display = "";
	}
	
	this.saveExpandedValues();

}