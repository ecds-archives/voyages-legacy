var EventLineGlobals = 
{

	eventLines: new Array(),

	registerEventLine: function(eventLine)
	{
		this.eventLines[eventLine.eventLineId] = eventLine;
	},
	
	toggleEvent: function(eventLineId, eventIndex)
	{
		var eventLine = EventLineGlobals.eventLines[eventLineId];
		if (eventLine) eventLine.toggleEvent(eventIndex);
	}

}

function EventLineEvent(position, markElementId, eventElementId)
{
	this.position = position;
	this.markElementId = markElementId;
	this.eventElementId = eventElementId;
}

function EventLineGraph(name, data)
{
	this.name = name;
	this.data = data;
}

function EventLineDataPoint(value, label)
{
	this.value = value;
	this.label = label;
}

function EventLine(eventLineId, indicatorContainerId, indicatorId, indicatorLabelId, barWidth, graphWidth, graphHeight, graphOffsetLeft, graphOffsetTop, events, graphs)
{
	this.eventLineId = eventLineId;
	this.indicatorContainerId = indicatorContainerId;
	this.indicatorId = indicatorId;
	this.indicatorLabelId = indicatorLabelId;
	this.events = events;
	this.graphs = graphs;
	this.barWidth = barWidth;
	this.graphWidth = graphWidth;
	this.graphHeight = graphHeight;
	this.graphOffsetLeft = graphOffsetLeft;
	this.graphOffsetTop = graphOffsetTop;
	this.visibleEventIndex = -1;
	EventAttacher.attachOnWindowEvent("load", this, "init");
}

EventLine.prototype.init = function()
{
	this.containerElement = document.getElementById(this.eventLineId);
	this.indicatorContainerElement = document.getElementById(this.indicatorContainerId);
	this.indicatorElement = document.getElementById(this.indicatorId);
	this.indicatorLabelElement = document.getElementById(this.indicatorLabelId);
	EventAttacher.attach(this.indicatorContainerElement, "mouseover", this, "mouseOver");
	EventAttacher.attach(this.indicatorContainerElement, "mouseout", this, "mouseOut");
	EventAttacher.attach(this.indicatorContainerElement, "mousemove", this, "mouseMove");
}

EventLine.prototype.mouseOver = function(event)
{
	this.indicatorElement.style.display = "block";
	this.indicatorLabelElement.style.display = "block";
}

EventLine.prototype.mouseOut = function(event)
{
	this.indicatorElement.style.display = "none";
	this.indicatorLabelElement.style.display = "none";
}

EventLine.prototype.mouseMove = function(event)
{

	var x = event.clientX - ElementUtils.getOffsetLeft(this.indicatorContainerElement);
	
	if (x < 0) x = 0;
	if (x > this.graphWidth) x = this.graphWidth;
	
	var pos = Math.floor(x / this.barWidth);

	var label = "";
	var maxValue = 0;
	for (var i = 0; i < this.graphs.length; i++)
	{
		var graph = this.graphs[i];
		var data = graph.data[pos];
		if (i > 0) label += "<br>";
		label += graph.name + ":&nbsp;" + data.label;
		if (data.value > maxValue) maxValue = data.value;
	}

	//this.indicatorElement.style.top = (this.graphHeight + this.graphOffsetTop - maxValue) + "px";
	//this.indicatorElement.style.height = maxValue + "px";

	this.indicatorElement.style.left = (pos * this.barWidth) + "px";
	this.indicatorLabelElement.style.left = (pos * this.barWidth) + "px";

	this.indicatorLabelElement.innerHTML = label;

}

EventLine.prototype.setEventVisibility = function(eventIndex, visible)
{
	var event = this.events[eventIndex];
	var eventElement = document.getElementById(event.eventElementId);
	var markElement = document.getElementById(event.markElementId);
	if (visible)
	{
		eventElement.style.display = "block";
		markElement.className = "event-line-mark-pressed";
	}
	else
	{
		eventElement.style.display = "none";
		markElement.className = "event-line-mark";
	}
}

EventLine.prototype.toggleEvent = function(eventIndex)
{
	if (!this.events) return;
	if (eventIndex < 0 || this.events.length <= eventIndex) return;
	
	if (this.visibleEventIndex != -1)
	{
		this.setEventVisibility(this.visibleEventIndex, false);
	}
	
	if (this.visibleEventIndex != eventIndex)
	{
		this.setEventVisibility(eventIndex, true);
		this.visibleEventIndex = eventIndex;
	}
	else
	{
		this.visibleEventIndex = -1;
	}
	
}