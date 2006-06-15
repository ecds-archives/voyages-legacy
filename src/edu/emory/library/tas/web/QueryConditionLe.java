package edu.emory.library.tas.web;

public class QueryConditionLe extends QueryCondition
{
	
	private Object value;

	public QueryConditionLe(String attributeName)
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
