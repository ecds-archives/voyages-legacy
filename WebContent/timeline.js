var TimelineGlobals = 
{

	timelines: new Array(),

	registerTimeline: function(timeline)
	{
		this.timelines[timeline.timelineId] = timeline;
	}

}

function Timeline(builderId, formName, markerWidth, noOfMarkers, leftExtentFieldName, rightExtentFieldName, sliderContainerElementId, knobLeftElementId, knobRightElementId, selectionElementId)
{
	this.builderId = builderId;
	this.formName = formName;
	this.markerWidth = markerWidth;
	this.noOfMarkers = noOfMarkers;
	this.leftExtentFieldName = leftExtentFieldName;
	this.rightExtentFieldName = rightExtentFieldName;
	this.sliderContainerElementId = sliderContainerElementId;
	this.knobLeftElementId = knobLeftElementId;
	this.knobRightElementId = knobRightElementId;
	this.selectionElementId = selectionElementId;
	EventAttacher.attachOnWindowEvent("load", this, "init");
}

Timeline.prototype.init = function()
{

	var form = document.forms[this.formName];
	this.leftExtent = form.elements[this.leftExtentFieldName];
	this.rightExtent = form.elements[this.rightExtentFieldName];
	this.sliderContainerElement = document.getElementById(this.sliderContainerElementId);
	this.knobLeftElement = document.getElementById(this.knobLeftElementId);
	this.knobRightElement = document.getElementById(this.knobRightElementId);
	this.selectionElement = document.getElementById(this.selectionElementId);
	
	EventAttacher.attach(this.knobLeftElement, "mousedown", this, "leftKnobMouseDown");
	EventAttacher.attach(this.knobRightElement, "mousedown", this, "rightKnobMouseDown");
	
	this.knobLeftWidth = this.knobLeftElement.offsetWidth;
	this.knobRightWidth = this.knobRightElement.offsetWidth;
	
	var leftExtent = this.leftExtent.value ?  parseInt(this.leftExtent.value) : 0;
	var rightExtent = this.rightExtent.value ? parseInt(this.rightExtent.value) : this.noOfMarkers - 1;
	this.setPosition(leftExtent, rightExtent, true, true);
	
}

Timeline.prototype.setPosition = function(leftExtent, rightExtent, changeLeft, changeRight)
{

	if (changeLeft)
	{
	
		if (leftExtent < 0)
			leftExtent = 0;
		
		this.leftExtent.value = leftExtent;
		
		var leftKnobPos = leftExtent * this.markerWidth - (leftExtent / 2);
		
		this.knobLeftElement.style.left =
			(leftKnobPos - (this.knobLeftWidth / 2)) + "px";
		
		this.sliderContainerElement.style.left =
			(leftKnobPos + (this.knobLeftWidth / 2)) + "px";

	}
	
	if (changeRight)
	{

		if (rightExtent >= this.noOfMarkers)
			rightExtent = this.noOfMarkers - 1;
		
		this.rightExtent.value = rightExtent;

		var rightKnobPos = rightExtent * this.markerWidth - (rightExtent / 2);
	
		this.sliderContainerElement.style.right =
			(rightKnobPos - (this.knobLeftWidth / 2)) + "px";
	
		this.knobRightElement.style.left =
			(rightKnobPos - (this.knobRightWidth / 2)) + "px";
	
	}

}

Timeline.prototype.leftKnobMouseDown = function(event)
{
	this.sliding = true;
	this.startX = event.clientX;
	this.startOffsetLeft = this.leftKnobElement.offsetLeft;
	EventAttacher.attachOnWindowEvent("mousemove", this, "leftKnobMouseMove");
	EventAttacher.attachOnWindowEvent("mouseup", this, "mouseUp");
}

Timeline.prototype.rightKnobMouseDown = function(event)
{
	this.sliding = true;
	this.startX = event.clientX;
	this.startOffsetLeft = this.rightKnobElement.offsetLeft;
	EventAttacher.attachOnWindowEvent("mousemove", this, "rightKnobMouseMove");
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

Timeline.prototype.leftKnobMouseMove = function(event)
{
	var newExtent = this.calculateExtentFromX(this.startOffsetLeft + (event.clientX - this.startX));
	this.setPosition(null, newExtent, false, true);
}

Timeline.prototype.mouseUp = function(event)
{
	EventAttacher.detachOnWindowEvent("mousemove", this, "leftKnobMouseMove");
	EventAttacher.detachOnWindowEvent("mousemove", this, "rightKnobMouseMove");
	EventAttacher.detachOnWindowEvent("mouseup", this, "mouseUp");
}
