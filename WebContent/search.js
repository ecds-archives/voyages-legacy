var selectedConditionsCount = 0;

window.onload = function()
{
	var frm = document.forms["search"];
	var fieldsSelect = frm.elements["fields"];
	for (var i=0; i<fields.length; i++)
	{
		var field = fields[i];
		var fieldOption = document.createElement("option");
		fieldsSelect.options.add(fieldOption);
		fieldOption.text =
			field.label +
			" (" + field.type + ": " + getLabelForType(field.type) + ")";
	}
}

function getLabelForType(type)
{
	switch(type)
	{
		case 0: return "Integer";
		case 1: return "Integer";
		case 5: return "Decimal number";
		case 3: return "Date";
		case 2: return "String";
		case 4: return "Choice";
	}
	return "";
}

function addConditionFrame(field, conditionIndex)
{

	var conditionsTable = document.getElementById("conditionsTable");
	
	var conditionTr = conditionsTable.insertRow(conditionIndex);
	conditionTr.className = "";
	
	var conditionTd = conditionTr.insertCell(0);
	conditionTd.className = "";

	var removeConditionTd = conditionTr.insertCell(1);
	removeConditionTd.className = "";
	
	var removeConditionBtn = document.createElement("input");
	removeConditionBtn.type = "button";
	removeConditionBtn["conditionIndex"] = conditionIndex;
	removeConditionBtn.onclick = removeCondition;
	removeConditionBtn.value = "Remove";
	removeConditionTd.appendChild(removeConditionBtn);
	
	var nameHidden = document.createElement("input");
	nameHidden.type = "text";
	nameHidden.name = "selected-conditions";
	nameHidden.value = field.name;
	removeConditionTd.appendChild(nameHidden);

	return conditionTd;

}

function getConditionFrame(conditionIndex)
{
	var conditionsTable = document.getElementById("conditionsTable");
	return conditionsTable.rows[conditionIndex];
}

function addConditionString(field, frame, conditionIndex)
{

	var tbl = document.createElement("table");
	frame.appendChild(tbl);

	var tr = tbl.insertRow(0);
	var labelTd = tr.insertCell(0);
	var textboxTd = tr.insertCell(1);
	
	labelTd.innerHTML = field.label;
	
	var textbox = document.createElement("input");
	textbox.type = "text";
	textbox.name = field.name;
	textboxTd.appendChild(textbox);

}

function addConditionRange(field, frame, conditionIndex)
{

	var tbl = document.createElement("table");
	frame.appendChild(tbl);

	var tr = tbl.insertRow(0);
	var labelTd = tr.insertCell(0);
	var typeTd = tr.insertCell(1);
	var rsTextboxTd = tr.insertCell(2);
	var rangeMarkTd = tr.insertCell(3);
	var reTextboxTd = tr.insertCell(4);
	var leTextboxTd = tr.insertCell(5);
	var geTextboxTd = tr.insertCell(6);
	var eqTextboxTd = tr.insertCell(7);
	
	labelTd.innerHTML = field.label;
	
	var typeSelect = document.createElement("select");
	typeSelect.name = "";
	typeSelect["conditionTable"] = tbl;
	typeSelect["conditionIndex"] = conditionIndex;
	typeSelect.onchange = rangeTypeChanged;
	typeTd.appendChild(typeSelect);

	var typeRangeOption = document.createElement("option");
	typeSelect.options.add(typeRangeOption);
	typeRangeOption.text = "is between";
	typeRangeOption.value = "range";
	
	var typeLeOption = document.createElement("option");
	typeSelect.options.add(typeLeOption);
	typeLeOption.text = "is less or equal than";
	typeLeOption.value = "le";

	var typeGeOption = document.createElement("option");
	typeSelect.options.add(typeGeOption);
	typeGeOption.text = "is greater or equal than";
	typeGeOption.value = "ge";

	var typeEqOption = document.createElement("option");
	typeSelect.options.add(typeEqOption);
	typeEqOption.text = "is equal to";
	typeEqOption.value = "eq";
	
	var rsTextbox = document.createElement("input");
	rsTextbox.type = "text";
	rsTextbox.name = field.name + "-rs";
	rsTextboxTd.appendChild(rsTextbox);
	
	rangeMarkTd.innerHTML = "&nbsp;-&nbsp;";

	var reTextbox = document.createElement("input");
	reTextbox.type = "text";
	reTextbox.name = field.name + "-re";
	reTextboxTd.appendChild(reTextbox);

	var leTextbox = document.createElement("input");
	leTextbox.type = "text";
	leTextbox.name = field.name + "-le";
	leTextboxTd.appendChild(leTextbox);

	var geTextbox = document.createElement("input");
	geTextbox.type = "text";
	geTextbox.name = field.name + "-ge";
	geTextboxTd.appendChild(geTextbox);

	var eqTextbox = document.createElement("input");
	eqTextbox.type = "text";
	eqTextbox.name = field.name + "-eq";
	eqTextboxTd.appendChild(eqTextbox);
	
	setRangeType(tbl, 0, true);

}

function setRangeType(tbl, type, changeSelect)
{

	var tr = tbl.rows[0];

	if (changeSelect)
	{
		var typeSelect = tr.cells[1].childNodes[0];
		typeSelect.selectedIndex = type;
	}

	tr.cells[2].style.display = type == 0 ? "table-cell" : "none";
	tr.cells[3].style.display = type == 0 ? "table-cell" : "none";
	tr.cells[4].style.display = type == 0 ? "table-cell" : "none";
	tr.cells[5].style.display = type == 1 ? "table-cell" : "none";
	tr.cells[6].style.display = type == 2 ? "table-cell" : "none";
	tr.cells[7].style.display = type == 3 ? "table-cell" : "none";

}

function rangeTypeChanged()
{
	var tbl = this["conditionTable"];
	setRangeType(tbl, this.selectedIndex, false);
}

function addConditionDictionary(field, frame, conditionIndex)
{

	var tbl = document.createElement("table");
	frame.appendChild(tbl);

	var tr = tbl.insertRow(0);
	var labelTd = tr.insertCell(0);
	var selectTd = tr.insertCell(1);
	
	labelTd.innerHTML = field.label;
	
	var dictSelect = document.createElement("select");
	dictSelect.name = field.name;
	var dict = dictionaries[field.dictionary];
	for (var i=0; i<dict.length; i++)
	{
		var dictOption = document.createElement("option");
		dictOption.value = dict[i].id;
		dictOption.text = dict[i].name;
		dictSelect.options.add(dictOption);
	}
	selectTd.appendChild(dictSelect);

}

function isConditionSelected(name)
{
	var frm = document.forms["search"];
	var selectedConditions = frm.elements["selected-conditions"];
	if (selectedConditionsCount == 1)
	{
		if (selectedConditions.value == name)
		{
			return true;
		}
	}
	else if (selectedConditionsCount > 1)
	{
		for (var i=0; i<selectedConditions.length; i++)
		{
			if (selectedConditions[i].value == name)
			{
				return true;
			}
		}
	}
	return false;
}

function addCondition()
{

	var frm = document.forms["search"];
	var fieldIndex = frm.elements["fields"].selectedIndex;
	var field = fields[fieldIndex];
	
	// is it there?
	if (isConditionSelected(field.name))
	{
		alert("This condition is already there.");
		return;
	}
	
	// create a HTML frame for it
	var fieldFrame = addConditionFrame(field, selectedConditionsCount++);

	// create controls for it	
	switch (field.type)
	{
		// integer, long, float, date
		case 0:
		case 1:
		case 5:
		case 3:
			addConditionRange(field, fieldFrame);
			break;
	
		// string 
		case 2:
			addConditionString(field, fieldFrame);
			break;

		// dictionary
		case 4:
			addConditionDictionary(field, fieldFrame);
			break;
	
	}

}

function removeCondition()
{
	var conditionsTable = document.getElementById("conditionsTable");
	var conditionsIndex = this["conditionIndex"];
	conditionsTable.deleteRow(conditionsIndex);
}