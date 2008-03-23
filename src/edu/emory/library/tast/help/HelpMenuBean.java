package edu.emory.library.tast.help;

import javax.faces.component.UIParameter;

public class HelpMenuBean
{

	private UIParameter activeSectionParam;

	public UIParameter getActiveSectionParam()
	{
		return activeSectionParam;
	}

	public void setActiveSectionParam(UIParameter activeSectionParam)
	{
		this.activeSectionParam = activeSectionParam;
	}

	public String getActiveSection()
	{
		return (String) activeSectionParam.getValue();
	}

}
