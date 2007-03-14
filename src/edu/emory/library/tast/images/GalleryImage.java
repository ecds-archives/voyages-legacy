package edu.emory.library.tast.images;

import edu.emory.library.tast.util.StringUtils;

public class GalleryImage
{
	
	private String id;
	private String imageName;
	private String label;
	private String description;
	
	public GalleryImage(String id, String imageName, String label, String description)
	{
		this.imageName = imageName;
		this.label = label;
		this.description = description;
		this.id = id;
	}

	public GalleryImage(String id, String imageName, String label)
	{
		this.imageName = imageName;
		this.label = label;
		this.id = id;
	}

	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public boolean hasDescription()
	{
		return !StringUtils.isNullOrEmpty(description);
	}

	public String getImageName()
	{
		return imageName;
	}
	
	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}