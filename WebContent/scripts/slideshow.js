var SlideshowGlobals = 
{

	slideshows: new Array(),

	registerSlideshow: function(slideshow)
	{
		SlideshowGlobals.slideshows[slideshow.slideshowId] = slideshow;
	},
	
	prevButtonMouseOver: function(slideshowId)
	{
		var slideshow = SlideshowGlobals.slideshows[slideshowId];
		if (slideshow) slideshow.prevButtonMouseOver();
	},

	prevButtonMouseOut: function(slideshowId)
	{
		var slideshow = SlideshowGlobals.slideshows[slideshowId];
		if (slideshow) slideshow.prevButtonMouseOut();
	},

	nextButtonMouseOver: function(slideshowId)
	{
		var slideshow = SlideshowGlobals.slideshows[slideshowId];
		if (slideshow) slideshow.nextButtonMouseOver();
	},

	nextButtonMouseOut: function(slideshowId)
	{
		var slideshow = SlideshowGlobals.slideshows[slideshowId];
		if (slideshow) slideshow.nextButtonMouseOut();
	},

	prevSlide: function(slideshowId)
	{
		var slideshow = SlideshowGlobals.slideshows[slideshowId];
		if (slideshow) slideshow.prevSlide();
	},

	nextSlide: function(slideshowId)
	{
		var slideshow = SlideshowGlobals.slideshows[slideshowId];
		if (slideshow) slideshow.nextSlide();
	}

}

function Slideshow(
	slideshowId,
	frameId,
	imageId,
	captionId,
	prevButtonId,
	nextButtonId,
	prevButtonImageUrl,
	nextButtonImageUrl,
	prevButtonHighlightedUrl,
	nextButtonHighlightedUrl,
	loadingImageUrl,
	loadingImageWidth,
	loadingImageHeight,
	wrap,
	images)
{

	this.slideshowId = slideshowId;
	this.frameId = frameId;
	this.imageId = imageId;
	this.captionId = captionId;
	this.prevButtonId = prevButtonId; 
	this.nextButtonId = nextButtonId;
	this.prevButtonImageUrl = prevButtonImageUrl;
	this.nextButtonImageUrl = nextButtonImageUrl;
	this.prevButtonHighlightedUrl = prevButtonHighlightedUrl;
	this.nextButtonHighlightedUrl = nextButtonHighlightedUrl;
	this.loadingImageUrl = loadingImageUrl;
	this.loadingImageWidth = loadingImageWidth;
	this.loadingImageHeight = loadingImageHeight;
	this.wrap = wrap;
	this.images = images;
	
	this.currentSlideIdx = 0;
	this.loadingImage = null;
	
}

function SlideshowImage(imageUrl, caption)
{
	this.imageUrl = imageUrl;
	this.caption = caption;
}

Slideshow.prototype.prevSlide = function()
{
	if (this.wrap || this.currentSlideIdx > 0)
	{
		var newIdx = this.currentSlideIdx - 1;
		if (newIdx < 0) newIdx = this.images.length - 1;
		this.gotoSlide(newIdx);
	}
}

Slideshow.prototype.nextSlide = function()
{
	if (this.wrap || this.currentSlideIdx < this.images.length - 1)
	{
		var newIdx = this.currentSlideIdx + 1;
		if (newIdx == this.images.length) newIdx = 0;
		this.gotoSlide(newIdx);
	}
}

Slideshow.prototype.ensureLoadingImage = function()
{
	if (this.loadingImage)
		return;
		
	var frame = document.getElementById(this.frameId);
	
	var width = ElementUtils.getOffsetWidth(frame);
	var height = ElementUtils.getOffsetHeight(frame);
	
	this.loadingImage = document.createElement("img");
	
	this.loadingImage.src = this.loadingImageUrl;
	this.loadingImage.width = this.loadingImageWidth; 
	this.loadingImage.height = this.loadingImageHeight;
	this.loadingImage.border = 0;
	
	this.loadingImage.style.position = "absolute";
	this.loadingImage.style.left = ((width - this.loadingImageWidth) / 2) + "px";
	this.loadingImage.style.top = ((height - this.loadingImageHeight) / 2) + "px";
	
	frame.style.position = "relative";
	this.loadingImage.style.display = "none";
	frame.appendChild(this.loadingImage);

}

Slideshow.prototype.gotoSlide = function(slideIdx)
{

	this.currentSlideIdx = slideIdx;
	
	if (!this.loadingImageUrl)
	{
	
		this.changeImage(true, true);
		
	}
	else
	{
	
		var img = document.getElementById(this.imageId);
	
		this.ensureLoadingImage();
		
		img.style.visibility = "hidden";
		this.loadingImage.style.display = "";
		
		this.changeImage(true, false);
		
		EventAttacher.attach(img, "load", this, "slideLoaded");
	
	}

}

Slideshow.prototype.slideLoaded = function()
{
	
	var img = document.getElementById(this.imageId);

	this.loadingImage.style.display = "none";
	img.style.visibility = "visible";
	
	this.changeImage(false, true);
	
	EventAttacher.detach(img, "load", this, "slideLoaded");
	
}

Slideshow.prototype.changeImage = function(changeImg, changeCaption)
{

	if (changeImg)
	{
		var image = document.getElementById(this.imageId);
		image.src = this.images[this.currentSlideIdx].imageUrl;
	}
	
	if (changeCaption)
	{
		var caption = document.getElementById(this.captionId);
		caption.innerHTML = this.images[this.currentSlideIdx].caption;
	}
	
}

Slideshow.prototype.prevButtonMouseOver = function()
{
	var prevButton = document.getElementById(this.prevButtonId);
	prevButton.src = this.prevButtonHighlightedUrl;
}

Slideshow.prototype.prevButtonMouseOut = function()
{
	var prevButton = document.getElementById(this.prevButtonId);
	prevButton.src = this.prevButtonImageUrl;
}

Slideshow.prototype.nextButtonMouseOver = function()
{
	var nextButton = document.getElementById(this.nextButtonId);
	nextButton.src = this.nextButtonHighlightedUrl;
}

Slideshow.prototype.nextButtonMouseOut = function()
{
	var nextButton = document.getElementById(this.nextButtonId);
	nextButton.src = this.nextButtonImageUrl;
}