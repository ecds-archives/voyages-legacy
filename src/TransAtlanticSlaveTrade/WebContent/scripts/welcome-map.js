var WelcomeMapGlobals = 
{

	welcomeMaps: new Array(),
	
	register: function(welcomeMap)
	{
		WelcomeMapGlobals.welcomeMaps[welcomeMap.welcomeMapId] = welcomeMap;
	}

}

function WelcomeMap(welcomeMapId, defaultElementId, places)
{

	this.welcomeMapId = welcomeMapId;
	this.defaultElementId = defaultElementId;
	this.places = places;
	
	this.currentPlaceIndex = -1;
	this.showDefaultTextDelayHandle = null;
	
	EventAttacher.attachOnWindowEvent("load", this, "init");
	
}

function WelcomeMapPlace(imageElementId, imageNormalSrc, imageHighlightedSrc, descElementId)
{

	this.imageElementId = imageElementId;
	this.imageNormalSrc = imageNormalSrc;
	this.imageHighlightedSrc = imageHighlightedSrc;
	this.descElementId = descElementId;
	
	this.active = false;
	
}

WelcomeMap.prototype.init = function()
{

	for (var i = 0; i < this.places.length; i++)
	{
		var place = this.places[i];
		
		var img = document.getElementById(place.imageElementId);
		
		var preloadNomalImage = new Image();
		preloadNomalImage.src = place.imageNormalSrc;
		
		var preloadHighlightedSrc = new Image();
		preloadHighlightedSrc.src = place.imageHighlightedSrc;

		EventAttacher.attach(img, "mouseover", this, "onMouseOver", i);
		EventAttacher.attach(img, "mouseout", this, "onMouseOut", i);
		
	}

}

WelcomeMap.prototype.showPlace = function(placeIndex)
{

	var place = this.places[placeIndex];

	var desc = document.getElementById(place.descElementId);
	var img = document.getElementById(place.imageElementId);
	
	img.src = place.imageHighlightedSrc;

	if (false && Scriptaculous)
	{
		new Effect.BlindDown(desc, {duration: 0.25});
	}
	else
	{
		desc.style.display = "";
	}

}

WelcomeMap.prototype.hidePlace = function(placeIndex, rightNow)
{

	var place = this.places[placeIndex];

	var desc = document.getElementById(place.descElementId);
	var img = document.getElementById(place.imageElementId);
	
	img.src = place.imageNormalSrc;
	
	if (false && !rightNow && Scriptaculous)
	{
		new Effect.Fade(desc);
	}
	else
	{
		desc.style.display = "none";
	}

}

WelcomeMap.prototype.showDefaultText = function()
{
	var text = document.getElementById(this.defaultElementId);
	text.style.display = "";
}

WelcomeMap.prototype.hideDefaultText = function()
{
	var text = document.getElementById(this.defaultElementId);
	text.style.display = "none";
}

WelcomeMap.prototype.onMouseOver = function(event, placeIndex)
{

	if (this.currentPlaceIndex != -1)
		this.hidePlace(currentPlaceIndex);
		
	Timer.cancelCall(this.showDefaultTextDelayHandle);
	this.hideDefaultText();

	this.currentPlaceIndex = placeIndex;
	this.showPlace(placeIndex);

}

WelcomeMap.prototype.onMouseOut = function(event, placeIndex)
{

	if (this.currentPlaceIndex != -1)
		this.hidePlace(this.currentPlaceIndex, false);
	
	this.showDefaultTextDelayHandle = Timer.extendCall(
		this.showDefaultTextDelayHandle,
		this, "showDefaultText", 1000)
		
	this.currentPlaceIndex = -1;

}