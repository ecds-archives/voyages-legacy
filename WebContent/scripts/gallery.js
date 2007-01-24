
var items = null;

function filterItems(objId, lookFor) {
	element = document.getElementById(objId);
	lookFor = lookFor.value.toLowerCase();
	if (items == null) {
		items = new Array();
		for (i = 0; i < element.options.length; i++) {
			items[i] = element.options[i];
		}
	}
	while (element.options.length != 0) {
		element.options[0] = null;
	}
	if (lookFor != "") {
		var j = 0;
		for (i = 0; i < items.length; i++) {
			if (items[i].text.toLowerCase().indexOf(lookFor) != -1) {
				element.options[j++] = items[i];
			}
		}
	} else {
		for (i = 0; i < items.length; i++) {	
			element.options[i] = items[i];	
		}
	}
	
}