var MapsGlobal = 
{

	MAP_TOOL_ZOOM: 1,
	MAP_TOOL_PAN: 2,

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
		bubbleTextId // inner <td> for the text in the bubble
	)
	{
	
		var map = new Map();
		this.maps[mapId] = map;
		
		// server and map file
		map.map_file = mapFile;
		map.map_tiles_server = mapTilesServer;
		
		// HTML
		map.fixed_size = fixedSize;
		map.map_control_id = mapControlId;
		map.map_frame_id = mapFrameId;
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
		map.bubble_id = bubbleId;
		map.bubble_text_id = bubbleTextId;
		
		// buttons
		if (buttonBackId)
		{
			var btn = new MapButtonGoBack(buttonBackId, map);
			map.registerGoBackButton(btn);
		}

		if (buttonForwardId)
		{
			var btn = new MapButtonGoForward(buttonForwardId, map);
			map.registerGoForwardButton(btn);
		}

		if (buttonZoomPlusId)
			new MapButtonZoomPlus(buttonZoomPlusId, map);

		if (buttonZoomMinusId)
			new MapButtonZoomMinus(buttonZoomMinusId, map);
			
		if (sliderBgId && sliderKnobId)
			new MapZoomSlider(sliderBgId, sliderKnobId, map);
			
		if (buttonPanId && buttonZoomId)
			new MapZoomAndPanButtons(buttonPanId, buttonZoomId, formName, fieldNameForMouseMode, map);

		if (mapSizes && fieldNameForMapSize)
			new MapZoomSize(mapSizes, formName, fieldNameForMapSize, map);

		// call init after page loads
		EventAttacher.attachOnWindowEvent("load", map, "init");
	
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

function Map()
{

	// server and map file
	this.map_file = null;
	this.map_tiles_server = "servlet/maptile";

	// container
	this.fixed_size = true;
	this.width = 0;
	this.height = 0;
	this.map_control = null;
	this.map_control_id = null;
	this.min_map_control_width = 600;
	this.min_map_control_height = 400;
	
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
	this.map_frame = null;
	this.map_frame_id = null;
	this.tiles_map = null;
	this.map_blank_img = "blank.png";
	
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
	
	// current position
	this.first_tile_col = null;
	this.first_tile_row = null;
	this.first_tile_vx = null;
	this.first_tile_vy = null;
	this.scale = null;
	this.scale_min = 1;
	this.scale_max = 100;
	this.scale_factor_plus = 2.0;
	this.scale_factor_minus = 0.5;
	
	// zooming and panning
	this.nav_mouse_mode = MapsGlobal.MAP_TOOL_ZOOM;
	this.map_mouse_mode = MapsGlobal.MAP_TOOL_PAN;
	this.zoom_slider = null;
	this.selector_border_width = 1;
	this.selector_color = "White"; //"#0066CC";
	this.selector_opacity = 30;
	this.map_selector = null;
	this.nav_selector = null;
	
	// points of interest
	this.points = null;
	this.pointsExtraSpace = 10;
	this.pointsByTiles = new Array();
	
	// bubble
	this.bubble_id = null;
	this.bubble_text_id = null;
	this.bubble = null;
	this.bubble_text = null;
	
	// scale indicator & selector
	this.scale_bar_indicator = null;
	this.scale_text_indicator = null;
	this.has_scale_indicator = false;
	
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
	this.has_hidden_extend_fields = false;
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
	this.init_listeners = new Array();

}

function MapZoomState(scale, cx, cy)
{
	this.cx = cx;
	this.cy = cy;
	this.scale = scale;
}

function MapTile(img)
{
	this.img = img;
	this.valid = false;
	this.points = null;
	this.url = "";
}

/////////////////////////////////////////////////////////
// generic functions
/////////////////////////////////////////////////////////

Map.prototype.falseFnc = function()
{
	return false;
}

/////////////////////////////////////////////////////////
// generic geometric functions
/////////////////////////////////////////////////////////

Map.prototype.computeLength = function(x, y)
{
	return Math.sqrt(this.computeLengthSquared(x, y));
}

Map.prototype.computeLengthSquared = function(x, y)
{
	return x*x + y*y;
}

Map.prototype.computeDistancePointLine = function(px, py, x1, y1, x2, y2)
{
	return Math.abs(
		(x2-x1)*(y1-py) - (x1-px)*(y2-y1)) /
		this.computeLength(x2-x1, y2-y1);
}

Map.prototype.computeAngle = function(x1, y1, x2, y2, x3, y3)
{
	var v1x = x1-x2;
	var v1y = y1-y2;
	var v2x = x3-x2;
	var v2y = y3-y2;
	return Math.acos (
		(v1x*v2x + v1y*v2y) /
		(this.computeLength(v1x, v1y) * this.computeLength(v2x, v2y)));
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
	return v / this.scale;
}

Map.prototype.fromRealToPx = function(v)
{
	return v * this.scale;
}

Map.prototype.roundAndCapScale = function(s)
{
	s = Math.round(s);
	if (s > this.scale_max) s = this.scale_max;
	if (s < this.scale_min) s = this.scale_min;
	return s;
}


/////////////////////////////////////////////////////////
// generic function for drawing the selector
/////////////////////////////////////////////////////////

Map.prototype.drawSelector = function(sel, x1, y1, x2, y2)
{

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
	sel.style.display = "block";
	sel.style.left = (x1) + "px";
	sel.style.top = (y1) + "px";
	sel.style.width = (x2-x1) + "px";
	sel.style.height = (y2-y1) + "px";

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

	if (!this.has_scale_indicator)
		return;

	var meters = this.fromPxToReal(100);
	var digits = Math.floor(Math.log(meters) / Math.LN10);
	var closest_power = Math.pow(10, digits);
	meters = Math.floor(meters / closest_power) * closest_power;
	var pixels = this.fromRealToPx(meters);
	
	scale_bar_indicator.style.width = Math.round(pixels) + "px";
	scale_text_indicator.innerHTML = this.formatMetersForDisplay(meters);

}

Map.prototype.scaleIndicatorInit = function()
{
	this.scale_bar_indicator = document.getElementById("map-scale-bar");
	this.scale_text_indicator = document.getElementById("map-scale-text");
	this.has_scale_indicator = this.scale_bar_indicator && this.scale_text_indicator;
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
	
	switch (this.map_mouse_mode)
	{
		case MapsGlobal.MAP_TOOL_ZOOM:
		
			// hide selector
			this.mapHideSelector();
			
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
			this.splitPointsToTiles();
			break;
			
	}
	
}

Map.prototype.mapMouseMove = function(event)
{

	// new position
	var x = event.clientX - this.vport_offset_left;
	var y = event.clientY - this.vport_offset_top;
	
	switch (this.map_mouse_mode)
	{
		case MapsGlobal.MAP_TOOL_PAN:
		
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
			
			break;
			
		case MapsGlobal.MAP_TOOL_ZOOM:

			// draw selector
			this.mapDrawSelector(
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

Map.prototype.mapDrawSelector = function(x1, y1, x2, y2)
{
	this.drawSelector(
		this.map_selector,
		x1 + this.map_frame.scrollLeft,
		y1 + this.map_frame.scrollTop,
		x2 + this.map_frame.scrollLeft,
		y2 + this.map_frame.scrollTop);
}

Map.prototype.mapHideSelector = function()
{
	this.map_selector.style.display = "none";
}


/////////////////////////////////////////////////////////
// map size
/////////////////////////////////////////////////////////

Map.prototype.zoomPlus = function(notify_zoom_change)
{
	this.changeScale(this.scale * this.scale_factor_plus, notify_zoom_change);
}

Map.prototype.zoomMinus = function(notify_zoom_change)
{
	this.changeScale(this.scale * this.scale_factor_minus, notify_zoom_change);
}

Map.prototype.changeScaleNormalized = function(t, notify_zoom_change)
{
	this.changeScale(this.scale_min + t * (this.scale_max - this.scale_min), notify_zoom_change);
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

Map.prototype.changeScale = function(new_scale, notify_zoom_change)
{

	// has it changed?
	new_scale = this.roundAndCapScale(new_scale);
	if (new_scale == this.scale) return;
	
	// get old center
	var cx = this.getCenterX();
	var cy = this.getCenterY();
	
	// center
	this.setScaleAndCenterMapTo(new_scale, cx, cy, true, notify_zoom_change);

}

Map.prototype.zoomMapTo = function(x1, y1, x2, y2, save_state, border, notify_zoom_change)
{

	// ensure that first is top left
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
	
	// center and dimensions
	var cx = 0.5 * (x1 + x2);
	var cy = 0.5 * (y1 + y2);
	var width = x2 - x1;
	var height = y2 - y1;

	// compute where we have to fit it
	if (border == null) border = 0;
	var new_vport_width = this.getVportWidth() - 2*border;
	var new_vport_height = this.getVportHeight() - 2*border;
	if (new_vport_width < 100) new_vport_width = 100;
	if (new_vport_height < 100) new_vport_height = 100;

	// adjust to the ratio
	var vport_ratio = new_vport_width / new_vport_height;
	if (width/height < vport_ratio)
	{
		width = vport_ratio * (y2 - y1);
	}
	else
	{
		height = (x2 - x1) / vport_ratio;
	}
	x1 = cx - 0.5 * width;
	x2 = cx + 0.5 * width;
	y1 = cy - 0.5 * height;
	y2 = cy + 0.5 * height;
	width = x2 - x1;
	height = y2 - y1;
	
	// set scale
	var new_scale = new_vport_width / width;
	
	// center it
	this.setScaleAndCenterMapTo(new_scale, cx, cy, save_state, notify_zoom_change);
	
}

Map.prototype.setScaleAndCenterMapTo = function(new_scale, x, y, save_state, notify_zoom_change)
{

	// scale has changed
	new_scale = this.roundAndCapScale(new_scale);
	if (this.scale != new_scale)
	{
		this.scale = new_scale;
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

	// set it
	this.first_tile_col = col;
	this.first_tile_row = row;
	this.first_tile_vx = vx;
	this.first_tile_vy = vy;
	
	// retile points
	this.splitPointsToTiles();
	
	// draw all tiles
	this.positionTiles(true);
	
	// position poits of interest
	//this.drawPointsOfInterest();
	//this.repositionPointsOfInterest();
	
	// remember current state
	if (save_state) this.zoomSaveState();
	
	// notify change of zoom
	if (notify_zoom_change) this.notifyZoomChange();
	
}


/////////////////////////////////////////////////////////
// main drawing procedures
/////////////////////////////////////////////////////////

Map.prototype.saveState = function()
{
	if (this.has_hidden_extend_fields)
	{
		this.field_x1.value = this.getMapX1();
		this.field_y1.value = this.getMapY1();
		this.field_x2.value = this.getMapX2();
		this.field_y2.value = this.getMapY2();
	}
}


Map.prototype.updateTile = function(update_img, map_tile, x, y, col, row)
{
	if (update_img)
	{
	
		// points
		if (map_tile.points)
		{
			this.map_frame.removeChild(map_tile.points.getRoot());
		}
		var pointsTile = this.pointsByTiles[col + ":" + row];
		if (pointsTile)
		{
			map_tile.points = pointsTile;
			this.map_frame.insertBefore(pointsTile.getRoot(), this.map_selector);
		}
		else
		{
			map_tile.points = null;
		}
		
		// map
		map_tile.img.src = this.getBlankTileUrl();
		map_tile.url = this.createTileUrl(col, row);
		map_tile.valid = false;
	}
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

Map.prototype.refreshInvalidatedTiles = function()
{
	for (var i=0; i<this.visible_rows+1; i++)
	{
		for (var j=0; j<this.visible_cols+1; j++)
		{
			var tile = this.tiles_map[i][j];
			if (!tile.valid)
			{
				tile.img.src = tile.url;
			}
		}
	}
}

Map.prototype.positionTiles = function(update_img, row_from, row_to, col_from, col_to)
{

	this.saveState();
	
	if (row_from == null) row_from = 0;
	if (row_to == null) row_to = this.visible_rows;
	if (col_from == null) col_from = 0;
	if (col_to == null) col_to = this.visible_cols;
	
	for (var i=row_from; i<=row_to; i++)
		for (var j=col_from; j<=col_to; j++)
			this.updateTile(
				update_img,
				this.tiles_map[i][j], 
				this.first_tile_vx + (j * this.tile_width),
				this.first_tile_vy + (i * this.tile_height),
				this.first_tile_col + j,
				this.first_tile_row - i);
				
	Timer.cancelCall(this.postponed_refresh_id);
	this.postponed_refresh_id = Timer.delayedCall(this, "refreshInvalidatedTiles", 10);		

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
			
			this.updateTile(true, map_tile, 
				this.first_tile_vx, null,
				this.first_tile_col, this.first_tile_row - i);
				
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
			
			this.updateTile(true, map_tile, 
				(this.first_tile_vx + (this.visible_cols+1)*this.tile_width), null,
				this.first_tile_col + this.visible_cols, this.first_tile_row - i);
			
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

			this.updateTile(true, first_map_row[j], 
				null, this.first_tile_vy,
				this.first_tile_col + j, this.first_tile_row);
			
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
		
			this.updateTile(true, last_map_row[j], 
				null, (this.first_tile_vy + (this.visible_rows+1)*this.tile_height),
				this.first_tile_col + j, this.first_tile_row - this.visible_rows);
		
		}
		
	}
	
	// position last unchanged rectangle of tiles
	// false means = do not change URLs of them
	this.positionTiles(false,
		update_row_from, update_row_to,
		update_col_from, update_col_to);
		
	// position poits of interest
	// this.repositionPointsOfInterest();

}

Map.prototype.createTileUrl = function(col, row)
{
	return this.map_tiles_server + "?" +
		"c=" + col + "&" +
		"r=" + row + "&" +
		"s=" + this.scale + "&" +
		"w=" + this.tile_width + "&" +
		"h=" + this.tile_height + "&" +
		"m=" + this.map_file;
}

Map.prototype.getBlankTileUrl = function()
{
	return this.map_blank_img;
//	return this.map_tiles_server + "?" +
//		"m=" + this.map_file;
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
	this.setScaleAndCenterMapTo(state.scale, state.cx, state.cy, false, true);

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
	this.map_mouse_mode = MapsGlobal.MAP_TOOL_PAN;
	this.revertToDefaultCursor();
}

Map.prototype.setMouseModeToZoom = function()
{
	this.map_mouse_mode = MapsGlobal.MAP_TOOL_ZOOM;
	this.revertToDefaultCursor();
}

Map.prototype.revertToDefaultCursor = function()
{
	if (this.map_mouse_mode == MapsGlobal.MAP_TOOL_PAN)
	{
		this.map_frame.style.cursor = "move";
	}
	else
	{
		this.map_frame.style.cursor = "crosshair";
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
		if (this.computeLengthSquared(x-pnt.vx, y-pnt.vy) <= 25)
		{
			this.showLabel(null, i);
			return;
		}
	}
	this.hideLabel(null);
}

Map.prototype.watchForLabels = function()
{
	EventAttacher.attach(this.map_frame, "mousemove", this, "showHideLabel");
}

Map.prototype.forgetLabels = function()
{
	this.hideLabel(null);
	EventAttacher.detach(this.map_frame, "mousemove", this, "showHideLabel");
}

Map.prototype.splitPointsToTiles = function()
{

	for (var i=0; i<this.points.length; i++)
	{
		var pnt = this.points[i];
		pnt.vx = this.fromRealToVportX(pnt.x);
		pnt.vy = this.fromRealToVportY(pnt.y);
	}

	return;

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

}

Map.prototype.showLabel = function(event, pntIndex)
{
	var pnt = this.points[pntIndex];
	
	var x = this.fromRealToVportX(pnt.x);
	var y = this.fromRealToVportY(pnt.y);

	this.bubble_text.innerHTML = pnt.text;
	this.bubble.style.display = "block";
	
	//pnt.circ.setAttributeNS(null, "r", "8");

	this.bubble.style.left = (x + 5) + "px";
	this.bubble.style.top = (y - 5 - this.bubble.offsetHeight) + "px";
	
	this.map_frame.style.cursor = "pointer";

}

Map.prototype.hideLabel = function(event)
{
	this.revertToDefaultCursor();
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
	this.positionTiles(true);
	
	this.setScaleAndCenterMapTo(this.scale, cx, cy, false, true);

}

Map.prototype.mapWindowResize = function()
{	
	this.changeSizeByWindow();
	this.updateControlsLayout();
	this.positionTiles(true);
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
	if (!this.fixed_size)
	{
		this.map_control.style.left = this.map_control.offsetLeft + "px";
		this.map_control.style.top = this.map_control.offsetTop + "px";
	}
	this.map_control.style.width = this.width + "px";
	this.map_control.style.height = this.height + "px";
	
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
	this.map_frame.style.left = (left) + "px";
	this.map_frame.style.top = (top) + "px";
	this.map_frame.style.width = (this.vport_width) + "px";
	this.map_frame.style.height = (this.vport_height) + "px";
	
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
				this.map_frame.removeChild(tile.img);
				if (tile.points) this.map_frame.removeChild(tile.points.getRoot());
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
			tile.onmousedown = this.falseFnc;
			tile.style.position = "absolute";
			tile.width = this.tile_width;
			tile.height = this.tile_height;
			tile.style.left = (j * this.tile_width) + "px";
			tile.style.top = (i * this.tile_height) + "px";
			tile.points = null;
			this.map_frame.insertBefore(tile, this.map_selector);
		}
	}
	
}

Map.prototype.mapControlsInit = function()
{

	this.map_control = document.getElementById(this.map_control_id);
	this.map_control.style.display = "block";
	
	this.map_control_top_tools = document.getElementById(this.map_control_top_tools_id);
	if (this.map_control_top_tools) this.map_control_top_tools.style.position = "absolute";
	
	this.map_control_bottom_tools = document.getElementById(this.map_control_bootom_tools_id);
	if (this.map_control_bottom_tools) this.map_control_bottom_tools.style.position = "absolute";

	this.map_control_left_tools = document.getElementById(this.map_control_left_tools_id);
	if (this.map_control_left_tools) this.map_control_left_tools.style.position = "absolute";
	
	this.map_control_right_tools = document.getElementById(this.map_control_right_tools_id);
	if (this.map_control_right_tools) this.map_control_right_tools.style.position = "absolute";
	
	this.bubble = document.getElementById(this.bubble_id)
	this.bubble_text = document.getElementById(this.bubble_text_id)
	if (this.bubble) this.bubble.style.position = "absolute";

	this.map_frame = document.getElementById(this.map_frame_id);
	this.map_frame.style.backgroundColor = "White";
	this.map_frame.style.position = "absolute";
	this.map_frame.style.overflow = "hidden";
	if (IE)
	{
		this.map_frame.unselectable = "on";
	}
	if (GK)
	{
		this.map_frame.style.MozUserFocus = "none";
		this.map_frame.style.MozUserSelect = "none";
	}

	EventAttacher.attach(this.map_frame, "mousedown", this, "mapStartDrag");
	// EventAttacher.attach(this.map_frame, "mouseup", this, "mapStopDrag");
	if (IE) EventAttacher.attach(this.map_frame, "mousewheel", this, "mapMouseWheel");

	this.map_selector = document.createElement("DIV");
	this.map_selector.style.position = "absolute";
	this.map_selector.style.display = "none";
	this.map_selector.style.borderStyle = "solid";
	this.map_selector.style.borderWidth = this.selector_border_width + "px";
	this.map_selector.style.borderColor = this.selector_color;
	this.map_selector.style.left = "0px";
	this.map_selector.style.top = "0px";
	this.map_selector.style.width = "200px";
	this.map_selector.style.height = "200px";
	this.map_frame.appendChild(this.map_selector);
	
	var bg = document.createElement("DIV");
	bg.style.width = "100%";
	bg.style.height = "100%";
	bg.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=" + this.selector_opacity + ")";
	bg.style.MozOpacity = this.selector_opacity / 100;
	bg.style.backgroundColor = this.selector_color;
	this.map_selector.appendChild(bg);

	if (!this.fixed_size)
		EventAttacher.attachOnWindowEvent("resize", this, "mapWindowResize");
	
}

Map.prototype.hiddenFieldsInit = function()
{

	var form = document.forms[this.form_name];
	if (!form)
	{
		this.has_hidden_extend_fields = false;
		return;
	}

	this.field_x1 = form.elements[this.field_name_x1];
	this.field_y1 = form.elements[this.field_name_y1];
	this.field_x2 = form.elements[this.field_name_x2];
	this.field_y2 = form.elements[this.field_name_y2];
	
	this.has_hidden_extend_fields = this.field_x1 && this.field_y1 && this.field_x2 && this.field_y2;

}

Map.prototype.restoreState = function()
{

	// zoom
	if (this.has_hidden_extend_fields)
	{
		var x1 = parseFloat(this.field_x1.value);
		var y1 = parseFloat(this.field_y1.value);
		var x2 = parseFloat(this.field_x2.value);
		var y2 = parseFloat(this.field_y2.value);
		this.zoomMapTo(x1, y1, x2, y2, false, 0, false);
	}
	else
	{
		this.zoomMapTo(-180, -90, 180, 90, false, 0, false);
	}
	
	// and init history if empty	
	if (this.zoom_history_pos == -1)
	{
		this.zoomSaveState();
	}

}

Map.prototype.registerInitListener = function(object, method, arg)
{
	var reg = new Object();
	reg.object = object;
	reg.method = method;
	reg.arg = arg;
	this.init_listeners.push(reg);
}

Map.prototype.invokeInitListeners = function()
{
	for (var i=0; i<this.init_listeners.length; i++)
	{
		var reg = this.init_listeners[i];
		reg.object[reg.method](reg.arg);
	}
}

/////////////////////////////////////////////////////////
// main init
/////////////////////////////////////////////////////////

Map.prototype.init = function()
{

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
	this.restoreState();
	this.watchForLabels();
	
	// init tools
	this.invokeInitListeners();
	
}

/////////////////////////////////////////////////////////
// generic button functions
/////////////////////////////////////////////////////////
/*
function MapButton(elementId, map)
{
	this.element = null;
	this.map = map;
	this.elementId = elementId;
	EventAttacher.attach(window, "load", this, "init");
}

MapButton.prototype.init = function()
{
	this.element = document.getElementById(this.elementId);
	EventAttacher.attach(this.element, "click", this, "click");
}
*/

/////////////////////////////////////////////////////////
// zoom + button
/////////////////////////////////////////////////////////

function MapButtonZoomPlus(elementId, map)
{
	this.element = null;
	this.map = map;
	this.elementId = elementId;
	this.map.registerInitListener(this, "init", null);
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
	this.map.registerInitListener(this, "init", null);
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
	this.map.registerInitListener(this, "init", null);
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
	this.map.registerInitListener(this, "init", null);
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
	this.map.registerInitListener(this, "init", null);
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
	this.map.registerInitListener(this, "init", null);
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
	this.map.registerZoomSlider(this);
	this.map.registerInitListener(this, "init", null);
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

MapZoomSlider.prototype.mouseDown = function(event)
{
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
	var pos = event.clientX;
	var t = this.capKnobPosition(this.startOffsetLeft + (pos - this.startPos)) / this.sliderEffectiveWidth;
	this.map.changeScaleNormalized(t, false);
	EventAttacher.detachOnWindowEvent("mousemove", this, "mouseMove");
	EventAttacher.detachOnWindowEvent("mouseup", this, "mouseUp");
}

MapZoomSlider.prototype.zoomChanged = function()
{
	this.setKnobNormalizedPosition(this.map.getScaleNormalized());
}

/////////////////////////////////////////////////////////
// points of interest
/////////////////////////////////////////////////////////

function PointOfInterest(x, y, name, text)
{
	this.x = x;
	this.y = y;
	this.name = name;
	this.text = text;
}