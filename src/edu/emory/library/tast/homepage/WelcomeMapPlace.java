package edu.emory.library.tast.homepage;

public class WelcomeMapPlace
{
	
	private int x;
	private int y;
	private int imageWidth;
	private int imageHeight;
	private String imageUrl;
	private String imageUrlSelected;
	private String text;
	
	public WelcomeMapPlace(int x, int y, int imageWidth, int imageHeight, String imageUrl, String imageUrlSelected, String text)
	{
		this.x = x;
		this.y = y;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.imageUrl = imageUrl;
		this.imageUrlSelected = imageUrlSelected;
		this.text = text;
	}

	public int getImageHeight()
	{
		return imageHeight;
	}
	
	public void setImageHeight(int imageHeight)
	{
		this.imageHeight = imageHeight;
	}
	
	public String getImageUrl()
	{
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}
	
	public int getImageWidth()
	{
		return imageWidth;
	}
	
	public void setImageWidth(int imageWidth)
	{
		this.imageWidth = imageWidth;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public int getX()
	{
		return x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}

	public String getImageUrlSelected()
	{
		return imageUrlSelected;
	}

	public void setImageUrlSelected(String imageUrlSelected)
	{
		this.imageUrlSelected = imageUrlSelected;
	}

}
