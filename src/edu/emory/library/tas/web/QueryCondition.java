package edu.emory.library.tas.web;

import edu.emory.library.tas.util.query.Conditions;

public abstract class QueryCondition
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
	
	public abstract void addToConditions(Conditions conditions) throws QueryInvalidValueException;

}
