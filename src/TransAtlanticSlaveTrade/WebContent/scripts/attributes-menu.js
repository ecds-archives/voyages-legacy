var AttributesMenu = {

	menus: new Array(),
	
	debug: function(string) {
		document.getElementById("debug").innerHTML += string + '<br>';
	},
	
	delayclose: function(menuId, menu) {		
		AttributesMenu.menus[menuId] = Timer.delayedFunction(AttributesMenu.funct, 50, menu);
		//AttributesMenu.debug('delay close called: ' + menuId);
	},
	
	funct: function(menu) {
		menu.style.display = 'none';
	},
	
	cancelclose: function(menuId, menu) {
		if (AttributesMenu.menus[menuId] != null) {
			Timer.cancelCall(AttributesMenu.menus[menuId]);
			delete AttributesMenu.menus[menuId];
		}
		menu.style.display = 'block';
		//AttributesMenu.debug('cancel close called: ' + menuId);
	}
	
}