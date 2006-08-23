package edu.emory.library.tast.dm.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public abstract class Attribute  {

	private static HashMap config = new HashMap();

	private static final long serialVersionUID = -8780232223504322861L;

	public final static int IMPORT_TYPE_IGNORE = -1;

	public final static int IMPORT_TYPE_NUMERIC = 0;

	public final static int IMPORT_TYPE_STRING = 1;

	public final static int IMPORT_TYPE_DATE = 2;

	public static final String DATE_ATTRIBUTE = "DateAttribute";

	public static final String DICTIONARY_ATTRIBUTE = "DictionaryAttribute";

	public static final String NUMERIC_ATTRIBUTE = "NumericAttribute";

	public static final String STRING_ATTRIBUTE = "StringAttribute";

	private String objectType;
	
	private String name;

	private String userLabel;

	private String description;

	private Integer category;

	private Integer importType;

	private String importName;

	public Attribute(String name, String objectType) {
		this.name = name;
		this.objectType = objectType;
	}
	
	public Attribute(Node xmlNode, String objectType) {
		System.out.println(xmlNode);
		String name = parseAttribute(xmlNode, "name");
		String userLabel = parseAttribute(xmlNode, "userLabel");
		Integer importType = new Integer(IMPORT_TYPE_IGNORE);
		if (parseAttribute(xmlNode, "importType") != null) {
			importType = new Integer(parseAttribute(xmlNode, "importType"));
		}
		String importName = parseAttribute(xmlNode, "importName");
		Integer category = new Integer(parseAttribute(xmlNode, "category"));
		String description = parseAttribute(xmlNode, "desc");
		
		this.objectType = objectType;
		
		this.name = name;
		this.userLabel = userLabel;
		this.importType = importType;
		this.importName = importName;
		this.description = description;
		this.category = category;
	}
	
	public Attribute(String name, String userLabel, Integer importType, String importName) {
		this.name = name;
		this.userLabel = userLabel;
		this.importType = importType;
		this.importName = importName;
	}
	
	protected String parseAttribute(Node xmlNode, String attributeName) {
		Node namedItem = xmlNode.getAttributes().getNamedItem(attributeName);
		if (namedItem != null) {
			String attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				return attribute;
			}
		}
		return null;
	}

	public String getImportName() {
		if (importName == null)
			return getName();
		return importName;
	}

	public Integer getImportType() {
		return importType;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public void setImportType(Integer importType) {
		this.importType = importType;
	}

	public String encodeToString() {
		return "Attribute_" + this.getName();
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
							String objectType = node.getAttributes().getNamedItem("objectType").getNodeValue();
							NodeList attrs = node.getChildNodes();
							List attrsList = new ArrayList();
							for (int j = 0; j < attrs.getLength(); j++) {
								if (attrs.item(j).getNodeType() == Node.ELEMENT_NODE) {
									attrsList.add(Attribute.fromXML(attrs.item(j), objectType));
								}
							}
							config.put(objectType, attrsList.toArray(new Attribute[] {}));
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

	public static Attribute fromXML(Node xmlNode, String objectType) {

		Node attrType = xmlNode.getAttributes().getNamedItem("attrType");
		if (attrType != null) {
			String attrTypeStr = attrType.getNodeValue();
			if (DateAttribute.ATTR_TYPE_NAME.equals(attrTypeStr)) {
				return new DateAttribute(xmlNode, objectType);
			} else if (DictionaryAttribute.ATTR_TYPE_NAME.equals(attrTypeStr)) {
				return new DictionaryAttribute(xmlNode, objectType);
			} else if (NumericAttribute.ATTR_TYPE_NAME.equals(attrTypeStr)) {
				return new NumericAttribute(xmlNode, objectType);
			} else if (StringAttribute.ATTR_TYPE_NAME.equals(attrTypeStr)) {
				return new StringAttribute(xmlNode, objectType);
			} else {
				throw new RuntimeException("Unexpected attrType value: " + attrTypeStr);
			}
		} else {
			throw new RuntimeException("attrType not found in attribute node!");
		}
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public boolean isVisibleByCategory(int category)
	{
		return
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
		return (name == null && theOther.getName() == null)
				|| (name != null && name.equals(theOther.getName()));
	}

	public int hashCode() {
		if (name == null)
			return super.hashCode();
		return name.hashCode();
	}
	
	public abstract  Object parse(String[] values, int options) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException;
	
	public abstract String getTypeDisplayName();

	public abstract String getHQLWherePath(Map bindings);
	
	public abstract String getHQLSelectPath(Map bindings);

	public abstract boolean isOuterjoinable();
	
	public abstract String getHQLOuterJoinPath(Map bindings);
	
	public abstract String getHQLParamName();

	public static String decodeType(Attribute attribute) {
		if (attribute instanceof DateAttribute) {
			return DATE_ATTRIBUTE;
		} else if (attribute instanceof DictionaryAttribute) {
			return DICTIONARY_ATTRIBUTE;
		} else if (attribute instanceof NumericAttribute) {
			return NUMERIC_ATTRIBUTE;
		} else if (attribute instanceof StringAttribute) {
			return STRING_ATTRIBUTE;
		} else {
			throw new RuntimeException("Unknown attribute type!");
		}
	}

	public String decodeType() {
		return decodeType(this);
	}
	
	public String getObjectType() {
		return objectType;
	}
}