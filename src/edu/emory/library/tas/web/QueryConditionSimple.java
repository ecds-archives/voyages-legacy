package edu.emory.library.tas.web;

public class QueryConditionSimple extends QueryCondition
{
	
	private String value;

	public QueryConditionSimple()
	{
		super();
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
