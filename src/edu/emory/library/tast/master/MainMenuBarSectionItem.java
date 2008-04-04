package edu.emory.library.tast.master;

public class MainMenuBarSectionItem
{
	
	private String id;
	private String imageUrlNormal;
	private String imageUrlHighlighted;
	private String imageUrlActive;
	private int imageWidth;
	private int imageHeight;
	private String url;
	private MainMenuBarPageItem[] subItems;
	private String boxCssClass;
	
	public MainMenuBarSectionItem(String id, String url, String imageUrlNormal, String imageUrlHighlighted, String imageUrlActive, int imageWidth, int imageHeight, String boxCssClass, MainMenuBarPageItem[] subItems)
	{
		this.id = id;
		this.url = url;
		this.imageUrlNormal = imageUrlNormal;
		this.imageUrlHighlighted = imageUrlHighlighted;
		this.imageUrlActive = imageUrlActive;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.boxCssClass = boxCssClass;
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

	public String getBoxCssClass()
	{
		return boxCssClass;
	}

	public void setBoxCssClass(String boxCssClass)
	{
		this.boxCssClass = boxCssClass;
	}

	public String getImageUrlHighlighted()
	{
		return imageUrlHighlighted;
	}

	public void setImageUrlHighlighted(String imageUrlHighlighted)
	{
		this.imageUrlHighlighted = imageUrlHighlighted;
	}

}
