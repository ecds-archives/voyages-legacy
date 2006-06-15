package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;

public class Query
{

	List conditions = new ArrayList();
	
	public void addCondition(QueryCondition queryCondition)
	{
		conditions.add(queryCondition);
	}
	
	public boolean addConditionOn(String atttibuteName)
	{
		
		if (containsConditionOn(atttibuteName))
			return false;
		
		SchemaColumn col = Voyage.getSchemaColumn(atttibuteName);
		if (col == null)
			return false;
	
		QueryCondition queryCondition = null;
		switch (col.getType())
		{
			case SchemaColumn.TYPE_STRING:
				queryCondition = new QueryConditionText(atttibuteName);
				break;
				
			case SchemaColumn.TYPE_INTEGER:
			case SchemaColumn.TYPE_LONG:
			case SchemaColumn.TYPE_FLOAT:
			case SchemaColumn.TYPE_DATE:
				queryCondition = new QueryConditionRange(atttibuteName);
				break;
				
			case SchemaColumn.TYPE_DICT:
				queryCondition = new QueryConditionList(atttibuteName);
				break;
		}
		
		if (queryCondition != null)
		{
			addCondition(queryCondition);
			return true;
		}
		else
		{
			return false;
		}
		
	}

	public boolean containsConditionOn(String attributeName)
	{
		for (Iterator iterQueryCondition = conditions.iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			if (queryCondition.getAttributeName().equals(attributeName))
			{
				return true;
			}
		}
		return false;
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