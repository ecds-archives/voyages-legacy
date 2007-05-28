package edu.emory.library.tast.database.query;

import org.w3c.dom.Node;


public class QueryConditionBoolean extends QueryCondition
{
	
	private static final long serialVersionUID = 940301730134650051L;

	public static final String TYPE = "boolean";
	
	private boolean checked = true;

	public QueryConditionBoolean(String searchableAttributeId)
	{
		super(searchableAttributeId);
		this.checked = false;
	}

	public QueryConditionBoolean(String searchableAttributeId, boolean checked)
	{
		super(searchableAttributeId);
		this.checked = checked;
	}
	
	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionBoolean)) return false;
		QueryConditionBoolean queryConditionBoolean = (QueryConditionBoolean) obj;
		return queryConditionBoolean.checked == this.checked;
	}
	
	protected Object clone()
	{
		QueryConditionBoolean newQueryCondition = new QueryConditionBoolean(getSearchableAttributeId());
		newQueryCondition.setChecked(checked);
		return newQueryCondition;
	}

	public String toXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<condition >");
		appendAttribute(buffer, "type", TYPE);
		appendAttribute(buffer, "attribute", this.getSearchableAttributeId());
		appendAttribute(buffer, "value", new Boolean(this.checked));
		buffer.append("/>\n");
		return buffer.toString();
	}

	public static QueryCondition fromXML(Node node) {
		QueryConditionBoolean qc = new QueryConditionBoolean(getXMLProperty(node, "attribute"));
		qc.checked = Boolean.valueOf(getXMLProperty(node, "value")).booleanValue();
		return qc;
	}
}