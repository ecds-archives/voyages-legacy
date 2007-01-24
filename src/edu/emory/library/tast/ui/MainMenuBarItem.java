package edu.emory.library.tast.ui;

public class MainMenuBarItem
{
	
	private String label;
	private String imageNormal;
	private String imageActive;
	private MainMenuBarSubItem[] subItems;
	
	public String getImageActive()
	{
		return imageActive;
	}

	public void setImageActive(String imageActive)
	{
		this.imageActive = imageActive;
	}
	
	public String getImageNormal()
	{
		return imageNormal;
	}
	
	public void setImageNormal(String imageNormal)
	{
		this.imageNormal = imageNormal;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public MainMenuBarSubItem[] getSubItems()
	{
		return subItems;
	}
	
	public void setSubItems(MainMenuBarSubItem[] subItems)
	{
		this.subItems = subItems;
	}

}
