// container
var map_container;
var map_control;
var map_control_top_tools;
var map_control_bottom_tools;
var map_control_left_tools;
var map_control_right_tools;
var map_container_offset_left;
var map_container_offset_top;
var min_map_control_width = 600;
var min_map_control_height = 400;

// references to HTML elements
var map_frame = null;
var tiles_map = null;
var map_blank_img = "blank.png";

// viewport config
var visible_rows = 5;
var visible_cols = 5;
var vport_offset_left;
var vport_offset_top;
var vport_width;
var vport_height;
var tile_width = 300;
var tile_height = 300;
var revert_axis_x = false;
var revert_axis_y = false;

// zoom history
var zoom_history = new Array();
var zoom_history_pos = -1;
var zoom_history_max = 100;

// for dragging
var dragging_start_x;
var dragging_start_y;

// current position
var first_tile_col;
var first_tile_row;
var first_tile_vx;
var first_tile_vy;
var scale;
var scale_min = 0.01;
var scale_max = 10000;
var scale_factor_plus = 2.0;
var scale_factor_minus = 0.666666;

// zooming and panning
var ZOOM = 1;
var PAN = 2;
var nav_mouse_mode = ZOOM;
var map_mouse_mode = PAN;
var selector_border_width = 1;
var selector_color = "Gray"; //"#0066CC";
var selector_opacity = 30;
var map_selector = null;
var nav_selector = null;

// scale indicator & selector
var scale_bar_indicator;
var scale_text_indicator;
var scale_sel_width = 200;
var scale_sel_btn_width = 4;
var scale_sel;
var scale_sel_offset_left;
var scale_sel_offset_top;

// pan tools
var tool_pan_up;
var tool_pan_down;
var tool_pan_left;
var tool_pan_right;
var pan_tool_delta = 20;
var pan_tool_delay = 50;
var pan_tool_timout_id;
var pan_tool_dx;
var pan_tool_dy;

// server
var map_tiles_server = "servlet/MapFeederServlet";

function PointGPS(x, y, len, time, speed, place)
{
	this.x = x;
	this.y = y;
	this.len = len;
	this.time = time;
	this.speed = speed;
	this.place = place;
}

function ZoomState(scale, cx, cy)
{
	this.cx = cx;
	this.cy = cy;
	this.scale = scale;
}

/////////////////////////////////////////////////////////
// mozilla specific functions
/////////////////////////////////////////////////////////

function find_child_element_by_id(parent, tag_name, id)
{
	var coll = parent.getElementsByTagName(tag_name);
	var coll_length = coll.length;
	for (var i=0; i<coll_length; i++)
		if (coll[i].id == id)
			return coll[i];
	return null;
}

/////////////////////////////////////////////////////////
// generic functions
/////////////////////////////////////////////////////////

function false_fnc()
{
	return false;
}

/////////////////////////////////////////////////////////
// generic geometric functions
/////////////////////////////////////////////////////////

function compute_length(x, y)
{
	return Math.sqrt(compute_length_2(x, y));
}

function compute_length_2(x, y)
{
	return x*x + y*y;
}

function compute_distance_point_line(px, py, x1, y1, x2, y2)
{
	return Math.abs((x2-x1)*(y1-py) - (x1-px)*(y2-y1)) / compute_length(x2-x1, y2-y1);
}

function compute_angle(x1, y1, x2, y2, x3, y3)
{
	var v1x = x1-x2;
	var v1y = y1-y2;
	var v2x = x3-x2;
	var v2y = y3-y2;
	return Math.acos ((v1x*v2x + v1y*v2y) / (compute_length(v1x, v1y) * compute_length(v2x, v2y)));
}


/////////////////////////////////////////////////////////
// generic functions for coordinates etc.
/////////////////////////////////////////////////////////

function get_vport_ratio()
{
	return get_vport_width() / get_vport_height();
}

function get_vport_width()
{
	return vport_width;
}

function get_vport_height()
{
	return vport_height;
}

function from_vport_to_real_x(x)
{
	return first_tile_col * get_tile_real_width() + from_px_to_real(x-first_tile_vx);
}

function from_vport_to_real_y(y)
{
	return (first_tile_row+1) * get_tile_real_height() - from_px_to_real(y-first_tile_vy);
}

function from_real_to_vport_x(x)
{
	return from_real_to_px(x - first_tile_col * get_tile_real_width()) + first_tile_vx;
}

function from_real_to_vport_y(y)
{
	return from_real_to_px((first_tile_row+1) * get_tile_real_height() - y) + first_tile_vy;
}

function get_map_x1()
{
	return from_vport_to_real_x(0);
}

function get_map_y2()
{
	return from_vport_to_real_y(0);
}

function get_map_x2()
{
	return from_vport_to_real_x(get_vport_width());
}

function get_map_y1()
{
	return from_vport_to_real_y(get_vport_height());
}

function get_center_y()
{
	return from_vport_to_real_y(0.5 * get_vport_height());
}

function get_center_x()
{
	return from_vport_to_real_x(0.5 * get_vport_width());
}

function get_tile_real_width()
{
	return from_px_to_real(tile_width);
}

function get_tile_real_height()
{
	return from_px_to_real(tile_height);
}

function from_px_to_real(v)
{
	return v / scale;
}

function from_real_to_px(v)
{
	return v * scale;
}

function revert_axis_direction_x(x)
{
	return revert_axis_x ? route_x1 + (route_x2 - x) : x;
}

function revert_axis_direction_y(y)
{
	return revert_axis_y ? route_y1 + (route_y2 - y) : y;
}

/////////////////////////////////////////////////////////
// generic function for drawing the selector
/////////////////////////////////////////////////////////

function draw_selector(sel, x1, y1, x2, y2)
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
	x2 -= 3*selector_border_width;
	y2 -= 3*selector_border_width;
	
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

function format_meters_for_display(meters)
{
	if (meters >= 1000)
		return (meters/1000) + "&nbsp;km";
	else
		return (meters) + "&nbsp;m";
}

function update_scale_indicator()
{

	if (!scale_sel) return;

	var meters = from_px_to_real(100);
	var digits = Math.floor(Math.log(meters) / Math.LN10);
	var closest_power = Math.pow(10, digits);
	meters = Math.floor(meters / closest_power) * closest_power;
	var pixels = from_real_to_px(meters);
	
	scale_bar_indicator.style.width = Math.round(pixels) + "px";
	scale_text_indicator.innerHTML = format_meters_for_display(meters);

	var scale_fraction = (scale - scale_min)/(scale_max - scale_min);
	
	if (IE)
		scale_sel.firstChild.style.left = Math.round(scale_sel_width - scale_fraction * (scale_sel_width - scale_sel_btn_width) - scale_sel_btn_width) + "px";
	else
		scale_sel.childNodes[1].style.left = Math.round(scale_sel_width - scale_fraction * (scale_sel_width - scale_sel_btn_width) - scale_sel_btn_width) + "px";
	
}

function scale_indicator_precompute_pos()
{
	scale_bar_indicator = document.getElementById("map-scale-bar");
	scale_text_indicator = document.getElementById("map-scale-text");
}

function scale_indicator_init()
{
	scale_indicator_precompute_pos();
}

/////////////////////////////////////////////////////////
// map zoom/pan
/////////////////////////////////////////////////////////

function map_start_drag(event)
{
	event = get_event(event);

	// set onMouseMove handler
	map_frame.onmousemove = map_mouse_move;
	//if (IE) map_frame.style.cursor = "url(ruka-dole.cur)";
	
	// init position
	dragging_start_x = event.clientX - vport_offset_left + get_element_scroll_left(map_frame);
	dragging_start_y = event.clientY - vport_offset_top + get_element_scroll_top(map_frame);
	
}

function map_stop_drag(event)
{
	event = get_event(event);

	// cancel onMouseMove handler
	map_frame.onmousemove = null;
	//if (IE) map_frame.style.cursor = "url(ruka.cur)";

	// new position
	var x = event.clientX - vport_offset_left + get_element_scroll_left(map_frame);
	var y = event.clientY - vport_offset_top + get_element_scroll_top(map_frame);
	
	switch (map_mouse_mode)
	{
		case ZOOM:
		
			// hide selector
			map_hide_selector();
			
			// has the mouse moved?
			if (dragging_start_x == x && dragging_start_y == y)
				return;
			
			// and zoom
			zoom_map_to(
				from_vport_to_real_x(dragging_start_x),
				from_vport_to_real_y(dragging_start_y),
				from_vport_to_real_x(x),
				from_vport_to_real_y(y));
			
			break;
			
		case PAN:
		
			break;
			
	}
	
}

function map_mouse_move(event)
{
	event = get_event(event);
	
	// new position
	var x = event.clientX - vport_offset_left + get_element_scroll_left(map_frame);
	var y = event.clientY - vport_offset_top + get_element_scroll_top(map_frame);
	
	switch (map_mouse_mode)
	{
		case PAN:
		
			// position change
			var dx = x - dragging_start_x;
			var dy = y - dragging_start_y;

			// remember for the next turn
			dragging_start_x = x;
			dragging_start_y = y;
			
			// change pos
			first_tile_vx += dx;
			first_tile_vy += dy;

			// adjust tile if needed
			adjust_tiles_after_pan();
			
			break;
			
		case ZOOM:

			// draw selector
			map_draw_selector(
				dragging_start_x, dragging_start_y,
				x, y);

			break;
	
	}
	
}

function map_mouse_wheel(event)
{
	event = get_event(event);
	
	// change the zoom
	if (event.wheelDelta < 0)
	{
		change_scale((-event.wheelDelta/120) * scale * scale_factor_plus);
	}
	else
	{
		change_scale(event.wheelDelta/120 * scale * scale_factor_minus);
	}
	
	// we don't want to scroll the page
	event.cancelBubble = true;
	return false;
	
}

function map_draw_selector(x1, y1, x2, y2)
{
	draw_selector(
		map_selector,
		x1 + map_frame.scrollLeft,
		y1 + map_frame.scrollTop,
		x2 + map_frame.scrollLeft,
		y2 + map_frame.scrollTop);
}

function map_hide_selector()
{
	map_selector.style.display = "none";
}


/////////////////////////////////////////////////////////
// map size
/////////////////////////////////////////////////////////


function zoom_plus()
{
	change_scale(scale * scale_factor_plus);
}

function zoom_minus()
{
	change_scale(scale * scale_factor_minus);
}

function change_scale(new_scale)
{

	// has it changed?
	if (new_scale == scale) return;

	// get old center
	var cx = get_center_x();
	var cy = get_center_y();
	
	// center
	set_scale_and_center_map_to(new_scale, cx, cy, true);

}

function zoom_map_to(x1, y1, x2, y2, border)
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
	var vport_width = get_vport_width() - 2*border;
	var vport_height = get_vport_height() - 2*border;
	if (vport_width < 100) vport_width = 100;
	if (vport_height < 100) vport_height = 100;

	// adjust to the ratio
	var vport_ratio = vport_width/vport_height;
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
	var new_scale = vport_width / width;

	// center it
	set_scale_and_center_map_to(new_scale, cx, cy, true);
	
}

function set_scale_and_center_map_to(new_scale, x, y, save_state)
{

	// scale has changed
	if (scale != new_scale)
	{

		// set and check
		scale = new_scale;
		if (scale < scale_min) scale = scale_min;
		if (scale_max < scale) scale = scale_max;
		
		// updates
		update_scale_indicator();

	}
	
	// middle tile
	var col = Math.floor(x/get_tile_real_width());
	var row = Math.floor(y/get_tile_real_height());
	
	// position of the middle tile in vport
	var vx = Math.round(get_vport_width()/2 - from_real_to_px(x - col * get_tile_real_width()));
	var vy = Math.round(get_vport_height()/2 - from_real_to_px((row+1) * get_tile_real_height() - y));
	
	// compute the position of the top left tile
	while (!(-tile_width < vx && vx <= 0))
	{
		col --;
		vx -= tile_width;
	}
	while (!(-tile_height < vy && vy <= 0))
	{
		row ++;
		vy -= tile_height;
	}

	// set it
	first_tile_col = col;
	first_tile_row = row;
	first_tile_vx = vx;
	first_tile_vy = vy;

	// remeber state, default = true
	if (save_state == null || save_state)
		zoom_save_state();
	
	// draw all tiles
	position_tiles(true);
	
}


/////////////////////////////////////////////////////////
// drawing map and route
/////////////////////////////////////////////////////////

function update_tile(update_img, map_tile, x, y, col, row)
{
	if (x != null) map_tile.style.left = x + "px";
	if (y != null) map_tile.style.top = y + "px";
	if (update_img)
	{
		map_tile.src = map_blank_img;
		map_tile.src = create_tile_map_url(col, row);
	}
}

function position_tiles(update_img, row_from, row_to, col_from, col_to)
{

	if (row_from == null) row_from = 0;
	if (row_to == null) row_to = visible_rows;
	if (col_from == null) col_from = 0;
	if (col_to == null) col_to = visible_cols;
	
	for (var i=row_from; i<=row_to; i++)
		for (var j=col_from; j<=col_to; j++)
			update_tile(update_img, tiles_map[i][j], 
				(first_tile_vx + (j * tile_width)), (first_tile_vy + (i * tile_height)),
				first_tile_col + j, first_tile_row - i);
				
}

function adjust_tiles_after_pan()
{

	// unchanged rectangle of tiles
	var update_row_from = 0;
	var update_row_to = visible_rows;
	var update_col_from = 0;
	var update_col_to = visible_cols;
	
	// move columns from right to left
	while (0 < first_tile_vx)
	{
	
		update_col_from ++;
	
		first_tile_vx -= tile_width;
		first_tile_col --;
		
		for (var i=0; i<visible_rows+1; i++)
		{

			var map_tile = tiles_map[i].pop();
			tiles_map[i].unshift(map_tile);
			
			update_tile(true, map_tile, 
				first_tile_vx, null,
				first_tile_col, first_tile_row - i);
				
		}
		
	}

	// move columns from left to right
	while (first_tile_vx < -tile_width)
	{

		update_col_to --;

		first_tile_vx += tile_width;
		first_tile_col ++;
		
		for (var i=0; i<visible_rows+1; i++)
		{
		
			var map_tile = tiles_map[i].shift();
			tiles_map[i].push(map_tile);
			
			update_tile(true, map_tile, 
				(first_tile_vx + (visible_cols+1)*tile_width), null,
				first_tile_col + visible_cols, first_tile_row - i);
			
		}
		
	}

	// move rows from bottom to top
	while (0 < first_tile_vy)
	{
	
		update_row_from ++;
	
		first_tile_vy -= tile_height;
		first_tile_row ++;
		
		var first_map_row = tiles_map.pop();
		tiles_map.unshift(first_map_row);
		
		for (var j=0; j<visible_cols+1; j++)
		{

			update_tile(true, first_map_row[j], 
				null, first_tile_vy,
				first_tile_col + j, first_tile_row);
			
		}
		
	}
	
	// move rows from top to bottom
	while (first_tile_vy < -tile_height)
	{
	
		update_row_to --;

		first_tile_vy += tile_height;
		first_tile_row --;
		
		var last_map_row = tiles_map.shift();
		tiles_map.push(last_map_row);
		
		for (var j=0; j<visible_cols+1; j++)
		{
		
			update_tile(true, last_map_row[j], 
				null,(first_tile_vy + (visible_rows+1)*tile_height),
				first_tile_col + j, first_tile_row - visible_rows);
		
		}
		
	}
	
	// position last unchanged rectangle of tiles
	// false means = do not change URLs of them
	position_tiles(false,
		update_row_from, update_row_to,
		update_col_from, update_col_to);
	
	
}

function create_tile_map_url(col, row)
{
	return create_map_url(
		revert_axis_direction_x((col + 0.5) * get_tile_real_width()),
		revert_axis_direction_y((row + 0.5) * get_tile_real_height()),
		scale, 
		tile_width,
		tile_height);
}

function create_map_url(x, y, s, w, h)
{
	return map_tiles_server + "?" +
		"x=" + x + "&" +
		"y=" + y + "&" +
		"s=" + s + "&" +
		"w=" + w + "&" +
		"h=" + h + "&" +
		"path=__map__file";
}


/////////////////////////////////////////////////////////
// zooming history
/////////////////////////////////////////////////////////

function zoom_save_state()
{
	
	// current state
	var state = new ZoomState(scale, get_center_x(), get_center_y());

	// delete elements after current element
	while (zoom_history_pos+1 < zoom_history.length)
		zoom_history.pop();
	
	// save the state
	zoom_history_pos++;
	zoom_history.push(state);
	
	// too many?
	while (zoom_history_max < zoom_history.length)
	{
		zoom_history.shift();
		zoom_history_pos--;
	}

}

function zoom_restore_current()
{
	var state = zoom_history[zoom_history_pos];
	set_scale_and_center_map_to(state.scale, state.cx, state.cy, false);
}

function zoom_go_back()
{
	if (!zoom_can_go_back()) return;
	zoom_history_pos--;
	zoom_restore_current();
}

function zoom_go_forward()
{
	if (!zoom_can_go_forward()) return;
	zoom_history_pos++;
	zoom_restore_current();
}

function zoom_can_go_back()
{
	return zoom_history_pos > 0;
}

function zoom_can_go_forward()
{
	return zoom_history_pos+1 < zoom_history.length;
}


/////////////////////////////////////////////////////////
// scale selector
/////////////////////////////////////////////////////////

function scale_sel_click(event)
{
	event = get_event(event);

	// position
	var x = get_event_relative_pos_x(event) - scale_sel_btn_width/2;
	
	// change scale
	change_scale( (scale_sel_width-x)/scale_sel_width * (scale_max - scale_min) + scale_min );

}

function scale_sel_init()
{
	scale_sel = document.getElementById("map-scale-sel");
	if (!scale_sel) return;
	scale_sel.style.width = (scale_sel_width) + "px";
	scale_sel.style.cursor = "pointer";
	scale_sel.onclick = scale_sel_click;
	
	if (IE)
		scale_sel.firstChild.style.width = (scale_sel_btn_width) + "px";
	else
		scale_sel.childNodes[1].style.width = (scale_sel_btn_width) + "px";

	scale_sel_offset_left = get_element_offset_left(scale_sel);
	scale_sel_offset_top = get_element_offset_top(scale_sel);
}


/////////////////////////////////////////////////////////
// pan tool
/////////////////////////////////////////////////////////

function panning_next_move()
{

	first_tile_vx += pan_tool_dx;
	first_tile_vy += pan_tool_dy;
	adjust_tiles_after_pan();
	
	pan_tool_timout_id = setTimeout("panning_next_move()", pan_tool_delay);

}

function pan_up_mouse_down()
{
	pan_tool_dx = 0;
	pan_tool_dy = pan_tool_delta;
	panning_next_move();
}

function pan_down_mouse_down()
{
	pan_tool_dx = 0;
	pan_tool_dy = -pan_tool_delta;
	panning_next_move();
}

function pan_left_mouse_down()
{
	pan_tool_dx = pan_tool_delta;
	pan_tool_dy = 0;
	panning_next_move();
}

function pan_right_mouse_down()
{
	pan_tool_dx = -pan_tool_delta;
	pan_tool_dy = 0;
	panning_next_move();
}

function pan_mouse_up()
{
	clearTimeout(pan_tool_timout_id);
}

function pan_tool_init()
{

	tool_pan_up = document.getElementById("pan-up");
	tool_pan_down = document.getElementById("pan-down");
	tool_pan_left = document.getElementById("pan-left");
	tool_pan_right = document.getElementById("pan-right");
	
	if (tool_pan_up)
	{
		tool_pan_up.unselectable = "on";
		tool_pan_up.onmousedown = pan_up_mouse_down;
		tool_pan_up.onmouseup = pan_mouse_up;
	}
	
	if (tool_pan_down)
	{
		tool_pan_down.unselectable = "on";
		tool_pan_down.onmousedown = pan_down_mouse_down;
		tool_pan_down.onmouseup = pan_mouse_up;
	}
	
	if (tool_pan_left)
	{
		tool_pan_left.unselectable = "on";
		tool_pan_left.onmousedown = pan_left_mouse_down;
		tool_pan_left.onmouseup = pan_mouse_up;
	}
	
	if (tool_pan_right)
	{
		tool_pan_right.unselectable = "on";
		tool_pan_right.onmousedown = pan_right_mouse_down;
		tool_pan_right.onmouseup = pan_mouse_up;
	}
	
}


/////////////////////////////////////////////////////////
// map control: resizing and init
/////////////////////////////////////////////////////////

function map_window_resize()
{	
	change_map_size();
	position_tiles(true);
	scale_indicator_precompute_pos();
}
	
function change_map_size()
{
	
	// total width and height
	var width = Math.max((get_page_width() - map_control.offsetLeft), min_map_control_width);
	var height = Math.max((get_page_height() - map_control.offsetTop), min_map_control_height);
	
	// offsets
	var top = map_control_top_tools ? map_control_top_tools.offsetHeight : 0;
	var bottom = map_control_bottom_tools ? map_control_bottom_tools.offsetHeight : 0;
	var left = map_control_left_tools ? map_control_left_tools.offsetWidth : 0;
	var right = map_control_right_tools ? map_control_right_tools.offsetWidth : 0;
	
	// set vport size
	vport_width = width - left - right;
	vport_height = height - top - bottom;
	
	// control
	map_control.style.left = map_control.offsetLeft + "px";
	map_control.style.top = map_control.offsetTop + "px";
	map_control.style.width = width + "px";
	map_control.style.height = height + "px";
	
	// top tools
	if (map_control_top_tools)
	{
		map_control_top_tools.style.left = "0px";
		map_control_top_tools.style.top = "0px";
		map_control_top_tools.style.width = (width) + "px";
	}

	// bottom tools
	if (map_control_bottom_tools)
	{
		map_control_bottom_tools.style.left = "0px";
		map_control_bottom_tools.style.top = (height - bottom) + "px";
		map_control_bottom_tools.style.width = (width) + "px";
	}

	// left tools
	if (map_control_left_tools)
	{
		map_control_left_tools.style.left = "0px";
		map_control_left_tools.style.top = (top) + "px";
		map_control_left_tools.style.height = (height - bottom - top) + "px";
	}

	// right tools
	if (map_control_right_tools)
	{
		map_control_right_tools.style.left = (width - right) + "px";
		map_control_right_tools.style.top = (top) + "px";
		map_control_right_tools.style.height = (height - bottom - top) + "px";
	}
	
	// this is used at many places
	vport_offset_left = left + map_control.offsetLeft;
	vport_offset_top = top + map_control.offsetTop;

	// map itself
	map_frame.style.left = (left) + "px";
	map_frame.style.top = (top) + "px";
	map_frame.style.width = (vport_width) + "px";
	map_frame.style.height = (vport_height) + "px";
	
	// number of tiles needed
	visible_cols = Math.floor(vport_width / tile_width) + 1;
	visible_rows = Math.floor(vport_height / tile_height) + 1;
	
	// delete old tiles for map
	if (tiles_map != null)
		for (var i=0; i<tiles_map.length; i++)
			for (var j=0; j<tiles_map[i].length; j++)
				map_frame.removeChild(tiles_map[i][j]);

	// tiles for maps
	tiles_map = new Array();
	for (var i=0; i<visible_rows+1; i++)
	{
		tiles_map[i] = new Array();
		for (var j=0; j<visible_cols+1; j++)
		{			
			var tile = document.createElement("IMG");
			tiles_map[i][j] = tile;
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
			tile.onmousedown = false_fnc;
			tile.style.position = "absolute";
			tile.width = tile_width;
			tile.height = tile_height;
			tile.style.left = (j * tile_width) + "px";
			tile.style.top = (i * tile_height) + "px";
			map_frame.insertBefore(tile, map_selector);
		}
	}
	
}

function map_control_init()
{

	/*
	map_container = document.getElementById("plocha");
	map_container_offset_left = get_element_offset_left(map_container);
	map_container_offset_top = get_element_offset_top(map_container);
	*/
	
	map_control = document.getElementById("form:map-control");
	map_control.style.display = "block";
	
	map_control_top_tools = document.getElementById("form:map-tools-top");
	alert(map_control_top_tools.offsetHeight);
	if (map_control_top_tools) map_control_top_tools.style.position = "absolute";
	
	map_control_bottom_tools = document.getElementById("form:map-tools-bottom");
	if (map_control_bottom_tools) map_control_bottom_tools.style.position = "absolute";

	map_control_left_tools = document.getElementById("form:map-tools-left");
	if (map_control_left_tools) map_control_left_tools.style.position = "absolute";
	
	map_control_right_tools = document.getElementById("form:map-tools-right");
	if (map_control_right_tools) map_control_right_tools.style.position = "absolute";

	map_frame = document.getElementById("form:map-frm");
	map_frame.style.backgroundColor = "White";
	map_frame.style.position = "absolute";
	map_frame.style.overflow = "hidden";
	if (IE)
	{
		//map_frame.style.cursor = "url(ruka.cur)";
		map_frame.unselectable = "on";
	}
	if (GK)
	{
		map_frame.style.MozUserFocus = "none";
		map_frame.style.MozUserSelect = "none";
	}

	map_frame.onmousedown = map_start_drag;
	map_frame.onmouseup = map_stop_drag;
	if (IE) map_frame.onmousewheel = map_mouse_wheel;

	map_selector = document.createElement("DIV");
	map_selector.style.position = "absolute";
	map_selector.style.display = "none";
	map_selector.style.borderStyle = "solid";
	map_selector.style.borderWidth = selector_border_width + "px";
	map_selector.style.borderColor = selector_color;
	map_selector.style.left = "0px";
	map_selector.style.top = "0px";
	map_selector.style.width = "200px";
	map_selector.style.height = "200px";
	map_frame.appendChild(map_selector);
	
	var bg = document.createElement("DIV");
	bg.style.width = "100%";
	bg.style.height = "100%";
	bg.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=" + selector_opacity + ")";
	bg.style.MozOpacity = selector_opacity / 100;
	bg.style.backgroundColor = selector_color;
	map_selector.appendChild(bg);
	
}


/////////////////////////////////////////////////////////
// main init
/////////////////////////////////////////////////////////

function map_init()
{

	// control
	map_control_init();
	change_map_size();
	
	// scale indicator - down left	
	scale_indicator_init();

	// scale selector - top right
	scale_sel_init();

	// pan tools
	pan_tool_init();

	// show something
	zoom_map_to(-180, -90, 180, 90);	
	
	// last thing, we don't want to fire this
	// when we create HTML elements
	//window.onresize = window_resize;

}

window.onresize = function()
{
	map_window_resize();
}
