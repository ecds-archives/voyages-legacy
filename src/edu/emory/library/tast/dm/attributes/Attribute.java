package edu.emory.library.tast.dm.attributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.jasper.xmlparser.TreeNode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;
import edu.emory.library.tast.util.HibernateUtil;

public class Attribute  {

	private static HashMap config = new HashMap();

	private static final long serialVersionUID = -8780232223504322861L;

	public final static int TYPE_INTEGER = 0;

	public final static int TYPE_LONG = 1;

	public final static int TYPE_FLOAT = 5;

	public final static int TYPE_STRING = 2;

	public final static int TYPE_DATE = 3;

	public final static int TYPE_DICT = 4;

	public final static int IMPORT_TYPE_IGNORE = -1;

	public final static int IMPORT_TYPE_NUMERIC = 0;

	public final static int IMPORT_TYPE_STRING = 1;

	public final static int IMPORT_TYPE_DATE = 2;

	private String name;

	private Long id;

	private String userLabel;

	private Integer type;

	private String dictionary;

	private String description;

	private Boolean visible;

	private Integer category;

	private Integer length = new Integer(-1);

	private Integer importType;

	private String importName;

	private String importDateDay;

	private String importDateMonth;

	private String importDateYear;

	public Attribute() {
	}

	public Attribute(String name, Integer type, String dictionary, Integer importType, String importName,
			String importDateDay, String importDateMonth, String importDateYear, String userLabel, Integer length) {
		this.name = name;
		this.type = type;
		this.length = length;
		this.dictionary = dictionary;
		this.userLabel = userLabel;
		this.importType = importType;
		this.importName = importName;
		this.importDateDay = importDateDay;
		this.importDateMonth = importDateMonth;
		this.importDateYear = importDateYear;
	}

	public static Attribute loadById(Long id) {
		Session session = HibernateUtil.getSession();
		Attribute attr = loadById(id, session);
		session.close();
		return attr;
	}

	public static Attribute loadById(Long id, Session session) {
		Criteria crit = session.createCriteria(Attribute.class);
		crit.add(Expression.eq("id", id));
		crit.setMaxResults(1);
		List list = crit.list();
		if (list == null || list.size() == 0)
			return null;
		return (Attribute) list.get(0);
	}

	public static Attribute newForVoyages() {
		Attribute attribute = new Attribute();
		return attribute;
	}

	public static Attribute newForVoyages(Session session) {
		Attribute attribute = new Attribute();
		return attribute;
	}

	public static Attribute newForSlaves() {
		Attribute attribute = new Attribute();
		return attribute;
	}

	public static Attribute newForSlaves(Session session) {
		Attribute attribute = new Attribute();
		return attribute;
	}

	public String getImportDateDay() {
		return importDateDay;
	}

	public String getImportDateMonth() {
		return importDateMonth;
	}

	public String getImportDateYear() {
		return importDateYear;
	}

	public String getImportName() {
		if (importName == null)
			return getName();
		return importName;
	}

	public Integer getImportType() {
		return importType;
	}

	public void setImportDateDay(String importDateDay) {
		this.importDateDay = importDateDay;
	}

	public void setImportDateMonth(String importDateMonth) {
		this.importDateMonth = importDateMonth;
	}

	public void setImportDateYear(String importDateYear) {
		this.importDateYear = importDateYear;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public void setImportType(Integer importType) {
		this.importType = importType;
	}

	public List loadContainingCompoundAttributes(Session session) {
		Query query = session.createQuery("from CompoundAttribute a where :attr = some elements(a.attributes)");
		query.setParameter("attr", this);
		return query.list();
	}

	public List loadContainingCompoundAttributes() {
		Session session = HibernateUtil.getSession();
		List groups = loadContainingCompoundAttributes(session);
		session.close();
		return groups;
	}

	public String encodeToString() {
		return "Attribute_" + this.getId();
	}

	public TreeNode toXML() {
		TreeNode node = new TreeNode("attribute");
		node.addAttribute("id", this.getId() + "");
		if (this.getName() != null) {
			node.addAttribute("name", this.getName());
		}
		node.addAttribute("category", this.getCategory() + "");
		if (this.getDescription() != null) {
			node.addAttribute("desc", this.getDescription());
		}
		if (this.getDictionary() != null) {
			node.addAttribute("dictionary", this.getDictionary());
		}
		node.addAttribute("length", this.getLength() + "");
		node.addAttribute("type", this.getType() + "");
		if (this.getUserLabel() != null) {
			node.addAttribute("userLabel", this.getUserLabel());
		}
		node.addAttribute("visible", this.getVisible() + "");
		node.addAttribute("importType", this.importType + "");
		if (this.getImportName() != null) {
			node.addAttribute("importName", this.importName);
		}
		if (this.getImportDateDay() != null) {
			node.addAttribute("importDateDay", this.importDateDay);
		}
		if (this.getImportDateMonth() != null) {
			node.addAttribute("importDateMonth", this.importDateMonth);
		}
		if (this.getImportDateYear() != null) {
			node.addAttribute("importDateYear", this.importDateYear);
		}
		return node;
	}

	public static Attribute[] loadAttributesForType(String type) {
		if (config.isEmpty()) {
			try {
				Document document = new XMLConfiguration("attributes.xml").getDocument(); 
				Node mainNode = document.getFirstChild();
				if (mainNode != null) {
					NodeList objects = mainNode.getChildNodes();
					for (int i = 0; i < objects.getLength(); i++) {
						Node node = objects.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							NodeList attrs = node.getChildNodes();
							List attrsList = new ArrayList();
							for (int j = 0; j < attrs.getLength(); j++) {
								if (attrs.item(j).getNodeType() == Node.ELEMENT_NODE) {
									attrsList.add(Attribute.fromXML(attrs.item(j)));
								}
							}
							config.put(node.getAttributes().getNamedItem("objectType").getNodeValue(), attrsList.toArray(new Attribute[] {}));
						}
					}
				}
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (Attribute[]) config.get(type);
	}

	public static Attribute fromXML(Node xmlNode) {

		Attribute attr = new Attribute();
		String attribute;

		Node namedItem = xmlNode.getAttributes().getNamedItem("id");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setId(new Long(attribute));
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("name");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setName(attribute);
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("category");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setCategory(new Integer(attribute));
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("desc");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setDescription(attribute);
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("dictionary");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setDictionary(attribute);
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("length");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setLength(new Integer(attribute));
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("type");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setType(new Integer(attribute));
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("userLabel");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setUserLabel(attribute);
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("visible");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setVisible(new Boolean(attribute));
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("importType");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setImportType(new Integer(attribute));
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("importName");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setImportName(attribute);
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("importDateDay");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setImportDateDay(attribute);
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("importDateMonth");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setImportDateMonth(attribute);
			}
		}

		namedItem = xmlNode.getAttributes().getNamedItem("importDateYear");
		if (namedItem != null) {
			attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				attr.setImportDateYear(attribute);
			}
		}

		return attr;
	}


	public Object parse(String value, int options) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return parse(new String[] { value }, options);
	}

	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return parse(new String[] { value }, 0);
	}

	public Object parse(String[] values) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return parse(values, 0);
	}

	public Object parse(String[] values, int options) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{

		String value;
		switch (getType().intValue()) {
		case TYPE_STRING:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			if (length.intValue() != -1 && value.length() > length.intValue())
				throw new StringTooLongException();

			return value;

		case TYPE_INTEGER:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			try {
				return new Integer(values[0]);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_LONG:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			try {
				return new Long(values[0]);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_FLOAT:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			try {
				return new Float(values[0]);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_DATE:

			boolean separate = values.length == 3 && values[0] != null && values[1] != null && values[2] != null;
			boolean single = values.length == 1 && values[0] != null;

			if (!(separate || single))
				throw new InvalidNumberOfValuesException();

			if (separate) {

				String day = values[0].trim();
				String month = values[1].trim();
				String year = values[2].trim();

				if (day.length() == 0 || month.length() == 0
						|| year.length() == 0)
					return null;

				try {
					Calendar cal = Calendar.getInstance();
					cal.clear();
					cal.set(Integer.parseInt(year),
							Integer.parseInt(month) - 1, Integer.parseInt(day));
					// Timestamp tstamp = new Timestamp(Integer.parseInt(year),
					// Integer.parseInt(month),
					// Integer.parseInt(day),
					// 0,0,0,0);
					return cal.getTime();
				} catch (NumberFormatException nfe) {
					throw new InvalidDateException();
				}

			} else if (single) {

				value = values[0].trim();

				if (value.length() == 0)
					return null;

				try {
					DateFormat dateFormat = DateFormat.getDateInstance();
					return dateFormat.parse(value);
				} catch (ParseException e) {
					throw new InvalidDateException();
				}
			}

		case TYPE_DICT:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			Integer remoteId = null;
			try {
				remoteId = new Integer(value);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

			Dictionary dicts[] = Dictionary.loadDictionaryByRemoteId(getDictionary(),
					remoteId);
			if (dicts.length > 0) {
				return dicts[0];
			} else {
				Dictionary dict = Dictionary.createNew(getDictionary());
				dict.setRemoteId(remoteId);
				dict.setName(remoteId.toString());
				dict.save();
				return dict;
			}

		default:
			return null;

		}

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public String getName() {
		return name;
	}

	public void setName(String attrName) {
		this.name = attrName;
	}

	public String getUserLabelOrName()
	{
		if (userLabel != null && userLabel.length() > 0) return userLabel;
		if (name != null && name.length() > 0) return name;
		return "";
	}

	public Integer getCategory()
	{
		return category;
	}

	public void setCategory(Integer category)
	{
		this.category = category;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		if (type == null) {
			type = new Integer(-1);
		} else {
			this.type = type;
		}
	}

	public String getTypeDisplayName() {
		if (type == null)
			return "";
		switch (type.intValue()) {
		case TYPE_INTEGER:
			return "Integer";
		case TYPE_LONG:
			return "Integer";
		case TYPE_FLOAT:
			return "Decimal";
		case TYPE_STRING:
			return "Text";
		case TYPE_DATE:
			return "Date";
		case TYPE_DICT:
			return "List " + dictionary;
		default:
			return "";
		}
	}
	
	public String getDictionary() {
		return dictionary;
	}

	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}

	public boolean isDictinaory() {
		return dictionary != null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		if (length != null)
			this.length = length;
	}

	public Boolean getVisible()
	{
		return visible;
	}

	public void setVisible(Boolean visible)
	{
		this.visible = visible;
	}
	
	public boolean isVisible()
	{
		return
			visible != null &&
			visible.booleanValue();
	}
	
	public boolean isVisibleByCategory(int category)
	{
		return
			isVisible() &&
			this.category != null &&
			this.category.intValue() <= category;
	}

	public String toString() {
		if (this.userLabel != null && !this.userLabel.equals("")) {
			return this.userLabel;
		} else {
			return this.name;
		}
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Attribute))
			return false;
		Attribute theOther = (Attribute) obj;
		return (id == null && theOther.getId() == null)
				|| (id != null && id.equals(theOther.getId()));
	}

	public int hashCode() {
		if (id == null)
			return super.hashCode();
		return id.hashCode();
	}

}