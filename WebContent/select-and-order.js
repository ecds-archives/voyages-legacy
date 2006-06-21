var SelectAndOrder =
{

	add: function(formName, baseId, sortable)
	{
		var info = this.getInfo(formName, baseId);
		this.shift(info, info.availableSelect, info.selectedSelect, !sortable);
		this.updateHiddenField(info);
	},
	
	remove: function(formName, baseId)
	{
		var info = this.getInfo(formName, baseId);
		this.shift(info, info.selectedSelect, info.availableSelect, true);
		this.updateHiddenField(info);
	},
	
	moveUp: function(formName, baseId)
	{
		var info = this.getInfo(formName, baseId);
		
		var select = info.selectedSelect;
		var options = select.options;
		var curr, next;
	
	    if (options.length <= 1)
	    	return;
	
	    next = options[0];
	    for (var i=1; i<options.length; i++)
	    {
	    	curr = options[i-1];
	    	next = options[i];
	        if (!curr.selected && next.selected)
	        {
	        	select.remove(i);
	        	select.add(next, curr);
	        }
	    }
	
		this.updateHiddenField(info);
	},
	
	moveDown: function(formName, baseId)
	{
		var info = this.getInfo(formName, baseId);
		
		var select = info.selectedSelect;
		var options = select.options;
		var curr, next, prev;
	
	    if (options.length <= 1)
	    	return;
	
	    next = options[options.length-1];
	    for (var i = options.length-2; 0 <= i; i--)
	    {
	    	prev = i < options.length-2 ? options[i+2] : null;
	    	curr = options[i+1];
	    	next = options[i];
	        if (!curr.selected && next.selected)
	        {
	        	select.remove(i);
	        	select.add(next, prev);
	        }
	    }
	
		this.updateHiddenField(info);
	},
	
	getInfo: function(formName, baseId)
	{
		var frm = document.forms[formName];
		var info = new Object();
		info.availableSelect = frm.elements[baseId + "_available"];
		info.selectedSelect = frm.elements[baseId + "_selected"];
		info.availableValues = frm.elements[baseId + "_available_values"];
		info.selectedValues = frm.elements[baseId + "_selected_values"];
		return info;
	},
	
	shift: function(info, sourceSelect, destSelect, order)
	{
	
		var sourceOptions = sourceSelect.options;
		var destOptions = destSelect.options;
		var i;
		
		for (var i = 0; i < sourceOptions.length; i++)
		{
			if (sourceOptions[i].selected)
			{
			
				var opt = document.createElement("option");
				opt.value = sourceOptions[i].value;
				opt.text = sourceOptions[i].text;
		
				sourceSelect.remove(i);
				i--;
				
				var nextOption = null;
				if (order && destOptions.length > 0)
				{
					var orderNumber = this.getItemOrderNumber(opt.value);
					if (orderNumber < this.getItemOrderNumber(destOptions[0].value))
					{
						nextOption = destOptions[0];
					}
					else
					{
						for (var j = 0; j < destOptions.length-1; j++)
						{
							if (orderNumber >= this.getItemOrderNumber(destOptions[j].value))
							{
								nextOption = destOptions[j+1];
								break;
							}
						}
					}
				}
				destSelect.add(opt, nextOption);
			
			}
		}

	},
	
	getItemId: function(value)
	{
		var pos = value.indexOf(":");
		return value.substr(pos + 1);
	},
	
	getItemOrderNumber: function(value)
	{
		return parseInt(value);
	},

	updateHiddenField: function(info)
	{
		
		var availableOptions = info.availableSelect;
		var selectedOptions = info.selectedSelect;

		var availableValues = new Array();
		for (i = 0; i < availableOptions.length; i++)
			availableValues.push(this.getItemId(availableOptions[i].value));
			
		var selectedValues = new Array();
		for (i = 0; i < selectedOptions.length; i++)
			selectedValues.push(this.getItemId(selectedOptions[i].value));

		info.availableValues.value = availableValues.join(",");
		info.selectedValues.value = selectedValues.join(",");
		
	}

}