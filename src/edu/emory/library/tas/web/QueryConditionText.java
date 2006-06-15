package edu.emory.library.tas.web;

import edu.emory.library.tas.util.query.Conditions;

public class QueryConditionText extends QueryCondition
{
	
	private String value;

	public QueryConditionText(String attributeName)
	{
		super(attributeName);
	}

	public void addToConditions(Conditions conditions) throws QueryInvalidValueException
	{
		conditions.addCondition(getAttributeName(), value, Conditions.OP_LIKE);
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

}
