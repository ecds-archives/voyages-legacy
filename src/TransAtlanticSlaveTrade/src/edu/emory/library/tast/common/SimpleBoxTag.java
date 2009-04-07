package edu.emory.library.tast.common;

import javax.faces.webapp.UIComponentTag;

public class SimpleBoxTag extends UIComponentTag
{

	public String getComponentType()
	{
		return "SimpleBox";
	}

	public String getRendererType()
	{
		return null;
	}

}
