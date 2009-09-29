package edu.emory.library.tast.master;

public class HelpLink
{
	
	private String label;
	private String href;
	private boolean openInNewWindow;
	
	public HelpLink(String label, String href, boolean openInNewWindow)
	{
		this.label = label;
		this.href = href;
		this.openInNewWindow = openInNewWindow;
	}

	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public boolean isOpenInNewWindow()
	{
		return openInNewWindow;
	}

	public void setOpenInNewWindow(boolean openInNewWindow)
	{
		this.openInNewWindow = openInNewWindow;
	}

}
