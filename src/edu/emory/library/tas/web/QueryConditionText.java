package edu.emory.library.tas.web;

import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.util.query.Conditions;

public class QueryConditionText extends QueryCondition
{
	
	private String value = "";
	
	public QueryConditionText(AbstractAttribute attribute)
	{
		super(attribute);
	}

	public boolean addToConditions(Conditions conditions) 
	{
//		if (isNonEmpty())
//			conditions.addCondition(getAttributeId(), value.trim(), Conditions.OP_LIKE);
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
		QueryConditionText newQueryCondition = new QueryConditionText(getAttribute());
		newQueryCondition.setValue(value);
		return newQueryCondition;
	}

}
