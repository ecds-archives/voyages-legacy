package edu.emory.library.tast.ui;

public class MainMenuBarSectionItem
{
	
	private String id;
	private String imageUrlNormal;
	private String imageUrlActive;
	private String url;
	private int imageWidth;
	private int imageHeight;
	private MainMenuBarPageItem[] subItems;
	
	public MainMenuBarSectionItem(String id, String url, String imageUrlNormal, String imageUrlActive, int imageWidth, int imageHeight, MainMenuBarPageItem[] subItems)
	{
		this.id = id;
		this.url = url;
		this.imageUrlNormal = imageUrlNormal;
		this.imageUrlActive = imageUrlActive;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.subItems = subItems;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getImageUrlActive()
	{
		return imageUrlActive;
	}

	public void setImageUrlActive(String imageActive)
	{
		this.imageUrlActive = imageActive;
	}
	
	public String getImageUrlNormal()
	{
		return imageUrlNormal;
	}
	
	public void setImageUrlNormal(String imageNormal)
	{
		this.imageUrlNormal = imageNormal;
	}
	
	public int getImageHeight()
	{
		return imageHeight;
	}

	public void setImageHeight(int imageHeight)
	{
		this.imageHeight = imageHeight;
	}

	public int getImageWidth()
	{
		return imageWidth;
	}

	public void setImageWidth(int imageWidth)
	{
		this.imageWidth = imageWidth;
	}

	public MainMenuBarPageItem[] getSubItems()
	{
		return subItems;
	}
	
	public void setSubItems(MainMenuBarPageItem[] subItems)
	{
		this.subItems = subItems;
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
