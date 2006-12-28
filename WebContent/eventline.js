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
	},
	
	highlightEvent: function(eventLineId, eventIndex)
	{
		var eventLine = EventLineGlobals.eventLines[eventLineId];
		if (eventLine) eventLine.highlightEvent(eventIndex);
	},
	
	blurEvent: function(eventLineId, eventIndex)
	{
		var eventLine = EventLineGlobals.eventLines[eventLineId];
		if (eventLine) eventLine.blurEvent(eventIndex);
	}

}

function EventLineEvent(textId, x, text)
{
	this.textId = textId;
	this.x = x;
	this.text = text;
}

function EventLineGraph(name, legendValueElementId, baseCssClass, eventCssClass, maxValue, minValue, x, y)
{
	this.name = name;
	this.baseCssClass = baseCssClass;
	this.eventCssClass = eventCssClass;
	this.maxValue = maxValue;
	this.minValue = minValue;
	this.legendValueElementId = legendValueElementId;
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
	labelsContainerId,
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
	eventsHeight,
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
	
	this.labelsContainerId = labelsContainerId;
	
	this.zoomLevelFieldName = zoomLevelFieldName;
	this.offsetFieldName = offsetFieldName;
	
	this.viewportHeight = viewportHeight;
	this.graphHeight = graphHeight;
	
	this.selectorOffset = selectorOffset;
	this.selectorHeight = selectorHeight;
	this.selectorBarWidth = zoomLevels[0].barWidth;
	this.selectorViewSpan = zoomLevels[0].viewSpan;
	this.slotsCount = zoomLevels[0].viewSpan;
	this.graphWidth = zoomLevels[0].viewSpan * zoomLevels[0].barWidth;

	this.leftLabelsWidth = leftLabelsWidth;
	this.leftLabelsMargin = leftLabelsMargin;
	this.topLabelsHeight = topLabelsHeight;
	this.topLabelsMargin = topLabelsMargin;
	this.eventsHeight = eventsHeight;
	
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
	
	this.labelsContainer = document.getElementById(this.labelsContainerId);
	this.labelsContainer.style.width = (this.leftLabelsWidth + this.leftLabelsMargin + this.graphWidth) + "px";
	this.labelsContainer.style.height = (this.topLabelsHeight + this.topLabelsMargin + this.graphHeight + this.selectorHeight) + "px";

	var frm = document.forms[this.formName];
	this.zoomLevelField = frm.elements[this.zoomLevelFieldName];
	this.offsetField = frm.elements[this.offsetFieldName];
	
	this.findMaxValue();
	this.initEvents();

	this.createSelector();

	this.initSlots();
	this.refresh(parseInt(this.zoomLevelField.value), parseInt(this.offsetField.value));

	this.createVerticalLabels();

	this.createHorizontalLabels(this.selectorOffset, 0,
		this.topLabelsHeight + this.topLabelsMargin + this.graphHeight + this.eventsHeight + this.selectorHeight,
		"top", false, null);

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
			slotStyle.left = (k*this.selectorBarWidth) + "px";
			slotStyle.width = (this.selectorBarWidth) + "px";
			slotStyle.top = (this.selectorHeight - barHeight) + "px";
			slotStyle.height = (barHeight) + "px";
			slot.className = graph.baseCssClass;
			//slotStyle.backgroundColor = this.eventsLookup[this.selectorOffset + k] ? graph.eventColor : graph.baseColor;
			
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
			slot.style.height = "0px";
			slot.style.className = graph.baseCssClass;
			slots.push(slot);
		}
	}
}

EventLine.prototype.initEvents = function()
{
	this.eventsLookup = new Array();
	for (var i = 0; i < this.events.length; i++)
	{
		var event = this.events[i];
		this.eventsLookup[event.x] = event;
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
	
	if (this.offset < this.selectorOffset)
		this.offset = this.selectorOffset;

	if (this.offset + zoomLevelObj.viewSpan > this.selectorOffset + this.selectorViewSpan)
		this.offset = this.selectorOffset + this.selectorViewSpan - zoomLevelObj.viewSpan;
		
	this.zoomLevelField.value = this.zoomLevel;
	this.offsetField.value = this.offset;
	
	this.graphsContainer.style.width = (zoomLevelObj.barWidth * zoomLevelObj.viewSpan) + "px";
	this.graphsContainer.style.height = (this.graphHeight) + "px";
	
	this.indicatorContainerElement.style.width = (zoomLevelObj.barWidth * zoomLevelObj.viewSpan) + "px";
	this.indicatorElement.style.width = (zoomLevelObj.barWidth) + "px";
	
	this.selector.style.display = "block";
	this.leftSelector.style.display = "block";
	this.rightSelector.style.display = "block";
	this.redrawSelector(this.offset, zoomLevelObj.viewSpan);
	
	var currBarWidth = zoomLevelObj.barWidth;
	var currViewSpan = zoomLevelObj.viewSpan;
	var innerBarWidth = currBarWidth > 2 ? currBarWidth - 1 : currBarWidth;
	
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
			var slot = slots[k];
			var slotStyle = slot.style;
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
			slotStyle.width = (innerBarWidth) + "px";
			slot.className = this.eventsLookup[this.offset + k] ? graph.eventCssClass : graph.baseCssClass;
			//slotStyle.backgroundColor = graph.baseColor;
		}

	}
	
	for (var i = 0; i < this.events.length; i++)
	{
	
		var event = this.events[i];
		
		if (i + this.offset <= event.x && event.x <= i + this.offset + currViewSpan)
		{
		
			var eventTable = event.element;
			
			if (!eventTable)
			{
			
				eventTable = event.element = document.createElement("table");
				
				eventTable.cellSpacing = 0;
				eventTable.cellPadding = 0;
				eventTable.border = 0;
				
				eventTable.style.position = "absolute";
				eventTable.style.height = this.eventsHeight + "px";
				eventTable.style.width = this.eventsHeight + "px";
				eventTable.className = "event-line-event-label";
				
				eventTable.insertRow(0).insertCell(0).innerHTML = (i + 1);

				this.graphsContainer.appendChild(eventTable);
				EventAttacher.attach(eventTable, "mouseover", this, "eventMouseOver", i);
				EventAttacher.attach(eventTable, "mouseout", this, "eventMouseOut", i);

			}
			
			eventTable.style.left = (currBarWidth * (event.x - this.offset)) + "px";
			eventTable.style.top = (this.graphHeight) + "px";
			
		}
		else
		{
		
			if (event.element)
			{
				this.graphsContainer.removeChild(event.element);
				EventAttacher.detach(eventTable, "mouseover", this, "eventMouseOver");
				EventAttacher.detach(eventTable, "mouseout", this, "eventMouseOut");
				event.element = null;
			}
		
		}
		
	}
	
	if (this.horizontalLabels)
	{
		for (var i = 0; i < this.horizontalLabels.length; i++)
		{
			this.labelsContainer.removeChild(this.horizontalLabels[i].label);
			this.labelsContainer.removeChild(this.horizontalLabels[i].line);
		}
	}
	
	this.horizontalLabels = new Array();
	this.createHorizontalLabels(this.offset, this.zoomLevel, 0, "bottom", true, this.horizontalLabels);

}

EventLine.prototype.blurEvent = function(index)
{
	var event = this.events[index]
	event.element.className = "event-line-event-label";
	document.getElementById(event.textId).className = "event-line-event";
}

EventLine.prototype.eventMouseOut = function(mouseEvent, index)
{
	this.blurEvent(index);
}

EventLine.prototype.highlightEvent = function(index)
{
	var event = this.events[index]
	event.element.className = "event-line-event-label-selected";
	document.getElementById(event.textId).className = "event-line-event-selected";
}

EventLine.prototype.eventMouseOver = function(mouseEvent, index)
{
	this.highlightEvent(index);

	/*
	
	if (this.selectedEvent && this.selectedEvent.element)
	{
		this.selectedEvent.element.className = "event-line-event-label";
		document.getElementById(this.selectedEvent.textId).className = "event-line-event";
	}
		
	this.selectedEvent = this.events[index];
	this.selectedEvent.element.className = "event-line-event-label-selected";
	document.getElementById(this.selectedEvent.textId).className = "event-line-event-selected";
	
	*/
}

EventLine.prototype.createHorizontalLabels = function(offset, zoomLevel, topPositon, verticalAlign, createLines, array)
{

	var topLabelWidth = 100;
	
	var viewSpan = this.zoomLevels[zoomLevel].viewSpan;
	var labelsSpacing = this.zoomLevels[zoomLevel].labelsSpacing;
	var barWidth = this.zoomLevels[zoomLevel].barWidth;
	var majorLabels = this.zoomLevels[zoomLevel].majorLabels;
	
	var firstLabelSlot = labelsSpacing * Math.ceil(offset / labelsSpacing) - offset;
	
	for (var i = firstLabelSlot; i < viewSpan; i += labelsSpacing)
	{
	
		var left = ((i * barWidth) + barWidth / 2) + this.leftLabelsWidth + this.leftLabelsMargin;
		var value = i + offset;
		var major = value % majorLabels == 0;
		
		var labelLine = null;
		if (createLines)
		{
			labelLine = document.createElement("div");
			labelLine.style.backgroundColor = major ? "#CCCCCC" : "#CCCCCC";
			labelLine.style.position = "absolute";
			labelLine.style.top = (this.topLabelsHeight + this.topLabelsMargin) + "px";
			labelLine.style.left = left + "px";
			labelLine.style.width = "1px";
			labelLine.style.height = this.graphHeight + "px";
		}

		var labelTable = document.createElement("table");
		var labelCell = labelTable.insertRow(0).insertCell(0);
		
		labelTable.style.position = "absolute";
		labelTable.style.top = topPositon + "px";
		labelTable.style.left = (left - topLabelWidth/2) + "px";
		labelCell.style.width = topLabelWidth + "px";
		labelCell.style.height = this.topLabelsHeight + "px";
		labelCell.style.textAlign = "center";
		labelCell.style.verticalAlign = verticalAlign;
		labelCell.style.fontWeight = major ? "bold" : "normal";

		labelCell.innerHTML = value;
		
		if (array) array.push(new EventLineHorizontalLabel(labelTable, labelLine));
		
		this.labelsContainer.appendChild(labelTable);
		if (createLines) this.labelsContainer.appendChild(labelLine);
		
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
			this.labelsContainer.removeChild(this.verticalLabels[i].label);
			this.labelsContainer.removeChild(this.verticalLabels[i].line);
		}
	}
	
	this.verticalLabels = new Array();
	
	var decimalPlaces = 0;
	if (this.verticalLabelsSpacing <= 1)
	{
		var pow = this.verticalLabelsSpacing;
		while (pow < 1)
		{
			decimalPlaces ++;
			pow *= 10;
		}
	}
	
	for (var i = 0; i <= this.viewportHeight; i += this.verticalLabelsSpacing)
	{

		var barHeight = Math.round((i / this.viewportHeight) * this.graphHeight);
		var top = this.graphHeight + this.topLabelsHeight + this.topLabelsMargin - barHeight;
		var major = i % this.verticalLabelsMajorSpacing == 0;
		
		var labelLine = document.createElement("div");
		labelLine.style.backgroundColor = major ? "#CCCCCC" : "#CCCCCC";
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

		labelCell.innerHTML = i.toFixed(decimalPlaces);
		
		this.verticalLabels.push(new EventLineVerticalLabel(labelTable, labelLine));
		this.labelsContainer.appendChild(labelTable);
		this.labelsContainer.appendChild(labelLine);

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
	//this.indicatorLabelElement.style.display = "block";
}

EventLine.prototype.graphMouseOut = function(event)
{
	this.indicatorElement.style.display = "none";
	//this.indicatorLabelElement.style.display = "none";
}

EventLine.prototype.graphMouseMove = function(event)
{

	var x = ElementUtils.getEventMouseElementX(event, this.indicatorContainerElement);
	
	if (x < 0) x = 0;
	if (x > this.graphWidth) x = this.graphWidth;

	var barWidth = this.zoomLevels[this.zoomLevel].barWidth;
	var pos = Math.floor(x / barWidth);

	for (var i = 0; i < this.graphs.length; i++)
	{
		var graph = this.graphs[i];
		var value = graph.currentY[pos];
		var legendValueElement = document.getElementById(graph.legendValueElementId);
		if (legendValueElement)	legendValueElement.innerHTML = ": " + value;
	}
	
	this.indicatorElement.style.left = (pos * barWidth) + "px";
	this.indicatorLabelElement.style.left = (pos * barWidth) + "px";

	/*

	var x = ElementUtils.getEventMouseElementX(event, this.indicatorContainerElement);
	
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
	
	var event = this.eventsLookup[pos + this.offset];
	if (event) label += "<br><b>" + event.text + "</b>";

	//this.indicatorElement.style.top = (this.graphHeight + this.graphOffsetTop - maxValue) + "px";
	//this.indicatorElement.style.height = maxValue + "px";

	this.indicatorElement.style.left = (pos * barWidth) + "px";
	this.indicatorLabelElement.style.left = (pos * barWidth) + "px";

	this.indicatorLabelElement.innerHTML = label;
	
	*/

}

EventLine.prototype.getSelectorWidthPx = function()
{
	return this.selectorBarWidth * this.zoomLevels[this.zoomLevel].viewSpan;
}

EventLine.prototype.getSelectorLeftPx = function()
{
	return this.selectorBarWidth * (this.offset - this.selectorOffset);
}

EventLine.prototype.getSelectorRightPx = function()
{
	return this.getSelectorLeftPx() + this.getSelectorWidthPx();
}

EventLine.prototype.selectorMouseDown = function(event)
{
	this.startDragX = event.clientX;
	EventAttacher.attachOnWindowEvent("mouseup", this, "selectorMouseUp");
	EventAttacher.attachOnWindowEvent("mousemove", this, "selectorMouseMove");
}

EventLine.prototype.selectorMouseMove = function(event)
{

	var dx = event.clientX - this.startDragX;
	
	var currentViewSpan = this.zoomLevels[this.zoomLevel].viewSpan;
	var selectorRight = this.selectorOffset + this.selectorViewSpan;
	
	var newOffset = Math.round(this.offset +  dx / this.selectorBarWidth);
	if (newOffset < this.selectorOffset) newOffset = this.selectorOffset;
	if (selectorRight < newOffset + currentViewSpan) newOffset = selectorRight - currentViewSpan;
	
	this.redrawSelector(newOffset, currentViewSpan);
}

EventLine.prototype.selectorMouseUp = function(event)
{
	var dx = event.clientX - this.startDragX;

	var currentViewSpan = this.zoomLevels[this.zoomLevel].viewSpan;
	var selectorRight = this.selectorOffset + this.selectorViewSpan;
	
	var newOffset = Math.round(this.offset +  dx / this.selectorBarWidth);
	if (newOffset < this.selectorOffset) newOffset = this.selectorOffset;
	if (selectorRight < newOffset + currentViewSpan) newOffset = selectorRight - currentViewSpan;
		
	this.refresh(this.zoomLevel, newOffset);
	
	EventAttacher.detachOnWindowEvent("mouseup", this, "selectorMouseUp");
	EventAttacher.detachOnWindowEvent("mousemove", this, "selectorMouseMove");
	
}

EventLine.prototype.leftSelectorMouseDown = function(event)
{
	EventAttacher.attachOnWindowEvent("mouseup", this, "leftSelectorMouseUp");
	EventAttacher.attachOnWindowEvent("mousemove", this, "leftSelectorMouseMove");
}

EventLine.prototype.leftSelectorMouseMove = function(event)
{

	var newLeft = ElementUtils.getEventMouseElementX(event, this.selectorContainer);

	var originalRight = this.getSelectorRightPx();
	if (newLeft < 0) newLeft = 0;
	if (newLeft > originalRight) newLeft = originalRight;
	
	this.redrawSelectorUsingLeftRight(newLeft, originalRight);

}

EventLine.prototype.leftSelectorMouseUp = function(event)
{
	
	var originalRight = this.getSelectorRightPx();

	var newLeft = ElementUtils.getEventMouseElementX(event, this.selectorContainer);
	var newWidth = originalRight - newLeft;
	
	if (newLeft < 0) newLeft = 0;
	if (newLeft > originalRight) newLeft = originalRight;

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

	var originalLeft = this.getSelectorLeftPx();
	
	var newRight = ElementUtils.getEventMouseElementX(event, this.selectorContainer);
	if (newRight < originalLeft) newRight = originalLeft;
	if (newRight > this.selectorWidth) newRight = this.graphWidth;
	
	var newWidth = newRight - originalLeft;
	var newViewSpan = Math.round(newWidth / this.selectorBarWidth);
	
	this.redrawSelector(this.offset, newViewSpan);

}

EventLine.prototype.rightSelectorMouseUp = function(event)
{
	
	var newRight = ElementUtils.getEventMouseElementX(event, this.selectorContainer);

	var originalLeft = this.getSelectorLeftPx();
	if (newRight < originalLeft) newRight = originalLeft;
	if (newRight > this.selectorWidth) newRight = this.graphWidth;
	
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