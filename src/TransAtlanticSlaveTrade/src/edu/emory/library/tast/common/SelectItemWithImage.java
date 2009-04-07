package edu.emory.library.tast.common;

public class SelectItemWithImage extends SelectItem
{

	private static final long serialVersionUID = -4506145475072849454L;
	
	private String imageUrl;

	public SelectItemWithImage(String text, String value, String imageUrl)
	{
		super(text, value);
		this.imageUrl = imageUrl;
	}

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

}
