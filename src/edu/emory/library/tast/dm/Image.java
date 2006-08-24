package edu.emory.library.tast.dm;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.util.HibernateUtil;

public class Image
{
	
//	private static Map attributes = new HashMap();
//	static
//	{
//		attributes.put("id", new StringAttribute("name", "Image"));
//		attributes.put("name", new NumericAttribute("name", "Image"));
//		attributes.put("description", new NumericAttribute("description", "Image"));
//		attributes.put("fileName", new NumericAttribute("fileName", "Image"));
//		attributes.put("width", new NumericAttribute("width", "Image"));
//		attributes.put("height", new NumericAttribute("height", "Image"));
//		attributes.put("mimeType", new NumericAttribute("mimeType", "Image"));
//		attributes.put("thumbnailFileName", new NumericAttribute("thumbnailFileName", "Image"));
//		attributes.put("thumbnailmimeType", new NumericAttribute("thumbnailmimeType", "Image"));
//	}
	
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
	
//	public String getThumbnailUrl(String contextPath)
//	{
//		return contextPath + "/images/" + thumbnailFileName;
//	}
//
//	public String getImageUrl(String contextPath)
//	{
//		return contextPath + "/images/" + fileName;
//	}
//
//	public String getImageUrl()
//	{
//		return "images/" + fileName;
//	}

	public static Image[] getImagesArray()
	{
		List list = getImagesList();
		Image[] images = new Image[list.size()];
		list.toArray(images);
		return images;
	}
	
	public static List getImagesList()
	{
//		QueryValue qValue = new QueryValue("Image");
//		qValue.setOrderBy(new Attribute[] {Image.getAttribute("name")});
//		qValue.setOrder(QueryValue.ORDER_ASC);
//		qValue.setCacheable(true);
//		return qValue.executeQueryList();
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		List list = sess.createCriteria(Image.class).addOrder(Order.asc("name")).list();
		transaction.commit();
		sess.close();
		return list;
	}

	public static Image loadById(int imageId)
	{
//		Conditions conditions = new Conditions();
//		conditions.addCondition(Image.getAttribute("id"), new Integer(imageId), Conditions.OP_EQUALS);
//		QueryValue qValue = new QueryValue("Image", conditions);
//		List list = qValue.executeQueryList();
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		List list = sess.createCriteria(Image.class).add(Restrictions.eq("id", new Integer(imageId))).list();
		transaction.commit();
		sess.close();
		if (list == null || list.size() == 0) return null;
		return (Image) list.get(0);
	}
	
//	public static Attribute getAttribute(String name)
//	{
//		return (Attribute)attributes.get(name);
//	}

}
	
