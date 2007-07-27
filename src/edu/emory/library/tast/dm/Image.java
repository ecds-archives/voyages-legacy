package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.Languages;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.CategoryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.PortAttribute;
import edu.emory.library.tast.dm.attributes.RegionAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class Image implements Comparable
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new StringAttribute("id", "Image"));
		attributes.put("title", new StringAttribute("title", "Image"));
		attributes.put("description", new StringAttribute("description", "Image"));
		attributes.put("fileName", new StringAttribute("fileName", "Image"));
		attributes.put("width", new NumericAttribute("width", "Image", NumericAttribute.TYPE_INTEGER));
		attributes.put("height", new NumericAttribute("height", "Image", NumericAttribute.TYPE_INTEGER));
		attributes.put("mimeType", new StringAttribute("mimeType", "Image"));
		attributes.put("regions", new RegionAttribute("regions", "Image"));
		attributes.put("ports", new PortAttribute("ports", "Image"));
		attributes.put("voyageid", new NumericAttribute("voyageid", "Image", NumericAttribute.TYPE_INTEGER));
		attributes.put("order", new NumericAttribute("order", "Image", NumericAttribute.TYPE_INTEGER));
		attributes.put("category", new CategoryAttribute("category", "Image"));
		attributes.put("date", new NumericAttribute("date", "Image", NumericAttribute.TYPE_INTEGER));
		attributes.put("ready", new BooleanAttribute("readyToGo", "Image"));
	}
	
	private int id;
	private int width;
	private int height;
	private int size;
	private String fileName;
	private String mimeType;
	private int order;

	private String title;
	private String description;
	private String creator;
	private String source;
	private int date;
	private String language;
	
	private String references;
	private String comments;
	private boolean emory = false;
	private String emoryLocation;
	private int imageStatus = 0;
	private int authorizationStatus = 0;
	private boolean readyToGo = false;
	private Set voyageIds = new HashSet();
	private ImageCategory category;

	private Set regions = new HashSet(); 
	private Set ports = new HashSet();
	
	public int getDate()
	{
		return date;
	}

	public void setDate(int date)
	{
		this.date = date;
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

	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
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
	
	public static List getImagesByVoyageId(Session sess, int voyageId)
	{

		String hqlImages =
				"from Image " +
				"where " + voyageId + " = some elements(voyageIds) " +
				"order by date asc, id asc";

		return sess.createQuery(hqlImages).list();

	}

	public static Image loadById(int imageId, Session sess)
	{
		List list = sess.createCriteria(Image.class).add(Restrictions.eq("id", new Integer(imageId))).list();
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

	public ImageCategory getCategory() {
		return category;
	}

	public void setCategory(ImageCategory category) {
		this.category = category;
	}

	public Set getVoyageIds()
	{
		return voyageIds;
	}

	public void setVoyageIds(Set voyageids)
	{
		this.voyageIds = voyageids;
	}
	
	public static void main(String[] args)
	{
		
		Integer testImageId = new Integer(711844);
		Integer testVoyageId = new Integer(666);
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		List images = sess.createCriteria(Image.class).add(Restrictions.eq("id", testImageId)).list();
		Image image = (Image) images.get(0);

		List voyages = sess.createCriteria(Voyage.class).
			add(Restrictions.in("voyageid", image.getVoyageIds())).
			add(Restrictions.eq("revision", new Integer(Voyage.getCurrentRevision()))).list();

		System.out.println("Image ID = " + image.getId());

		System.out.println("Voyage IDs in image:");
		for (Iterator iter = image.getVoyageIds().iterator(); iter.hasNext();)
		{
			Integer vid = (Integer) iter.next();
			System.out.println(" * " + vid);
		}
		
		System.out.println("Actual voyages:");
		for (Iterator iter = voyages.iterator(); iter.hasNext();)
		{
			Voyage v = (Voyage) iter.next();
			System.out.println(" * " + v.getVoyageid());
		}
		
//		String hqlImages = "from Image i where " + testVoyageId + " = some elements(i.voyageIds)";
//		List imagesByVoyageId = sess.createQuery(hqlImages).list();
		
//		List imagesByVoyageId = Image.getImagesByVoyageId(sess, testVoyageId);

//		String hqlImages = "from Image i join i.voyageIds as voyageId where voyageId = " + testVoyageId;
//		List imagesByVoyageId = sess.createQuery(hqlImages).list();

//		does not work: see http://www.hibernate.org/117.html
//		List imagesByVoyageId =
//			sess.createCriteria(Image.class).
//			createCriteria("voyageIds", "voyageId").
//			add(Restrictions.eq("voyageId", testVoyageId)).
//			list();

		String hqlImages =
			"from Image where " +
			"(upper(title) like upper(:title0)) and " +
			"(upper(description) like upper(:description0) and upper(description) like upper(:description1)) and " +
			"category.id = :categoryId and " +
			":dateFrom <= date and " +
			":dateTo >= date and " +
			":voyageId = some elements(voyageIds)";

		Query q = sess.createQuery(hqlImages);
		q.setParameter("title0", "%Brig%");
		q.setParameter("description0", "%American%");
		q.setParameter("description1", "%vessel%");
		q.setParameter("categoryId", new Long(1));
		q.setParameter("dateFrom", new Integer(1800));
		q.setParameter("dateTo", new Integer(1900));
		q.setParameter("voyageId", new Integer(666));
		List imagesByVoyageId = q.list();
		
		System.out.println("Images with linked to voyage ID = " + testVoyageId);
		for (Iterator iter = imagesByVoyageId.iterator(); iter.hasNext();)
		{
			Image i = (Image) iter.next();
			System.out.println(" * " + i.getTitle() + " (" + i.getDate() + ")");
		}

		transaction.commit();
		sess.close();

	}

	public int compareTo(Object obj)
	{
		Image that = (Image) obj;
		if (this.date < that.date)
		{
			return -1;
		}
		else if (this.date > that.date)
		{
			return 1;
		}
		else
		{
			if (this.id < that.id)
			{
				return -1;
			}
			else if (this.id > that.id)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
	
	public boolean equals(Object obj)
	{
		Image that = (Image) obj;
		if (that.id == 0) return false;
		if (this.id == 0) return false;
		return that.id == this.id;
	}

}