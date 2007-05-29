package edu.emory.library.tast.database.query;

import org.w3c.dom.Node;

import edu.emory.library.tast.util.XMLUtils;



public class QueryConditionNumeric extends QueryConditionRange
{
	
	private static final long serialVersionUID = -7863875106659949813L;

	public static final String TYPE = "numeric";
	
	private String from;
	private String to;
	private String ge;
	private String le;
	private String eq;
	
	public QueryConditionNumeric(String searchableAttributeId)
	{
		super(searchableAttributeId);
	}

	public QueryConditionNumeric(String searchableAttributeId, int type)
	{
		super(searchableAttributeId);
		this.type = type;
	}

	public String getEq()
	{
		return eq;
	}

	public void setEq(String eq)
	{
		this.eq = eq;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getGe()
	{
		return ge;
	}

	public void setGe(String ge)
	{
		this.ge = ge;
	}

	public String getLe()
	{
		return le;
	}

	public void setLe(String le)
	{
		this.le = le;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public String getFromForDisplay()
	{
		return from;
	}

	public String getToForDisplay()
	{
		return to;
	}

	public String getLeForDisplay()
	{
		return le;
	}

	public String getGeForDisplay()
	{
		return ge;
	}

	public String getEqForDisplay()
	{
		return eq;
	}
	
	private boolean compareTextFields(String val1, String val2)
	{
		return
			(val1 == null && val2 == null) ||
			(val1 != null && val1.equals(val2));
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionNumeric))
			return false;

		QueryConditionNumeric queryConditionRange = (QueryConditionNumeric) obj;
		return
			type == queryConditionRange.getType() &&
			compareTextFields(from, queryConditionRange.getFrom()) &&
			compareTextFields(to, queryConditionRange.getTo()) &&
			compareTextFields(le, queryConditionRange.getLe()) &&
			compareTextFields(ge, queryConditionRange.getGe()) &&
			compareTextFields(eq, queryConditionRange.getEq());
	}
	
	protected Object clone()
	{
		QueryConditionNumeric newQueryCondition = new QueryConditionNumeric(getSearchableAttributeId(), getType());
		newQueryCondition.setType(type);
		newQueryCondition.setFrom(from);
		newQueryCondition.setTo(to);
		newQueryCondition.setLe(le);
		newQueryCondition.setGe(ge);
		newQueryCondition.setEq(eq);
		return newQueryCondition;
	}

	public String toXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<condition ");
		XMLUtils.appendAttribute(buffer, "type", TYPE);
		XMLUtils.appendAttribute(buffer, "attribute", this.getSearchableAttributeId());
		XMLUtils.appendAttribute(buffer, "from", from);
		XMLUtils.appendAttribute(buffer, "to", to);
		XMLUtils.appendAttribute(buffer, "ge", ge);
		XMLUtils.appendAttribute(buffer, "le", le);
		XMLUtils.appendAttribute(buffer, "eq", eq);
		XMLUtils.appendAttribute(buffer, "querytype", new Integer(this.type));
		buffer.append("/>\n");
		return buffer.toString();
	}
	
	public static QueryCondition fromXML(Node node) {
		QueryConditionNumeric qc = new QueryConditionNumeric(XMLUtils.getXMLProperty(node, "attribute"));
		qc.from = XMLUtils.getXMLProperty(node, "from");
		qc.to = XMLUtils.getXMLProperty(node, "to");
		qc.ge = XMLUtils.getXMLProperty(node, "ge");
		qc.le = XMLUtils.getXMLProperty(node, "le");
		qc.eq = XMLUtils.getXMLProperty(node, "eq");
		qc.type = Integer.parseInt(XMLUtils.getXMLProperty(node, "querytype"));
		return qc;
	}
}
