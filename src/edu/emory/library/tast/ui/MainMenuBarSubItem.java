package edu.emory.library.tast.ui;

public class MainMenuBarSubItem
{
	
	private String label;
	private String url;
	
	public MainMenuBarSubItem(String label, String url)
	{
		this.label = label;
		this.url = url;
	}

	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}

}
