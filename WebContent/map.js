var MapsGlobal = 
{

	MAP_TOOL_ZOOM: 1,
	MAP_TOOL_PAN: 2,
	MAP_TOOL_SELECTOR: 3,
	
	SCALE_FACTOR: 100000,

	maps: new Array(),
	
	registerMap: function(
		mapId, // unique id
		fixedSize, // true/false
		mapTilesServer, // servlet
		mapFile, // ServerMap map file
		mapControlId, // main container 
		mapFrameId, // frame for the map
		mapToolsTopId, // top tools element
		mapToolsBottomId, // bottom tools element
		mapToolsLeftId, // left tools element
		mapToolsRightId, // top tools element
		formName, // form name containing the hidden fields
		fieldNameX1, // name of the hidden field for x1
		fieldNameX2, // name of the hidden field for x2
		fieldNameY1, // name of the hidden field for y1
		fieldNameY2,  // name of the hidden field for y2
		fieldNameZoomHistory, // field for serialized zoom history 
		buttonBackId, // back button
		buttonForwardId, // forward button
		buttonZoomPlusId, // zoom + button
		buttonZoomMinusId, // zoom - button
		sliderBgId, // slider background
		sliderKnobId, // slider knob
		buttonPanId, // pan button
		buttonZoomId, // zoom button
		fieldNameForMouseMode, // mouse mode hidden field
		mapSizes, // available map sizes
		fieldNameForMapSize, // hidden field for selected map size
		pointsOfInterest, // array of points with labels
		bubbleId, // bubble <table>
		bubbleTextId, // inner <td> for the text in the bubble
		scaleIndicatorTextId,
		scaleIndicatorBarId,
		miniMapControlId,
		miniMapFrameId,
		miniMapToggleId,
		fieldNameMiniMapVisibility,
		miniMapPosition,
		miniMapWidth,
		miniMapHeight
	)
	{
	
		var map = new Map();
		this.maps[mapId] = map;
		
		// server and map file
		map.mapFile = mapFile;
		map.server = mapTilesServer;
		
		// HTML
		map.fixedSize = fixedSize;
		map.map_control_id = mapControlId;
		map.frameId = mapFrameId;
		map.map_control_top_tools_id = mapToolsTopId;
		map.map_control_bottom_tools_id = mapToolsBottomId;
		map.map_control_left_tools_id = mapToolsLeftId;
		map.map_control_right_tools_id = mapToolsRightId;
		
		// hidden fields for maitaining state
		map.form_name = formName;
		map.field_name_x1 = fieldNameX1;
		map.field_name_y1 = fieldNameX2;
		map.field_name_x2 = fieldNameY1;
		map.field_name_y2 = fieldNameY2;
		map.field_name_zoom_history = fieldNameZoomHistory;
		
		// points
		map.points = pointsOfInterest;
		
		// bubble
		map.bubbleId = bubbleId;
		map.bubbleTextId = bubbleTextId;
		
		// scale indicator
		map.scale_bar_indicator_id = scaleIndicatorBarId;
		map.scale_text_indicator_id = scaleIndicatorTextId;
		
		// minimap
		if (miniMapControlId && miniMapFrameId)
		{
			var miniMap = new Map();
			miniMap.mapFile = mapFile;
			miniMap.server = mapTilesServer;
			miniMap.fixedSize = true;
			miniMap.map_control_id = miniMapControlId;
			miniMap.frameId = miniMapFrameId;
			miniMap.mainMap = map;
			
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
		if (sliderBgId && sliderKnobId)
			new MapZoomSlider(sliderBgId, sliderKnobId, map);
			
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
	}
	
}

/////////////////////////////////////////////////////////
// rectangle structure
/////////////////////////////////////////////////////////

function MapRectangle(x1, y1, x2, y2)
{
	if (x2 < x1)
	{
		var x = x2;
		x2 = x1;
		x1 = x;
	}
	if (y2 < y1)
	{
		var y = y2;
		y2 = y1;
		y1 = y;
	}
	this.x1 = x1;
	this.x2 = x2;
	this.y1 = y1;
	this.y2 = y2;
}

MapRectangle.prototype.getX1 = function()
{
	return this.x1;
}

MapRectangle.prototype.getX2 = function()
{
	return this.x2;
}

MapRectangle.prototype.getY1 = function()
{
	return this.y1;
}

MapRectangle.prototype.getY2 = function()
{
	return this.y2;
}

MapRectangle.prototype.getCenterX = function()
{
	return 0.5 * (this.x1 + this.x2);
}

MapRectangle.prototype.getCenterY = function()
{
	return 0.5 * (this.y1 + this.y2);
}

MapRectangle.prototype.getWidth = function()
{
	return this.x2 - this.x1;
}

MapRectangle.prototype.getHeight = function()
{
	return this.y2 - this.y1;
}

MapRectangle.prototype.scaleBy = function(scale)
{
	var cx = this.getCenterX();
	var cy = this.getCenterY();
	var w = this.getWidth();
	var h = this.getHeight();
	x1 = cx - scale * w/2;
	x2 = cx + scale * w/2;
	y1 = cy - scale * h/2;
	y2 = cy + scale * h/2;
}

/////////////////////////////////////////////////////////
// utilities
/////////////////////////////////////////////////////////

var MapUtils =
{

	RECTANGLE_FIT_DEST_OVER_SRC: 1,
	RECTANGLE_FIT_SRC_OVER_DEST: 2,

	fitRectangle: function(srcRect, destWidth, destHeight, type, border)
	{
		return MapUtils.fitRectangleByDimen(
			srcRect.getWidth(), srcRect.getHeight(),
			destWidth, destHeight,
			type, border);
	},

	fitRectangleByDimen: function(srcWidth, srcHeight, destWidth, destHeight, type, border)
	{
	
		// compute where we have to fit it
		if (border == null) border = 0;
		var destEffectiveWidth = destWidth - 2*border;
		var destEffectiveHeight = destHeight - 2*border;
	
		// adjust to the ratio
		var destRatio = destEffectiveWidth / destEffectiveHeight;
		var srcRatio = srcWidth / srcHeight;
		
		// set scale
		if (srcRatio < destRatio)
		{
			if (type == MapUtils.RECTANGLE_FIT_DEST_OVER_SRC)
				return destEffectiveHeight / srcHeight;
			else
				return srcHeight / destEffectiveHeight;
		}
		else
		{
			if (type == MapUtils.RECTANGLE_FIT_DEST_OVER_SRC)
				return destEffectiveWidth / srcWidth;
			else
				return srcWidth / destEffectiveWidth;
		}
		
	},
	
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

	// server and map file
	this.mapFile = null;
	this.server = "servlet/maptile";

	// container
	this.fixedSize = true;
	this.width = 0;
	this.height = 0;
	this.map_control = null;
	this.map_control_id = null;
	this.min_map_control_width = 600;
	this.min_map_control_height = 400;
	
	// current position
	this.first_tile_col = null;
	this.first_tile_row = null;
	this.first_tile_vx = null;
	this.first_tile_vy = null;
	this.scale = null;
	this.scale_min = 1;
	this.scale_max = 100000;
	this.scale_factor_plus = 2.0;
	this.scale_factor_minus = 0.5;

	// tools
	this.map_control_top_tools = null;
	this.map_control_top_tools_id = null;
	this.map_control_bottom_tools = null;
	this.map_control_bottom_tools_id = null;
	this.map_control_left_tools = null;
	this.map_control_left_tools_id = null;
	this.map_control_right_tools = null;
	this.map_control_right_tools_id = null;
	
	// references to HTML elements
	this.frame = null;
	this.frameId = null;
	this.tiles_map = null;
	this.map_blank_img = "../../blank.png";
	
	// viewport config
	this.visible_rows = -1;
	this.visible_cols = -1;
	this.vport_offset_left = -1;
	this.vport_offset_top = -1;
	this.vport_width = -1;
	this.vport_height = -1;
	this.tile_width = 160;
	this.tile_height = 120;
	this.postponed_refresh_id = -1;
	
	// zoom history
	this.field_name_zoom_history = null;
	this.field_zoom_history = null;
	this.zoom_history = new Array();
	this.zoom_history_pos = -1;
	this.zoom_history_max = 100;
	this.go_back_button = null;
	this.go_forward_button = null;
	
	// for dragging
	this.dragging_start_x = null;
	this.dragging_start_y = null;
	
	// zooming and panning
	this.mouseMode = MapsGlobal.MAP_TOOL_PAN;
	this.zoom_slider = null;
	this.selector_border_width = 1;
	this.selector_color = "White"; //"#0066CC";
	this.selector_opacity = 30;
	this.selector = null;
	
	// selector mode
	this.selectorBorder = 30;
	this.selectorX1 = null;
	this.selectorY1 = null;
	this.selectorX2 = null;
	this.selectorY2 = null;
	this.selectorVportX1 = null;
	this.selectorVportY1 = null;
	this.selectorVportX2 = null;
	this.selectorVportY2 = null;
	
	// reference to a minimap
	this.miniMapToggleId = null;
	this.miniMapToggle = null;
	this.miniMap = null;
	this.mainMap = null;
	this.miniMapPosition = null;
	
	// points of interest
	this.points = null;
	this.pointsExtraSpace = 10;
	this.pointsByTiles = new Array();
	
	// bubble
	this.bubbleId = null;
	this.bubbleTextId = null;
	this.bubble = null;
	this.bubbleText = null;
	
	// scale indicator & selector
	this.scale_bar_indicator_id = null;
	this.scale_text_indicator_id = null;
	this.scale_bar_indicator = null;
	this.scale_text_indicator = null;
	this.hasScaleIndicator = false;
	
	// pan tools
	this.tool_pan_up = null;
	this.tool_pan_down = null;
	this.tool_pan_left = null;
	this.tool_pan_right = null;
	this.pan_tool_delta = 20;
	this.pan_tool_delay = 50;
	this.pan_tool_timeout_id = null;
	this.pan_tool_dx = null;
	this.pan_tool_dy = null;
	
	// hidden fields for maitaining state
	this.hasHiddenExtendFields = false;
	this.formName = null;
	this.field_name_x1 = null;
	this.field_name_y1 = null;
	this.field_name_x2 = null;
	this.field_name_y2 = null;
	this.field_x1 = null;
	this.field_y1 = null;
	this.field_x2 = null;
	this.field_y2 = null;
	
	// init handler
	this.initListeners = new EventQueue();
	
}

/////////////////////////////////////////////////////////
// some internal objects
/////////////////////////////////////////////////////////

function MapZoomState(scale, cx, cy)
{
	this.cx = cx;
	this.cy = cy;
	this.scale = scale;
}

function MapTile(img)
{
	this.img = img;
	this.points = null;
	this.url = "";
	this.oldUrl = "";
	//this.valid = false;
}

function PointOfInterest(x, y, name, text)
{
	this.x = x;
	this.y = y;
	this.name = name;
	this.text = text;
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
	return this.vport_width;
}

Map.prototype.getVportHeight = function()
{
	return this.vport_height;
}

Map.prototype.fromVportToRealX = function(x)
{
	return this.first_tile_col * this.getTileRealWidth() +
		this.fromPxToReal(x-this.first_tile_vx);
}

Map.prototype.fromVportToRealY = function(y)
{
	return (this.first_tile_row+1) * this.getTileRealHeight() -
		this.fromPxToReal(y-this.first_tile_vy);
}

Map.prototype.fromRealToVportX = function(x)
{
	return this.fromRealToPx(x - this.first_tile_col * this.getTileRealWidth()) +
		this.first_tile_vx;
}

Map.prototype.fromRealToVportY = function(y)
{
	return this.fromRealToPx((this.first_tile_row+1) * this.getTileRealHeight() - y) +
		this.first_tile_vy;
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
	return this.fromPxToReal(this.tile_width);
}

Map.prototype.getTileRealHeight = function()
{
	return this.fromPxToReal(this.tile_height);
}

Map.prototype.fromPxToReal = function(v)
{
	return v / (this.scale / MapsGlobal.SCALE_FACTOR);
}

Map.prototype.fromRealToPx = function(v)
{
	return v * (this.scale / MapsGlobal.SCALE_FACTOR);
}

Map.prototype.roundAndCapScale = function(s)
{
	s = Math.round(s);
	if (s > this.scale_max) s = this.scale_max;
	if (s < this.scale_min) s = this.scale_min;
	return s;
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

	//debug("drawSelector (x1 = " + x1 + ", y1 = " + y1 + ", x2 = " + x2 + ", y2 = " + y2 + ")");

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
	x2 -= 3*this.selector_border_width;
	y2 -= 3*this.selector_border_width;
	
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
	
	this.scale_bar_indicator.style.width = Math.round(pixels) + "px";
	this.scale_text_indicator.innerHTML = meters + "&deg;"  //this.formatMetersForDisplay(meters);

}

Map.prototype.scaleIndicatorInit = function()
{
	this.scale_bar_indicator = document.getElementById(this.scale_bar_indicator_id);
	this.scale_text_indicator = document.getElementById(this.scale_text_indicator_id);
	this.hasScaleIndicator = this.scale_bar_indicator && this.scale_text_indicator;
}

/////////////////////////////////////////////////////////
// map zoom/pan
/////////////////////////////////////////////////////////

Map.prototype.mapStartDrag = function(event)
{

	// set onMouseMove handler
	EventAttacher.attachOnWindowEvent("mousemove", this, "mapMouseMove");
	EventAttacher.attachOnWindowEvent("mouseup", this, "mapStopDrag");
	this.forgetLabels();
	
	// init position
	this.dragging_start_x = event.clientX - this.vport_offset_left;
	this.dragging_start_y = event.clientY - this.vport_offset_top;
	
}

Map.prototype.mapStopDrag = function(event)
{

	// cancel onMouseMove handler
	EventAttacher.detachOnWindowEvent("mousemove", this, "mapMouseMove");
	EventAttacher.detachOnWindowEvent("mouseup", this, "mapStopDrag");
	this.watchForLabels();

	// new position
	var x = event.clientX - this.vport_offset_left;
	var y = event.clientY - this.vport_offset_top;
	
	switch (this.mouseMode)
	{
		case MapsGlobal.MAP_TOOL_ZOOM:
		
			// hide selector
			this.hideSelector();
			
			// has the mouse moved?
			if (this.dragging_start_x == x && this.dragging_start_y == y)
				return;
			
			// and zoom
			this.zoomMapTo(
				this.fromVportToRealX(this.dragging_start_x),
				this.fromVportToRealY(this.dragging_start_y),
				this.fromVportToRealX(x),
				this.fromVportToRealY(y),
				true, 0, true);
			
			break;
			
		case MapsGlobal.MAP_TOOL_PAN:
		case MapsGlobal.MAP_TOOL_SELECTOR:
		
			// precompute points position wrt. the new vport
			this.precomputePointsPositions();
			
			// update associated map
			this.updateMainMap();
			this.updateMiniMap();

			break;
			
	}
	
}

Map.prototype.mapMouseMove = function(event)
{

	// new position
	var x = event.clientX - this.vport_offset_left;
	var y = event.clientY - this.vport_offset_top;
	
	switch (this.mouseMode)
	{
		case MapsGlobal.MAP_TOOL_PAN:
		case MapsGlobal.MAP_TOOL_SELECTOR:
		
			// position change
			var dx = x - this.dragging_start_x;
			var dy = y - this.dragging_start_y;

			// remember for the next turn
			this.dragging_start_x = x;
			this.dragging_start_y = y;
			
			// change pos
			this.first_tile_vx += dx;
			this.first_tile_vy += dy;

			// adjust tile if needed
			this.adjustTilesAfterPan();
			
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
				this.dragging_start_x, this.dragging_start_y,
				x, y);

			break;
	
	}
	
}

Map.prototype.mapMouseWheel = function(event)
{
	// change the zoom
	if (event.wheelDelta < 0)
	{
		this.changeScale((-event.wheelDelta/120) * this.scale * this.scale_factor_plus, true);
	}
	else
	{
		this.changeScale(event.wheelDelta/120 * this.scale * this.scale_factor_minus, true);
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
	this.changeScale(
		this.scale * this.scale_factor_plus,
		notifyZoomChange);
}

Map.prototype.zoomMinus = function(notifyZoomChange)
{
	this.changeScale(
		this.scale * this.scale_factor_minus,
		notifyZoomChange);
}

Map.prototype.changeScaleNormalized = function(t, notifyZoomChange)
{
	this.changeScale(
		this.scale_min + t * (this.scale_max - this.scale_min),
		notifyZoomChange);
}

Map.prototype.getScaleNormalized = function()
{
	return (this.scale - this.scale_min) / (this.scale_max - this.scale_min);
}

Map.prototype.registerZoomSlider = function(zoom_slider)
{
	this.zoom_slider = zoom_slider;
}

Map.prototype.notifyZoomChange = function()
{
	if (this.zoom_slider) this.zoom_slider.zoomChanged();
}

Map.prototype.changeScale = function(newScale, notifyZoomChange)
{

	// has it changed?
	new_scale = this.roundAndCapScale(newScale);
	if (newScale == this.scale) return;
	
	// get old center
	var cx = this.getCenterX();
	var cy = this.getCenterY();
	
	// center
	this.setScaleAndCenterTo(newScale, cx, cy, true, notifyZoomChange, true, true);

}

// PUBLIC
Map.prototype.zoomMapTo = function(x1, y1, x2, y2, saveState, border, notifyZoomChange, updateMainMap, updateMiniMap)
{

	// does not make sense in selector mode
	if (this.mouseMode == MapsGlobal.MAP_TOOL_SELECTOR)
		return;

	// what we want to see
	var rect = new MapRectangle(x1, y1, x2, y2);

	// compute new scale
	var newScale = MapUtils.fitRectangle(
		rect,
		this.getVportWidth(),
		this.getVportHeight(),
		MapUtils.RECTANGLE_FIT_DEST_OVER_SRC,
		border);
		
	// adjust by factor
	newScale *= MapsGlobal.SCALE_FACTOR;
		
	// zoom
	this.setScaleAndCenterTo(
		newScale,
		rect.getCenterX(),
		rect.getCenterY(),
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
	
	// what we want to see
	var rect = new MapRectangle(x1, y1, x2, y2);

	// compute new scale
	var newScale = MapUtils.fitRectangle(
		rect,
		this.getVportWidth(),
		this.getVportHeight(),
		MapUtils.RECTANGLE_FIT_DEST_OVER_SRC,
		this.selectorBorder);
		
	// adjust by factor
	newScale *= MapsGlobal.SCALE_FACTOR;

	// zoom
	this.setScaleAndCenterTo(
		newScale,
		rect.getCenterX(),
		rect.getCenterY(),
		saveState,
		notifyZoomChange,
		updateMainMap,
		updateMiniMap);

}

// PUBLIC
Map.prototype.centerTo = function(x, y, saveState, notifyZoomChange, updateMainMap, updateMiniMap)
{
	this.setScaleAndCenterTo(
		this.scale,
		x, y,
		saveState,
		notifyZoomChange,
		updateMainMap,
		updateMiniMap);
}

// PRIVATE
Map.prototype.setScaleAndCenterTo = function(newScale, x, y, saveState, notifyZoomChange, updateMainMap, updateMiniMap)
{

	// scale has changed
	newScale = this.roundAndCapScale(newScale);
	if (this.scale != newScale)
	{
		this.scale = newScale;
		this.updateScaleIndicator();
	}
	
	// middle tile
	var col = Math.floor(x / this.getTileRealWidth());
	var row = Math.floor(y / this.getTileRealHeight());
	
	// position of the middle tile in vport
	var vx = Math.round(this.getVportWidth()/2 - this.fromRealToPx(x - col * this.getTileRealWidth()));
	var vy = Math.round(this.getVportHeight()/2 - this.fromRealToPx((row+1) * this.getTileRealHeight() - y));
	
	// compute the position of the top left tile
	while (!(-this.tile_width < vx && vx <= 0))
	{
		col --;
		vx -= this.tile_width;
	}
	while (!(-this.tile_height < vy && vy <= 0))
	{
		row ++;
		vy -= this.tile_height;
	}

	// set it (these are the most important variables)
	this.first_tile_col = col;
	this.first_tile_row = row;
	this.first_tile_vx = vx;
	this.first_tile_vy = vy;
	
	// points of interest
	this.precomputePointsPositions();
	
	// draw all tiles
	this.positionTiles(true, false);
	
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
	this.map_control.style.visibility = "visible";
}

Map.prototype.hide = function()
{
	this.map_control.style.visibility = "hidden";
}

Map.prototype.isVisible = function()
{
	return this.map_control.style.visibility != "hidden";
}

Map.prototype.initMiniMap = function()
{

	if (!this.miniMap)
		return;
		
	this.miniMapToggle = document.getElementById(this.miniMapToggleId);
	
	if (this.miniMapToggle)
		EventAttacher.attach(this.miniMapToggle, "click", this, "hideShowMiniMap")

	this.fieldMiniMapVisibility =
		document.forms[this.form_name].elements[this.fieldNameMiniMapVisibility];

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
		var a = new Animation(this.miniMap.map_control, 10, 400, miniMapHideFunction, this.miniMap);
		//a.setSizes(this.miniMapWidth, this.miniMapHeight, 0, 0);
		a.setOpacities(1, 0);
		a.start();
		
		this.fieldMiniMapVisibility.value = "false";
		this.miniMapToggle.className = this.getCssClassForMinimapToggleButton(false);
		
	}
	else
	{

		this.miniMap.show();
		var a = new Animation(this.miniMap.map_control, 10, 400);
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
	if (this.hasHiddenExtendFields)
	{
		this.field_x1.value = this.getMapX1();
		this.field_y1.value = this.getMapY1();
		this.field_x2.value = this.getMapX2();
		this.field_y2.value = this.getMapY2();
	}
}

/*
Map.prototype.updateTile = function(update_img, map_tile, x, y, col, row)
{
	//if (update_img)
	//{
	
		// points
//		if (map_tile.points)
//		{
//			this.frame.removeChild(map_tile.points.getRoot());
//		}
//		var pointsTile = this.pointsByTiles[col + ":" + row];
//		if (pointsTile)
//		{
//			map_tile.points = pointsTile;
//			this.frame.insertBefore(pointsTile.getRoot(), this.selector);
//		}
//		else
//		{
//			map_tile.points = null;
//		}
		
		// map
		if (map_tile.url != map_tile.oldUrl)
		{
			map_tile.valid = false;
			map_tile.img.src = this.getBlankTileUrl();
		}
		else
		{
			map_tile.valid = true;
		}

	//}
	if (x != null)
	{
		if (map_tile.points) map_tile.points.setX(x - this.pointsExtraSpace);
		map_tile.img.style.left = x + "px";
	}
	if (y != null)
	{
		if (map_tile.points) map_tile.points.setY(y - this.pointsExtraSpace);
		map_tile.img.style.top = y + "px";
	}
}

Map.prototype.updateInvalidatedTiles = function()
{
	for (var i=0; i<this.visible_rows+1; i++)
	{
		for (var j=0; j<this.visible_cols+1; j++)
		{
			var tile = this.tiles_map[i][j];
			if (!tile.valid)
			{
				tile.valid = true;
				tile.url = tile.newUrl;
				tile.img.src = tile.newUrl;
			}
		}
	}
}
*/

Map.prototype.positionTiles = function(update_img, postpone, row_from, row_to, col_from, col_to)
{

	postpone = false;

	this.saveState();
	
	if (row_from == null) row_from = 0;
	if (row_to == null) row_to = this.visible_rows;
	if (col_from == null) col_from = 0;
	if (col_to == null) col_to = this.visible_cols;
	
	var needPosponedUpdate = false;
	for (var i=row_from; i<=row_to; i++)
	{
		for (var j=col_from; j<=col_to; j++)
		{
			var tile = this.tiles_map[i][j];
			var col = this.first_tile_col + j;
			var row = this.first_tile_row - i;
			var newUrl = this.createTileUrl(col, row);
			if (newUrl != tile.url)
			{
				tile.url = newUrl;
				tile.img.style.visibility = "hidden";
				tile.img.src = newUrl;
				/*
				if (postpone)
				{
					if (tile.valid) tile.img.src = this.getBlankTileUrl();
					tile.newUrl = newUrl;
					tile.valid = false;
					needPosponedUpdate = true;
				}
				else
				{
					tile.url = newUrl;
					tile.valid = true;
					tile.img.src = newUrl;
				}
				*/
			}
			/*
			else
			{
				tile.valid = true;
			}
			*/
			tile.img.style.left = (this.first_tile_vx + (j * this.tile_width)) + "px";
			tile.img.style.top = (this.first_tile_vy + (i * this.tile_height)) + "px";
		}
	}
	
	
	/*
	for (var i=row_from; i<=row_to; i++)
		for (var j=col_from; j<=col_to; j++)
			this.updateTile(
				update_img,
				this.tiles_map[i][j], 
				this.first_tile_vx + (j * this.tile_width),
				this.first_tile_vy + (i * this.tile_height),
				this.first_tile_col + j,
				this.first_tile_row - i);
	
	*/

	/*
	if (postpone && needPosponedUpdate)
	{
		Timer.cancelCall(this.postponed_refresh_id);
		this.postponed_refresh_id = Timer.delayedCall(this, "updateInvalidatedTiles", 10);		
	*/

}

Map.prototype.adjustTilesAfterPan = function()
{

	// unchanged rectangle of tiles
	var update_row_from = 0;
	var update_row_to = this.visible_rows;
	var update_col_from = 0;
	var update_col_to = this.visible_cols;
	
	// move columns from right to left
	while (0 < this.first_tile_vx)
	{
	
		update_col_from ++;
	
		this.first_tile_vx -= this.tile_width;
		this.first_tile_col --;
		
		for (var i=0; i<this.visible_rows+1; i++)
		{

			var map_tile = this.tiles_map[i].pop();
			this.tiles_map[i].unshift(map_tile);
			
//			this.updateTile(true, map_tile, 
//				this.first_tile_vx, null,
//				this.first_tile_col, this.first_tile_row - i);
				
		}
		
	}

	// move columns from left to right
	while (this.first_tile_vx < -this.tile_width)
	{

		update_col_to --;

		this.first_tile_vx += this.tile_width;
		this.first_tile_col ++;
		
		for (var i=0; i<this.visible_rows+1; i++)
		{
		
			var map_tile = this.tiles_map[i].shift();
			this.tiles_map[i].push(map_tile);
			
//			this.updateTile(true, map_tile, 
//				(this.first_tile_vx + (this.visible_cols+1)*this.tile_width), null,
//				this.first_tile_col + this.visible_cols, this.first_tile_row - i);
			
		}
		
	}

	// move rows from bottom to top
	while (0 < this.first_tile_vy)
	{
	
		update_row_from ++;
	
		this.first_tile_vy -= this.tile_height;
		this.first_tile_row ++;
		
		var first_map_row = this.tiles_map.pop();
		this.tiles_map.unshift(first_map_row);
		
		for (var j=0; j<this.visible_cols+1; j++)
		{

//			this.updateTile(true, first_map_row[j], 
//				null, this.first_tile_vy,
//				this.first_tile_col + j, this.first_tile_row);
			
		}
		
	}
	
	// move rows from top to bottom
	while (this.first_tile_vy < -this.tile_height)
	{
	
		update_row_to --;

		this.first_tile_vy += this.tile_height;
		this.first_tile_row --;
		
		var last_map_row = this.tiles_map.shift();
		this.tiles_map.push(last_map_row);
		
		for (var j=0; j<this.visible_cols+1; j++)
		{
		
//			this.updateTile(true, last_map_row[j], 
//				null, (this.first_tile_vy + (this.visible_rows+1)*this.tile_height),
//				this.first_tile_col + j, this.first_tile_row - this.visible_rows);
		
		}
		
	}
	
	// position last unchanged rectangle of tiles
	// false means = do not change URLs of them
	this.positionTiles(true, true);
//		update_row_from, update_row_to,
//		update_col_from, update_col_to);
//		
}

Map.prototype.createTileUrl = function(col, row)
{
	return this.server + "?" +
		"c=" + col + "&" +
		"r=" + row + "&" +
		"s=" + this.scale + "&" +
		"w=" + this.tile_width + "&" +
		"h=" + this.tile_height + "&" +
		"m=" + this.mapFile;
}

Map.prototype.getBlankTileUrl = function()
{
	return this.map_blank_img;
//	return this.server + "?" +
//		"m=" + this.mapFile;
}

/////////////////////////////////////////////////////////
// zooming history
/////////////////////////////////////////////////////////

Map.prototype.zoomSaveState = function()
{
	
	// current state
	var state = new MapZoomState(
		this.scale,
		this.getCenterX(),
		this.getCenterY());

	// delete elements after current element
	while (this.zoom_history_pos+1 < this.zoom_history.length)
		this.zoom_history.pop();
	
	// save the state
	this.zoom_history_pos++;
	this.zoom_history.push(state);
	
	// too many?
	while (this.zoom_history_max < this.zoom_history.length)
	{
		this.zoom_history.shift();
		this.zoom_history_pos--;
	}

	// save the entire history
	this.zoomHistorySave();

	// let buttons know
	this.zoomHistoryFireEvents();

}

Map.prototype.zoomHistoryMoveAndRestore = function(dir)
{

	// check
	var new_pos = this.zoom_history_pos + dir;
	if (!(0 <= new_pos && new_pos < this.zoom_history.length))
		return;

	// restore
	var state = this.zoom_history[new_pos];
	this.setScaleAndCenterTo(state.scale, state.cx, state.cy, false, true);

	// goto
	this.zoom_history_pos = new_pos;
	
	// let buttons know
	this.zoomHistoryFireEvents();
	
	// save the entire history
	this.zoomHistorySave();

}

Map.prototype.zoomHistorySave = function()
{

	// check if we care about saving the state
	if (!this.field_zoom_history)
		return;

	// "buffer"
	var str = new Array();
	
	// first the the position
	str.push(this.zoom_history_pos);
	
	// next the states
	for (var i = 0; i < this.zoom_history.length; i++)
	{
		var state = this.zoom_history[i];
		str.push(state.scale);
		str.push(state.cx);
		str.push(state.cy);
	}
	
	// store
	this.field_zoom_history.value = str.join(" ");
	
}

Map.prototype.zoomHistoryRestore = function()
{

	// main form
	var form = document.forms[this.form_name];
	if (!form) return;

	// hidden field
	this.field_zoom_history = form.elements[this.field_name_zoom_history];
	if (!this.field_zoom_history) return;
	
	// split values
	var values = this.field_zoom_history.value.split(/\s+/);
	
	// simple check
	if (values.length % 3 != 1)
		return;
		
	// history position
	this.zoom_history_pos = parseInt(values[0]);
	
	// history states
	var n = (values.length - 1) / 3;
	for (var i = 0; i < n; i++)
		this.zoom_history.push(
			new MapZoomState(
				parseInt(values[3*i+0]),
				parseFloat(values[3*i+1]),
				parseFloat(values[3*i+2])));

	// let buttons know
	this.zoomHistoryFireEvents();

}

Map.prototype.zoomHistoryFireEvents = function()
{
	if (this.go_back_button) this.go_back_button.historyChanged();
	if (this.go_forward_button) this.go_forward_button.historyChanged();
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
	return this.zoom_history_pos > 0;
}

Map.prototype.zoomCanGoForward = function()
{
	return this.zoom_history_pos+1 < this.zoom_history.length;
}

Map.prototype.registerGoForwardButton = function(button)
{
	this.go_forward_button = button;
}

Map.prototype.registerGoBackButton = function(button)
{
	this.go_back_button = button;
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

/////////////////////////////////////////////////////////
// panning by arrows around the map (not used)
/////////////////////////////////////////////////////////

Map.prototype.setMouseCursors = function()
{
	if (this.mouseMode == MapsGlobal.MAP_TOOL_PAN)
	{
		this.frame.style.cursor = "move";
		this.selector.style.cursor = "default";
	}
	else if (this.mouseMode == MapsGlobal.MAP_TOOL_ZOOM)
	{
		this.frame.style.cursor = "crosshair";
		this.selector.style.cursor = "default";
	}
	else if (this.mouseMode == MapsGlobal.MAP_TOOL_SELECTOR)
	{
		this.frame.style.cursor = "move";
		this.selector.style.cursor = "move";
	}
	else
	{
		this.frame.style.cursor = "default";
		this.selector.style.cursor = "default";
	}
}

Map.prototype.panningNextMove = function()
{

	this.first_tile_vx += this.pan_tool_dx;
	this.first_tile_vy += this.pan_tool_dy;
	this.adjustTilesAfterPan();
	
	this.pan_tool_timeout_id = Timer.delayedCall(this, "panningNextMove", this.pan_tool_delay);

}

Map.prototype.panUpMouseDown = function()
{
	this.pan_tool_dx = 0;
	this.pan_tool_dy = this.pan_tool_delta;
	this.panningNextMove();
}

Map.prototype.panDownMouseDown = function()
{
	pan_tool_dx = 0;
	pan_tool_dy = -pan_tool_delta;
	this.panningNextMove();
}

Map.prototype.panLeftMouseDown = function()
{
	this.pan_tool_dx = this.pan_tool_delta;
	this.pan_tool_dy = 0;
	this.panningNextMove();
}

Map.prototype.panRightMouseDown = function()
{
	this.pan_tool_dx = -this.pan_tool_delta;
	this.pan_tool_dy = 0;
	this.panningNextMove();
}

Map.prototype.panMouseUp = function()
{
	Timer.cancelCall(this.pan_tool_timeout_id);
}

Map.prototype.panToolInit = function()
{

	this.tool_pan_up = document.getElementById("pan-up");
	this.tool_pan_down = document.getElementById("pan-down");
	this.tool_pan_left = document.getElementById("pan-left");
	this.tool_pan_right = document.getElementById("pan-right");
	
	if (this.tool_pan_up)
	{
		this.tool_pan_up.unselectable = "on";
		EventAttacher.attach(this.tool_pan_up, "mousedown", this, "panUpMouseDown");
		EventAttacher.attach(this.tool_pan_up, "mouseup", this, "panMouseUp");
	}
	
	if (this.tool_pan_down)
	{
		tool_pan_down.unselectable = "on";
		EventAttacher.attach(this.tool_pan_down, "mousedown", this, "panDownMouseDown");
		EventAttacher.attach(this.tool_pan_down, "mouseup", this, "panMouseUp");
	}
	
	if (this.tool_pan_left)
	{
		this.tool_pan_left.unselectable = "on";
		EventAttacher.attach(this.tool_pan_left, "mousedown", this, "panLeftMouseDown");
		EventAttacher.attach(this.tool_pan_left, "mouseup", this, "panMouseUp");
	}
	
	if (this.tool_pan_right)
	{
		this.tool_pan_right.unselectable = "on";
		EventAttacher.attach(this.tool_pan_right, "mousedown", this, "panRightMouseDown");
		EventAttacher.attach(this.tool_pan_right, "mouseup", this, "panMouseUp");
	}
	
}

/////////////////////////////////////////////////////////
// points of interest
/////////////////////////////////////////////////////////

Map.prototype.showHideLabel = function(event)
{
	var x = event.clientX - this.vport_offset_left;
	var y = event.clientY - this.vport_offset_top;
	for (var i=0; i<this.points.length; i++)
	{
		var pnt = this.points[i];
		if (MapUtils.computeLengthSquared(x-pnt.vx, y-pnt.vy) <= 25)
		{
			this.showLabel(null, i);
			return;
		}
	}
	this.hideLabel(null);
}

Map.prototype.watchForLabels = function()
{
	if (!this.points) return;
	EventAttacher.attach(this.frame, "mousemove", this, "showHideLabel");
}

Map.prototype.forgetLabels = function()
{
	if (!this.points) return;
	this.hideLabel(null);
	EventAttacher.detach(this.frame, "mousemove", this, "showHideLabel");
}

Map.prototype.precomputePointsPositions = function()
{

	if (!this.points)
		return;

	for (var i=0; i<this.points.length; i++)
	{
		var pnt = this.points[i];
		pnt.vx = this.fromRealToVportX(pnt.x);
		pnt.vy = this.fromRealToVportY(pnt.y);
	}

/*

	// we don't have points
	if (!this.points && GraphicsGlobal.isSupported())
		return;

	// hashmap by col:row
	this.pointsByTiles = new Array();
	
	// efficiency
	var tileRealWidth = this.getTileRealWidth();
	var tileRealHeight = this.getTileRealHeight();
	
	// split
	for (var i=0; i<this.points.length; i++)
	{
		var pnt = this.points[i];

		// position		
		var col = Math.floor(pnt.x / tileRealWidth);
		var row = Math.floor(pnt.y / tileRealHeight);
		
		// do we have a tile for it?
		var pointsTileKey = col + ":" + row;
		var pointsTile = this.pointsByTiles[pointsTileKey];
		
		// no -> create new
		if (!pointsTile)
		{
			pointsTile = GraphicsGlobal.createGraphics();
			pointsTile.getRoot().style.position = "absolute";
			this.pointsByTiles[pointsTileKey] = pointsTile;
		}
		
		// position on the tile
		var posOnTileX = this.fromRealToPx(pnt.x) - col*this.tile_width + this.pointsExtraSpace;
		var posOnTileY = (row+1)*this.tile_height - this.fromRealToPx(pnt.y) + this.pointsExtraSpace;
		
		// draw a point on the tile
		var circ = pointsTile.createCircle();
		circ.setCenter(posOnTileX, posOnTileY);
		circ.setRadius(5);
		circ.setFill("Black");
		circ.setFillOpacity(1);
		circ.getRoot().style.cursor = "pointer";
		EventAttacher.attach(circ.getRoot(), "mouseover", this, "showLabel", i);
		EventAttacher.attach(circ.getRoot(), "mouseout", this, "hideLabel");
		pointsTile.appendChild(circ);
	
	}
	
*/

}

Map.prototype.showLabel = function(event, pntIndex)
{
	var pnt = this.points[pntIndex];
	
	var x = this.fromRealToVportX(pnt.x);
	var y = this.fromRealToVportY(pnt.y);

	this.bubbleText.innerHTML = pnt.text;
	this.bubble.style.display = "block";
	
	//pnt.circ.setAttributeNS(null, "r", "8");

	this.bubble.style.left = (x + 5) + "px";
	this.bubble.style.top = (y - 5 - this.bubble.offsetHeight) + "px";
	
	this.frame.style.cursor = "pointer";

}

Map.prototype.hideLabel = function(event)
{
	this.setMouseCursors();
	this.bubble.style.display = "none";
}


/////////////////////////////////////////////////////////
// map control: resizing and init
/////////////////////////////////////////////////////////

Map.prototype.setSize = function(width, height)
{

	this.width = width;
	this.height = height;
	
	var cx = this.getCenterX();
	var cy = this.getCenterY();
	
	this.updateControlsLayout();
	this.positionTiles(true, false);
	
	this.setScaleAndCenterTo(this.scale, cx, cy, false, true);

}

Map.prototype.mapWindowResize = function()
{	
	this.changeSizeByWindow();
	this.updateControlsLayout();
	this.positionTiles(true, false);
}

Map.prototype.initSizeByHTML = function()
{
	this.width = this.map_control.clientWidth;
	this.height = this.map_control.clientHeight;
}
	
Map.prototype.changeSizeByWindow = function()
{
	this.width = Math.max((ElementUtils.getPageWidth() - this.map_control.offsetLeft), this.min_map_control_width);
	this.height = Math.max((ElementUtils.getPageHeight() - this.map_control.offsetTop), this.min_map_control_height);
}

Map.prototype.updateControlsLayout = function()
{
	
	// offsets
	var top = this.map_control_top_tools ? this.map_control_top_tools.offsetHeight : 0;
	var bottom = this.map_control_bottom_tools ? this.map_control_bottom_tools.offsetHeight : 0;
	var left = this.map_control_left_tools ? this.map_control_left_tools.offsetWidth : 0;
	var right = this.map_control_right_tools ? this.map_control_right_tools.offsetWidth : 0;
	
	// set vport size
	this.vport_width = this.width - left - right;
	this.vport_height = this.height - top - bottom;
	
	// control
	if (!this.fixedSize)
	{
		this.map_control.style.left = this.map_control.offsetLeft + "px";
		this.map_control.style.top = this.map_control.offsetTop + "px";
	}
	if (this.map_control)
	{
		this.map_control.style.width = this.width + "px";
		this.map_control.style.height = this.height + "px";
	}
	
	// top tools
	if (this.map_control_top_tools)
	{
		this.map_control_top_tools.style.left = "0px";
		this.map_control_top_tools.style.top = "0px";
		this.map_control_top_tools.style.width = (this.width) + "px";
	}

	// bottom tools
	if (this.map_control_bottom_tools)
	{
		this.map_control_bottom_tools.style.left = "0px";
		this.map_control_bottom_tools.style.top = (this.height - bottom) + "px";
		this.map_control_bottom_tools.style.width = (this.width) + "px";
	}

	// left tools
	if (this.map_control_left_tools)
	{
		this.map_control_left_tools.style.left = "0px";
		this.map_control_left_tools.style.top = (top) + "px";
		this.map_control_left_tools.style.height = (this.height - bottom - top) + "px";
	}

	// right tools
	if (this.map_control_right_tools)
	{
		this.map_control_right_tools.style.left = (this.width - right) + "px";
		this.map_control_right_tools.style.top = (top) + "px";
		this.map_control_right_tools.style.height = (this.height - bottom - top) + "px";
	}
	
	// this is used at many places
	this.vport_offset_left = left + this.map_control.offsetLeft;
	this.vport_offset_top = top + this.map_control.offsetTop;

	// map itself
	this.frame.style.left = (left) + "px";
	this.frame.style.top = (top) + "px";
	this.frame.style.width = (this.vport_width) + "px";
	this.frame.style.height = (this.vport_height) + "px";
	
	// number of tiles needed
	this.visible_cols = Math.floor(this.vport_width / this.tile_width) + 1;
	this.visible_rows = Math.floor(this.vport_height / this.tile_height) + 1;
	
	// delete old tiles for map
	if (this.tiles_map != null)
	{
		for (var i=0; i<this.tiles_map.length; i++)
		{
			for (var j=0; j<this.tiles_map[i].length; j++)
			{
				var tile = this.tiles_map[i][j];
				this.frame.removeChild(tile.img);
				if (tile.points) this.frame.removeChild(tile.points.getRoot());
			}
		}
	}
	
	// tiles for maps
	this.tiles_map = new Array();
	for (var i=0; i<this.visible_rows+1; i++)
	{
		this.tiles_map[i] = new Array();
		for (var j=0; j<this.visible_cols+1; j++)
		{			
			var tile = document.createElement("IMG");
			tile.onload = function() {this.style.visibility = "visible"};
			this.tiles_map[i][j] = new MapTile(tile);
			if (IE)
			{
				tile.unselectable = "on";
				tile.galleryImg = "no";
			}
			else
			{
				tile.style.MozUserFocus = "none";
				tile.style.MozUserSelect = "none";
			}
			tile.onmousedown = MapUtils.falseFnc;
			tile.style.position = "absolute";
			tile.width = this.tile_width;
			tile.height = this.tile_height;
			tile.style.left = (j * this.tile_width) + "px";
			tile.style.top = (i * this.tile_height) + "px";
			tile.points = null;
			this.frame.insertBefore(tile, this.selector);
		}
	}
	
}

Map.prototype.mapControlsInit = function()
{

	this.map_control = document.getElementById(this.map_control_id);
	//this.map_control.style.display = "block";
	
	this.map_control_top_tools = document.getElementById(this.map_control_top_tools_id);
	if (this.map_control_top_tools) this.map_control_top_tools.style.position = "absolute";
	
	this.map_control_bottom_tools = document.getElementById(this.map_control_bootom_tools_id);
	if (this.map_control_bottom_tools) this.map_control_bottom_tools.style.position = "absolute";

	this.map_control_left_tools = document.getElementById(this.map_control_left_tools_id);
	if (this.map_control_left_tools) this.map_control_left_tools.style.position = "absolute";
	
	this.map_control_right_tools = document.getElementById(this.map_control_right_tools_id);
	if (this.map_control_right_tools) this.map_control_right_tools.style.position = "absolute";
	
	this.bubble = document.getElementById(this.bubbleId)
	this.bubbleText = document.getElementById(this.bubbleTextId)
	if (this.bubble) this.bubble.style.position = "absolute";

	this.frame = document.getElementById(this.frameId);
	this.frame.style.backgroundColor = "White";
	this.frame.style.position = "absolute";
	this.frame.style.overflow = "hidden";
	if (IE)
	{
		this.frame.unselectable = "on";
	}
	if (GK)
	{
		this.frame.style.MozUserFocus = "none";
		this.frame.style.MozUserSelect = "none";
	}

	EventAttacher.attach(this.frame, "mousedown", this, "mapStartDrag");
	if (IE) EventAttacher.attach(this.frame, "mousewheel", this, "mapMouseWheel");

	this.selector = document.createElement("DIV");
	this.selector.style.position = "absolute";
	this.selector.style.display = "none";
	this.selector.style.borderStyle = "solid";
	this.selector.style.borderWidth = this.selector_border_width + "px";
	this.selector.style.borderColor = this.selector_color;
	this.selector.style.left = "0px";
	this.selector.style.top = "0px";
	this.selector.style.width = "200px";
	this.selector.style.height = "200px";
	this.frame.appendChild(this.selector);
	
	var bg = document.createElement("DIV");
	bg.style.width = "100%";
	bg.style.height = "100%";
	bg.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=" + this.selector_opacity + ")";
	bg.style.MozOpacity = this.selector_opacity / 100;
	bg.style.backgroundColor = this.selector_color;
	this.selector.appendChild(bg);

	if (!this.fixedSize)
		EventAttacher.attachOnWindowEvent("resize", this, "mapWindowResize");
	
}

Map.prototype.hiddenFieldsInit = function()
{

	var form = document.forms[this.form_name];
	if (!form)
	{
		this.hasHiddenExtendFields = false;
		return;
	}

	this.field_x1 = form.elements[this.field_name_x1];
	this.field_y1 = form.elements[this.field_name_y1];
	this.field_x2 = form.elements[this.field_name_x2];
	this.field_y2 = form.elements[this.field_name_y2];
	
	this.hasHiddenExtendFields = this.field_x1 && this.field_y1 && this.field_x2 && this.field_y2;

}

Map.prototype.restoreState = function()
{

	// zoom
	if (this.hasHiddenExtendFields)
	{
		var x1 = parseFloat(this.field_x1.value);
		var y1 = parseFloat(this.field_y1.value);
		var x2 = parseFloat(this.field_x2.value);
		var y2 = parseFloat(this.field_y2.value);
		this.zoomMapTo(x1, y1, x2, y2, false, 0, false, false, true);
	}
	else
	{
		//this.zoomMapTo(-180, -90, 180, 90, false, 0, false, false, true);
		this.zoomMapTo(-18000000, -9000000, 18000000, 9000000, false, 0, false, false, true);
	}
	
	// and init history if empty
	if (this.zoom_history_pos == -1)
	{
		this.zoomSaveState();
	}

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
	this.mapControlsInit();
	this.initSizeByHTML();
	this.updateControlsLayout();
	
	// scale indicator	
	this.scaleIndicatorInit();

	// pan tools
	this.panToolInit();
	
	// zoom history
	this.zoomHistoryRestore();
	
	// show something
	if (restoreState)
	{
		this.restoreState();
		this.watchForLabels();
	}
	
	// init minimap
	this.initMiniMap();
		
	// let others know that we are done
	this.initListeners.invoke();
	
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

function MapZoomSlider(bgElementId, knobElementId, map)
{
	this.bgElementId = bgElementId;
	this.knobElementId = knobElementId;
	this.map = map;
	this.scale = 0;
	this.sliding = false;
	this.map.registerZoomSlider(this);
	this.map.initListeners.register(this, "init", null);
}

MapZoomSlider.prototype.init = function()
{

	this.bg = document.getElementById(this.bgElementId);
	this.knob = document.getElementById(this.knobElementId);
	
	this.knob.style.display = "block";

	this.sliderWidth = ElementUtils.getOffsetWidth(this.bg);
	this.knobWidth = ElementUtils.getOffsetWidth(this.knob);
	this.sliderOffsetLeft = ElementUtils.getOffsetLeft(this.bg);
	this.sliderEffectiveWidth = this.bg.offsetWidth - this.knob.offsetWidth;
	
	EventAttacher.attach(this.knob, "mousedown", this, "mouseDown");
	EventAttacher.attach(this.bg, "click", this, "click");
	
	this.zoomChanged();
}

MapZoomSlider.prototype.capKnobPosition = function(knobLeft)
{
	if (knobLeft < 0) knobLeft = 0;
	if (knobLeft > this.sliderEffectiveWidth) knobLeft = this.sliderEffectiveWidth;
	return knobLeft;
}

MapZoomSlider.prototype.setKnobNormalizedPosition = function(t)
{
	this.setKnobPosition(t * this.sliderEffectiveWidth);
}

MapZoomSlider.prototype.setKnobPosition = function(knobLeft)
{
	this.knob.style.left = this.capKnobPosition(knobLeft) + "px";
}

MapZoomSlider.prototype.click = function(event)
{
	//alert(this.sliderOffsetLeft);
	//alert(event.clientX);
	var pos = event.clientX;
	this.setKnobPosition(pos - this.sliderOffsetLeft - this.knobWidth / 2);
	var capPos = this.capKnobPosition(pos - this.sliderOffsetLeft - this.knobWidth / 2);
	var t = capPos / this.sliderEffectiveWidth;
	this.map.changeScaleNormalized(t, false);
}

MapZoomSlider.prototype.mouseDown = function(event)
{
	this.sliding = true;
	this.startPos = event.clientX;
	this.startOffsetLeft = this.knob.offsetLeft;
	this.lastPos = this.startPos;
	EventAttacher.attachOnWindowEvent("mousemove", this, "mouseMove");
	EventAttacher.attachOnWindowEvent("mouseup", this, "mouseUp");
}

MapZoomSlider.prototype.mouseMove = function(event)
{
	var pos = event.clientX;
	this.setKnobPosition(this.knob.offsetLeft + (pos - this.lastPos));
	this.lastPos = pos;
}

MapZoomSlider.prototype.mouseUp = function(event)
{
	EventAttacher.detachOnWindowEvent("mousemove", this, "mouseMove");
	EventAttacher.detachOnWindowEvent("mouseup", this, "mouseUp");
}

MapZoomSlider.prototype.zoomChanged = function()
{
	this.setKnobNormalizedPosition(this.map.getScaleNormalized());
}
