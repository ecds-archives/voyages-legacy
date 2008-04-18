package edu.emory.library.tast.essays;

import javax.faces.component.UIParameter;

public class EssaysBean
{
	
	private UIParameter paramActiveMenuId;
	
	public String getActiveMenuId()
	{
		return paramActiveMenuId != null ?
			(String) paramActiveMenuId.getValue() :
				null;
	}

	public UIParameter getParamActiveMenuId()
	{
		return paramActiveMenuId;
	}

	public void setParamActiveMenuId(UIParameter paramActiveMenuId)
	{
		this.paramActiveMenuId = paramActiveMenuId;
	}

}
