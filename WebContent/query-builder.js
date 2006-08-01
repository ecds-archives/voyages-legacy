var QueryBuilder = 
{

	timeoutIds: new Array(),

	updateTotal: function(builderId, formName, updateTotalFieldName, delay)
	{
	
		if (!ajaxAnywhere) return;
		
		// set to true when submit
		// so that the component can fire an event
		var flagField = document.forms[formName].elements[updateTotalFieldName];
		
		// old timeout id
		var oldTimeoutId = QueryBuilder.timeoutIds[builderId];

		// delay
		if (delay > 0)
			QueryBuilder.timeoutIds[builderId] = Timer.extendCall(
				oldTimeoutId,
				QueryBuilder, "doUpdateExpectedTotal",
				delay, flagField);
		
		// immediate
		else
			QueryBuilder.doUpdateExpectedTotal(flagField);
			
	},
	
	doUpdateExpectedTotal: function(flagField)
	{
		flagField.value = "true";
		ajaxAnywhere.submitAJAX(null, null);
	},
	
	moveConditionUp: function(builderId, formName, updateTotalFieldName, attributesFieldName, conditionDivId, attributeId)
	{
		var cond = document.getElementById(conditionDivId);
		var attrListField = document.forms[formName].elements[attributesFieldName];
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
	},
	
	moveConditionDown: function(builderId, formName, updateTotalFieldName, attributesFieldName, conditionDivId, attributeId)
	{
		var cond = document.getElementById(conditionDivId);
		var attrListField = document.forms[formName].elements[attributesFieldName];
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
	},
	
	deleteCondition: function(builderId, formName, updateTotalFieldName, attributesFieldName, conditionDivId, attributeId)
	{

		var cond = document.getElementById(conditionDivId);
		var attrListField = document.forms[formName].elements[attributesFieldName];
		var attrs = attrListField.value.split(',');
		for (var i=0; i<attrs.length; i++) 
		{
			if (attrs[i] == attributeId)
			{
				attrs.splice(i, 1);
				attrListField.value = attrs.join(',');
				if (Scriptaculous)
				{
					new Effect.Opacity(cond,
					{
						from: 1.0, to: 0.0, duration: 0.5,
						afterFinishInternal: function(effect) {effect.element.parentNode.removeChild(effect.element);}
					});
				}
				else
				{
					cond.parentNode.removeChild(cond);
				}
				QueryBuilder.updateTotal(builderId, formName, updateTotalFieldName, 0);
				return;
			}
		}	
	},
	
	changeNumericRangeType: function(
		builderId,
		formName,
		updateTotalFieldName,
		typeFieldName,
		fromId,
		dashId,
		toId,
		leId,
		geId,
		eqId)
	{
		var type = document.forms[formName].elements[typeFieldName].selectedIndex;
		document.getElementById(fromId).style.display = (type == 0) ? '' : 'none';
		document.getElementById(dashId).style.display = (type == 0) ? '' : 'none';
		document.getElementById(toId).style.display = (type == 0) ? '' : 'none';
		document.getElementById(leId).style.display = (type == 1) ? '' : 'none';
		document.getElementById(geId).style.display = (type == 2) ? '' : 'none';
		document.getElementById(eqId).style.display = (type == 3) ? '' : 'none';
		QueryBuilder.updateTotal(builderId, formName, updateTotalFieldName, 0);
	},
	
	changeDateRangeType: function(
		builderId,
		formName,
		updateTotalFieldName,
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
		tdEqYearId)
	{
		var type = document.forms['form'].elements[typeFieldName].selectedIndex;
		document.getElementById(tdFromMonthId).style.display = (type == 0) ? '' : 'none';
		document.getElementById(tdSlashBetweenStartId).style.display = (type == 0) ? '' : 'none';
		document.getElementById(tdFromYearId).style.display = (type == 0) ? '' : 'none';
		document.getElementById(tdSlashBetweenEndId).style.display = (type == 0) ? '' : 'none';
		document.getElementById(tdToMonthId).style.display = (type == 0) ? '' : 'none';
		document.getElementById(tdSlashBetweenEndId).style.display = (type == 0) ? '' : 'none';
		document.getElementById(tdToYearId).style.display = (type == 0) ? '' : 'none';
		document.getElementById(tdLeMonthId).style.display = (type == 1) ? '' : 'none';
		document.getElementById(tdSlashLeId).style.display = (type == 1) ? '' : 'none';
		document.getElementById(tdLeYearId).style.display = (type == 1) ? '' : 'none';
		document.getElementById(tdGeMonthId).style.display = (type == 2) ? '' : 'none';
		document.getElementById(tdSlashGeId).style.display = (type == 2) ? '' : 'none';
		document.getElementById(tdGeYearId).style.display = (type == 2) ? '' : 'none';
		document.getElementById(tdEqMonthId).style.display = (type == 3) ? '' : 'none';
		document.getElementById(tdSlashEqId).style.display = (type == 3) ? '' : 'none';
		document.getElementById(tdEqYearId).style.display = (type == 3) ? '' : 'none';
		QueryBuilder.updateTotal(builderId, formName, updateTotalFieldName, 0);
	},
	
	toggleMonth: function(builderId, formName, updateTotalFieldName, monthFieldName, monthTdId)
	{
		var monthInput = document.forms[formName].elements[monthFieldName];
		var monthTd = document.getElementById(monthTdId);
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
		QueryBuilder.updateTotal(builderId, formName, updateTotalFieldName, 0);
	},
	
	showList: function(builderId, formName, updateTotalFieldName, attributeId, hiddenFieldName, displayFieldName)
	{
	
		var url = "dictionary-list.jsp" +
			"?attributeId=" + encodeURIComponent(attributeId) + 
			"&formName=" + encodeURIComponent(formName) +
			"&updateTotalFieldName=" + encodeURIComponent(updateTotalFieldName) +
			"&hiddenFieldName=" + encodeURIComponent(hiddenFieldName) +
			"&displayFieldName=" + encodeURIComponent(displayFieldName) +
			"&builderId=" + encodeURIComponent(builderId);

		window.open(url, "search-list", "width=300,height=500,resizable=yes,scrollbars=yes,status=no");

	}

}