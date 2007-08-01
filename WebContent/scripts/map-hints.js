// configuration variable for the hint object, these setting will be shared among all hints created by this object
var HINTS_CFG = {
	'wise'       : true, // don't go off screen, don't overlap the object in the document
	'margin'     : 10, // minimum allowed distance between the hint and the window edge (negative values accepted)
	'gap'        : 0, // minimum allowed distance between the hint and the origin (negative values accepted)
	'align'      : 'tcbc', // align of the hint and the origin (by first letters origin's top|middle|bottom left|center|right to hint's top|middle|bottom left|center|right)
	'css'        : 'hintsClass', // a style class name for all hints, applied to DIV element (see style section in the header of the document)
	'show_delay' : 200, // a delay between initiating event (mouseover for example) and hint appearing
	'hide_delay' : 500, // a delay between closing event (mouseout for example) and hint disappearing
	'follow'     : false, // hint follows the mouse as it moves
	'z-index'    : 100, // a z-index for all hint layers
	'IEfix'      : false, // fix IE problem with windowed controls visible through hints (activate if select boxes are visible through the hints)
	'IEtrans'    : ['blendTrans(DURATION=.3)', 'blendTrans(DURATION=.3)'], // [show transition, hide transition] - nice transition effects, only work in IE5+
	'opacity'    : 100 // opacity of the hint in %%
};
// text/HTML of the hints
var HINTS_ITEMS = [
	'Previous',
	'Next',
	'Pan mode',
	'Zoom mode',
	'Zoom in',
	'Zoom out',
	'Map size',
	'Zoom level',
	'Enable/disable mini-map'
];

var myHint = new THints (HINTS_ITEMS, HINTS_CFG);