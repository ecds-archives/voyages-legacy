package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.List;

public class QueryConditionList extends QueryCondition
{
	
	private List values = new ArrayList();

	public QueryConditionList(String attributeName)
	{
		super(attributeName);
	}

	public List getValues()
	{
		return values;
	}

	public void setValues(List values)
	{
		this.values = values;
	}

}
