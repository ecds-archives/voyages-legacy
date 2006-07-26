var GraphicsGlobalSVG =
{
	NS: "http://www.w3.org/2000/svg"
}

function GraphicsSVG()
{
	this.svg = document.createElementNS(GraphicsGlobalSVG.NS, "svg");
}

GraphicsSVG.prototype.hide = function()
{
	this.svg.style.display = "none";
}

GraphicsSVG.prototype.show = function()
{
	this.svg.style.display = "block";
}

GraphicsSVG.prototype.createCircle = function()
{
	return new GraphicsCircleSVG();
}

GraphicsSVG.prototype.getRootDOM = function()
{
	return this.svg;
}

GraphicsSVG.prototype.appendChild = function(child)
{
	this.svg.appendChild(child.getRoot());
}

/////////////////////////////////////////////////////////
// circle
/////////////////////////////////////////////////////////

function GraphicsCircleSVG()
{
	this.circle = document.createElementNS(GraphicsGlobalSVG.NS, "circle");
}

GraphicsCircleSVG.prototype.getRoot = function()
{
	return this.circle;
}

GraphicsCircleSVG.prototype.setCenter = function(x, y)
{
	this.setCenterX(x);
	this.setCenterY(y);
}

GraphicsCircleSVG.prototype.setCenterX = function(x)
{
	this.circle.setAttributeNS(null, "cx", x);
}

GraphicsCircleSVG.prototype.setCenterY = function(y)
{
	this.circle.setAttributeNS(null, "cy", y);
}

GraphicsCircleSVG.prototype.setRadius = function(r)
{
	this.circle.setAttributeNS(null, "r", r);
}

GraphicsCircleSVG.prototype.setFill = function(fill)
{
	this.circle.setAttributeNS(null, "fill", fill);
}

GraphicsCircleSVG.prototype.setFillOpacity = function(opacity)
{
	this.circle.setAttributeNS(null, "fill-opacity", opacity);
}