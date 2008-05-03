package edu.emory.library.tast.database.query;

import edu.emory.library.tast.util.StringUtils;

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
	
	public UrlParam[] createUrlParamValue()
	{
		String attrId = getSearchableAttributeId();
		switch (getType())
		{
			case QueryConditionRange.TYPE_BETWEEN:
				return new UrlParam[] {
						new UrlParam(attrId + "From",String.valueOf(from)),
						new UrlParam(attrId + "To", String.valueOf(to))};
			
			case QueryConditionRange.TYPE_EQ:
				return new UrlParam[] {
						new UrlParam(attrId, String.valueOf(eq))};
			
			case QueryConditionRange.TYPE_LE:
				return new UrlParam[] {
						new UrlParam(attrId + "To", String.valueOf(le))};
			
			case QueryConditionRange.TYPE_GE:
				return new UrlParam[] {
						new UrlParam(attrId + "From", String.valueOf(ge))};
				
			default:
				throw new RuntimeException("unexpected type");
		}
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionNumeric))
			return false;

		QueryConditionNumeric queryConditionRange = (QueryConditionNumeric) obj;
		return
			type == queryConditionRange.getType() &&
			StringUtils.compareStrings(from, queryConditionRange.getFrom()) &&
			StringUtils.compareStrings(to, queryConditionRange.getTo()) &&
			StringUtils.compareStrings(le, queryConditionRange.getLe()) &&
			StringUtils.compareStrings(ge, queryConditionRange.getGe()) &&
			StringUtils.compareStrings(eq, queryConditionRange.getEq());
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
	
}
