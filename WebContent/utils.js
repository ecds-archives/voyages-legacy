var IE = navigator.userAgent.indexOf("MSIE 7.0") || navigator.userAgent.indexOf("MSIE 6.0") != -1 || navigator.userAgent.indexOf("MSIE 5.5") != -1;
var GK = navigator.userAgent.indexOf("Gecko") != -1;

function Animation(element, x1, y1, w1, h1, o1, x2, y2, w2, h2, o2, steps, duration, callWhenDone)
{
	this.element = element;
	this.x1 = x1;
	this.y1 = y1;
	this.w1 = w1;
	this.h1 = h1;
	this.o1 = o1;
	this.x2 = x2;
	this.y2 = y2;
	this.w2 = w2;
	this.h2 = h2;
	this.o2 = o2;
	this.steps = steps;
	this.delay = Math.round(duration / steps);
	this.callWhenDone = callWhenDone;
}

Animation.prototype.start = function()
{
	this.pos = 0;
	this.move();
}

Animation.prototype.move = function()
{

	var t = this.pos / this.steps;
	this.element.style.left = Math.round(this.x1 + t*(this.x2-this.x1)) + "px";
	this.element.style.top = Math.round(this.y1 + t*(this.y2-this.y1)) + "px";
	this.element.style.width = Math.round(this.w1 + t*(this.w2-this.w1)) + "px";
	this.element.style.height = Math.round(this.h1 + t*(this.h2-this.h1)) + "px";
	this.element.style.opacity = this.o1 + t*(this.o2-this.o1);

	if (this.pos < this.steps)
	{
		this.pos ++;
		Timer.delayedCall(this, "move", this.delay);
	}
	else
	{
		if (this.callWhenDone)
			this.callWhenDone();
	}

}

var EventUtils = 
{

	getRelativePosX: function(e)
	{
		return e.x ? e.x : e.layerX;
	},
	
	getRelativePosX: function(e)
	{
		return e.y ? e.y : e.layerY;
	}

}

var ElementUtils =
{

	getOffsetWidth: function(el)
	{
		return el.offsetWidth;
	},
	
	getOffsetHeight: function(el)
	{
		return el.offsetHeight;
	},

	getOffsetLeft: function(el)
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
		
	},
	
	getOffsetTop: function(el)
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
		
	},
	
	deleteAllChildren: function(el)
	{
		while (el.hasChildNodes())
			el.removeChild(el.firstChild);
	},
	
	getPageWidth: function()
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
	},
	
	getPageHeight: function()
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
	},
	
	getScrollLeft: function(el)
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
	},
	
	getScrollTop: function(el)
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

}

var EventAttacher =
{
	map: new Array(),

	attach: function(element, eventType, object, handler, args)
	{

		if (element.attachEvent)
		{
			element.attachEvent("on" + eventType, EventAttacher.globalHandler);
		}
		else if (element.addEventListener)
		{
			element.addEventListener(eventType, EventAttacher.globalHandler, false);
		}

		var reg = new Object();
		EventAttacher.map.push(reg);

		if (object == null) object = window;
		reg.element = element;
		reg.object = object;
		reg.eventType = eventType;
		reg.handler = handler;
		reg.args = args;

	},

	attachById: function(elementId, eventType, object, handler, args)
	{
		var element = document.getElementById(elementId);
		this.attach(element, eventType, object, handler, args);
	},
	
	detach: function(element, eventType, object, handler)
	{
		var noOnTheSameType = 0;	
		for (var i=0; i<EventAttacher.map.length; i++)
		{
			var reg = EventAttacher.map[i];
			if (reg.element == element && reg.eventType == eventType)
			{
				noOnTheSameType ++;
				if (reg.object == object && reg.handler == handler)
				{
					EventAttacher.map.splice(i, 1);
					noOnTheSameType --;
					i --;;
				}
			}
		}
		if (noOnTheSameType == 0)
		{
			if (element.detachEvent)
			{
				element.detachEvent("on" + eventType, EventAttacher.globalHandler);
			}
			else if (element.removeEventListener)
			{
				element.removeEventListener(eventType, EventAttacher.globalHandler, false);
			}
		}
	},
	
	detachById: function(elementId, eventType, object, handler)
	{
		var element = document.getElementById(elementId);
		EventAttacher.detach(element, eventType, object, handler);
	},

	globalHandler: function(event)
	{
		if (!event) event = window.event;
		var element = this; // event.srcElement ? event.srcElement : this;
		//alert(EventAttacher.map.length);
		for (var i=0; i<EventAttacher.map.length; i++)
		{
			var reg = EventAttacher.map[i];
			if (reg.element == element && reg.eventType == event.type)
			{
				reg.object[reg.handler](event, reg.args);
			}
		}
	}

}

var Timer =
{

	map: new Array(),
	nextId: 0,
	
	delayedCall: function(object, method, delay)
	{
	
		var id = Timer.nextId;
		Timer.nextId ++;
	
		if (object == null) object = window;
		var reg = new Object();
		reg.object = object;
		reg.method = method;
		reg.tid = window.setTimeout("Timer.globalHandler(" + id + ")", delay);
		
		Timer.map["call_" + id] = reg;
		
		return id;

	},
	
	cancelCall: function(id)
	{
		var reg = Timer.map["call_" + id];
		if (reg)
		{
			window.clearTimeout(reg.tid);
			delete Timer.map["call_" + id];
		}
	},
	
	globalHandler: function(id)
	{
		var reg = Timer.map["call_" + id];
		if (reg)
		{
			delete Timer.map["call_" + id];
			reg.object[reg.method]();
		}
	}

}

var ObjectUtils = 
{
	printObject: function(obj)
	{
		if (!obj)
		{
			alert(obj);
		}
		else
		{
			var ret = "";
			for (var k in obj) ret += k + " = " + obj[k] + "\n";
			alert(ret);
		}
	}
}