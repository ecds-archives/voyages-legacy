function getScrolling()
{
	var x = 0;
	var y = 0;
	if (self.pageXOffset || self.pageYOffset)
	{
		x = self.pageXOffset;
		y = self.pageYOffset;
	}
	else if ((document.documentElement && document.documentElement.scrollLeft)||(document.documentElement && document.documentElement.scrollTop))
	{
		x = document.documentElement.scrollLeft;
		y = document.documentElement.scrollTop;
	}
	else if (document.body)
	{
		x = document.body.scrollLeft;
		y = document.body.scrollTop;
	}
	return x + "," + y;
}
