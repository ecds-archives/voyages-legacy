package edu.emory.library.tast.database.tabscommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.database.query.searchables.UserCategories;
import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.StringUtils;

/**
 * Attribute used in table.
 * 
 */
public class VisibleAttribute implements VisibleAttributeInterface
{

	private static Map visibleAttributes = new HashMap();

	private Attribute[] attributes;

	private String name;

	private UserCategories userCategory;

	private String userLabel;

	private boolean date;

	private String format;

	public static VisibleAttributeInterface[] loadVisibleAttributes()
	{
		if (visibleAttributes.isEmpty()) loadConfig();
		return (VisibleAttributeInterface[]) visibleAttributes.values().toArray(new VisibleAttributeInterface[] {});
	}

	private static void loadConfig()
	{
		try
		{
			Document document = new XMLConfiguration("table-attributes.xml").getDocument();
			Node mainNode = document.getFirstChild();
			if (mainNode != null)
			{
				if (mainNode.getNodeType() == Node.ELEMENT_NODE)
				{
					NodeList attrs = mainNode.getChildNodes();
					for (int j = 0; j < attrs.getLength(); j++)
					{
						if (attrs.item(j).getNodeType() == Node.ELEMENT_NODE)
						{
							VisibleAttribute attr = VisibleAttribute.fromXML(attrs.item(j));
							visibleAttributes.put(attr.getName(), attr);
						}
					}
				}
			}
		}
		catch (ConfigurationException e)
		{
			e.printStackTrace();
		}
	}

	public static VisibleAttributeInterface[] getAllAttributes()
	{
		return (VisibleAttributeInterface[]) ((Map) visibleAttributes.get(new Integer(1))).values().toArray(new VisibleAttributeInterface[] {});
	}

	private static VisibleAttribute fromXML(Node node)
	{

		String name = node.getAttributes().getNamedItem("id").getNodeValue();
		String category = node.getAttributes().getNamedItem("userCategory").getNodeValue();
		String userLabel = node.getAttributes().getNamedItem("userLabel").getNodeValue();
		String format = null;
		if (node.getAttributes().getNamedItem("format") != null)
		{
			format = node.getAttributes().getNamedItem("format").getNodeValue();
		}

		boolean date = false;
		if (node.getAttributes().getNamedItem("date") != null)
		{
			date = "true".equals(node.getAttributes().getNamedItem("date").getNodeValue());
		}

		List attributesList = new ArrayList();
		NodeList attrsList = node.getChildNodes();
		for (int j = 0; j < attrsList.getLength(); j++)
		{
			Node attrsListChild = attrsList.item(j);
			if (attrsListChild != null)
			{
				NodeList attrs = attrsListChild.getChildNodes();
				for (int i = 0; i < attrs.getLength(); i++)
				{
					Node attr = attrs.item(i);
					if (attr.getNodeType() == Node.ELEMENT_NODE)
					{
						String attrName = attr.getAttributes().getNamedItem("name").getNodeValue();
						attributesList.add(Voyage.getAttribute(attrName));
					}
				}
			}
		}
		VisibleAttribute attr = new VisibleAttribute(name, (Attribute[]) attributesList.toArray(new Attribute[] {}));
		String[] cats = category.split(",");
		UserCategories cat = new UserCategories();
		for (int i = 0; i < cats.length; i++)
		{
			cat.addTo(UserCategory.parse(cats[i]));
		}
		attr.setUserCategories(cat);
		attr.setUserLabel(userLabel);
		attr.setDate(date);
		if (!StringUtils.isNullOrEmpty(format))
		{
			attr.setFormat(format);
		}
		return attr;
	}

	private void setFormat(String format)
	{
		this.format = format;
	}

	public VisibleAttribute(String name, Attribute[] attributes)
	{
		this.name = name;
		this.attributes = attributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getAttributes()
	 */
	public Attribute[] getAttributes()
	{
		return attributes;
	}

	public void setAttributes(Attribute[] attributes)
	{
		this.attributes = attributes;
	}

	public void setUserCategories(UserCategories category)
	{
		this.userCategory = category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getUserLabel()
	 */
	public String getUserLabel()
	{
		return userLabel;
	}

	public void setUserLabel(String userLabel)
	{
		this.userLabel = userLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getName()
	 */
	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return this.getUserLabelOrName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getUserLabelOrName()
	 */
	public String getUserLabelOrName()
	{
		return (this.userLabel == null || "".equals(this.userLabel)) ? this.name : this.userLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#encodeToString()
	 */
	public String encodeToString()
	{
		return "Attribute_" + this.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface#getType()
	 */
	public String getType()
	{
		if (this.attributes[0] instanceof DateAttribute)
		{
			return DATE_ATTRIBUTE;
		}
		else if (this.attributes[0] instanceof StringAttribute)
		{
			return STRING_ATTRIBUTE;
		}
		else if (this.attributes[0] instanceof DictionaryAttribute)
		{
			return DICTIONARY_ATTRIBUTE;
		}
		else if (this.attributes[0] instanceof NumericAttribute)
		{
			return NUMERIC_ATTRIBUTE;
		}
		else if (this.attributes[0] instanceof BooleanAttribute)
		{
			return BOOLEAN_ATTRIBUTE;
		}
		else
		{
			throw new RuntimeException("Not supported attribute type: " + this.attributes[0].getClass());
		}
	}

	public static VisibleAttributeInterface getAttribute(String id)
	{
		if (visibleAttributes.isEmpty()) loadConfig();
		return (VisibleAttributeInterface) visibleAttributes.get(id);
	}

	public boolean isDate()
	{
		return date;
	}

	public void setDate(boolean date)
	{
		this.date = date;
	}

	public boolean isInUserCategory(UserCategory category)
	{
		return this.userCategory.isIn(category);
	}

	public Attribute getQueryAttribute()
	{
		return this.attributes[0];
	}

	public String getFormat()
	{
		return format;
	}

}
