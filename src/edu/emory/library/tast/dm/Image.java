package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.Languages;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.PortAttribute;
import edu.emory.library.tast.dm.attributes.RegionAttribute;
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
		attributes.put("title", new NumericAttribute("title", "Image"));
		attributes.put("description", new NumericAttribute("description", "Image"));
		attributes.put("fileName", new NumericAttribute("fileName", "Image"));
		attributes.put("width", new NumericAttribute("width", "Image"));
		attributes.put("height", new NumericAttribute("height", "Image"));
		attributes.put("mimeType", new NumericAttribute("mimeType", "Image"));
		attributes.put("people", new DictionaryAttribute("people", "Image"));
		attributes.put("regions", new RegionAttribute("regions", "Image"));
		attributes.put("ports", new PortAttribute("ports", "Image"));
	}
	
	private int id;
	private int width;
	private int height;
	private int size;
	private String fileName;
	private String mimeType;

	private String title;
	private String description;
	private String creator;
	private String source;
	private String date;
	private String language;
	
	private String references;
	private String comments;
	private boolean emory = false;
	private String emoryLocation;
	private int imageStatus = 0;
	private int authorizationStatus = 0;
	private boolean readyToGo = false;

	private Set regions; 
	private Set ports;
	private Set people;
	
	public String getDate()
	{
		return date;
	}

	public void setDate(String dateCreated)
	{
		this.date = dateCreated;
	}

	public String getLanguage()
	{
		return language;
	}

	public String getLanguageName()
	{
		return Languages.getNameByCode(language);
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String source)
	{
		this.creator = source;
	}

	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public String getReferences()
	{
		return references;
	}

	public void setReferences(String references)
	{
		this.references = references;
	}
	
	public boolean isEmory()
	{
		return emory;
	}

	public void setEmory(boolean emory)
	{
		this.emory = emory;
	}

	public String getEmoryLocation()
	{
		return emoryLocation;
	}

	public void setEmoryLocation(String emoryLocation)
	{
		this.emoryLocation = emoryLocation;
	}

	public int getImageStatus()
	{
		return imageStatus;
	}

	public void setImageStatus(int imageStatus)
	{
		this.imageStatus = imageStatus;
	}

	public int getAuthorizationStatus()
	{
		return authorizationStatus;
	}

	public void setAuthorizationStatus(int authorizationStatus)
	{
		this.authorizationStatus = authorizationStatus;
	}

	public boolean isReadyToGo()
	{
		return readyToGo;
	}

	public void setReadyToGo(boolean readyToGo)
	{
		this.readyToGo = readyToGo;
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
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
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

		QueryValue query = null;
		if (!StringUtils.isNullOrEmpty(searchFor))
		{
			searchFor = "%" + searchFor + "%";
			Conditions conds = new Conditions(Conditions.JOIN_AND);
			conds.addCondition(getAttribute("title"), searchFor, Conditions.OP_LIKE);
			query = new QueryValue("Image", conds);
		}
		else
		{
			query = new QueryValue("Image");
		}
		
		query.setOrderBy(new Attribute[] {getAttribute("title")});
		query.setOrder(QueryValue.ORDER_ASC);
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
	
	public static boolean checkDate(String date)
	{
		
		date = date.trim();
		
		Pattern regEx = Pattern.compile("(\\d{4})(?:-(\\d{1,2})(?:-(\\d{1,2}))?)?");
		Matcher matcher = regEx.matcher(date);
		if (!matcher.matches()) return false;
		
		String monthStr = matcher.group(2); 
		if (monthStr != null)
		{
			int month = Integer.parseInt(monthStr);
			if (month <= 0 || 12 < month) return false;
			String dayString = matcher.group(3); 
			if (dayString != null)
			{
				int day = Integer.parseInt(dayString);
				if (day <= 0 || 31 < day) return false;
			}
		}

		return true;
		
	}

//	public static void main(String[] args)
//	{
//		
//		Image.checkDate("1234");
//		
//	}

}
	