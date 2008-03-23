var SlidingBoxGlobals = 
{

	boxes: Array(),

	registerBox: function(box)
	{
		this.boxes[box.boxId] = box;
	}

}

function SlidingBox(columnId, boxId)
{

	this.columnId = columnId;
	this.boxId = boxId;

	this.slide = true;
	this.anim = null;
	this.animDelay = 100;
	this.animStep = 10000000;
	
	EventAttacher.attachOnWindowEvent("load", this, "init");

}

SlidingBox.prototype.init = function()
{

	var column = document.getElementById(this.columnId);
	var box = document.getElementById(this.boxId);
	
	var boxHeight = ElementUtils.getOffsetHeight(box);
	
	this.columnTop = ElementUtils.getPosTop(column);
	this.lowerBound = ElementUtils.getPosTop(box);
	this.upperBound = this.columnTop + ElementUtils.getOffsetHeight(column) - boxHeight;
	
	box.style.position = "absolute";

	var phantom = document.createElement("div");
	phantom.style.height = boxHeight + "px";
	phantom.style.overflow = "hidden";
	column.appendChild(phantom);

	this.reposition(false);
	
	EventAttacher.attachOnWindowEvent("scroll", this, "repositionAfterScroll");
	
}

SlidingBox.prototype.repositionAfterScroll = function()
{
	this.reposition(true);
}

SlidingBox.prototype.reposition = function()
{

	var column = document.getElementById(this.columnId);
	var box = document.getElementById(this.boxId);

	var newTop = ElementUtils.getPageScrollTop();
	if (newTop < this.lowerBound)
	{
		newTop = this.lowerBound;
	}
	else if (newTop > this.upperBound)
	{
		newTop = this.upperBound;
	}
	
	if (this.slide)
	{
	
		this.currentPos = ElementUtils.getPosTop(box);
		this.targetPos = newTop;
		
		box.style.display = "none";

		if (this.anim) Timer.cancelCall(this.anim);
		this.anim = Timer.delayedCall(this, "move", this.animDelay);
		
	}
	else
	{
	
		box.style.top = newTop + "px";
	
	}

}

SlidingBox.prototype.move = function()
{

	if (Math.abs(this.currentPos - this.targetPos) <= this.animStep)
	{
		this.currentPos = this.targetPos;
	}
	else if (this.currentPos < this.targetPos)
	{
		this.currentPos += this.animStep;
	}
	else
	{
		this.currentPos -= this.animStep;
	}
	
	var box = document.getElementById(this.boxId);
	box.style.top = this.currentPos + "px";
	box.style.display = "";
	
	if (this.currentPos == this.targetPos)
	{
		Timer.cancelCall(this.anim);
		this.anim = null;
	}
	else
	{
		this.anim = Timer.delayedCall(this, "move", this.animDelay);
	}

}
