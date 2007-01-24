var TimelineGlobals = 
{

	timelines: new Array(),

	registerTimeline: function(timeline)
	{
		this.timelines[timeline.timelineId] = timeline;
	}

}

function Timeline(builderId, formName, markerWidth, noOfMarkers, leftExtentFieldName, rightExtentFieldName, sliderContainerElementId, leftKnobElementId, rightKnobElementId, selectionElementId)
{
	this.builderId = builderId;
	this.formName = formName;
	this.markerWidth = markerWidth;
	this.noOfMarkers = noOfMarkers;
	this.leftExtentFieldName = leftExtentFieldName;
	this.rightExtentFieldName = rightExtentFieldName;
	this.sliderContainerElementId = sliderContainerElementId;
	this.leftKnobElementId = leftKnobElementId;
	this.rightKnobElementId = rightKnobElementId;
	this.selectionElementId = selectionElementId;
	EventAttacher.attachOnWindowEvent("load", this, "init");
}

Timeline.prototype.init = function()
{

	var form = document.forms[this.formName];
	this.leftExtentField = form.elements[this.leftExtentFieldName];
	this.rightExtentField = form.elements[this.rightExtentFieldName];
	this.sliderContainerElement = document.getElementById(this.sliderContainerElementId);
	this.leftKnobElement = document.getElementById(this.leftKnobElementId);
	this.rightKnobElement = document.getElementById(this.rightKnobElementId);
	this.selectionElement = document.getElementById(this.selectionElementId);
	
	EventAttacher.attach(this.leftKnobElement, "mousedown", this, "leftKnobMouseDown");
	EventAttacher.attach(this.rightKnobElement, "mousedown", this, "rightKnobMouseDown");
	EventAttacher.attach(this.selectionElement, "mousedown", this, "selectionMouseDown");
	
	this.knobLeftWidth = this.leftKnobElement.offsetWidth;
	this.knobRightWidth = this.rightKnobElement.offsetWidth;
	
	this.leftExtent = this.leftExtentField.value ?  parseInt(this.leftExtentField.value) : 0;
	this.rightExtent = this.rightExtentField.value ? parseInt(this.rightExtentField.value) : this.noOfMarkers - 1;
	this.setPosition(this.leftExtent, this.rightExtent, true, true);
	
}

Timeline.prototype.cropExtent = function(extent, min, max)
{
	if (extent < min)
		return min;
	else if (extent > max)
		return max;
	else
		return extent;
}

Timeline.prototype.setPosition = function(leftExtent, rightExtent, changeLeft, changeRight)
{

	if (!changeLeft && !changeRight)
	{
		return;
	}		
	else if (changeLeft && !changeRight)
	{
		leftExtent = this.cropExtent(leftExtent, 0, this.rightExtent);
	}		
	else if (!changeLeft && changeRight)
	{
		rightExtent = this.cropExtent(rightExtent, this.leftExtent, this.noOfMarkers-1);
	}		
	else
	{
		leftExtent = this.cropExtent(leftExtent, 0, this.noOfMarkers-1);
		rightExtent = this.cropExtent(rightExtent, leftExtent, this.noOfMarkers-1);
	}

	if (changeLeft)
	{
		this.leftExtent = leftExtent;
		this.leftExtentField.value = leftExtent;
	}
	
	if (changeRight)
	{
		this.rightExtent = rightExtent;
		this.rightExtentField.value = rightExtent;
	}
	
	var leftKnobPos = this.leftExtent * this.markerWidth + (this.markerWidth / 2);
	var rightKnobPos = this.rightExtent * this.markerWidth + (this.markerWidth / 2);
	
	this.leftKnobElement.style.left =
		(leftKnobPos - this.knobLeftWidth) + "px";
	
	this.rightKnobElement.style.left =
		(rightKnobPos) + "px";

	this.selectionElement.style.left =
		(leftKnobPos) + "px";

	this.selectionElement.style.width =
		(rightKnobPos - leftKnobPos) + "px";

}

Timeline.prototype.leftKnobMouseDown = function(event)
{
	this.startX = event.clientX;
	this.startOffsetLeft = this.leftKnobElement.offsetLeft;
	EventAttacher.attachOnWindowEvent("mousemove", this, "leftKnobMouseMove");
	EventAttacher.attachOnWindowEvent("mouseup", this, "mouseUp");
}

Timeline.prototype.rightKnobMouseDown = function(event)
{
	this.startX = event.clientX;
	this.startOffsetLeft = this.rightKnobElement.offsetLeft;
	EventAttacher.attachOnWindowEvent("mousemove", this, "rightKnobMouseMove");
	EventAttacher.attachOnWindowEvent("mouseup", this, "mouseUp");
}

Timeline.prototype.selectionMouseDown = function(event)
{
	this.startX = event.clientX;
	this.startOffsetLeft = this.selectionElement.offsetLeft;
	EventAttacher.attachOnWindowEvent("mousemove", this, "selectionMouseMove");
	EventAttacher.attachOnWindowEvent("mouseup", this, "mouseUp");
}

Timeline.prototype.calculateExtentFromX = function(x)
{
	return Math.floor(x / this.markerWidth);
}

Timeline.prototype.leftKnobMouseMove = function(event)
{
	var newExtent = this.calculateExtentFromX(this.startOffsetLeft + (event.clientX - this.startX));
	this.setPosition(newExtent, null, true, false);
}

Timeline.prototype.rightKnobMouseMove = function(event)
{
	var newExtent = this.calculateExtentFromX(this.startOffsetLeft + (event.clientX - this.startX));
	this.setPosition(null, newExtent, false, true);
}

Timeline.prototype.selectionMouseMove = function(event)
{

	var currentWidth = this.rightExtent - this.leftExtent;
	
	var newLeftExtent = this.calculateExtentFromX(this.startOffsetLeft + (event.clientX - this.startX));
	if (newLeftExtent < 0)
	{
		newLeftExtent = 0;
	}
	
	var newRightExtent = newLeftExtent + currentWidth;
	if (newRightExtent > this.noOfMarkers - 1)
	{
		newRightExtent = this.noOfMarkers - 1;
		newLeftExtent = this.noOfMarkers - 1 - currentWidth;
	}
	
	this.setPosition(newLeftExtent, newRightExtent, true, true);
}

Timeline.prototype.mouseUp = function(event)
{
	EventAttacher.detachOnWindowEvent("mousemove", this, "leftKnobMouseMove");
	EventAttacher.detachOnWindowEvent("mousemove", this, "rightKnobMouseMove");
	EventAttacher.detachOnWindowEvent("mousemove", this, "selectionMouseMove");
	EventAttacher.detachOnWindowEvent("mouseup", this, "mouseUp");
}
