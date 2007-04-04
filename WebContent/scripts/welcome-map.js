var WelcomeMapGlobals = 
{

	welcomeMaps: new Array(),
	
	register: function(welcomeMap)
	{
		WelcomeMapGlobals.welcomeMaps[welcomeMap.welcomeMapId] = welcomeMap;
	},

	showPlace: function(welcomeMapId, placeIndex)
	{
		var welcomeMap = WelcomeMapGlobals.welcomeMaps[welcomeMapId];
		if (welcomeMap) welcomeMap.showPlace(placeIndex);
	},

	hidePlace: function(welcomeMapId, placeIndex)
	{
		var welcomeMap = WelcomeMapGlobals.welcomeMaps[welcomeMapId];
		if (welcomeMap) welcomeMap.hidePlace(placeIndex);
	}

}

function WelcomeMap(welcomeMapId, initialText, textElementId, places)
{
	this.welcomeMapId = welcomeMapId;
	this.initialText = initialText;
	this.textElementId = textElementId;
	this.places = places;
}

function WelcomeMapPlace(imageElementId, normalUrl, highlightedUrl, text)
{
	this.imageElementId = imageElementId;
	this.normalUrl = normalUrl;
	this.highlightedUrl = highlightedUrl;
	this.text = text;
}

WelcomeMap.prototype.showPlace = function(placeIndex)
{

	var textElement = document.getElementById(this.textElementId);
	var imageElement = document.getElementById(this.places[placeIndex].imageElementId);
	
	textElement.innerHTML = this.places[placeIndex].text;
	imageElement.src = this.places[placeIndex].highlightedUrl;

}

WelcomeMap.prototype.hidePlace = function(placeIndex)
{

	var textElement = document.getElementById(this.textElementId);
	var imageElement = document.getElementById(this.places[placeIndex].imageElementId);
	
	textElement.innerHTML = this.initialText;
	imageElement.src = this.places[placeIndex].normalUrl;

}