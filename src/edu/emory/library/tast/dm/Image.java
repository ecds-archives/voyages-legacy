package edu.emory.library.tast.dm;

import java.util.List;

import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class Image
{
	
	private int id;
	private String name;
	private String description;
	private String fileName;
	private int width;
	private int height;
	private String mimeType;
	private String thumbnailFileName;
	private String thumbnailmimeType;
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getMimeType()
	{
		return mimeType;
	}
	
	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public String getThumbnailFileName()
	{
		return thumbnailFileName;
	}

	public void setThumbnailFileName(String thumbnailFileName)
	{
		this.thumbnailFileName = thumbnailFileName;
	}

	public String getThumbnailMimeType()
	{
		return thumbnailmimeType;
	}

	public void setThumbnailMimeType(String thumbnailmimeType)
	{
		this.thumbnailmimeType = thumbnailmimeType;
	}
	
	public String getThumbnailUrl(String contextPath)
	{
		return contextPath + "/images/" + thumbnailFileName;
	}

	public String getImageUrl(String contextPath)
	{
		return contextPath + "/images/" + fileName;
	}

	public String getImageUrl()
	{
		return "images/" + fileName;
	}

	public static Image[] getImagesArray()
	{
		List list = getImagesList();
		Image[] images = new Image[list.size()];
		list.toArray(images);
		return images;
	}
	
	public static List getImagesList()
	{
		QueryValue qValue = new QueryValue("Image");
		qValue.setOrderBy(new String[] {"name"});
		qValue.setOrder(QueryValue.ORDER_ASC);
		qValue.setCacheable(true);
		return qValue.executeQueryList();
	}

	public static Image loadById(int imageId)
	{
		Conditions conditions = new Conditions();
		conditions.addCondition("id", new Integer(imageId), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Image", conditions);
		List list = qValue.executeQueryList();
		if (list.size() == 0) return null;
		return (Image) list.get(0);
	}
	
}
	
