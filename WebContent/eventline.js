var EventLineGlobals = 
{

	eventLines: new Array(),

	registerEventLine: function(eventLine)
	{
		this.eventLines[eventLine.eventLineId] = eventLine;
	}

}

function EventLineEvent(position, title, text)
{
	this.position = position;
	this.title = title;
	this.text = text;
}

function EventLineGraph(name, data)
{
	this.name = name;
	this.data = data;
}

function EventLine(eventLineId, indicatorId, barWidth, graphOffsetLeft, graphOffsetTop, events, graphs)
{
	this.eventLineId = eventLineId;
	this.events = events;
	this.graphs = graphs;
	this.barWidth = barWidth;
	this.graphOffsetLeft = graphOffsetLeft;
	this.graphOffsetTop = graphOffsetTop;
	EventAttacher.attachOnWindowEvent("load", this, "init");
}

Timeline.prototype.init = function()
{
	this.containerElement = document.getElementById(this.eventLineId);
	EventAttacher.attach(this.containerElement, "mouseover", this, "mouseOver");
	EventAttacher.attach(this.containerElement, "mouseout", this, "mouseOut");
	EventAttacher.attach(this.containerElement, "mousemove", this, "mouseMove");
}

Timeline.prototype.mouseOver = function(event)
{
	var x = event.clientX - ElementUtils.getOffsetLeft(this.containerElement) - this.graphOffsetLeft;
}

Timeline.prototype.mouseOut = function(event)
{
	this.containerElement.display = "none";
}

Timeline.prototype.mouseMove = function(event)
{
	this.containerElement.display = "block";
}

