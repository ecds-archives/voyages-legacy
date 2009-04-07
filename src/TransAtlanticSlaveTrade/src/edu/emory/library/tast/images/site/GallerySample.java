package edu.emory.library.tast.images.site;

import edu.emory.library.tast.images.GalleryImage;


public class GallerySample
{
	
	private String categoryName;
	private long categoryId;
	private GalleryImage[] images;
	
	public GallerySample(String categoryName, long categoryId, GalleryImage[] images)
	{
		this.categoryName = categoryName;
		this.categoryId = categoryId;
		this.images = images;
	}

	public String getCategoryName()
	{
		return categoryName;
	}
	
	public long getCategoryId()
	{
		return categoryId;
	}
	
	public GalleryImage[] getImages()
	{
		return images;
	}

}
