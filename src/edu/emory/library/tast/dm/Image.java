package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class Image
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new StringAttribute("name", "Image"));
		attributes.put("name", new NumericAttribute("name", "Image"));
		attributes.put("description", new NumericAttribute("description", "Image"));
		attributes.put("fileName", new NumericAttribute("fileName", "Image"));
		attributes.put("width", new NumericAttribute("width", "Image"));
		attributes.put("height", new NumericAttribute("height", "Image"));
		attributes.put("mimeType", new NumericAttribute("mimeType", "Image"));
		attributes.put("thumbnailFileName", new NumericAttribute("thumbnailFileName", "Image"));
		attributes.put("thumbnailmimeType", new NumericAttribute("thumbnailmimeType", "Image"));
	}
	
	private int id;
	private String name;
	private String description;
	private String fileName;
	private int width;
	private int height;
	private String mimeType;
	private String thumbnailFileName;
	private String thumbnailMimeType;
	private Set regions; 
	private Set ports;
	private Set people;
	
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
		return thumbnailMimeType;
	}

	public void setThumbnailMimeType(String thumbnailmimeType)
	{
		this.thumbnailMimeType = thumbnailmimeType;
	}
	
	public Set getPorts()
	{
		return ports;
	}

	public void setPorts(Set ports)
	{
		this.ports = ports;
	}

	public Set getRegions()
	{
		return regions;
	}

	public void setRegions(Set regions)
	{
		this.regions = regions;
	}
	
	public Set getPeople()
	{
		return people;
	}

	public void setPeople(Set people)
	{
		this.people = people;
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
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		List list = getImagesList();
		transaction.commit();
		sess.close();
		return list;
	}

	public static List getImagesList(Session sess)
	{
//		return sess.createCriteria(Image.class).addOrder(Order.asc("name")).list();
		QueryValue query = new QueryValue("Image");
		return query.executeQueryList(sess);
	}

	public static Image loadById(int imageId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		Image image = loadById(imageId, sess);
		transaction.commit();
		sess.close();
		return image;
	}

	public static Image loadById(int imageId, Session sess)
	{
//		List list = sess.createCriteria(Image.class).add(Restrictions.eq("id", new Integer(imageId))).list();
//		if (list == null || list.size() == 0) return null;
//		return (Image) list.get(0);
		
		Conditions conditions = new Conditions();
		conditions.addCondition(Image.getAttribute("id"), new Integer(imageId), Conditions.OP_EQUALS);		
		QueryValue query = new QueryValue("Image", conditions);
		List list = query.executeQueryList(sess);
		if (list == null || list.size() == 0) return null;
		return (Image) list.get(0);		
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

}
	
