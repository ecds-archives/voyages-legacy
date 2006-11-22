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

function EventLineEvent(x, text)
{
	this.x = x;
	this.text = text;
}

function EventLineGraph(name, color, maxValue, minValue, x, y)
{
	this.name = name;
	this.color = color;
	this.maxValue = maxValue;
	this.minValue = minValue;
	this.x = x;
	this.y = y;
}

function EventLineZoomLevel(barWidth, labelsSpacing, majorLabels, viewSpan)
{
	this.barWidth = barWidth;
	this.labelsSpacing = labelsSpacing;
	this.majorLabels = majorLabels;
	this.viewSpan = viewSpan;
}

function EventLineHorizontalLabel(label, line)
{
	this.label = label;
	this.line = line;
}

function EventLineVerticalLabel(label, line)
{
	this.label = label;
	this.line = line;
}

function EventLine(
	eventLineId,
	formName,
	graphsContainerId,
	selectorContainerId,
	selectorId,
	leftSelectorId,
	rightSelectorId,
	indicatorContainerId,
	indicatorId,
	indicatorLabelId,
	zoomLevelFieldName,
	offsetFieldName,
	viewportHeight,
	graphHeight,
	selectorOffset,
	selectorHeight,
	leftLabelsWidth,
	leftLabelsMargin,
	topLabelsHeight,
	topLabelsMargin,
	events,
	graphs,
	zoomLevels,
	verticalLabelsSpacing,
	verticalLabelsMajorSpacing)
{

	this.eventLineId = eventLineId;
	this.formName = formName;

	this.graphsContainerId = graphsContainerId;
	
	this.selectorContainerId = selectorContainerId;
	this.selectorId = selectorId;
	this.leftSelectorId = leftSelectorId;
	this.rightSelectorId = rightSelectorId;
	
	this.indicatorContainerId = indicatorContainerId;
	this.indicatorId = indicatorId;
	this.indicatorLabelId = indicatorLabelId;
	
	this.zoomLevelFieldName = zoomLevelFieldName;
	this.offsetFieldName = offsetFieldName;
	
	this.viewportHeight = viewportHeight;
	this.graphHeight = graphHeight;
	
	this.selectorOffset = selectorOffset;
	this.selectorHeight = selectorHeight;
	this.selectorBarWidth = zoomLevels[0].barWidth;
	this.selectorViewSpan = zoomLevels[0].viewSpan;
	this.slotsCount = zoomLevels[0].viewSpan;

	this.leftLabelsWidth = leftLabelsWidth;
	this.leftLabelsMargin = leftLabelsMargin;
	this.topLabelsHeight = topLabelsHeight;
	this.topLabelsMargin = topLabelsMargin;
	
	this.events = events;
	this.graphs = graphs;
	this.zoomLevels = zoomLevels;
	
	this.verticalLabelsSpacing = verticalLabelsSpacing;
	this.verticalLabelsMajorSpacing = verticalLabelsMajorSpacing;
	
	this.visibleEventIndex = -1;
	
	EventAttacher.attachOnWindowEvent("load", this, "init");

}

EventLine.prototype.init = function()
{

	this.containerElement = document.getElementById(this.eventLineId);
	
	this.graphsContainer = document.getElementById(this.graphsContainerId);
	
	this.selectorContainer = document.getElementById(this.selectorContainerId);
	this.selector = document.getElementById(this.selectorId);
	this.leftSelector = document.getElementById(this.leftSelectorId);
	this.rightSelector = document.getElementById(this.rightSelectorId);
	
	this.indicatorContainerElement = document.getElementById(this.indicatorContainerId);
	this.indicatorElement = document.getElementById(this.indicatorId);
	this.indicatorLabelElement = document.getElementById(this.indicatorLabelId);

	var frm = document.forms[this.formName];
	this.zoomLevelField = frm.elements[this.zoomLevelFieldName];
	this.offsetField = frm.elements[this.offsetFieldName];
	
	var t1 = new Date();
	this.findMaxValue();
	var t2 = new Date();
	this.createSelector();
	var t3 = new Date();
	this.initSlots();
	var t4 = new Date();
	this.refresh(parseInt(this.zoomLevelField.value), parseInt(this.offsetField.value));
	var t5 = new Date();

	/*	
	alert(
		"load HTML = " + (s2.getTime() - s1.getTime()) + "ms, " +
		"findMaxValue = " + (t2.getTime() - t1.getTime()) + "ms, " +
		"createSelector = " + (t3.getTime() - t2.getTime()) + "ms, " +
		"initSlots = " + (t4.getTime() - t3.getTime()) + "ms, " +
		"refresh = " + (t5.getTime() - t4.getTime()) + "ms");
	*/	

	EventAttacher.attach(this.indicatorContainerElement, "mouseover", this, "graphMouseOver");
	EventAttacher.attach(this.indicatorContainerElement, "mouseout", this, "graphMouseOut");
	EventAttacher.attach(this.indicatorContainerElement, "mousemove", this, "graphMouseMove");
	
	EventAttacher.attach(this.selector, "mousedown", this, "selectorMouseDown");
	EventAttacher.attach(this.leftSelector, "mousedown", this, "leftSelectorMouseDown");
	EventAttacher.attach(this.rightSelector, "mousedown", this, "rightSelectorMouseDown");

}

EventLine.prototype.findMaxValue = function()
{
	if (this.graphs && this.graphs.length > 0)
	{
		this.maxValue = this.graphs[0].maxValue;
		for (var i = 1; i < this.graphs.length; i++)
		{
			if (this.maxValue < this.graphs[i].maxValue)
			{
				this.maxValue = this.graphs[i].maxValue;
			}
		}
	}
}

EventLine.prototype.createSelector = function()
{

	this.selectorContainer.style.width = (this.selectorBarWidth * this.selectorViewSpan) + "px";
	
	for (var i = 0; i < this.graphs.length; i++)
	{
		var graph = this.graphs[i];
		var x = graph.x;
		var y = graph.y;
		
		var j = 0;
 		for (j = 0; j < x.length; i++)
		{
			if (this.selectorOffset <= x[j])
			{
				break;
			}
		}
		
		for (var k = 0; k < this.selectorViewSpan; k++)
		{

			//var slot = document.createElement("div");
			var slot = this.selectorContainer.childNodes[k];
			var slotStyle = slot.style;

			var value = 0;
			if (j < x.length && x[j] == this.selectorOffset + k)
			{
				value = y[j];
				j++;
			}
			
			var barHeight = Math.round((value / this.maxValue) * this.selectorHeight);

			slotStyle.position = "absolute";
			slotStyle.backgroundColor = graph.color;
			slotStyle.left = (k*this.selectorBarWidth) + "px";
			slotStyle.width = (this.selectorBarWidth - 1) + "px";
			slotStyle.top = (this.selectorHeight - barHeight) + "px";
			slotStyle.height = (barHeight) + "px";
			
			//this.selectorContainer.insertBefore(slot, this.selector);

		}

	}

}

EventLine.prototype.initSlots = function()
{
	for (var i = 0; i < this.graphs.length; i++)
	{
		var graph = this.graphs[i];
		var slots = graph.slots = new Array();
		for (var j = 0; j < this.slotsCount; j++)
		{
			var slot = this.graphsContainer.childNodes[i*this.slotsCount+j];
			slot.style.position = "absolute";
			slot.style.backgroundColor = graph.color;
			slot.style.height = "0px";
			slots.push(slot);
		}
	}
}

EventLine.prototype.refresh = function(zoomLevel, offset)
{

	if (!this.graphs || this.graphs.length == 0)
		return;

	this.offset = offset;
	
	if (zoomLevel != this.zoomLevel)
	{
		this.zoomLevel = zoomLevel;
		var currentViewSpan = this.zoomLevels[this.zoomLevel].viewSpan;
		for (var i = 0; i < this.graphs.length; i++)
		{
			var slots = this.graphs[i].slots;
			for (var j = 0; j < currentViewSpan; j++)
			{
				slots[j].style.display = "block";
			}
			for (var j = currentViewSpan; j < this.slotsCount; j++)
			{
				slots[j].style.display = "none";
			}
		}
	}
	
	var zoomLevelObj = this.zoomLevels[this.zoomLevel];
	
	this.graphsContainer.style.width = (zoomLevelObj.barWidth * zoomLevelObj.viewSpan) + "px";
	this.graphsContainer.style.height = (this.graphHeight) + "px";
	
	this.indicatorContainerElement.style.width = (zoomLevelObj.barWidth * zoomLevelObj.viewSpan) + "px";
	this.indicatorElement.style.width = (zoomLevelObj.barWidth) + "px";
	
	this.selector.style.display = "block";
	this.leftSelector.style.display = "block";
	this.rightSelector.style.display = "block";
	this.redrawSelector(this.offset, zoomLevelObj.viewSpan);
	
	var currBarWidth = zoomLevelObj.barWidth;
	
	for (var i = 0; i < this.graphs.length; i++)
	{
	
		var graph = this.graphs[i];
		var slots = graph.slots;
		var x = graph.x;
		var y = graph.y;
		
		var currentY = graph.currentY = new Array();
		
		var j = 0;
 		for (j = 0; j < x.length; j++)
		{
			if (this.offset <= x[j])
			{
				break;
			}
		}

		for (var k = 0; k < slots.length; k++)
		{
			var slotStyle = slots[k].style;
			var value = 0;
			if (j < x.length && x[j] == this.offset + k)
			{
				value = y[j];
				j++;
			}
			currentY.push(value);
			var barHeight = Math.round((value / this.viewportHeight) * this.graphHeight);
			if (barHeight > this.graphHeight) barHeight = this.graphHeight;
			slotStyle.top = (this.graphHeight - barHeight) + "px";
			slotStyle.height = (barHeight) + "px";
			slotStyle.left = (k*currBarWidth) + "px";
			slotStyle.width = (currBarWidth - 1) + "px";
		}

	}
	
	this.createHorizontalLabels();
	this.createVerticalLabels();
	
}


EventLine.prototype.createHorizontalLabels = function()
{

	var topLabelWidth = 100;
	
	if (this.horizontalLabels)
	{
		for (var i = 0; i < this.horizontalLabels.length; i++)
		{
			this.containerElement.removeChild(this.horizontalLabels[i].label);
			this.containerElement.removeChild(this.horizontalLabels[i].line);
		}
	}

	this.horizontalLabels = new Array();
	
	var viewSpan = this.zoomLevels[this.zoomLevel].viewSpan;
	var labelsSpacing = this.zoomLevels[this.zoomLevel].labelsSpacing;
	var barWidth = this.zoomLevels[this.zoomLevel].barWidth;
	var majorLabels = this.zoomLevels[this.zoomLevel].majorLabels;
	
	var firstLabelSlot = labelsSpacing * Math.ceil(this.offset / labelsSpacing) - this.offset;
	
	for (var i = firstLabelSlot; i < viewSpan; i += labelsSpacing)
	{
	
		var left = ((i * barWidth) + barWidth / 2) + this.leftLabelsWidth + this.leftLabelsMargin;
		var value = i + this.offset;
		var major = value % majorLabels == 0;
		
		var labelLine = document.createElement("div");
		labelLine.style.backgroundColor = major ? "#333333" : "#CCCCCC";
		labelLine.style.position = "absolute";
		labelLine.style.top = (this.topLabelsHeight + this.topLabelsMargin) + "px";
		labelLine.style.left = left + "px";
		labelLine.style.width = "1px";
		labelLine.style.height = this.graphHeight + "px";

		var labelTable = document.createElement("table");
		var labelCell = labelTable.insertRow(0).insertCell(0);
		
		labelTable.style.position = "absolute";
		labelTable.style.top = "0px";
		labelTable.style.left = (left - topLabelWidth/2) + "px";
		labelCell.style.width = topLabelWidth + "px";
		labelCell.style.height = this.topLabelsHeight + "px";
		labelCell.style.textAlign = "center";
		labelCell.style.verticalAlign = "bottom";
		labelCell.style.fontWeight = major ? "bold" : "normal";

		labelCell.innerHTML = i + this.offset;
		
		this.horizontalLabels.push(new EventLineHorizontalLabel(labelTable, labelLine));
		this.containerElement.appendChild(labelTable);
		this.containerElement.appendChild(labelLine);
		
	}

}

EventLine.prototype.createVerticalLabels = function()
{

	var leftLabelHeight = 25;

	var zoomLevelObj = this.zoomLevels[this.zoomLevel];
	var graphWidth = zoomLevelObj.barWidth * zoomLevelObj.viewSpan;
	
	if (this.verticalLabels)
	{
		for (var i = 0; i < this.verticalLabels.length; i++)
		{
			this.containerElement.removeChild(this.verticalLabels[i].label);
			this.containerElement.removeChild(this.verticalLabels[i].line);
		}
	}
	
	this.verticalLabels = new Array();
	
	for (var i = 0; i <= this.viewportHeight; i += this.verticalLabelsSpacing)
	{

		var barHeight = Math.round((i / this.viewportHeight) * this.graphHeight);
		var top = this.graphHeight + this.topLabelsHeight + this.topLabelsMargin - barHeight;
		var major = i % this.verticalLabelsMajorSpacing == 0;
		
		var labelLine = document.createElement("div");
		labelLine.style.backgroundColor = major ? "#333333" : "#CCCCCC";
		labelLine.style.position = "absolute";
		labelLine.style.top = top + "px";
		labelLine.style.left = (this.leftLabelsWidth + this.leftLabelsMargin) + "px";
		labelLine.style.height = "1px";
		labelLine.style.width = graphWidth + "px";

		var labelTable = document.createElement("table");
		var labelCell = labelTable.insertRow(0).insertCell(0);
		
		labelTable.style.position = "absolute";
		labelTable.style.top = (top - leftLabelHeight / 2) + "px";
		labelTable.style.left = "0px";
		labelCell.style.width = this.leftLabelsWidth + "px";
		labelCell.style.height = leftLabelHeight + "px";
		labelCell.style.textAlign = "right";
		labelCell.style.fontWeight = major ? "bold" : "normal";

		labelCell.innerHTML = i;
		
		this.verticalLabels.push(new EventLineVerticalLabel(labelTable, labelLine));
		this.containerElement.appendChild(labelTable);
		this.containerElement.appendChild(labelLine);

	}

}

EventLine.prototype.redrawSelectorUsingLeftRight = function(left, right)
{

	this.selector.style.left = left + "px";
	this.selector.style.width = (right - left) + "px";
	
	this.leftSelector.style.left = left + "px";
	this.leftSelector.style.width = "5px";
	this.leftSelector.style.backgroundColor = "#999999";
	
	this.rightSelector.style.left = (right - 5) + "px";
	this.rightSelector.style.width = "5px";
	this.rightSelector.style.backgroundColor = "#999999";

}

EventLine.prototype.redrawSelector = function(offset, span)
{
	var left = this.selectorBarWidth * (offset - this.selectorOffset);
	var right = left + this.selectorBarWidth * span;
	this.redrawSelectorUsingLeftRight(left, right);
}

EventLine.prototype.graphMouseOver = function(event)
{
	this.indicatorElement.style.display = "block";
	this.indicatorLabelElement.style.display = "block";
}

EventLine.prototype.graphMouseOut = function(event)
{
	this.indicatorElement.style.display = "none";
	this.indicatorLabelElement.style.display = "none";
}

EventLine.prototype.graphMouseMove = function(event)
{

	var x = event.clientX - ElementUtils.getOffsetLeft(this.indicatorContainerElement);
	
	if (x < 0) x = 0;
	if (x > this.graphWidth) x = this.graphWidth;

	var barWidth = this.zoomLevels[this.zoomLevel].barWidth;
	var pos = Math.floor(x / barWidth);

	var label = "";
	//var maxValue = 0;
	for (var i = 0; i < this.graphs.length; i++)
	{
		var graph = this.graphs[i];
		var value = graph.currentY[pos];
		if (i > 0) label += "<br>";
		label += graph.name + ":&nbsp;" + value;
		//if (data.value > maxValue) maxValue = value;
	}

	//this.indicatorElement.style.top = (this.graphHeight + this.graphOffsetTop - maxValue) + "px";
	//this.indicatorElement.style.height = maxValue + "px";

	this.indicatorElement.style.left = (pos * barWidth) + "px";
	this.indicatorLabelElement.style.left = (pos * barWidth) + "px";

	this.indicatorLabelElement.innerHTML = label;

}

EventLine.prototype.selectorMouseDown = function(event)
{
	this.startDragX = event.clientX;
	EventAttacher.attach(this.selector, "mouseup", this, "selectorMouseUp");
	EventAttacher.attach(this.selector, "mousemove", this, "selectorMouseMove");
}

EventLine.prototype.selectorMouseMove = function(event)
{
	var dx = event.clientX - this.startDragX;
	var newOffset = Math.round(this.offset +  dx / this.selectorBarWidth);
	this.redrawSelector(newOffset, this.zoomLevels[this.zoomLevel].viewSpan);
}

EventLine.prototype.selectorMouseUp = function(event)
{
	var dx = event.clientX - this.startDragX;
	var newOffset = Math.round(this.offset +  dx / this.selectorBarWidth);
		
	this.refresh(this.zoomLevel, newOffset);
	
	EventAttacher.detach(this.selector, "mouseup", this, "selectorMouseUp");
	EventAttacher.detach(this.selector, "mousemove", this, "selectorMouseMove");
	
}

EventLine.prototype.leftSelectorMouseDown = function(event)
{
	EventAttacher.attachOnWindowEvent("mouseup", this, "leftSelectorMouseUp");
	EventAttacher.attachOnWindowEvent("mousemove", this, "leftSelectorMouseMove");
}

EventLine.prototype.leftSelectorMouseMove = function(event)
{

	var originalLeft = this.selectorBarWidth * (this.offset - this.selectorOffset)
	var originalWidth = this.selectorBarWidth * this.zoomLevels[this.zoomLevel].viewSpan;
	var originalRight = originalLeft + originalWidth;
	
	var newLeft = event.clientX - ElementUtils.getOffsetLeft(this.selectorContainer);
	
	this.redrawSelectorUsingLeftRight(newLeft, originalRight);

}

EventLine.prototype.leftSelectorMouseUp = function(event)
{
	
	var originalLeft = this.selectorBarWidth * (this.offset - this.selectorOffset)
	var originalWidth = this.selectorBarWidth * this.zoomLevels[this.zoomLevel].viewSpan;
	var originalRight = originalLeft + originalWidth;
	
	var newLeft = event.clientX - ElementUtils.getOffsetLeft(this.selectorContainer);
	var newWidth = originalRight - newLeft;
	
	var newOffset = Math.round(this.selectorOffset + newLeft / this.selectorBarWidth);
	var newViewSpan = Math.round(newWidth / this.selectorBarWidth);
	
	var newZoomLevel = this.getClosestZoomLevel(newViewSpan);
	
	this.refresh(newZoomLevel, newOffset);
	
	EventAttacher.detachOnWindowEvent("mouseup", this, "leftSelectorMouseUp");
	EventAttacher.detachOnWindowEvent("mousemove", this, "leftSelectorMouseMove");
	
}

EventLine.prototype.rightSelectorMouseDown = function(event)
{
	EventAttacher.attachOnWindowEvent("mouseup", this, "rightSelectorMouseUp");
	EventAttacher.attachOnWindowEvent("mousemove", this, "rightSelectorMouseMove");
}

EventLine.prototype.rightSelectorMouseMove = function(event)
{

	var originalLeft = this.selectorBarWidth * (this.offset - this.selectorOffset)
	
	var newRight = event.clientX - ElementUtils.getOffsetLeft(this.selectorContainer);
	var newWidth = newRight - originalLeft;
	var newViewSpan = Math.round(newWidth / this.selectorBarWidth);
	
	this.redrawSelector(this.offset, newViewSpan);

}

EventLine.prototype.rightSelectorMouseUp = function(event)
{
	
	var originalLeft = this.selectorBarWidth * (this.offset - this.selectorOffset)
	var originalWidth = this.selectorBarWidth * this.zoomLevels[this.zoomLevel].viewSpan;
	var originalRight = originalLeft + originalWidth;
	
	var newRight = event.clientX - ElementUtils.getOffsetLeft(this.selectorContainer);
	var newWidth = newRight - originalLeft;
	var newViewSpan = Math.round(newWidth / this.selectorBarWidth);
	
	var newZoomLevel = this.getClosestZoomLevel(newViewSpan);
	var newOffset = Math.round(newRight / this.selectorBarWidth + this.selectorOffset) - this.zoomLevels[newZoomLevel].viewSpan;
	
	this.refresh(newZoomLevel, newOffset);
	
	EventAttacher.detachOnWindowEvent("mouseup", this, "rightSelectorMouseUp");
	EventAttacher.detachOnWindowEvent("mousemove", this, "rightSelectorMouseMove");
	
}

EventLine.prototype.getClosestZoomLevel = function(viewSpan)
{

	var minDiff = Math.abs(this.zoomLevels[0].viewSpan - viewSpan);
	var minIndx = 0;
	
	for (var i = 1; i < this.zoomLevels.length; i++)
	{
		var diff = Math.abs(this.zoomLevels[i].viewSpan - viewSpan);
		if (diff < minDiff)
		{
			minDiff = diff;
			minIndx = i;
		}
	}
	
	return minIndx;

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