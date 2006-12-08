package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.w3c.dom.Node;

public abstract class Attribute  {

	private static final long serialVersionUID = -8780232223504322861L;

	private String objectType;
	
	private String name;

	private String userLabel;

	private String description;

	public Attribute(String name, String objectType)
	{
		this.name = name;
		this.objectType = objectType;
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

	public String encodeToString() {
		return "Attribute_" + this.getName();
	}
	/*
	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return parse(new String[] { value }, 0);
	}

	public Object parse(String[] values) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return parse(values, 0);
	}
	*/

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public abstract String getHQLWherePath(Map bindings);
	
	public abstract String getHQLSelectPath(Map bindings);

	public abstract boolean isOuterjoinable();
	
	public abstract String getHQLOuterJoinPath(Map bindings);
	
	public abstract String getHQLParamName();
	
	public abstract Object getValueToCondition(Object value);

	public String getObjectType() {
		return objectType;
	}
}