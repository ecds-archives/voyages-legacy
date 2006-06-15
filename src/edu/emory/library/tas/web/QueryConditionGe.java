package edu.emory.library.tas.web;

public class QueryConditionGe extends QueryCondition
{
	
	private Object value;

	public QueryConditionGe(String attributeName)
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
