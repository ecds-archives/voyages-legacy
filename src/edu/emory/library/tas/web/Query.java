package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Query
{

	List conditions = new ArrayList();
	
	public void addCondition(QueryCondition queryCondition)
	{
		conditions.add(queryCondition);
	}
	
	protected Object clone()
	{
		Query newQuery = new Query();
		for (Iterator iterQueryCondition = conditions.iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			newQuery.addCondition(queryCondition);
		}
		return newQuery;
	}

	public List getConditions()
	{
		return conditions;
	}

}