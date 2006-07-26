var GraphicsGlobal = 
{

	createGraphics: function()
	{
		if (IE)
		{
			// not supported yet
			// return new GraphicsVML();
			return null;
		}
		else
		{
			return new GraphicsSVG();
		}
	}

}