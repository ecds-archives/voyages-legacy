package edu.emory.library.tas.web;

public class QueryCondition
{
	
	private String attributeName;

	public QueryCondition(String attributeName)
	{
		this.attributeName = attributeName;
	}

	public String getAttributeName()
	{
		return attributeName;
	}

}
