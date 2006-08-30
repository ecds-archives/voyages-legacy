package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class Image
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new StringAttribute("id", "Image"));
		attributes.put("name", new NumericAttribute("name", "Image"));
		attributes.put("description", new NumericAttribute("description", "Image"));
		attributes.put("fileName", new NumericAttribute("fileName", "Image"));
		attributes.put("width", new NumericAttribute("width", "Image"));
		attributes.put("height", new NumericAttribute("height", "Image"));
		attributes.put("mimeType", new NumericAttribute("mimeType", "Image"));
		attributes.put("people", new DictionaryAttribute("people", "Image"));
		attributes.put("regions", new DictionaryAttribute("regions", "Image"));
		attributes.put("ports", new DictionaryAttribute("ports", "Image"));
	}
	
	private int id;
	private String name;
	private String description;
	private String fileName;
	private String mimeType;
	private Set regions; 
	private Set ports;
	private Set people;
	private int width;
	private int height;
	
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
	
	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
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
		List list = getImagesList(sess);
		transaction.commit();
		sess.close();
		return list;
	}

	public static List getImagesList(Session sess)
	{
		return getImagesList(sess, null);
	}

	public static List getImagesList(Session sess, String searchFor)
	{
//		return sess.createCriteria(Image.class).addOrder(Order.asc("name")).list();

		if (!StringUtils.isNullOrEmpty(searchFor))
		{
			searchFor = "%" + searchFor + "%";
			Conditions conds = new Conditions(Conditions.JOIN_AND);
			conds.addCondition(getAttribute("name"), searchFor, Conditions.OP_LIKE);
			QueryValue query = new QueryValue("Image", conds);
			return query.executeQueryList(sess);
		}
		else
		{
			QueryValue query = new QueryValue("Image");
			return query.executeQueryList(sess);
		}
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
		
		Attribute[] orderBy = { 
			new SequenceAttribute(new Attribute[] {getAttribute("people"), Person.getAttribute("lastName")}),
			new SequenceAttribute(new Attribute[] {getAttribute("regions"), Region.getAttribute("name")}),
			new SequenceAttribute(new Attribute[] {getAttribute("ports"), Port.getAttribute("name")})};
		
		Conditions conditions = new Conditions();
		conditions.addCondition(Image.getAttribute("id"), new Integer(imageId), Conditions.OP_EQUALS);
		QueryValue query = new QueryValue(new String[] {"Image"}, new String[] {"i"}, conditions);
//		query.setOrderBy(orderBy);
//		query.setOrder(QueryValue.ORDER_ASC);
		List list = query.executeQueryList(sess);
		if (list == null || list.size() == 0) return null;
		return (Image) list.get(0);		
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

}
	