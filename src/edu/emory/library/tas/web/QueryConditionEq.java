package edu.emory.library.tas.web;

public class QueryConditionEq extends QueryCondition
{
	
	private Object value;

	public QueryConditionEq(String attributeName)
	{
		super(attributeName);
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}

}
