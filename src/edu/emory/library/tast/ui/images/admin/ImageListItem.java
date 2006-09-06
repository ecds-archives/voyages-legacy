package edu.emory.library.tast.ui.images.admin;

public class ImageListItem
{
	
	private String id;
	private String name;
	private String url; 
	private String[] subItems;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
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

	public String[] getSubItems()
	{
		return subItems;
	}

	public void setSubItems(String[] subItems)
	{
		this.subItems = subItems;
	}

}