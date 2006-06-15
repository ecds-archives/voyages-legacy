package edu.emory.library.tas.web;

public class QueryConditionSimple extends QueryCondition
{
	
	private String value;

	public QueryConditionSimple(String attributeName)
	{
		super(attributeName);
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
