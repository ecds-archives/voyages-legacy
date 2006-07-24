function GraphicsVML()
{
	this.NS = "http://www.w3.org/2000/svg";
	this.svg = document.createElementNS(this.NS, "svg");
}

GraphicsVML.prototype.drawCircle = function(x, y, r fill)
{
	var c = document.createElementNS(this.NS, "circle");
	c.setAttributeNS(null, "cx", x);
	c.setAttributeNS(null, "cy", y);
	c.setAttributeNS(null, "r", "30");
	if (fill) c.setAttributeNS(null, "fill", fill);
	this.svg.appendChild(c);
}

GraphicsVML.prototype.getDOM = function()
{
	this.
}
