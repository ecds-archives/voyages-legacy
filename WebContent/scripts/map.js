var MapsGlobal = 
{

	MAP_TOOL_ZOOM: 1,
	MAP_TOOL_PAN: 2,
	MAP_TOOL_SELECTOR: 3,
	
	maps: new Array(),
	
	registerMap: function(
		mapId, // unique id
		fixedSize, // true/false
		formName, // name of the form
		actionNameFieldName, // custom action name (not used and not fully implemented now)
		actionParamFieldName, // custom action param (not used and not fully implemented now)
		mapControlId, // main container 
		mapTilesContainerId, // frame for the map
		zoomLevels, // available zooms and their configurations
		fieldNameX1, // name of the hidden field for x1
		fieldNameY1, // name of the hidden field for y1
		fieldNameX2, // name of the hidden field for x2
		fieldNameY2, // name of the hidden field for y2
		fieldNameZoomHistory, // field for serialized zoom history 
		buttonBackId, // back button
		buttonForwardId, // forward button
		buttonZoomPlusId, // zoom + button
		buttonZoomMinusId, // zoom - button
		sliderSlotWidth, // one zoom level width on the slider
		slideContainerId, // slider container
		sliderKnobId, // slider knob
		buttonPanId, // pan button
		buttonZoomId, // zoom button
		fieldNameForMouseMode, // mouse mode hidden field
		mapSizes, // available map sizes
		fieldNameForMapSize, // hidden field for selected map size
		pointsOfInterest, // array of points with labels
		lines, // array of lines
		bubbleId, // bubble <table>
		bubbleTextId, // inner <td> for the text in the bubble
		scaleIndicatorTextId,
		scaleIndicatorBarId,
		miniMapControlId,
		miniMapTilesContainerId,
		miniMapZoomLevel,
		miniMapToggleId,
		fieldNameMiniMapVisibility,
		miniMapPosition,
		miniMapWidth,
		miniMapHeight,
		pointsSelectId
	)
	{
	
		var map = new Map();
		this.maps[mapId] = map;
		
		// HTML
		map.fixedSize = fixedSize;
		map.mapControlId = mapControlId;
		map.tilesContainerId = mapTilesContainerId;
		
		// hidden fields for maitaining state
		map.formName = formName;
		map.actionNameFieldName = actionNameFieldName;
		map.actionParamFieldName = actionParamFieldName;
		map.fieldNameX1 = fieldNameX1;
		map.fieldNameY1 = fieldNameY1;
		map.fieldNameX2 = fieldNameX2;
		map.fieldNameY2 = fieldNameY2;
		map.fieldNameZoomHistory = fieldNameZoomHistory;
		
		// debug
		map.debug = location.href.indexOf("?debug-map") != -1;
		
		// zooms
		map.zoomLevels = zoomLevels;

		// points and lines
		map.points = pointsOfInterest;
		map.lines = lines;
		
		// bubble
		map.bubbleId = bubbleId;
		map.bubbleTextId = bubbleTextId;
		
		// scale indicator
		map.scaleBarIndicatorId = scaleIndicatorBarId;
		map.scaleTextIndicatorId = scaleIndicatorTextId;

		// list of places		
		map.pointsSelectId = pointsSelectId;
		
		// minimap
		if (miniMapControlId && miniMapTilesContainerId)
		{

			var miniMap = new Map();
			miniMap.fixedSize = true;
			miniMap.mapControlId = miniMapControlId;
			miniMap.tilesContainerId = miniMapTilesContainerId;
			miniMap.mainMap = map;
			miniMap.zoomLevels = [miniMapZoomLevel];
			miniMap.zoomLevel = 0;
			miniMap.blockMove = false;
			
			map.miniMap = miniMap;
			map.fieldNameMiniMapVisibility = fieldNameMiniMapVisibility;
			map.miniMapPosition = miniMapPosition;
			map.miniMapWidth = miniMapWidth;
			map.miniMapHeight = miniMapHeight;
			if (miniMapToggleId) map.miniMapToggleId = miniMapToggleId;

		}
		
		// button: back
		if (buttonBackId)
		{
			var btn = new MapButtonGoBack(buttonBackId, map);
			map.registerGoBackButton(btn);
		}

		// button: forward
		if (buttonForwardId)
		{
			var btn = new MapButtonGoForward(buttonForwardId, map);
			map.registerGoForwardButton(btn);
		}

		// button: zoom +
		if (buttonZoomPlusId)
			new MapButtonZoomPlus(buttonZoomPlusId, map);

		// button: zoom -
		if (buttonZoomMinusId)
			new MapButtonZoomMinus(buttonZoomMinusId, map);
			
		// zoom slider
		if (sliderSlotWidth && slideContainerId && sliderKnobId && zoomLevels.length > 1)
			new MapZoomSlider(sliderSlotWidth, slideContainerId, sliderKnobId, map);
			
		// zoom: pan/zoom
		if (buttonPanId && buttonZoomId)
			new MapZoomAndPanButtons(buttonPanId, buttonZoomId, formName, fieldNameForMouseMode, map);

		// zoom: sizes
		if (mapSizes && fieldNameForMapSize)
			new MapZoomSize(mapSizes, formName, fieldNameForMapSize, map);

		// call init after page loads
		EventAttacher.attachOnWindowEvent("load", map, "init", false);
	
	},
	
	setMouseModeToPan: function(mapId)
	{
		var map = MapsGlobal.maps[mapId];
		if (map) map.setMouseModeToPan();
	},
	
	setMouseModeToZoom: function(mapId)
	{
		var map = MapsGlobal.maps[mapId];
		if (map) map.setMouseModeToZoom();
	},
	
	zoomPlus: function(mapId)
	{
		var map = MapsGlobal.maps[mapId];
		if (map) map.zoomPlus();
	},

	zoomMinus: function(mapId)
	{
		var map = MapsGlobal.maps[mapId];
		if (map) map.zoomMinus();
	},

	zoomGoBack: function(mapId)
	{
		var map = MapsGlobal.maps[mapId];
		if (map) map.zoomGoBack();
	},
	
	zoomGoForward: function(mapId)
	{
		var map = MapsGlobal.maps[mapId];
		if (map) map.zoomGoForward();
	},
	
	zoomCanGoBack: function(mapId)
	{
		var map = MapsGlobal.maps[mapId];
		if (map) return map.zoomCanGoBack();
		return false;
	},
	
	zoomCanGoForward: function(mapId)
	{
		var map = MapsGlobal.maps[mapId];
		if (map) return map.zoomCanGoForward();
		return false;
	},

	setSize: function(mapId, width, height)
	{
		var map = MapsGlobal.maps[mapId];
		if (map) map.setSize(width, height);
	},
	
	invokeAction: function(name, param)
	{
		var map = MapsGlobal.maps[mapId];
		if (map) map.invokeAction(name, param);
	}

}

/////////////////////////////////////////////////////////
// utilities
/////////////////////////////////////////////////////////

var MapUtils =
{

	computeLength: function(x, y)
	{
		return Math.sqrt(MapUtils.computeLengthSquared(x, y));
	},
	
	computeLengthSquared: function(x, y)
	{
		return x*x + y*y;
	},
	
	computeDistancePointLine: function(px, py, x1, y1, x2, y2)
	{
		return Math.abs(
			(x2-x1)*(y1-py) - (x1-px)*(y2-y1)) /
			MapUtils.computeLength(x2-x1, y2-y1);
	},
	
	computeAngle: function(x1, y1, x2, y2, x3, y3)
	{
		var v1x = x1-x2;
		var v1y = y1-y2;
		var v2x = x3-x2;
		var v2y = y3-y2;
		return Math.acos (
			(v1x*v2x + v1y*v2y) /
			(MapUtils.computeLength(v1x, v1y) * MapUtils.computeLength(v2x, v2y)));
	},
	
	falseFnc: function()
	{
		return false;
	}

}

/////////////////////////////////////////////////////////
// main map object
/////////////////////////////////////////////////////////

function Map()
{

	this.zoomLevel = 0;

	// for numbering of tiles,
	// the number of digits in a tile number
	this.tilesNameNumberLength = 2;

	// zoom history
	this.zoomHistory = [];
	this.zoomHistoryPos = -1;
	this.zoomHistoryMax = 100;
	
	// zooming and panning
	this.mouseMode = MapsGlobal.MAP_TOOL_PAN;
	this.selectorBorderWidth = 1;
	this.selectorColor = "White"; //"#0066CC";
	this.selectorOpacity = 30;
	
	// selector mode,
	// the border around the map
	this.selectorBorder = 30;
	
	//By default prevent moving map out of view
	this.blockMove = false;
	
	// init handler
	this.initListeners = new EventQueue();
	
	this.onZoomChange = null;
	this.updateFieldId = null;
	
}

/////////////////////////////////////////////////////////
// some internal objects
/////////////////////////////////////////////////////////

function MapZoomState(x1, x2, y1, y2)
{
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;
}

function MapTile(img)
{
	this.img = img;
	this.points = null;
	this.url = "";
	this.oldUrl = "";
}

function PointOfInterest(x, y, showAtZoom, label, text, symbols)
{
	this.x = x;
	this.y = y;
	this.showAtZoom = showAtZoom;
	this.label = label;
	this.text = text;
	this.symbols = symbols;
}

function MapLine(x1, y1, x2, y2, symbolSpacing, symbol)
{
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;
	this.symbolSpacing = symbolSpacing;
	this.symbol = symbol;
}

function MapSymbol(name, url, width, height, centerX, centerY)
{
	this.name = name;
	this.url = url;
	this.width = width;
	this.height = height;
	this.centerX = centerX;
	this.centerY = centerY;
}

function MapZoomLevel(tileWidth, tileHeight, bottomLeftTileX, bottomLeftTileY, tilesNumX, tilesNumY, scale, tilesDir)
{
	this.tileWidth = tileWidth;
	this.tileHeight = tileHeight;
	this.bottomLeftTileX = bottomLeftTileX;
	this.bottomLeftTileY = bottomLeftTileY;
	this.tilesNumX = tilesNumX;
	this.tilesNumY = tilesNumY;
	this.scale = scale; // (1px ~ scale degrees)
	this.tilesDir = tilesDir;
}

MapZoomLevel.prototype.getMapX1 = function() 
{
	return this.bottomLeftTileX; 
}

MapZoomLevel.prototype.getMapX2 = function() 
{
	return this.bottomLeftTileX + this.tilesNumX * this.tileWidth * this.scale; 
}

MapZoomLevel.prototype.getMapY1 = function() 
{
	return this.bottomLeftTileY; 
}

MapZoomLevel.prototype.getMapY2 = function() 
{
	return this.bottomLeftTileY + this.tilesNumY * this.tileHeight * this.scale; 
}

/////////////////////////////////////////////////////////
// generic functions for coordinates etc.
/////////////////////////////////////////////////////////

Map.prototype.getVportRatio = function() 
{
	return this.getVportWidth() / this.getVportHeight();
}

Map.prototype.getVportWidth = function()
{
	return this.vportWidth;
}

Map.prototype.getVportHeight = function()
{
	return this.vportHeight;
}

Map.prototype.fromVportToRealX = function(x)
{
	return this.zoomLevels[this.zoomLevel].bottomLeftTileX + 
		this.bottomLeftTileCol * this.getTileRealWidth() +
		this.fromPxToReal(x - this.bottomLeftTileVportX);
}

Map.prototype.fromVportToRealY = function(y)
{
	return this.zoomLevels[this.zoomLevel].bottomLeftTileY + 
		this.bottomLeftTileRow * this.getTileRealHeight() +
		this.fromPxToReal(this.vportHeight - y - this.bottomLeftTileVportY);
}

Map.prototype.fromRealToVportX = function(x)
{
	return this.fromRealToPx(x -
		(this.zoomLevels[this.zoomLevel].bottomLeftTileX + this.bottomLeftTileCol * this.getTileRealWidth())) +
		this.bottomLeftTileVportX;
}

Map.prototype.fromRealToVportY = function(y)
{
	return - this.fromRealToPx(y -
		(this.zoomLevels[this.zoomLevel].bottomLeftTileY + this.bottomLeftTileRow * this.getTileRealHeight())) -
		this.bottomLeftTileVportY + this.vportHeight;
}

Map.prototype.getMapX1 = function()
{
	return this.fromVportToRealX(0);
}

Map.prototype.getMapY2 = function()
{
	return this.fromVportToRealY(0);
}

Map.prototype.getMapX2 = function()
{
	return this.fromVportToRealX(this.getVportWidth());
}

Map.prototype.getMapY1 = function()
{
	return this.fromVportToRealY(this.getVportHeight());
}

Map.prototype.getCenterY = function()
{
	return this.fromVportToRealY(0.5 * this.getVportHeight());
}

Map.prototype.getCenterX = function()
{
	return this.fromVportToRealX(0.5 * this.getVportWidth());
}

Map.prototype.getTileRealWidth = function()
{
	return this.fromPxToReal(this.getTileWidth());
}

Map.prototype.getTileRealHeight = function()
{
	return this.fromPxToReal(this.getTileHeight());
}

Map.prototype.getTileWidth = function()
{
	return this.zoomLevels[this.zoomLevel].tileWidth;
}

Map.prototype.getTileHeight = function()
{
	return this.zoomLevels[this.zoomLevel].tileHeight;
}

Map.prototype.fromPxToReal = function(v)
{
	return v * this.zoomLevels[this.zoomLevel].scale;
}

Map.prototype.fromRealToPx = function(v)
{
	return v / this.zoomLevels[this.zoomLevel].scale;
}

Map.prototype.roundAndCapScale = function(s)
{
	s = Math.round(s);
	if (s > this.scaleMax) s = this.scaleMax;
	if (s < this.scaleMin) s = this.scaleMin;
	return s;
}

Map.prototype.isPointInsideVport = function(vx, vy)
{
	return 0 <= vx && vx < this.getVportWidth() && 0 <= vy && vy < this.getVportHeight();
}

Map.prototype.getClosestZoomLevel = function(scale)
{
	var minIndx = -1;
	var minDiff = 0;
	for (var i = 0; i < this.zoomLevels.length; i++)
	{
		var diff = Math.abs(this.zoomLevels[i].scale - scale);
		if (minIndx == -1 || diff < minDiff)
		{
			minIndx = i;
			minDiff = diff;
		}
	}
	return minIndx;
}

/////////////////////////////////////////////////////////
// selector drawing and hiding
/////////////////////////////////////////////////////////

Map.prototype.drawSelector = function(x1, y1, x2, y2)
{

	// round
	x1 = Math.round(x1);
	y1 = Math.round(y1);
	x2 = Math.round(x2);
	y2 = Math.round(y2);

	// make sure that they are in correct order
	if (x1 > x2)
	{
		var x = x2;
		x2 = x1;
		x1 = x;
	}
	if (y1 > y2)
	{
		var y = y2;
		y2 = y1;
		y1 = y;
	}
	
	// border size
	x2 -= 3*this.selectorBorderWidth;
	y2 -= 3*this.selectorBorderWidth;
	
	// non-neg size
	if (x2 < x1) x2 = x1;
	if (y2 < y1) y2 = y1;

	// draw
	this.selector.style.display = "block";
	this.selector.style.left = (x1) + "px";
	this.selector.style.top = (y1) + "px";
	this.selector.style.width = (x2-x1) + "px";
	this.selector.style.height = (y2-y1) + "px";

}

Map.prototype.hideSelector = function()
{
	this.selector.style.display = "none";
}

/////////////////////////////////////////////////////////
// selector mode functions
/////////////////////////////////////////////////////////

Map.prototype.hideSelector.getSelectorX1 = function()
{
	return this.selectorX1;
}

Map.prototype.hideSelector.getSelectorY1 = function()
{
	return this.selectorY1;
}

Map.prototype.hideSelector.getSelectorX2 = function()
{
	return this.selectorX2;
}

Map.prototype.hideSelector.getSelectorY2 = function()
{
	return this.selectorY2;
}

/////////////////////////////////////////////////////////
// scale indicator
/////////////////////////////////////////////////////////

Map.prototype.formatMetersForDisplay = function(meters)
{
	if (meters >= 1000)
		return (meters/1000) + "&nbsp;km";
	else
		return (meters) + "&nbsp;m";
}

Map.prototype.updateScaleIndicator = function()
{

	if (!this.hasScaleIndicator)
		return;

	var meters = this.fromPxToReal(100);
	var digits = Math.floor(Math.log(meters) / Math.LN10);
	var closest_power = Math.pow(10, digits);
	meters = Math.floor(meters / closest_power) * closest_power;
	var pixels = this.fromRealToPx(meters);
	
	this.scaleBarIndicator.style.width = Math.round(pixels) + "px";
	this.scaleTextIndicator.innerHTML = meters + "&deg;"  //this.formatMetersForDisplay(meters);

}

Map.prototype.scaleIndicatorInit = function()
{
	this.scaleBarIndicator = document.getElementById(this.scaleBarIndicatorId);
	this.scaleTextIndicator = document.getElementById(this.scaleTextIndicatorId);
	this.hasScaleIndicator = this.scaleBarIndicator && this.scaleTextIndicator;
}

/////////////////////////////////////////////////////////
// map zoom/pan
/////////////////////////////////////////////////////////

Map.prototype.mapStartDrag = function(event)
{

	// close label/bubble
	this.hideLabels(true);
	
	// disable debug
	if (this.debug)
		this.disableDebugShowCoordinates();

	// set onMouseMove handler
	EventAttacher.attach(document, "mousemove", this, "mapMouseMove");
	EventAttacher.attach(document, "mouseup", this, "mapStopDrag");
	
	// init position
	this.draggingStartX = ElementUtils.getEventMouseElementX(event, this.mapControl);
	this.draggingStartY = ElementUtils.getEventMouseElementY(event, this.mapControl);
	
}

Map.prototype.mapStopDrag = function(event)
{

	// cancel onMouseMove handler
	EventAttacher.detach(document, "mousemove", this, "mapMouseMove");
	EventAttacher.detach(document, "mouseup", this, "mapStopDrag");

	// new position
	var x = ElementUtils.getEventMouseElementX(event, this.mapControl);
	var y = ElementUtils.getEventMouseElementY(event, this.mapControl);
	
	switch (this.mouseMode)
	{
		case MapsGlobal.MAP_TOOL_ZOOM:
		
			// hide selector
			this.hideSelector();
			
			// has the mouse moved?
			if (this.draggingStartX == x && this.draggingStartY == y)
				return;
			
			// and zoom
			this.zoomMapTo(
				this.fromVportToRealX(this.draggingStartX),
				this.fromVportToRealY(this.draggingStartY),
				this.fromVportToRealX(x),
				this.fromVportToRealY(y),
				true, /* saveState */
				true, /* notifyZoomChange */
				false, /* updateMainMap */
				true /* updateMiniMap */ );
			
			break;
			
		case MapsGlobal.MAP_TOOL_PAN:
		case MapsGlobal.MAP_TOOL_SELECTOR:
		
			// precompute points position w.r.t. the new vport
			this.updatePoints();
			this.updateLines(false);
			
			// update associated map
			this.updateMainMap();
			this.updateMiniMap();

			break;
			
	}
	
	// enable debug
	if (this.debug)
		this.enableDebugShowCoordinates();
	
}

Map.prototype.mapMouseMove = function(event)
{

	// new position
	var x = ElementUtils.getEventMouseElementX(event, this.mapControl);
	var y = ElementUtils.getEventMouseElementY(event, this.mapControl);
	
	switch (this.mouseMode)
	{
		case MapsGlobal.MAP_TOOL_PAN:
		case MapsGlobal.MAP_TOOL_SELECTOR:
		
			// position change
			var dx =  (x - this.draggingStartX);
			var dy = -(y - this.draggingStartY);

			// remember for the next turn
			this.draggingStartX = x;
			this.draggingStartY = y;
			
			// pan
			this.panMapBy(dx, dy);
			
			// if selector -> recompute its position
			if (this.mouseMode == MapsGlobal.MAP_TOOL_SELECTOR)
			{
				this.selectorX1 = this.fromVportToRealX(this.selectorVportX1);
				this.selectorY1 = this.fromVportToRealX(this.selectorVportY1);
				this.selectorX2 = this.fromVportToRealX(this.selectorVportX2);
				this.selectorY2 = this.fromVportToRealX(this.selectorVportY2);
			}

			break;
			
		case MapsGlobal.MAP_TOOL_ZOOM:

			// draw selector
			this.drawSelector(
				this.draggingStartX, this.draggingStartY,
				x, y);

			break;
	
	}
	
}

Map.prototype.mapMouseWheel = function(event)
{
	// change the zoom
	if (event.wheelDelta < 0)
	{
		this.zoomPlus();
	}
	else
	{
		this.zoomMinus();
	}
	
	// we don't want to scroll the page
	event.cancelBubble = true;
	return false;
	
}

/////////////////////////////////////////////////////////
// map size
/////////////////////////////////////////////////////////

Map.prototype.zoomPlus = function(notifyZoomChange)
{
	this.changeZoomLevel(this.zoomLevel + 1, notifyZoomChange);
}

Map.prototype.zoomMinus = function(notifyZoomChange)
{
	this.changeZoomLevel(this.zoomLevel - 1, notifyZoomChange);
}

// to be changed ...
/*
Map.prototype.changeScaleNormalized = function(t, notifyZoomChange)
{
	this.changeScale(
		this.scaleMin + t * (this.scaleMax - this.scaleMin),
		notifyZoomChange);
}

Map.prototype.getScaleNormalized = function()
{
	return (this.scale - this.scaleMin) / (this.scaleMax - this.scaleMin);
}
*/

Map.prototype.registerZoomSlider = function(zoomSlider)
{
	this.zoomSlider = zoomSlider;
}

Map.prototype.notifyZoomChange = function()
{
	if (this.zoomSlider) this.zoomSlider.zoomChanged();
}

Map.prototype.changeZoomLevel = function(newZoomLevel, notifyZoomChange)
{

	// make sure that the zoom level is ok
	if (newZoomLevel < 0) newZoomLevel = 0;
	if (this.zoomLevels.length <= newZoomLevel) newZoomLevel = this.zoomLevels.length - 1;
	
	// has it changed?
	if (newZoomLevel == this.zoomLevel) return;
	
	// get old center
	var cx = this.getCenterX();
	var cy = this.getCenterY();
	
	// center
	this.setZoomAndCenterTo(newZoomLevel, cx, cy, true, notifyZoomChange, true, true, true);

}

// PRIVATE
Map.prototype.zoomMapToInternal = function(x1, y1, x2, y2, border, saveState, notifyZoomChange, updateMainMap, updateMiniMap)
{

	// make sure that x1 < x2 and y1 < y2
	if (x1 > x2)
	{
		var tmp = x1;
		x1 = x2;
		x2 = tmp;
	}
	if (y1 > y2)
	{
		var tmp = y1;
		y1 = y2;
		y2 = tmp;
	}
	
	// current viewport size (in pixels)
	var vportWidth = this.getVportWidth();
	var vportHeight = this.getVportHeight();
	
	// make sure that requested rectangle has the same ratio as the vport
	if ((x2 - x1) / (y2 - y1) > vportWidth / vportHeight)
	{
		var newDy = (x2 - x1) * (vportHeight / vportWidth);
		var cy = (y1+y2)/2; 
		y1 = cy - newDy/2; 
		y2 = cy + newDy/2; 
	}
	else
	{
		var newDx = (y2 - y1) * (vportWidth / vportHeight);
		var cx = (x1+x2)/2; 
		x1 = cx - newDx/2; 
		x2 = cx + newDx/2; 
	}
	
	// find the best scale
	var targetScale =  (x2-x1) / vportWidth;
	var bestScaleDist;
	var newZoomLevel = -1;
	for (var i = 0; i < this.zoomLevels.length; i++)
	{
		var thisDist = Math.abs(this.zoomLevels[i].scale - targetScale);
		if (newZoomLevel == -1 || thisDist < bestScaleDist)
		{
			bestScaleDist = thisDist;
			newZoomLevel = i;
		}
	}
	
	// zoom
	this.setZoomAndCenterTo(
		newZoomLevel,
		(x1 + x2) / 2,
		(y1 + y2) / 2,
		saveState,
		notifyZoomChange,
		updateMainMap,
		updateMiniMap,
		true);
	
	/*
	
	// the given extents can change (due the the actual size of the map)
	var adjustedX1; 
	var adjustedX2; 
	var adjustedY1; 
	var adjustedY2;

	// find optimal zoom level, i.e. the closest
	// one which contains all, or the first one
	var newZoomLevel = 0;
	for (var i = this.zoomLevels.length - 1; 0 <= i; i--)
	{
		var zoomLevelObj = this.zoomLevels[i]; 

		// adjust x1, y1, x2, y2 so that they do not fall outside the map itself
		var adjustedX1 = Math.max(zoomLevelObj.getMapX1(), x1); 
		var adjustedX2 = Math.min(zoomLevelObj.getMapX2(), x2); 
		var adjustedY1 = Math.max(zoomLevelObj.getMapY1(), y1); 
		var adjustedY2 = Math.min(zoomLevelObj.getMapY2(), y2);
		
		// calculate the px width and height 
		var widthPx = (adjustedX2 - adjustedX1) / zoomLevelObj.scale;
		var heightPx = (adjustedY2 - adjustedY1) / zoomLevelObj.scale;
		
		// does it fit?
		if (widthPx + 2*border <= vportWidth && heightPx + 2*border <= vportHeight)
		{
			newZoomLevel = i;
			break;
		}
		
	}
	
	// zoom
	this.setZoomAndCenterTo(
		newZoomLevel,
		(adjustedX1 + adjustedX2) / 2,
		(adjustedY1 + adjustedY2) / 2,
		saveState,
		notifyZoomChange,
		updateMainMap,
		updateMiniMap,
		true);

	*/
	
}

// PUBLIC
Map.prototype.zoomMapTo = function(x1, y1, x2, y2, saveState, notifyZoomChange, updateMainMap, updateMiniMap)
{

	// does not make sense in selector mode
	if (this.mouseMode == MapsGlobal.MAP_TOOL_SELECTOR)
		return;
		
	// zoom
	this.zoomMapToInternal(
		x1, y1, x2, y2,
		0,
		saveState,
		notifyZoomChange,
		updateMainMap,
		updateMiniMap);

}

// PUBLIC
Map.prototype.positionBySelector = function(x1, y1, x2, y2, saveState, notifyZoomChange, updateMainMap, updateMiniMap)
{

	// works only in selector mode
	if (this.mouseMode != MapsGlobal.MAP_TOOL_SELECTOR)
		return;

	// change selector position
	this.selectorX1 = x1;
	this.selectorY1 = y1;
	this.selectorX2 = x2;
	this.selectorY2 = y2;
	
	// zoom
	this.zoomMapToInternal(
		x1, y1, x2, y2,
		this.selectorBorder,
		saveState,
		notifyZoomChange,
		updateMainMap,
		updateMiniMap);
	
}

// PUBLIC
Map.prototype.centerTo = function(x, y, saveState, notifyZoomChange, updateMainMap, updateMiniMap)
{
	this.setZoomAndCenterTo(
		this.zoomLevel,
		x, y,
		saveState,
		notifyZoomChange,
		updateMainMap,
		updateMiniMap,
		true);
}

// PRIVATE
Map.prototype.setZoomAndCenterTo = function(newZoomLevel, x, y, saveState, notifyZoomChange, updateMainMap, updateMiniMap, resetSelectedPoint)
{

	// close label/bubble
	this.hideLabels(resetSelectedPoint);

	// make sure that the zoom level is ok
	if (newZoomLevel < 0) newZoomLevel = 0;
	if (this.zoomLevels.length <= newZoomLevel)
		newZoomLevel = this.zoomLevels.length - 1;
	
	// has the zoom changed?
	if (this.zoomLevel != newZoomLevel)
	{
		this.zoomLevel = newZoomLevel;
		this.updateScaleIndicator();
	}
	
	// calculate the main state variables:
	//   * this.bottomLeftTileCol
	//   * this.bottomLeftTileRow
	//   * this.bottomLeftTileVportX
	//   * this.bottomLeftTileVportY
	// it's safer to do all calculations by hand than rely
	// on the helper functions since the helper functions 
	// depend on the four previous variables
	var zoomLevelObj = this.zoomLevels[this.zoomLevel];
	var vportRealWidth = zoomLevelObj.scale * this.vportWidth;
	var vportRealHeight = zoomLevelObj.scale * this.vportHeight;
	var tileRealWidth = zoomLevelObj.scale * zoomLevelObj.tileWidth;
	var tileRealHeight = zoomLevelObj.scale * zoomLevelObj.tileHeight;
	
	// special case, map it smaller than the viewport in X
	if (zoomLevelObj.tilesNumX * tileRealWidth <= vportRealWidth)
	{
		this.bottomLeftTileCol = 0;
		this.bottomLeftTileVportX = Math.round((vportRealWidth - zoomLevelObj.tilesNumX * tileRealWidth) / zoomLevelObj.scale / 2);
	}
	
	// map is bigger than the viewport in X
	else
	{
		x = Math.min(x, zoomLevelObj.getMapX2() - vportRealWidth / 2);
		x = Math.max(x, zoomLevelObj.getMapX1() + vportRealWidth / 2);
		var vportBottomLeftRealX = x - vportRealWidth / 2;
		this.bottomLeftTileCol = Math.floor((vportBottomLeftRealX - zoomLevelObj.bottomLeftTileX) / tileRealWidth);
		this.bottomLeftTileVportX = Math.round(((this.bottomLeftTileCol * tileRealWidth + zoomLevelObj.bottomLeftTileX) - vportBottomLeftRealX) / zoomLevelObj.scale);
	}
	
	// special case, map it smaller than the viewport in Y
	if (zoomLevelObj.tilesNumY * tileRealHeight <= vportRealHeight)
	{
		this.bottomLeftTileRow = 0;
		this.bottomLeftTileVportY = Math.round((vportRealHeight - zoomLevelObj.tilesNumY * tileRealHeight) / zoomLevelObj.scale / 2);
	}

	// map is bigger than the viewport in Y
	else
	{
		y = Math.min(y, zoomLevelObj.getMapY2() - vportRealHeight / 2);
		y = Math.max(y, zoomLevelObj.getMapY1() + vportRealHeight / 2);
		var vportBottomLeftRealY = y - vportRealHeight / 2;
		this.bottomLeftTileRow = Math.floor((vportBottomLeftRealY - zoomLevelObj.bottomLeftTileY) / tileRealHeight);
		this.bottomLeftTileVportY = Math.round(((this.bottomLeftTileRow * tileRealHeight + zoomLevelObj.bottomLeftTileY) - vportBottomLeftRealY) / zoomLevelObj.scale);
	}

	// draw all tiles; this can also fix the position
	// if it's outside the correct range
	this.positionTiles();
	
	// points of interest and lines
	this.updatePoints();
	this.updateLines(true);

	// position selector (if in selector mode)
	if (this.mouseMode == MapsGlobal.MAP_TOOL_SELECTOR)
	{
		this.selectorVportX1 = this.fromRealToVportX(this.selectorX1);
		this.selectorVportY1 = this.fromRealToVportY(this.selectorY1);
		this.selectorVportX2 = this.fromRealToVportX(this.selectorX2);
		this.selectorVportY2 = this.fromRealToVportY(this.selectorY2);
		this.drawSelector(this.selectorVportX1, this.selectorVportY1, this.selectorVportX2, this.selectorVportY2);
	}
	
	// update associated main/mini-map
	if (updateMainMap) this.updateMainMap();
	if (updateMiniMap) this.updateMiniMap();
	
	// remember current state
	if (saveState)
		this.zoomSaveState();
	
	// notify change of zoom
	if (notifyZoomChange)
		this.notifyZoomChange();
		
		
}

/////////////////////////////////////////////////////////
// main/mini-map interaction
/////////////////////////////////////////////////////////

Map.prototype.show = function()
{
	this.mapControl.style.visibility = "visible";
}

Map.prototype.hide = function()
{
	this.mapControl.style.visibility = "hidden";
}

Map.prototype.isVisible = function()
{
	return this.mapControl.style.visibility != "hidden";
}

Map.prototype.initMiniMap = function()
{

	if (!this.miniMap)
		return;
		
	this.miniMapToggle = document.getElementById(this.miniMapToggleId);
	
	if (this.miniMapToggle)
		EventAttacher.attach(this.miniMapToggle, "click", this, "hideShowMiniMap")

	this.fieldMiniMapVisibility =
		document.forms[this.formName].elements[this.fieldNameMiniMapVisibility];

}

Map.prototype.getCssClassForMinimapToggleButton = function(visible)
{
	if (visible)
	{
		if (this.miniMapPosition == "top left")
			return "minimap-toggle-nw-expanded";
		if (this.miniMapPosition == "top right")
			return "minimap-toggle-ne-expanded";
		if (this.miniMapPosition == "bottom left")
			return "minimap-toggle-sw-expanded";
		if (this.miniMapPosition == "bottom right")
			return "minimap-toggle-se-expanded";
	}
	else
	{
		if (this.miniMapPosition == "top left")
			return "minimap-toggle-nw-collapsed";
		if (this.miniMapPosition == "top right")
			return "minimap-toggle-ne-collapsed";
		if (this.miniMapPosition == "bottom left")
			return "minimap-toggle-sw-collapsed";
		if (this.miniMapPosition == "bottom right")
			return "minimap-toggle-se-collapsed";
	}
}

Map.prototype.getCssClassForMinimap = function()
{
	if (this.miniMapPosition == "top left")
		return "minimap-control-nw";
	if (this.miniMapPosition == "top right")
		return "minimap-control-ne";
	if (this.miniMapPosition == "bottom left")
		return "minimap-control-sw";
	if (this.miniMapPosition == "bottom right")
		return "minimap-control-se";
}

Map.prototype.hideShowMiniMap = function()
{

	if (!this.miniMap)
		return;
	
	if (this.miniMap.isVisible())
	{
	
		var miniMapHideFunction = function(miniMap) {miniMap.hide()};
		var a = new Animation(this.miniMap.mapControl, 10, 400, miniMapHideFunction, this.miniMap);
		//a.setSizes(this.miniMapWidth, this.miniMapHeight, 0, 0);
		a.setOpacities(1, 0);
		a.start();
		
		this.fieldMiniMapVisibility.value = "false";
		this.miniMapToggle.className = this.getCssClassForMinimapToggleButton(false);
		
	}
	else
	{

		this.miniMap.show();
		var a = new Animation(this.miniMap.mapControl, 10, 400);
		//a.setSizes(0, 0, this.miniMapWidth, this.miniMapHeight);
		a.setOpacities(0, 1);
		a.start();

		this.fieldMiniMapVisibility.value = "true";
		this.miniMapToggle.className = this.getCssClassForMinimapToggleButton(true);

	}

}

Map.prototype.updateMiniMap = function()
{

	if (!this.miniMap)
		return;
		
	this.miniMap.positionBySelector(
		this.getMapX1(),
		this.getMapY1(),
		this.getMapX2(),
		this.getMapY2(),
		false, // no save state
		false, // no notify zoom change
		false, // no update minimap
		false  // no update mainmap
	); 

}

Map.prototype.updateMainMap = function()
{

	if (!this.mainMap)
		return;

	this.mainMap.centerTo(
		this.getCenterX(),
		this.getCenterY(),
		false, // no save state
		false, // no notify zoom change (since we are panning)
		false, // no update minimap
		false  // no update mainmap
	);

}

/////////////////////////////////////////////////////////
// main drawing procedures
/////////////////////////////////////////////////////////

Map.prototype.saveState = function()
{
	if (this.hasHiddenExtentsFields)
	{
		this.fieldX1.value = this.getMapX1();
		this.fieldX2.value = this.getMapX2();
		this.fieldY1.value = this.getMapY1();
		this.fieldY2.value = this.getMapY2();
	}
}

Map.prototype.positionTiles = function()
{

	var tileWidth = this.getTileWidth();
	var tileHeight = this.getTileHeight();

	this.saveState();
	
	for (var i = 0; i <= this.visibleRows; i++)
	{
		for (var j = 0; j <= this.visibleCols; j++)
		{
			var tile = this.tilesMap[i][j];
			var col = this.bottomLeftTileCol + j;
			var row = this.bottomLeftTileRow + i;
			var newUrl = this.createTileUrl(col, row);
			if (newUrl != tile.url)
			{
				tile.url = newUrl;
				tile.img.style.visibility = "hidden";
				tile.img.src = newUrl;
			}
			tile.img.style.left = (this.bottomLeftTileVportX + (j * tileWidth)) + "px";
			tile.img.style.top = (this.vportHeight - tileHeight - (this.bottomLeftTileVportY + (i * tileHeight))) + "px";
		}
	}

}

Map.prototype.panMapBy = function(dx, dy)
{

	// we may need this often
	var tilesNumX = this.zoomLevels[this.zoomLevel].tilesNumX;
	var tilesNumY = this.zoomLevels[this.zoomLevel].tilesNumY;
	var tileWidth = this.getTileWidth();
	var tileHeight = this.getTileHeight();
	var vportWidth = this.getVportWidth();
	var vportHeight = this.getVportHeight();
	
	// more only if the map is bigger than the viewport
	if (tilesNumX * tileWidth > this.vportWidth)
	{
	
		// move the viewport
		this.bottomLeftTileVportX += dx;

		// make sure that we are not outside the map		
		var zoomLevelObj = this.zoomLevels[this.zoomLevel];
		if (this.getMapX1() < zoomLevelObj.getMapX1())
		{
			this.bottomLeftTileVportX =
				this.bottomLeftTileCol * tileWidth;
		}
		else if (zoomLevelObj.getMapX2() < this.getMapX2())
		{
			this.bottomLeftTileVportX =
				(this.bottomLeftTileCol * tileWidth) -
				(tilesNumX * tileWidth - vportWidth); 
		}
		
		// move columns from right to left
		while (0 < this.bottomLeftTileVportX)
		{
		
			this.bottomLeftTileVportX -= tileWidth;
			this.bottomLeftTileCol--;
			
			for (var i = 0; i < this.visibleRows + 1; i++)
			{
				var mapTile = this.tilesMap[i].pop();
				this.tilesMap[i].unshift(mapTile);
			}
			
		}

		// move columns from left to right
		while (this.bottomLeftTileVportX + (this.visibleCols+1) * tileWidth < this.vportWidth)
		{
		
			this.bottomLeftTileVportX += tileWidth;
			this.bottomLeftTileCol++;
			
			for (var i = 0; i < this.visibleRows + 1; i++)
			{
				var mapTile = this.tilesMap[i].shift();
				this.tilesMap[i].push(mapTile);
			}
			
		}
		
	}
	
	// more only if the map is bigger than the viewport
	if (tilesNumY * tileHeight > this.vportHeight)
	{
	
		// move the viewport
		this.bottomLeftTileVportY += dy;
		
		// make sure that we are not outside the map		
		var zoomLevelObj = this.zoomLevels[this.zoomLevel];
		if (this.getMapY1() < zoomLevelObj.getMapY1())
		{
			this.bottomLeftTileVportY =
				this.bottomLeftTileRow * tileHeight;
		}
		else if (zoomLevelObj.getMapY2() < this.getMapY2())
		{
			this.bottomLeftTileVportY =
				(this.bottomLeftTileRow * tileHeight) -
				(tilesNumY * tileHeight - vportHeight); 
		}

		// move rows from bottom to top
		while (0 < this.bottomLeftTileVportY)
		{
		
			this.bottomLeftTileVportY -= tileHeight;
			this.bottomLeftTileRow --;
			
			var firstMapRow = this.tilesMap.pop();
			this.tilesMap.unshift(firstMapRow);
			
		}
		
		// move rows from top to bottom
		while (this.bottomLeftTileVportY + (this.visibleRows + 1) * tileHeight < this.vportHeight)
		{
		
			this.bottomLeftTileVportY += tileHeight;
			this.bottomLeftTileRow ++;
			
			var lastMapRow = this.tilesMap.shift();
			this.tilesMap.push(lastMapRow);
			
		}

	}
	
	// draw all tiles; this can also fix the position
	// if it's outside the correct range
	this.positionTiles();

	// update points and lines			
	this.updatePoints();
	this.updateLines(false);

}

Map.prototype.formatTileNumber = function(num)
{
	var numStr = num.toString();
	for (var i = 0; i < this.tilesNameNumberLength - numStr.length; i++) numStr = "0" + numStr;
	return numStr;
}

Map.prototype.createTileUrl = function(col, row)
{
	return this.zoomLevels[this.zoomLevel].tilesDir + "/" + 
		this.formatTileNumber(row) + "-" +
		this.formatTileNumber(col) + ".png";
}

/////////////////////////////////////////////////////////
// zooming history
/////////////////////////////////////////////////////////

Map.prototype.zoomSaveState = function()
{
	
	// current state
	var state = new MapZoomState(
		this.getMapX1(),
		this.getMapX2(),
		this.getMapY1(),
		this.getMapY2());

	// delete elements after current element
	while (this.zoomHistoryPos + 1 < this.zoomHistory.length)
		this.zoomHistory.pop();
	
	// save the state
	this.zoomHistoryPos++;
	this.zoomHistory.push(state);
	
	// too many?
	while (this.zoomHistoryMax < this.zoomHistory.length)
	{
		this.zoomHistory.shift();
		this.zoomHistoryPos--;
	}

	// save the entire history
	this.zoomHistorySave();

	// let buttons know
	this.zoomHistoryFireEvents();

}

Map.prototype.zoomHistoryMoveAndRestore = function(dir)
{

	// check
	var new_pos = this.zoomHistoryPos + dir;
	if (!(0 <= new_pos && new_pos < this.zoomHistory.length))
		return;

	// restore
	var state = this.zoomHistory[new_pos];
	
	// find the best zoom and zoom to it
	this.zoomMapTo(
		state.x1,
		state.y1,
		state.x2,
		state.y2,
		false, // saveState
		true,  // notifyZoomChange
		false, // updateMainMap
		true); // resetSelectedPoint

	// goto
	this.zoomHistoryPos = new_pos;
	
	// let buttons know
	this.zoomHistoryFireEvents();
	
	// save the entire history
	this.zoomHistorySave();

}

Map.prototype.zoomHistorySave = function()
{

	// check if we care about saving the state
	if (!this.fieldZoomHistory)
		return;

	// "buffer"
	var str = new Array();
	
	// first the the position
	str.push(this.zoomHistoryPos);
	
	// next the states
	for (var i = 0; i < this.zoomHistory.length; i++)
	{
		var state = this.zoomHistory[i];
		str.push(state.x1);
		str.push(state.y1);
		str.push(state.x2);
		str.push(state.y2);
	}
	
	// store
	this.fieldZoomHistory.value = str.join(" ");
	
}

Map.prototype.zoomHistoryRestore = function()
{

	// main form
	var form = document.forms[this.formName];
	if (!form) return;

	// hidden field
	this.fieldZoomHistory = form.elements[this.fieldNameZoomHistory];
	if (!this.fieldZoomHistory) return;
	
	// split values
	var values = this.fieldZoomHistory.value.split(/\s+/);
	
	// simple check
	if (values.length % 4 != 1)
		return;
		
	// history position
	this.zoomHistoryPos = parseInt(values[0]);
	
	// history states
	var n = (values.length - 1) / 4;
	for (var i = 0; i < n; i++)
		this.zoomHistory.push(
			new MapZoomState(
				parseFloat(values[4*i+1]),
				parseFloat(values[4*i+2]),
				parseFloat(values[4*i+3]),
				parseFloat(values[4*i+4])));

	// let buttons know
	this.zoomHistoryFireEvents();

}

Map.prototype.zoomHistoryFireEvents = function()
{
	if (this.goBackButton) this.goBackButton.historyChanged();
	if (this.goForwardButton) this.goForwardButton.historyChanged();
}

Map.prototype.zoomGoBack = function()
{
	this.zoomHistoryMoveAndRestore(-1);
}

Map.prototype.zoomGoForward = function()
{
	this.zoomHistoryMoveAndRestore(+1);
}

Map.prototype.zoomCanGoBack = function()
{
	return this.zoomHistoryPos > 0;
}

Map.prototype.zoomCanGoForward = function()
{
	return this.zoomHistoryPos+1 < this.zoomHistory.length;
}

Map.prototype.registerGoForwardButton = function(button)
{
	this.goForwardButton = button;
}

Map.prototype.registerGoBackButton = function(button)
{
	this.goBackButton = button;
}

/////////////////////////////////////////////////////////
// tools
/////////////////////////////////////////////////////////

Map.prototype.setMouseModeToPan = function()
{
	this.setMouseMode(MapsGlobal.MAP_TOOL_PAN);
}

Map.prototype.setMouseModeToZoom = function()
{
	this.setMouseMode(MapsGlobal.MAP_TOOL_ZOOM);
}

Map.prototype.setMouseModeToSelector = function()
{
	this.setMouseMode(MapsGlobal.MAP_TOOL_SELECTOR);
}

Map.prototype.setMouseMode = function(mode)
{
	this.mouseMode = mode;
	this.setMouseCursors();
}

Map.prototype.setMouseCursors = function()
{
	if (this.mouseMode == MapsGlobal.MAP_TOOL_PAN)
	{
		this.tilesContainer.style.cursor = "move";
		this.selector.style.cursor = "default";
	}
	else if (this.mouseMode == MapsGlobal.MAP_TOOL_ZOOM)
	{
		this.tilesContainer.style.cursor = "crosshair";
		this.selector.style.cursor = "default";
	}
	else if (this.mouseMode == MapsGlobal.MAP_TOOL_SELECTOR)
	{
		this.tilesContainer.style.cursor = "move";
		this.selector.style.cursor = "move";
	}
	else
	{
		this.tilesContainer.style.cursor = "default";
		this.selector.style.cursor = "default";
	}
}

/////////////////////////////////////////////////////////
// points of interest
/////////////////////////////////////////////////////////

Map.prototype.initPoints = function()
{

	if (!this.points)
		return;
		
	for (var i = 0; i < this.points.length; i++)
	{
	
		var pnt = this.points[i];

		var labelElement = pnt.labelElement = document.createElement("div");
		labelElement.innerHTML = pnt.label;
		labelElement.style.padding = "2px";
		labelElement.style.border = "1px solid Black";
		labelElement.style.backgroundColor = "White";
		labelElement.style.position = "absolute";
		labelElement.style.display = "";
		
		this.tilesContainer.appendChild(labelElement);

		for (var j = 0; j < pnt.symbols.length; j++)
		{

			var symbol = pnt.symbols[j];
			
			var symbolElement = symbol.element = document.createElement("img");
			symbolElement.src = symbol.url;
			symbolElement.width = symbol.width;
			symbolElement.height = symbol.height;
			symbolElement.style.position = "absolute";
			symbolElement.style.cursor = "default";
			symbolElement.style.display = "";
			
			EventAttacher.attach(symbolElement, "mouseover", this, "showLabel", i);
			EventAttacher.attach(symbolElement, "mouseout", this, "hideLabel");
			EventAttacher.attach(symbolElement, "click", this, "showAndFixLabel", i);

			this.tilesContainer.appendChild(symbolElement);

		}

	}
	
	this.initListOfPoints();
	
}

Map.prototype.initListOfPoints = function()
{

	if (!this.pointsSelectId)
		return;
		
	var pointsSelect = document.getElementById(this.pointsSelectId);
	if (!pointsSelect)
		return;
	
	ElementUtils.addOption(
		pointsSelect,
		"-1", "Select place");

	for (var i = 0; i < this.points.length; i++)
	{
		ElementUtils.addOption(
			pointsSelect,
			i, this.points[i].label);
	}
	
	EventAttacher.attach(pointsSelect, "change", this, "pointsSelectChanged");

}

Map.prototype.pointsSelectChanged = function()
{

	var pointsSelect = document.getElementById(this.pointsSelectId);
	
	var pntIdx = parseInt(pointsSelect.options[pointsSelect.selectedIndex].value);
	if (pntIdx == -1) return;
	
	var pnt = this.points[pntIdx];
	this.setZoomAndCenterTo(this.zoomLevel, pnt.x, pnt.y, true, false, true, true, false);
	this.showLabel(null, pntIdx, true);

}

Map.prototype.updatePoints = function()
{

	if (!this.points)
		return;

	var vportWidth = this.getVportWidth();
	var vportHeight = this.getVportHeight();

	var scaleRat = (this.scale - 1) / (this.scaleMax - 1) * 100;
	//var eps = 0.005;
	//var lastVisibleGroup = Math.round((scaleRat * (1 - eps) + eps) * this.pointGroups);

	for (var i = 0; i < this.points.length; i++)
	{
		
		var pnt = this.points[i];
		
		pnt.vx = this.fromRealToVportX(pnt.x);
		pnt.vy = this.fromRealToVportY(pnt.y);

		if (0 <= pnt.vx && pnt.vx < vportWidth && 0 <= pnt.vy && pnt.vy < vportHeight/* && (pnt.showAtZoom == this.zoomLevel || pnt.showAtZoom == -1)*/)
		{
		
			for (var j = 0; j < pnt.symbols.length; j++)
			{
				var symbol = pnt.symbols[j];
				var symbolElementStyle = symbol.element.style;
				symbolElementStyle.display = "";
				symbolElementStyle.left = (pnt.vx - symbol.centerX) + "px";
				symbolElementStyle.top = (pnt.vy - symbol.centerY) + "px";
			}

			var labelElementStyle = pnt.labelElement.style;
			if (true && pnt.showAtZoom <= scaleRat)
			{
				labelElementStyle.display = "";
				labelElementStyle.left = pnt.vx + "px";
				labelElementStyle.top = pnt.vy + "px";
			}
			else
			{
				labelElementStyle.display = "none";
			}

		}
		
		else
		{
			for (var j = 0; j < pnt.symbols.length; j++)
			{
				pnt.symbols[j].element.style.display = "none";
			}
			if (pnt.labelElement)
			{
				pnt.labelElement.style.display = "none";
			}
		}

	}

}

Map.prototype.showAndFixLabel = function(event, pntIndex)
{
	this.showLabel(event, pntIndex, true);
}

Map.prototype.showLabel = function(event, pntIndex, fix)
{

	if (this.fixedLabel && !fix)
		return;
		
	this.fixedLabel = !!fix;

	var pnt = this.points[pntIndex];
	
	var x = this.fromRealToVportX(pnt.x);
	var y = this.fromRealToVportY(pnt.y);

	this.bubbleText.innerHTML = pnt.text;
	this.bubble.style.display = "block";

	this.bubble.style.left = (x + 5) + "px";
	this.bubble.style.top = (y - 5 - this.bubble.offsetHeight) + "px";

}

Map.prototype.hideLabels = function(resetSelectedPlace)
{
	if (!this.bubble)
		return;
	
	this.fixedLabel = false;
	this.bubble.style.display = "none";
		
	if (resetSelectedPlace && this.pointsSelectId)
	{
		var pointsSelect = document.getElementById(this.pointsSelectId);
		if (pointsSelect)
		{
			pointsSelect.selectedIndex = 0;
		}
	}

}

Map.prototype.hideLabel = function(event)
{

	if (this.fixedLabel)
		return;

	this.setMouseCursors();
	this.bubble.style.display = "none";

}

/////////////////////////////////////////////////////////
// points of interest
/////////////////////////////////////////////////////////

Map.prototype.updateLines = function(zoomChanged)
{

	if (!this.lines)
		return;
		
	var firstSymbol = this.points ?
		this.points[0].symbols[0].element : null;

	var vportWidth = this.getVportWidth();
	var vportHeight = this.getVportHeight();

	for (var i = 0; i < this.lines.length; i++)
	{
	
		var line = this.lines[i];
		var cx = line.symbol.centerX;
		var cy = line.symbol.centerY;
		
		if (!line.dots) line.dots = new Array();
	
		var x1 = this.fromRealToVportX(line.x1);
		var y1 = this.fromRealToVportY(line.y1);
		var x2 = this.fromRealToVportX(line.x2);
		var y2 = this.fromRealToVportY(line.y2);
		
		var dx = x2 - x1;
		var dy = y2 - y1;
		var len = Math.sqrt(dx*dx + dy*dy);
		
		var dots = Math.floor(len / line.symbolSpacing);
		var offset = (len - dots * line.symbolSpacing) / 2;
		
		var dt = line.symbolSpacing / len;
		var t0 = offset / len;
		
		for (var j = 0; j < dots; j++)
		{
		
			var t = t0 + j*dt;
			var x = x1 + t * dx - cx;
			var y = y1 + t * dy - cy;
			
			var insideViewPort =
				0 <= x && x < vportWidth &&
				0 <= y && y < vportHeight;
				
			if (!insideViewPort)
			{
				if (j < line.dots.length)
					line.dots[j].style.display = "none";
				continue;
			}

			var dot;			
			if (j < line.dots.length)
			{
				dot = line.dots[j];
			}
			else
			{
				dot = document.createElement("img");
				line.dots.push(dot);
				dot.src = line.symbol.url;
				dot.width = line.symbol.width;
				dot.height = line.symbol.height;
				dot.style.position = "absolute";
				if (firstSymbol)
					this.tilesContainer.insertBefore(dot, firstSymbol);
				else
					this.tilesContainer.appendChild(dot);
			}
			
			dot.style.display = "";
			dot.style.left = x + "px";
			dot.style.top = y + "px";
			
		}
		
		for (var j = dots; j < line.dots.length; j++)
		{
			line.dots[j].style.display = "none";
		} 
		
	}

}

/////////////////////////////////////////////////////////
// map control: resizing and init
/////////////////////////////////////////////////////////

Map.prototype.setSize = function(width, height)
{

	this.vportWidth = width;
	this.vportHeight = height;
	
	this.updateMapControlsLayout();
	
	this.setZoomAndCenterTo(
		this.zoomLevel,
		this.getCenterX(),
		this.getCenterY(),
		true,
		false,
		false,
		true,
		true);

}

Map.prototype.updateMapControlsLayout = function()
{

	var tileWidth = this.getTileWidth();
	var tileHeight = this.getTileHeight();
	
	// control
	if (!this.fixedSize)
	{
		this.mapControl.style.left = this.mapControl.offsetLeft + "px";
		this.mapControl.style.top = this.mapControl.offsetTop + "px";
	}
	this.mapControl.style.width = this.vportWidth + "px";
	this.mapControl.style.height = this.vportHeight + "px";
	
	// map itself
	this.tilesContainer.style.left = "0px";
	this.tilesContainer.style.top = "0px";
	this.tilesContainer.style.width = (this.vportWidth) + "px";
	this.tilesContainer.style.height = (this.vportHeight) + "px";
	
	// number of tiles needed
	this.visibleCols = Math.floor(this.vportWidth / tileWidth) + 1;
	this.visibleRows = Math.floor(this.vportHeight / tileHeight) + 1;
	
	// delete old tiles for map
	if (this.tilesMap != null)
	{
		for (var i=0; i<this.tilesMap.length; i++)
		{
			for (var j=0; j<this.tilesMap[i].length; j++)
			{
				var tile = this.tilesMap[i][j];
				this.tilesContainer.removeChild(tile.img);
				if (tile.points) this.tilesContainer.removeChild(tile.points.getRoot());
			}
		}
	}
	
	// tiles for maps
	this.tilesMap = new Array();
	for (var i=0; i<this.visibleRows+1; i++)
	{
		this.tilesMap[i] = new Array();
		for (var j=0; j<this.visibleCols+1; j++)
		{			
			var tile = document.createElement("img");
			tile.onload = function() {this.style.visibility = "visible";};
			this.tilesMap[i][j] = new MapTile(tile);
			tile.unselectable = "on";
			tile.galleryImg = "no";
			tile.style.MozUserFocus = "none";
			tile.style.MozUserSelect = "none";
			tile.onmousedown = MapUtils.falseFnc;
			tile.style.position = "absolute";
			tile.width = tileWidth;
			tile.height = tileHeight;
			tile.style.left = (j * tileWidth) + "px";
			tile.style.top = (i * tileHeight) + "px";
			tile.points = null;
			this.tilesContainer.insertBefore(tile, this.selector);
		}
	}
	
}

Map.prototype.initMapControls = function()
{

	this.mapControl = document.getElementById(this.mapControlId);
	
	this.bubble = document.getElementById(this.bubbleId)
	this.bubbleText = document.getElementById(this.bubbleTextId)
	if (this.bubble) this.bubble.style.position = "absolute";
	
	this.tilesContainer = document.getElementById(this.tilesContainerId);
	this.tilesContainer.style.position = "absolute";
	this.tilesContainer.style.overflow = "hidden";
	this.tilesContainer.unselectable = "on";
	this.tilesContainer.style.MozUserFocus = "none";
	this.tilesContainer.style.MozUserSelect = "none";
	
	this.vportWidth = this.mapControl.offsetWidth;
	this.vportHeight = this.mapControl.offsetHeight;

	EventAttacher.attach(this.tilesContainer, "mousedown", this, "mapStartDrag");
	if (IE) EventAttacher.attach(this.tilesContainer, "mousewheel", this, "mapMouseWheel");

	this.selector = document.createElement("DIV");
	this.selector.style.position = "absolute";
	this.selector.style.display = "none";
	this.selector.style.borderStyle = "solid";
	this.selector.style.borderWidth = this.selectorBorderWidth + "px";
	this.selector.style.borderColor = this.selectorColor;
	this.selector.style.left = "0px";
	this.selector.style.top = "0px";
	this.selector.style.width = "200px";
	this.selector.style.height = "200px";
	this.tilesContainer.appendChild(this.selector);
	
	var bg = document.createElement("DIV");
	bg.style.width = "100%";
	bg.style.height = "100%";
	bg.style.filter = "alpha(opacity = " + this.selectorOpacity + ")";
	bg.style.opacity = this.selectorOpacity / 100;
	bg.style.backgroundColor = this.selectorColor;
	this.selector.appendChild(bg);

}

Map.prototype.hiddenFieldsInit = function()
{

	var form = document.forms[this.formName];
	if (!form)
	{
		this.hasHiddenExtentsFields = false;
	}
	else
	{
		this.fieldX1 = form.elements[this.fieldNameX1];
		this.fieldY1 = form.elements[this.fieldNameY1];
		this.fieldX2 = form.elements[this.fieldNameX2];
		this.fieldY2 = form.elements[this.fieldNameY2];
		this.hasHiddenExtentsFields = !!(this.fieldX1 && this.fieldY1 && this.fieldX2 && this.fieldY2);
	}
	
}

Map.prototype.restoreState = function()
{
	// zoom
	if (this.hasHiddenExtentsFields)
	{
		var x1 = parseFloat(this.fieldX1.value);
		var y1 = parseFloat(this.fieldY1.value);
		var x2 = parseFloat(this.fieldX2.value);
		var y2 = parseFloat(this.fieldY2.value);
		this.zoomMapTo(x1, y1, x2, y2, false, false, false, true, true);
	}
	
	// in case that the server components is not interested
	// in saving the max extent -> so we show the minimal
	else
	{
		this.setZoomAndCenterTo(0, 0, 0, false, false, false, true, true);
	}
	
	// and init history if empty
	if (this.zoomHistoryPos == -1)
	{
		this.zoomSaveState();
	}

}

/////////////////////////////////////////////////////////
// debug functionality
/////////////////////////////////////////////////////////

Map.prototype.enableDebugShowCoordinates = function()
{

	EventAttacher.attach(this.tilesContainer, "mousemove", this, "debugMouseMove");

}

Map.prototype.disableDebugShowCoordinates = function()
{

	EventAttacher.detach(this.tilesContainer, "mousemove", this, "debugMouseMove");

}

Map.prototype.debugMouseMove = function(event)
{

	var x = ElementUtils.getEventMouseElementX(event, this.mapControl);
	var y = ElementUtils.getEventMouseElementY(event, this.mapControl);
	
	var mapDebugCoordinates = document.getElementById("mapDebugCoordinates");
	if (mapDebugCoordinates)
		mapDebugCoordinates.innerHTML =
			"long = " + this.fromVportToRealX(x) + ", " +
			"lat = " + this.fromVportToRealY(y);

}

/////////////////////////////////////////////////////////
// custom action management
/////////////////////////////////////////////////////////

Map.prototype.invokeAction = function(name, param)
{
	var frm = document.forms[this.formName];
	frm.elements[this.actionNameFieldName].value = name;
	frm.elements[this.actionParamFieldName].value = param;
	frm.submit();
}

/////////////////////////////////////////////////////////
// main init
/////////////////////////////////////////////////////////

Map.prototype.init = function(restoreState)
{

	// minimap should be initialized first
	if (this.miniMap)
	{
		this.miniMap.init(false);
		this.miniMap.setMouseModeToSelector();
	}

	// hidden fields
	this.hiddenFieldsInit();

	// control
	this.initMapControls();
	this.updateMapControlsLayout();
	
	// scale indicator	
	this.scaleIndicatorInit();

	// zoom history
	this.zoomHistoryRestore();
	
	// points
	this.initPoints();
	
	// show something
	if (restoreState)
		this.restoreState();
	
	// init minimap
	this.initMiniMap();
		
	// let others know that we are done
	this.initListeners.invoke();
	
	// debug mode
	if (this.debug)
		this.enableDebugShowCoordinates();
	
}

/////////////////////////////////////////////////////////
// zoom + button
/////////////////////////////////////////////////////////

function MapButtonZoomPlus(elementId, map)
{
	this.element = null;
	this.map = map;
	this.elementId = elementId;
	this.map.initListeners.register(this, "init", null);
}

MapButtonZoomPlus.prototype.init = function()
{
	this.element = document.getElementById(this.elementId);
	EventAttacher.attach(this.element, "click", this, "click");
}

MapButtonZoomPlus.prototype.click = function(event)
{
	this.map.zoomPlus(true);
}


/////////////////////////////////////////////////////////
// zoom - button
/////////////////////////////////////////////////////////

function MapButtonZoomMinus(elementId, map)
{
	this.element = null;
	this.map = map;
	this.elementId = elementId;
	this.map.initListeners.register(this, "init", null);
}

MapButtonZoomMinus.prototype.init = function()
{
	this.element = document.getElementById(this.elementId);
	EventAttacher.attach(this.element, "click", this, "click");
}

MapButtonZoomMinus.prototype.click = function(event)
{
	this.map.zoomMinus(true);
}

/////////////////////////////////////////////////////////
// back button
/////////////////////////////////////////////////////////

function MapButtonGoBack(elementId, map)
{
	this.element = null;
	this.map = map;
	this.elementId = elementId;
	this.map.initListeners.register(this, "init", null);
}

MapButtonGoBack.prototype.init = function()
{
	this.element = document.getElementById(this.elementId);
	EventAttacher.attach(this.element, "click", this, "click");
}

MapButtonGoBack.prototype.click = function(event)
{
	this.map.zoomGoBack();
}

MapButtonGoBack.prototype.historyChanged = function()
{
	if (this.element)
		this.element.className =
			this.map.zoomCanGoBack() ? "map-icon-back" : "map-icon-back-off";
}

/////////////////////////////////////////////////////////
// forward button
/////////////////////////////////////////////////////////

function MapButtonGoForward(elementId, map)
{
	this.element = null;
	this.map = map;
	this.elementId = elementId;
	this.map.initListeners.register(this, "init", null);
}

MapButtonGoForward.prototype.init = function()
{
	this.element = document.getElementById(this.elementId);
	EventAttacher.attach(this.element, "click", this, "click");
}

MapButtonGoForward.prototype.click = function(event)
{
	this.map.zoomGoForward();
}

MapButtonGoForward.prototype.historyChanged = function()
{
	if (this.element)
		this.element.className =
			this.map.zoomCanGoForward() ? "map-icon-forward" : "map-icon-forward-off";
}


/////////////////////////////////////////////////////////
// zoom and pan buttons
/////////////////////////////////////////////////////////

function MapZoomAndPanButtons(panElementId, zoomElementId, formName, fieldNameForMouseMode, map)
{
	this.zoomElement = null;
	this.panElement = null;
	this.panElementId = panElementId;
	this.zoomElementId = zoomElementId;

	this.formName = formName;
	this.fieldNameForMouseMode = fieldNameForMouseMode;
	this.fieldForMouseMode = null;
	
	this.map = map;
	this.map.initListeners.register(this, "init", null);
}

MapZoomAndPanButtons.prototype.init = function()
{
	this.zoomElement = document.getElementById(this.zoomElementId);
	this.panElement = document.getElementById(this.panElementId);
	this.fieldForMouseMode = document.forms[this.formName].elements[this.fieldNameForMouseMode];

	EventAttacher.attach(this.zoomElement, "click", this, "clickZoom");
	EventAttacher.attach(this.panElement, "click", this, "clickPan");
}

MapZoomAndPanButtons.prototype.clickPan = function(event)
{
	this.map.setMouseModeToPan();
	this.fieldForMouseMode.value = "pan";
	this.zoomElement.className = "map-icon-zoom-off";
	this.panElement.className = "map-icon-pan";
}

MapZoomAndPanButtons.prototype.clickZoom = function(event)
{
	this.map.setMouseModeToZoom();
	this.fieldForMouseMode.value = "zoom";
	this.zoomElement.className = "map-icon-zoom";
	this.panElement.className = "map-icon-pan-off";
}

/////////////////////////////////////////////////////////
// size buttons
/////////////////////////////////////////////////////////

function MapZoomSize(mapSizes, formName, fieldNameForMapSize, map)
{
	this.mapSizes = mapSizes;
	this.formName = formName;
	this.fieldNameForMapSize = fieldNameForMapSize;
	this.fieldForMapSize = null;
	this.map = map;
	this.map.initListeners.register(this, "init", null);
}

MapZoomSize.prototype.init = function()
{
	var form = document.forms[this.formName];
	this.fieldForMapSize = form.elements[this.fieldNameForMapSize];
	for (var i=0; i<this.mapSizes.length; i++)
	{
		var ms = this.mapSizes[i];
		ms.element = document.getElementById(ms.elementId);
		EventAttacher.attach(ms.element, "click", this, "click", i);
	}
}

MapZoomSize.prototype.click = function(event, mapSizeIndex)
{
	var ms = this.mapSizes[mapSizeIndex];
	
	this.map.setSize(ms.width, ms.height);
	this.fieldForMapSize.value = ms.width + " " + ms.height;
	
	for (var i=0; i<this.mapSizes.length; i++)
	{
		var ms = this.mapSizes[i];
		var className = "map-icon-size-" + i;
		if (i != mapSizeIndex) className += "-off";
		ms.element.className = className;
	}
}

/////////////////////////////////////////////////////////
// slider
/////////////////////////////////////////////////////////

function MapZoomSlider(slotWidth, containerElementId, knobElementId, map)
{
	this.slotWidth = slotWidth;
	this.containerElementId = containerElementId;
	this.knobElementId = knobElementId;
	this.map = map;
	this.scale = 0;
	this.sliding = false;
	this.map.registerZoomSlider(this);
	this.map.initListeners.register(this, "init", null);
}

MapZoomSlider.prototype.init = function()
{

	this.cont = document.getElementById(this.containerElementId);
	this.knob = document.getElementById(this.knobElementId);
	
	this.knob.style.display = "block";
	
	EventAttacher.attach(this.knob, "mousedown", this, "mouseDown");
	EventAttacher.attach(this.cont, "click", this, "click");
	
	this.zoomChanged();
}

MapZoomSlider.prototype.capKnobPosition = function(knobLeft)
{
	if (knobLeft < 0) knobLeft = 0;
	if (knobLeft > this.sliderEffectiveWidth) knobLeft = this.sliderEffectiveWidth;
	return knobLeft;
}

MapZoomSlider.prototype.setKnobPosition = function(zoomLevel)
{
	this.knob.style.left = (zoomLevel * this.slotWidth) + "px";
}

MapZoomSlider.prototype.click = function(event)
{
	var zoomLevel = parseInt(ElementUtils.getEventMouseElementX(event, this.cont) / this.slotWidth);
	this.setKnobPosition(zoomLevel);
	this.map.changeZoomLevel(zoomLevel, false);
}

MapZoomSlider.prototype.mouseDown = function(event)
{
	this.sliding = true;
	EventAttacher.attach(document, "mousemove", this, "mouseMove");
	EventAttacher.attach(document, "mouseup", this, "mouseUp");
}

MapZoomSlider.prototype.mouseMove = function(event)
{
	var zoomLevel = parseInt(ElementUtils.getEventMouseElementX(event, this.cont) / this.slotWidth);
	if (zoomLevel < 0) zoomLevel = 0;
	if (zoomLevel >= this.map.zoomLevels.length) zoomLevel = this.map.zoomLevels.length - 1;
	this.setKnobPosition(zoomLevel);
}

MapZoomSlider.prototype.mouseUp = function(event)
{
	EventAttacher.detach(document, "mousemove", this, "mouseMove");
	EventAttacher.detach(document, "mouseup", this, "mouseUp");
}

MapZoomSlider.prototype.zoomChanged = function()
{
	this.setKnobPosition(this.map.zoomLevel);
}
