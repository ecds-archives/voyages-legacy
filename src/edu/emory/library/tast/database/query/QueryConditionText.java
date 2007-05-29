package edu.emory.library.tast.database.query;

import org.w3c.dom.Node;

import edu.emory.library.tast.util.XMLUtils;


public class QueryConditionText extends QueryCondition
{
	
	private static final long serialVersionUID = -650415782530711623L;
	
	public static final String TYPE = "text";
	
	private String value = "";
	
	public QueryConditionText(String searchableAttributeId)
	{
		super(searchableAttributeId);
	}
	
	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
	
	public boolean isNonEmpty()
	{
		return value != null && value.trim().length() > 0;
	}
	
	private boolean compareTextFields(String val1, String val2)
	{
		return
			(val1 == null && val2 == null) ||
			(val1 != null && val1.equals(val2));
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionText)) return false;
		QueryConditionText queryConditionText = (QueryConditionText) obj;
		return compareTextFields(queryConditionText.getValue(), value);
	}
	
	protected Object clone()
	{
		QueryConditionText newQueryCondition = new QueryConditionText(getSearchableAttributeId());
		newQueryCondition.setValue(value);
		return newQueryCondition;
	}

	public String toXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<condition ");
		XMLUtils.appendAttribute(buffer, "type", TYPE);
		XMLUtils.appendAttribute(buffer, "attribute", this.getSearchableAttributeId());
		XMLUtils.appendAttribute(buffer, "value", value);
		buffer.append("/>\n");
		return buffer.toString();
	}

	public static QueryCondition fromXML(Node node) {
		QueryConditionText qc = new QueryConditionText(XMLUtils.getXMLProperty(node, "attribute"));
		qc.value = XMLUtils.getXMLProperty(node, "value");
		return qc;
	}
}
