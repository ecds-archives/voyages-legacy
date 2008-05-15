function tooltipShow(event, tooltipId)
{
 	var tooltip = document.getElementById(tooltipId);
 	tooltipMove(event, tooltipId);
 	tooltip.style.visibility = "visible";
}

function tooltipHide(event, tooltipId)
{
 	var tooltip = document.getElementById(tooltipId);
 	tooltip.style.visibility = "hidden";
}

function tooltipMove(event, tooltipId)
{
 
 	var tooltip = document.getElementById(tooltipId);
 	if (!event) event = window.event;
 	
 	var x = ElementUtils.getEventMouseX(event);
 	var y = ElementUtils.getEventMouseY(event);
 	
	var vportWidth = ElementUtils.getPageWidth();
	var vportHeight = ElementUtils.getPageHeight();
	var vportLeft = ElementUtils.getPageScrollLeft();
	var vportTop = ElementUtils.getPageScrollTop();
	
	var width = ElementUtils.getOffsetWidth(tooltip);
	var height = ElementUtils.getOffsetHeight(tooltip);
	
	if (vportLeft + vportWidth - 10 < x + width)
		x = x - width - 5;
	else
		x = x + 25;
 	
	if (vportTop + vportHeight - 10 < y + height)
		y = y - height - 5;
	else
		y = y + 10;

 	tooltip.style.left = x + "px";
 	tooltip.style.top = y + "px";

}