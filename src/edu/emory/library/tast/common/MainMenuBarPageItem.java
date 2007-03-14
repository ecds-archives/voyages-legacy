package edu.emory.library.tast.common;

public class MainMenuBarPageItem
{
	
	private String id;
	private String label;
	private String url;
	
	public MainMenuBarPageItem(String id, String label, String url)
	{
		this.id = id;
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

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

}
