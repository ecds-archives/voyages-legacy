var TimelineGlobals = 
{

	timelines: new Array(),

	registerTimeline: function(timeline)
	{
		this.timelines[timeline.timelineId] = timeline;
	}

}

function Timeline(timelineId, formName, markerWidth, markersCount, leftExtentFieldName, rightExtentFieldName)
{
	this.timelineId = timelineId;
	this.formName = formName;
	this.markerWidth = markerWidth;
	this.markersCount = markersCount;
	this.leftExtentFieldName = leftExtentFieldName;
	this.rightExtentFieldName = rightExtentFieldName;
	EventAttacher.attachOnWindowEvent("load", this, "init");
}

Timeline.prototype.init = function()
{

	var form = document.forms[this.formName];
	this.leftExtentField = form.elements[this.leftExtentFieldName];
	this.rightExtentField = form.elements[this.rightExtentFieldName];
	this.table = document.getElementById(this.timelineId);
	
	this.tableOffsetLeft = ElementUtils.getOffsetLeft(this.table.rows[0].cells[0])

	var cells = this.table.rows[0].cells;
	for (var i = 0; i < this.markersCount; i++)
		EventAttacher.attach(
			cells[i], "mousedown",
			this, "mouseDown", i);
	
	this.leftExtent = parseInt(this.leftExtentField.value);
	this.rightExtent = parseInt(this.rightExtentField.value);
	
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

	// check correct ranges
	if (!changeLeft && !changeRight)
	{
		return;
	}		
	else if (changeLeft && !changeRight)
	{
		leftExtent = this.cropExtent(leftExtent, 0, this.rightExtent-1);
	}		
	else if (!changeLeft && changeRight)
	{
		rightExtent = this.cropExtent(rightExtent, this.leftExtent+1, this.markersCount-1);
	}		
	else
	{
		leftExtent = this.cropExtent(leftExtent, 0, this.markersCount-2);
		rightExtent = this.cropExtent(rightExtent, leftExtent+1, this.markersCount-1);
	}

	// remember old selection
	var oldLeftExtent = this.leftExtent;
	var oldRightExtent = this.rightExtent;

	// change it now
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
	
	// redraw cells
	var cells = this.table.rows[0].cells;
	for (var i = 0; i < this.markersCount; i++)
	{
	
		var parity = 0;
		if (oldLeftExtent <= i && i <= oldRightExtent) parity++;
		if (this.leftExtent <= i && i <= this.rightExtent) parity++;
		
		// only those that need which have changed
		if (parity == 1 || true)
		{

			var className;
			var x = "";
			if (0 < i && i < this.markersCount-1)
			{
				if (this.leftExtent < i && i < this.rightExtent)
					{className = "timeline-inside"; x = "I";}
				else if (i < this.leftExtent || this.rightExtent < i)
					{className = "timeline-outside"; x = "O";}
				else if (i == this.leftExtent)
					{className = "timeline-left-marker"; x = "LM";}
				else
					{className = "timeline-right-marker"; x = "RM";}
			}
			else if (i == 0)
			{
				if (this.leftExtent == 0)
					{className = "timeline-left-boundary-marker"; x = "LBM";}
				else
					{className = "timeline-left-boundary"; x = "LB";}
			}
			else
			{
				if (this.rightExtent == this.markersCount-1)
					{className = "timeline-right-boundary-marker"; x = "RBM";}
				else
					{className = "timeline-right-boundary"; x = "RB";}
			}

			cells[i].className = className;
			
		}

	}
	
}

Timeline.prototype.mouseDown = function(event, markerIndex)
{

	if (markerIndex == this.leftExtent)
	{
		EventAttacher.attachOnWindowEvent("mousemove", this, "leftKnobMouseMove");
		EventAttacher.attachOnWindowEvent("mouseup", this, "mouseUp");
	}
	else if (markerIndex == this.rightExtent)
	{
		EventAttacher.attachOnWindowEvent("mousemove", this, "rightKnobMouseMove");
		EventAttacher.attachOnWindowEvent("mouseup", this, "mouseUp");
	}
	else if (this.leftExtent < markerIndex && markerIndex < this.rightExtent)
	{
		this.prevIndex = this.getMarkerIndexByMouseX(event.clientX);
		EventAttacher.attachOnWindowEvent("mousemove", this, "selectionMouseMove");
		EventAttacher.attachOnWindowEvent("mouseup", this, "mouseUp");
	}
		
}

Timeline.prototype.getMarkerIndexByMouseX = function(x)
{
	return Math.floor((x - this.tableOffsetLeft) / this.markerWidth);
}

Timeline.prototype.leftKnobMouseMove = function(event)
{
	var newExtent = this.getMarkerIndexByMouseX(event.clientX);
	this.setPosition(newExtent, null, true, false);
}

Timeline.prototype.rightKnobMouseMove = function(event)
{
	var newExtent = this.getMarkerIndexByMouseX(event.clientX);
	this.setPosition(null, newExtent, false, true);
}

Timeline.prototype.selectionMouseMove = function(event)
{

	var newIndex = this.getMarkerIndexByMouseX(event.clientX);
	var diff = newIndex - this.prevIndex;
	
	if (diff != 0)
	{
		
		var newLeftExtent;
		var newRightExtent;
		
		if (this.leftExtent + diff < 0)
		{
			newLeftExtent = 0;
			newRightExtent = this.rightExtent - this.leftExtent;
		}
		else if (this.rightExtent + diff > this.markersCount - 1)
		{
			newLeftExtent = this.markersCount - 1 - (this.rightExtent - this.leftExtent);
			newRightExtent = this.markersCount - 1;
		}
		else
		{
			newLeftExtent = this.leftExtent + diff;
			newRightExtent = this.rightExtent + diff;
		}
		
		this.setPosition(newLeftExtent, newRightExtent, true, true);
		this.prevIndex = newIndex;

	}
}

Timeline.prototype.mouseUp = function(event)
{
	EventAttacher.detachOnWindowEvent("mousemove", this, "leftKnobMouseMove");
	EventAttacher.detachOnWindowEvent("mousemove", this, "rightKnobMouseMove");
	EventAttacher.detachOnWindowEvent("mousemove", this, "selectionMouseMove");
	EventAttacher.detachOnWindowEvent("mouseup", this, "mouseUp");
}
