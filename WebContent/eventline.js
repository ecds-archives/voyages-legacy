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

function EventLine(eventLineId, indicatorContainerId, indicatorId, indicatorLabelId, barWidth, graphWidth, graphOffsetLeft, graphOffsetTop, events, graphs)
{
	this.eventLineId = eventLineId;
	this.indicatorContainerId = indicatorContainerId;
	this.indicatorId = indicatorId;
	this.indicatorLabelId = indicatorLabelId;
	this.events = events;
	this.graphs = graphs;
	this.barWidth = barWidth;
	this.graphWidth = graphWidth;
	this.graphOffsetLeft = graphOffsetLeft;
	this.graphOffsetTop = graphOffsetTop;
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
	//this.indicatorElement.style.display = "none";
}

EventLine.prototype.mouseMove = function(event)
{

	var x = event.clientX - ElementUtils.getOffsetLeft(this.indicatorContainerElement);
	
	if (x < 0) x = 0;
	if (x > this.graphWidth) x = this.graphWidth;
	
	this.indicatorElement.style.left = x + "px";
	this.indicatorLabelElement.style.left = x + "px";
	
	var pos = Math.floor(x / this.barWidth);

	var label = "";
	for (var i = 0; i < this.graphs.length; i++)
	{
		if (i > 0) label += "<br>";
		label += this.graphs[i].name + ":&nbsp;" + this.graphs[i].data[pos];
	}
	this.indicatorLabelElement.innerHTML = label;

}

