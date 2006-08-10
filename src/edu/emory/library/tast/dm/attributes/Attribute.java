package edu.emory.library.tast.dm.attributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.crimson.tree.XmlDocument;
import org.apache.crimson.tree.XmlDocumentBuilder;
import org.apache.jasper.xmlparser.TreeNode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sun.misc.Resource;
import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import edu.emory.library.tast.util.HibernateUtil;

public class Attribute extends AbstractAttribute {

	private static HashMap config = new HashMap();

	private static final long serialVersionUID = -8780232223504322861L;

	public final static int IMPORT_TYPE_IGNORE = -1;

	public final static int IMPORT_TYPE_NUMERIC = 0;

	public final static int IMPORT_TYPE_STRING = 1;

	public final static int IMPORT_TYPE_DATE = 2;

	private Integer importType;

	private String importName;

	private String importDateDay;

	private String importDateMonth;

	private String importDateYear;

	public Attribute() {
	}

	public Attribute(String name, Integer type, String dictionary, Integer importType, String importName,
			String importDateDay, String importDateMonth, String importDateYear, String userLabel, Integer length,
			ObjectType objType) {
		super(name, type, dictionary, userLabel, objType, length);
		this.importType = importType;
		this.importName = importName;
		this.importDateDay = importDateDay;
		this.importDateMonth = importDateMonth;
		this.importDateYear = importDateYear;
	}

	public static AbstractAttribute loadById(Long id) {
		Session session = HibernateUtil.getSession();
		AbstractAttribute attr = loadById(id, session);
		session.close();
		return attr;
	}

	public static AbstractAttribute loadById(Long id, Session session) {
		Criteria crit = session.createCriteria(Attribute.class);
		crit.add(Expression.eq("id", id));
		crit.setMaxResults(1);
		List list = crit.list();
		if (list == null || list.size() == 0)
			return null;
		return (AbstractAttribute) list.get(0);
	}

	public static Attribute newForVoyages() {
		Attribute attribute = new Attribute();
		attribute.setObjectType(ObjectType.getVoyages());
		return attribute;
	}

	public static Attribute newForVoyages(Session session) {
		Attribute attribute = new Attribute();
		attribute.setObjectType(ObjectType.getVoyages(session));
		return attribute;
	}

	public static Attribute newForSlaves() {
		Attribute attribute = new Attribute();
		attribute.setObjectType(ObjectType.getSlaves());
		return attribute;
	}

	public static Attribute newForSlaves(Session session) {
		Attribute attribute = new Attribute();
		attribute.setObjectType(ObjectType.getSlaves(session));
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

}