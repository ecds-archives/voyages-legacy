package edu.emory.library.tast.images.site;

public class ImageCategoryDescriptor
{
	
	private String name;
	private long id;
	private long imagesCount;
	
	public ImageCategoryDescriptor(long id, String name, long imagesCount)
	{
		this.name = name;
		this.id = id;
		this.imagesCount = imagesCount;
	}

	public String getName()
	{
		return name;
	}
	
	public long getId()
	{
		return id;
	}

	public long getImagesCount()
	{
		return imagesCount;
	}

}
