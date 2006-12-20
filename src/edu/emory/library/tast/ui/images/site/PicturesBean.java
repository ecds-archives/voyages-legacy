package edu.emory.library.tast.ui.images.site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.Person;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.ui.search.query.QueryBuilderQuery;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionList;
import edu.emory.library.tast.ui.search.query.QueryConditionNumeric;
import edu.emory.library.tast.ui.search.query.QueryConditionRange;
import edu.emory.library.tast.ui.search.query.QueryConditionText;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.ui.search.query.SearchParameters;
import edu.emory.library.tast.ui.search.stat.ComparableSelectItem;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

public class PicturesBean {

	public static final String PERSON_ENCODE = "person_";

	public static final String PORT_ENCODE = "port_";

	public static final String REGION_ENCODE = "region_";

	public static final String VOYAGE_ENCODE = "voyage_";

	private static Map galleryUserLabels = new HashMap();
	
	static {
		galleryUserLabels.put("people", "people");
		galleryUserLabels.put("regions", "regions");
		galleryUserLabels.put("ports", "ports");
		galleryUserLabels.put("ships", "ships");
	}
	
	private PictureGalery pictureGalery;

	private SearchBean searchBean;
	
	private String visibleImage;

	private String lastGalleryName;

	private Object object;

	private Object id;

	public PicturesBean() {

//		Session session = HibernateUtil.getSession();
//		Transaction transaction = session.beginTransaction();
//
//		Image[] images = (Image[]) Image.getImagesList(session).toArray(
//				new Image[] {});
//		GaleryImage[] galeryImage = new GaleryImage[images.length];
//		for (int i = 0; i < images.length; i++) {
//			Image image = images[i];
//			galeryImage[i] = getGalleryImage(image);
//		}
//
//		this.pictureGalery = new PictureGalery(galeryImage);
//
//		transaction.commit();
//		session.close();
		this.pictureGalery = new PictureGalery(new GaleryImage[] {});

	}

	private GaleryImage getGalleryImage(Image image) {
		GaleryImage galeryImage = null;		
		Person[] persons = (Person[]) image.getPeople().toArray(
				new Person[] {});
		Port[] ports = (Port[]) image.getPorts().toArray(new Port[] {});
		Region[] regions = (Region[]) image.getRegions().toArray(
				new Region[] {});
		galeryImage = new GaleryImage();
		galeryImage.setImage(image);
		galeryImage.setPeople(persons);
		galeryImage.setPorts(ports);
		galeryImage.setRegions(regions);
		return galeryImage;
	}

	public PictureGalery getPictureGalery() {
		String object = this.getURLParam("obj");
		String id = this.getURLParam("id");
		if (object != null && id != null && (!object.equals(this.object) || !id.equals(this.id))) {
			this.id = id;
			this.object = object;
			Conditions conditions = new Conditions();
			QueryValue qValue = new QueryValue(new String[] {"Image"}, new String[] {"i"}, conditions);
			if (object.equals("people")) {
				conditions.addCondition(new DirectValueAttribute(id),
						new SequenceAttribute(new Attribute[] {Image.getAttribute("people"), Person.getAttribute("id")}), 
					Conditions.OP_IN);
			} else if (object.equals("ports")) {
				conditions.addCondition(new DirectValueAttribute(id),
						new SequenceAttribute(new Attribute[] {Image.getAttribute("ports"), Person.getAttribute("id")}), 
					Conditions.OP_IN);
			} else if (object.equals("regions")) {
				conditions.addCondition(new DirectValueAttribute(id),
						new SequenceAttribute(new Attribute[] {Image.getAttribute("regions"), Person.getAttribute("id")}), 
					Conditions.OP_IN);
			} else if (object.equals("ships")) {
				
			}
			System.out.println(qValue.toStringWithParams().conditionString);
			Session session = HibernateUtil.getSession();
			Transaction transaction = session.beginTransaction();
			Object[] ret = qValue.executeQuery(session);
			GaleryImage[] gallImages = new GaleryImage[ret.length];
			for (int i = 0; i < ret.length; i++) {
				Image img = (Image)ret[i];
				gallImages[i] = this.getGalleryImage(img);
			}
			this.pictureGalery = new PictureGalery(gallImages);
			transaction.commit();
			session.close();
		} 
		
		if (object == null || id == null) {
			this.pictureGalery = new PictureGalery(new GaleryImage[] {});
		}
		
		return pictureGalery;
	}

	public void setPictureGalery(PictureGalery pictures) {
		this.pictureGalery = pictures;
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}
	
	public String getLastGalleryName() {
		if (this.lastGalleryName == null) {
			return "people";
		}
		return this.lastGalleryName;
	}

	public SelectItem[] getCurrentObjects() {
		
		ArrayList items = new ArrayList();
		
		String gallery = this.getURLParam("gal");
		System.out.println("Gallery is: " + gallery);
		this.lastGalleryName = gallery;
		if ("people".equals(gallery)) {
			
			Session session = HibernateUtil.getSession();
			Transaction transaction = session.beginTransaction();
			Object[] result = getImagesWith(Image.getAttribute("people"), session);
			for (int i = 0; i < result.length; i++) {
				Image image = (Image)result[i];
				Set persons = image.getPeople();
				Iterator iter = persons.iterator();
				while (iter.hasNext()) {
					Person p = (Person) iter.next();
					SelectItem item = new ComparableSelectItem(String.valueOf(p.getId()), p.getLastName());
					if (!items.contains(item)) {
						items.add(item);
					}
				}
			}
			
			transaction.commit();
			session.close();
			
		} else if ("ports".equals(gallery)) {
			
			Session session = HibernateUtil.getSession();
			Transaction transaction = session.beginTransaction();
			Object[] result = getImagesWith(Image.getAttribute("ports"), session);
			for (int i = 0; i < result.length; i++) {
				Image image = (Image)result[i];
				Set ports = image.getPorts();
				Iterator iter = ports.iterator();
				while (iter.hasNext()) {
					Port p = (Port) iter.next();
					SelectItem item = new ComparableSelectItem(String.valueOf(p.getId()), p.getName());
					if (!items.contains(item)) {
						items.add(item);
					}
				}
			}
			
			transaction.commit();
			session.close();
			
		} else if ("regions".equals(gallery)) {
			
			Session session = HibernateUtil.getSession();
			Transaction transaction = session.beginTransaction();
			Object[] result = getImagesWith(Image.getAttribute("regions"), session);
			for (int i = 0; i < result.length; i++) {
				Image image = (Image)result[i];
				Set regions = image.getRegions();
				Iterator iter = regions.iterator();
				while (iter.hasNext()) {
					Region p = (Region) iter.next();
					SelectItem item = new ComparableSelectItem(String.valueOf(p.getId()), p.getName());
					if (!items.contains(item)) {
						items.add(item);
					}
				}
			}
			
			transaction.commit();
			session.close();
			
		} else if ("ships".equals(gallery)) {
		} else {
			Session session = HibernateUtil.getSession();
			Transaction transaction = session.beginTransaction();
			Object[] result = getImagesWith(Image.getAttribute("people"), session);
			for (int i = 0; i < result.length; i++) {
				Image image = (Image)result[i];
				Set persons = image.getPeople();
				Iterator iter = persons.iterator();
				while (iter.hasNext()) {
					Person p = (Person) iter.next();
					SelectItem item = new ComparableSelectItem(String.valueOf(p.getId()), p.getLastName());
					if (!items.contains(item)) {
						items.add(item);
					}
				}
			}
			
			transaction.commit();
			session.close();
		}
		
		Collections.sort(items);
		return (SelectItem[])items.toArray(new SelectItem[] {});
	}

	private Object[] getImagesWith(Attribute attr, Session session) {
		Conditions conditions = new Conditions();
		conditions.addCondition(attr, new DirectValue(new DirectValueAttribute("empty")), Conditions.OP_IS_NOT);
		QueryValue qValue = new QueryValue(new String[] {"Image"}, new String[] {"i"}, conditions);
		Object[] result = qValue.executeQuery(session);
		return result;
	}

	public String getVisibleImage() {
		return visibleImage;
	}

	public void setVisibleImage(String visibleImage) {
		this.visibleImage = visibleImage;
	}
	
	public String getGallery() {
		return this.getURLParam("gal");
	}
	
	public String getGalleryUserName() {
		if (this.getURLParam("gal") == null) {
			return (String)galleryUserLabels.get("people");
		}
		return (String)galleryUserLabels.get(this.getURLParam("gal"));
	}
	
	private String getURLParam(String paramName) {
		if (((ServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameterMap() == null) {
			return null;
		}
		return ((ServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest())
					.getParameter(paramName);
		
	}
	
	public String getCurrentPath() {
		
		String object = this.getURLParam("obj");
		String id = this.getURLParam("id");
		Conditions conditions = new Conditions();
		QueryValue qValue = null;
		if (object == null || id == null) {
			return "Invalid parameters!";
		}
		if (object.equals("people")) {
			qValue = new QueryValue(new String[] {"Person"}, new String[] {"p"}, conditions);
			conditions.addCondition(Person.getAttribute("id"), new Integer(id), Conditions.OP_EQUALS);
		} else if (object.equals("ports")) {
			qValue = new QueryValue(new String[] {"Port"}, new String[] {"p"}, conditions);
			conditions.addCondition(Person.getAttribute("id"), new Long(id), Conditions.OP_EQUALS);
		} else if (object.equals("regions")) {
			qValue = new QueryValue(new String[] {"Region"}, new String[] {"p"}, conditions);
			conditions.addCondition(Person.getAttribute("id"), new Long(id), Conditions.OP_EQUALS);
		} else if (object.equals("ships")) {
			
		} else {
			return "Invalid parameters!";
		}
		Object[] ret = qValue.executeQuery();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("Images / ").append(galleryUserLabels.get(object)).append(" / ");
		if (ret.length != 0) {
			buffer.append(ret[0].toString());
		} else {
			buffer.append("unknown");
		}
		return buffer.toString();
	}
}
