package edu.emory.library.tast.common;

public class MenuItemSection extends MenuItem
{
	
	private String imageUrl;
	private int imageWidth;
	private int imageHeight;

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String iconImg)
	{
		this.imageUrl = iconImg;
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

}
