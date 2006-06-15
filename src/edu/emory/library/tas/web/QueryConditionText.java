package edu.emory.library.tas.web;

public class QueryConditionText extends QueryCondition
{
	
	private String value;

	public QueryConditionText(String attributeName)
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
