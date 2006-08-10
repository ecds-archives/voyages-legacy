package edu.emory.library.tast.ui.search.query;

import edu.emory.library.tast.util.query.Conditions;

public class QueryConditionText extends QueryCondition
{
	
	private static final long serialVersionUID = -650415782530711623L;
	
	private String value = "";
	
	public QueryConditionText(String searchableAttributeId)
	{
		super(searchableAttributeId);
	}
	
	public boolean addToConditions(Conditions conditions, boolean markErrors) 
	{
		return true;
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

}
