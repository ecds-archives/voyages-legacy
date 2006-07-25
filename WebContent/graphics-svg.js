function GraphicsSVG()
{
	this.NS = "http://www.w3.org/2000/svg";
	this.svg = document.createElementNS(this.NS, "svg");
	this.svg.setAttributeNS(null, "overflow", "visible");
}

GraphicsSVG.prototype.hide = function()
{
	this.svg.style.display = "none";
}

GraphicsSVG.prototype.show = function()
{
	this.svg.style.display = "block";
}

GraphicsSVG.prototype.drawCircle = function(x, y, r, fill)
{
	var c = document.createElementNS(this.NS, "circle");
	c.setAttributeNS(null, "cx", x);
	c.setAttributeNS(null, "cy", y);
	c.setAttributeNS(null, "r", r);
	if (fill) c.setAttributeNS(null, "fill", fill);
	this.svg.appendChild(c);
	return c;
}

GraphicsSVG.prototype.getRootDOM = function()
{
	return this.svg;
}
