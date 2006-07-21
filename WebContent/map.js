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
		buttonBackId,
		buttonForwardId,
		buttonZoomPlusId,
		buttonZoomMinusId,
		buttonPanId,
		buttonZoomId,
		buttonSizeSmallId,
		sizeSmallX,
		sizeSmallY,
		buttonSizeMediumId,
		sizeMediumX,
		sizeMediumY,
		buttonSizeBigId,
		sizeBigX,
		sizeBigY
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
		
		// buttons
		if (buttonBackId)
			new MapButtonGoBack(buttonBackId, map);

		if (buttonForwardId)
			new MapButtonGoForward(buttonForwardId, map);

		if (buttonZoomPlusId)
			new MapButtonZoomPlus(buttonZoomPlusId, map);

		if (buttonZoomMinusId)
			new MapButtonZoomMinus(buttonZoomMinusId, map);
			
		if (buttonPanId && buttonZoomId)
			new MapZoomAndPanButtons(buttonPanId, buttonZoomId, map);

		if (buttonSizeSmallId && sizeSmallX && sizeSmallY && buttonSizeMediumId && sizeMediumX && sizeMediumY && buttonSizeBigId && sizeBigX && sizeBigY)
			new MapZoomSize(buttonSizeSmallId, sizeSmallX, sizeSmallY, buttonSizeMediumId, sizeMediumX, sizeMediumY, buttonSizeBigId, sizeBigX, sizeBigY, map);

		// call init after page loads
		EventAttacher.attach(window, "load", map, "init");
	
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
	this.scale_max = 10000;
	this.scale_factor_plus = 2.0;
	this.scale_factor_minus = 0.666666;
	
	// zooming and panning
	this.nav_mouse_mode = MapsGlobal.MAP_TOOL_ZOOM;
	this.map_mouse_mode = MapsGlobal.MAP_TOOL_PAN;
	this.selector_border_width = 1;
	this.selector_color = "Gray"; //"#0066CC";
	this.selector_opacity = 30;
	this.map_selector = null;
	this.nav_selector = null;
	
	// scale indicator & selector
	this.scale_bar_indicator = null;
	this.scale_text_indicator = null;
	this.scale_sel_width = 200;
	this.scale_sel_btn_width = 4;
	this.scale_sel = null;
	this.scale_sel_offset_left = null;
	this.scale_sel_offset_top = null;
	
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
	this.has_hidden_state_fields = false;
	this.formName = null;
	this.field_name_x1 = null;
	this.field_name_y1 = null;
	this.field_name_x2 = null;
	this.field_name_y2 = null;
	this.field_x1 = null;
	this.field_y1 = null;
	this.field_x2 = null;
	this.field_y2 = null;

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
// scale indicator and selector
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

	if (!this.scale_sel) return;

	var meters = this.fromPxToReal(100);
	var digits = Math.floor(Math.log(meters) / Math.LN10);
	var closest_power = Math.pow(10, digits);
	meters = Math.floor(meters / closest_power) * closest_power;
	var pixels = this.fromRealToPx(meters);
	
	scale_bar_indicator.style.width = Math.round(pixels) + "px";
	scale_text_indicator.innerHTML = this.formatMetersForDisplay(meters);

	var scale_fraction =
		(this.scale - this.scale_min) /
		(this.scale_max - this.scale_min);
	
	var scale_left = Math.round(
		this.scale_sel_width -
		this.scale_fraction * (this.scale_sel_width - this.scale_sel_btn_width) -
		this.scale_sel_btn_width);
	
	if (IE)
		scale_sel.firstChild.style.left = scale_left + "px";
	else
		scale_sel.childNodes[1].style.left = scale_left + "px";
	
}

Map.prototype.scaleIndicatorPrecomputePos = function()
{
	this.scale_bar_indicator = document.getElementById("map-scale-bar");
	this.scale_text_indicator = document.getElementById("map-scale-text");
}

Map.prototype.scaleIndicatorInit = function()
{
	this.scaleIndicatorPrecomputePos();
}

/////////////////////////////////////////////////////////
// map zoom/pan
/////////////////////////////////////////////////////////

Map.prototype.mapStartDrag = function(event)
{

	// set onMouseMove handler
	EventAttacher.attach(this.map_frame, "mousemove", this, "mapMouseMove");
	//if (IE) map_frame.style.cursor = "url(ruka-dole.cur)";
	
	// init position
	this.dragging_start_x = event.clientX - this.vport_offset_left + ElementUtils.getScrollLeft(this.map_frame);
	this.dragging_start_y = event.clientY - this.vport_offset_top + ElementUtils.getScrollTop(this.map_frame);
	
}

Map.prototype.mapStopDrag = function(event)
{

	// cancel onMouseMove handler
	EventAttacher.detach(this.map_frame, "mousemove", this, "mapMouseMove");
	//if (IE) this.map_frame.style.cursor = "url(ruka.cur)";

	// new position
	var x = event.clientX - this.vport_offset_left + ElementUtils.getScrollLeft(this.map_frame);
	var y = event.clientY - this.vport_offset_top + ElementUtils.getScrollTop(this.map_frame);
	
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
				this.fromVportToRealY(y));
			
			break;
			
		case MapsGlobal.MAP_TOOL_PAN:
		
			break;
			
	}
	
}

Map.prototype.mapMouseMove = function(event)
{

	// new position
	var x = event.clientX - this.vport_offset_left + ElementUtils.getScrollLeft(this.map_frame);
	var y = event.clientY - this.vport_offset_top + ElementUtils.getScrollTop(this.map_frame);
	
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
		this.changeScale((-event.wheelDelta/120) * this.scale * this.scale_factor_plus);
	}
	else
	{
		this.changeScale(event.wheelDelta/120 * this.scale * this.scale_factor_minus);
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

Map.prototype.zoomPlus = function()
{
	this.changeScale(this.scale * this.scale_factor_plus);
}

Map.prototype.zoomMinus = function()
{
	this.changeScale(this.scale * this.scale_factor_minus);
}

Map.prototype.changeScale = function(new_scale)
{

	// has it changed?
	new_scale = this.roundAndCapScale(new_scale);
	if (new_scale == this.scale) return;

	// get old center
	var cx = this.getCenterX();
	var cy = this.getCenterY();
	
	// center
	this.setScaleAndCenterMapTo(new_scale, cx, cy, true);

}

Map.prototype.zoomMapTo = function(x1, y1, x2, y2, border)
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
	this.setScaleAndCenterMapTo(new_scale, cx, cy, true);
	
}

Map.prototype.setScaleAndCenterMapTo = function(new_scale, x, y, save_state)
{

	// scale has changed
	new_scale = this.roundAndCapScale(new_scale);
	if (this.scale != new_scale)
	{

		// set
		this.scale = new_scale;
		
		// updates
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

	// remeber state, default = true
	if (save_state == null || save_state)
		this.zoomSaveState();
	
	// draw all tiles
	this.positionTiles(true);
	
}


/////////////////////////////////////////////////////////
// drawing map and route
/////////////////////////////////////////////////////////

Map.prototype.saveState = function()
{
	if (this.has_hidden_state_fields)
	{
		this.field_x1.value = this.getMapX1();
		this.field_y1.value = this.getMapY1();
		this.field_x2.value = this.getMapX2();
		this.field_y2.value = this.getMapY2();
	}
}


Map.prototype.updateTile = function(update_img, map_tile, x, y, col, row)
{
	if (x != null) map_tile.img.style.left = x + "px";
	if (y != null) map_tile.img.style.top = y + "px";
	if (update_img)
	{
		map_tile.img.src = this.map_blank_img;
		map_tile.url = this.createTileUrl(col, row);
		map_tile.valid = false;
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

Map.prototype.postponedRefresh = function()
{
	Timer.cancelCall(this.postponed_refresh_id);
	this.postponed_refresh_id = Timer.delayedCall(this, "refreshInvalidatedTiles", 10);		
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
				
	this.postponedRefresh();

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

}

Map.prototype.zoomRestoreCurrent = function()
{
	var state = this.zoom_history[this.zoom_history_pos];
	this.setScaleAndCenterMapTo(state.scale, state.cx, state.cy, false);
	this.zoomHistoryFireEvents();
}

Map.prototype.zoomHistoryFireEvents = function()
{
	if (this.go_back_button) this.go_back_button.historyChanged();
	if (this.go_forward_button) this.go_forward_button.historyChanged();
}

Map.prototype.zoomGoBack = function()
{
	if (!this.zoomCanGoBack()) return;
	this.zoom_history_pos--;
	this.zoomRestoreCurrent();
}

Map.prototype.zoomGoForward = function()
{
	if (!this.zoomCanGoForward()) return;
	this.zoom_history_pos++;
	this.zoomRestoreCurrent();
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
// scale selector
/////////////////////////////////////////////////////////

Map.prototype.scaleSelClick = function(event)
{

	// position
	var x = EventUtils.getRelativePosX(event) - scale_sel_btn_width/2;
	
	// change scale
	this.changeScale( (scale_sel_width-x)/scale_sel_width * (scale_max - scale_min) + scale_min );

}

Map.prototype.scaleSelInit = function()
{
	this.scale_sel = document.getElementById("map-scale-sel");
	if (!this.scale_sel) return;
	
	this.scale_sel.style.width = (scale_sel_width) + "px";
	this.scale_sel.style.cursor = "pointer";
	EventAttacher.attach(this.scale_sel, "click", this, "scaleSelClick");
	
	if (IE)
		this.scale_sel.firstChild.style.width = (scale_sel_btn_width) + "px";
	else
		this.scale_sel.childNodes[1].style.width = (scale_sel_btn_width) + "px";

	this.scale_sel_offset_left = ElementUtils.getOffsetLeft(this.scale_sel);
	this.scale_sel_offset_top = ElementUtils.getOffsetTop(this.scale_sel);
}

/////////////////////////////////////////////////////////
// tools
/////////////////////////////////////////////////////////

Map.prototype.setMouseModeToPan = function()
{
	this.map_mouse_mode = MapsGlobal.MAP_TOOL_PAN;
}

Map.prototype.setMouseModeToZoom = function()
{
	this.map_mouse_mode = MapsGlobal.MAP_TOOL_ZOOM;
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
	this.scaleIndicatorPrecomputePos();
	
	this.setScaleAndCenterMapTo(this.scale, cx, cy, false);

}

Map.prototype.mapWindowResize = function()
{	
	this.changeSizeByWindow();
	this.updateControlsLayout();
	this.positionTiles(true);
	this.scaleIndicatorPrecomputePos();
}

Map.prototype.initSizeByHTML = function()
{
	this.width = this.map_control.offsetWidth;
	this.height = this.map_control.offsetHeight;
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
		for (var i=0; i<this.tiles_map.length; i++)
			for (var j=0; j<this.tiles_map[i].length; j++)
				this.map_frame.removeChild(this.tiles_map[i][j].img);

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

	this.map_frame = document.getElementById(this.map_frame_id);
	this.map_frame.style.backgroundColor = "White";
	this.map_frame.style.position = "absolute";
	this.map_frame.style.overflow = "hidden";
	if (IE)
	{
		//this.map_frame.style.cursor = "url(ruka.cur)";
		this.map_frame.unselectable = "on";
	}
	if (GK)
	{
		this.map_frame.style.MozUserFocus = "none";
		this.map_frame.style.MozUserSelect = "none";
	}

	EventAttacher.attach(this.map_frame, "mousedown", this, "mapStartDrag");
	EventAttacher.attach(this.map_frame, "mouseup", this, "mapStopDrag");
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
		EventAttacher.attach(window, "resize", this, "mapWindowResize");
	
}

Map.prototype.hiddenFieldsInit = function()
{

	var form = document.forms[this.form_name];
	if (!form)
	{
		this.has_hidden_state_fields = false;
		return;
	}

	this.field_x1 = form.elements[this.field_name_x1];
	this.field_y1 = form.elements[this.field_name_y1];
	this.field_x2 = form.elements[this.field_name_x2];
	this.field_y2 = form.elements[this.field_name_y2];
	
	this.has_hidden_state_fields = this.field_x1 && this.field_y1 && this.field_x2 && this.field_y2;

}

Map.prototype.restoreState = function()
{
	if (this.has_hidden_state_fields)
	{
		var x1 = parseFloat(this.field_x1.value);
		var y1 = parseFloat(this.field_y1.value);
		var x2 = parseFloat(this.field_x2.value);
		var y2 = parseFloat(this.field_y2.value);
		this.zoomMapTo(x1, y1, x2, y2);
	}
	else
	{
		this.zoomMapTo(-180, -90, 180, 90);
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
	
	// scale indicator - down left	
	this.scaleIndicatorInit();

	// scale selector - top right
	this.scaleSelInit();

	// pan tools
	this.panToolInit();

	// show something
	this.restoreState();
	
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
	EventAttacher.attach(window, "load", this, "init");
}

MapButtonZoomPlus.prototype.init = function()
{
	this.element = document.getElementById(this.elementId);
	EventAttacher.attach(this.element, "click", this, "click");
}

MapButtonZoomPlus.prototype.click = function(event)
{
	this.map.zoomPlus();
}


/////////////////////////////////////////////////////////
// zoom - button
/////////////////////////////////////////////////////////

function MapButtonZoomMinus(elementId, map)
{
	this.element = null;
	this.map = map;
	this.elementId = elementId;
	EventAttacher.attach(window, "load", this, "init");
}

MapButtonZoomMinus.prototype.init = function()
{
	this.element = document.getElementById(this.elementId);
	EventAttacher.attach(this.element, "click", this, "click");
}

MapButtonZoomMinus.prototype.click = function(event)
{
	this.map.zoomMinus();
}

/////////////////////////////////////////////////////////
// back button
/////////////////////////////////////////////////////////

function MapButtonGoBack(elementId, map)
{
	this.element = null;
	this.map = map;
	this.elementId = elementId;
	this.map.registerGoBackButton(this);
	EventAttacher.attach(window, "load", this, "init");
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
	element.className = this.map.zoomCanGoBack ? "map-icon-back" : "map-icon-back-off";
}

/////////////////////////////////////////////////////////
// forward button
/////////////////////////////////////////////////////////

function MapButtonGoForward(elementId, map)
{
	this.element = null;
	this.map = map;
	this.elementId = elementId;
	this.map.registerGoForwardButton(this);
	EventAttacher.attach(window, "load", this, "init");
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
	this.element.className = this.map.zoomCanGoForward ? "map-icon-forward" : "map-icon-forward-off";
}


/////////////////////////////////////////////////////////
// zoom and pan buttons
/////////////////////////////////////////////////////////

function MapZoomAndPanButtons(panElementId, zoomElementId, map)
{
	this.zoomElement = null;
	this.panElement = null;
	this.panElementId = panElementId;
	this.zoomElementId = zoomElementId;
	this.map = map;
	EventAttacher.attach(window, "load", this, "init");
}

MapZoomAndPanButtons.prototype.init = function()
{
	this.zoomElement = document.getElementById(this.zoomElementId);
	this.panElement = document.getElementById(this.panElementId);
	EventAttacher.attach(this.zoomElement, "click", this, "clickZoom");
	EventAttacher.attach(this.panElement, "click", this, "clickPan");
}

MapZoomAndPanButtons.prototype.clickZoom = function(event)
{
	this.map.setMouseModeToZoom();
	this.zoomElement.className = "map-icon-zoom";
	this.panElement.className = "map-icon-pan-off";
}

MapZoomAndPanButtons.prototype.clickPan = function(event)
{
	this.map.setMouseModeToPan();
	this.zoomElement.className = "map-icon-zoom-off";
	this.panElement.className = "map-icon-pan";
}


/////////////////////////////////////////////////////////
// size buttons
/////////////////////////////////////////////////////////

function MapZoomSize(smallElementId, sizeSmallX, sizeSmallY, mediumElementId, sizeMediumX, sizeMediumY, bigElementId, sizeBigX, sizeBigY, map)
{
	this.smallSizeX = sizeSmallX;
	this.smallSizeY = sizeSmallY;
	this.mediumSizeX = sizeMediumX;
	this.mediumSizeY = sizeMediumY;
	this.bigSizeX = sizeBigX;
	this.bigSizeY = sizeBigY;
	this.smallElement = null;
	this.mediumElement = null;
	this.bigElement = null;
	this.smallElementId = smallElementId;
	this.mediumElementId = mediumElementId;
	this.bigElementId = bigElementId;
	this.map = map;
	EventAttacher.attach(window, "load", this, "init");
}

MapZoomSize.prototype.init = function()
{
	this.smallElement = document.getElementById(this.smallElementId);
	this.mediumElement = document.getElementById(this.mediumElementId);
	this.bigElement = document.getElementById(this.bigElementId);
	EventAttacher.attach(this.smallElement, "click", this, "clickSmall");
	EventAttacher.attach(this.mediumElement, "click", this, "clickMedium");
	EventAttacher.attach(this.bigElement, "click", this, "clickBig");
}

MapZoomSize.prototype.clickSmall = function(event)
{
	this.map.setSize(this.smallSizeX, this.smallSizeY);
	this.smallElement.className = "map-icon-size-small";
	this.mediumElement.className = "map-icon-size-medium-off";
	this.bigElement.className = "map-icon-size-big-off";
}

MapZoomSize.prototype.clickMedium = function(event)
{
	this.map.setSize(this.mediumSizeX, this.mediumSizeY);
	this.smallElement.className = "map-icon-size-small-off";
	this.mediumElement.className = "map-icon-size-medium";
	this.bigElement.className = "map-icon-size-big-off";
}

MapZoomSize.prototype.clickBig = function(event)
{
	this.map.setSize(this.bigSizeX, this.bigSizeY);
	this.smallElement.className = "map-icon-size-small-off";
	this.mediumElement.className = "map-icon-size-medium-off";
	this.bigElement.className = "map-icon-size-big";
}