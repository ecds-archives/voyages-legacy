var Popups =
{

	popups: Array(),

	registerPopup: function(popup)
	{
		this.popups[popup.popuId] = popup;
	},
	
	close: function(popuId)
	{
		var popup = Popups.popups[popuId];
		if (popup) popup.close();
	}
	
}

function Popup(popupId, blackoutId, contentId, width, height)
{
	
	this.popupId = popupId;
	this.blackoutId = blackoutId;
	this.contentId = contentId;
	this.width = width;
	this.height = height;
	
	EventAttacher.attachOnWindowEvent("load", this, "init");
	
}

Popup.prototype.init = function()
{

	var blackoutFrame = document.getElementById(this.blackoutId);
	var contentFrame = document.getElementById(this.contentId);
	
	blackoutFrame.style.position = "absolute";
	contentFrame.style.position = "absolute";
	
	this.reposition();
	
	blackoutFrame.style.display = "";
	contentFrame.style.display = "";

	EventAttacher.attachOnWindowEvent("resize", this, "reposition");
	EventAttacher.attachOnWindowEvent("scroll", this, "reposition");
	
}

Popup.prototype.reposition = function()
{

	var blackoutFrame = document.getElementById(this.blackoutId);
	var contentFrame = document.getElementById(this.contentId);

	var vportWidth = ElementUtils.getPageWidth();
	var vportHeight = ElementUtils.getPageHeight();
	var vportLeft = ElementUtils.getPageScrollLeft();
	var vportTop = ElementUtils.getPageScrollTop();
	
	var contentWidth = this.width;
	var contentHeight = this.height;
	var contentLeft = (vportWidth - this.width) / 2;
	var contentTop = (vportHeight - this.height) / 2;

	blackoutFrame.style.left = vportLeft + "px";
	blackoutFrame.style.top = vportTop + "px";
	blackoutFrame.style.width = vportWidth + "px";
	blackoutFrame.style.height = vportHeight + "px";
	
	contentFrame.style.left = contentLeft + "px";
	contentFrame.style.top = contentTop + "px";
	contentFrame.style.width = contentWidth + "px";
	contentFrame.style.height = contentHeight + "px";

}

Popup.prototype.close = function()
{

	var blackoutFrame = document.getElementById(this.blackoutId);
	var contentFrame = document.getElementById(this.contentId);

	blackoutFrame.style.display = "none";
	contentFrame.style.display = "none";
	
	EventAttacher.detachOnWindowEvent("resize", this, "reposition");
	EventAttacher.detachOnWindowEvent("scroll", this, "reposition");

}