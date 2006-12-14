package edu.emory.library.tast.reditor;

import javax.faces.webapp.UIComponentTag;

public class EditorTag extends UIComponentTag
{

	public String getComponentType()
	{
		return "RecordEditor";
	}

	public String getRendererType()
	{
		return null;
	}

}
