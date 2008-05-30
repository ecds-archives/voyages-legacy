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

	public boolean isIntroExpanded()
	{
		String activeMenuId = getActiveMenuId();
		return activeMenuId != null && activeMenuId.startsWith("essays-intro");
	}

	public boolean isSeasonalityExpanded()
	{
		String activeMenuId = getActiveMenuId();
		return activeMenuId != null && activeMenuId.startsWith("essays-seasonality");
	}

}
