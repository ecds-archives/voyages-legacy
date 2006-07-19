var IE = navigator.userAgent.indexOf("MSIE 7.0") || navigator.userAgent.indexOf("MSIE 6.0") != -1 || navigator.userAgent.indexOf("MSIE 5.5") != -1;
var GK = navigator.userAgent.indexOf("Gecko") != -1;

function get_event(e)
{
	return window.event ? window.event : e;
}

function get_event_relative_pos_x(e)
{
	return e.x ? e.x : e.layerX;
}

function get_event_relative_pos_y(e)
{
	return e.y ? e.y : e.layerY;
}

function get_element_offset_left(el)
{
	// IE
	if (el.clientLeft)
	{
		return el.clientLeft;
	}
	
	// others
	var curleft = 0;
	while (el.offsetParent)
	{
		curleft += el.offsetLeft
		el = el.offsetParent;
	}
	return curleft;
	
}

function get_element_offset_top(el)
{

	// IE
	if (el.clientTop)
	{
		return el.clientTop;
	}
	
	// others
	var curtop = 0;
	while (el.offsetParent)
	{
		curtop += el.offsetTop
		el = el.offsetParent;
	}
	return curtop;
	
}

function delete_all_children(el)
{
	while (el.hasChildNodes())
		el.removeChild(el.firstChild);
}

function get_page_width()
{
	if (self.pageXOffset) // all except IE
	{
		return self.innerWidth;
	}
	else if (document.documentElement && document.documentElement.clientWidth) // IE6 Strict
	{
		return document.documentElement.clientWidth;
	}
	else if (document.body) // all other IE
	{
		return document.body.clientWidth;
	}
}

function get_page_height()
{
	if (self.pageYOffset) // all except IE
	{
		return self.innerHeight;
	}
	else if (document.documentElement && document.documentElement.clientHeight) // IE6 Strict
	{
		return document.documentElement.clientHeight;
	}
	else if (document.body) // all other IE
	{
		return document.body.clientHeight;
	}
}

function get_element_scroll_left(el)
{
	var offset = 0;
	if (el.offsetParent != null)	// IE
	{
		var actual = el;
		while(actual)
		{
			if (actual.scrollLeft != null)
				offset += actual.scrollLeft;
			actual=actual.offsetParent;
		}
	}
	else // all other IE
	{
		offset = document.body.scrollLeft;
	}
	return offset;
}

function get_element_scroll_top(el)
{
	var offset = 0;
	if (el.offsetParent != null)	// IE
	{
		var actual = el;
		while(actual)
		{
			if (actual.scrollTop != null)
				offset += actual.scrollTop;
			actual=actual.offsetParent;
		}
	}
	else // all other IE
	{
		offset = document.body.scrollTop;
	}
	return offset;
}

var Events =
{
	map: null,

	findRegistration: function(element, eventName)
	{
		if (Events.map == null) Events.map = new Array();
		for (var i=0; i<Events.map.length; i++)
		{
			var req = Events.map[i];
			if (req.element == element && req.eventName == eventName)
			{
				return req;
			}
		}
		return null;
	},

	attach: function(element, eventName, handler)
	{

		if (element.attachEvent)
		{
			element.attachEvent("on" + eventName, Events.globalHandler);
		}
		else if (element.addEventListener)
		{
			element.addEventListener(eventName, Events.globalHandler, false);
		}

		var reg = Events.findRegistration(element, eventName);
		if (reg == null)
		{
			reg = new Object();
			this.map.push(reg);
		}

		reg.element = element;
		reg.eventName = eventName;
		reg.handler = handler;

	},

	attachById: function(elementId, eventName, handler)
	{
		var element = document.getElementById(elementId);
		this.attach(element, eventName, handler);
	},

	globalHandler: function(event)
	{
		if (!event) event = window.event;
		var element = event.srcElement ? event.srcElement : this;
		var reg = Events.findRegistration(element, event.type);
		if (reg != null) reg.handler.call();
	}

}
