package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;

public class Query
{

	List conditions = new ArrayList();
	Map attributeNames = new Hashtable();
	
	public void addCondition(QueryCondition queryCondition)
	{
		if (queryCondition == null) return;
		conditions.add(queryCondition);
		attributeNames.put(queryCondition.getAttributeName(), queryCondition);
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
				queryCondition = new QueryConditionDictionary(atttibuteName);
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
		return attributeNames.containsKey(attributeName);
	}

	public QueryCondition getCondition(String attributeName)
	{
		return (QueryCondition) attributeNames.get(attributeName);
	}

	protected Object clone()
	{
		Query newQuery = new Query();
		for (Iterator iterQueryCondition = conditions.iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			newQuery.addCondition((QueryCondition) queryCondition.clone());
		}
		return newQuery;
	}
	
	public boolean equals(Object obj)
	{
		if (obj instanceof Query)
		{
			Query theOtherQuery = (Query) obj;
			if (theOtherQuery == null)
				return false;
			
			if (getConditionCount() != theOtherQuery.getConditionCount())
				return false;
			
			for (Iterator iterAttr = attributeNames.keySet().iterator(); iterAttr.hasNext();)
			{
				String attr = (String) iterAttr.next();
				QueryCondition theOtherQueryCondition = theOtherQuery.getCondition(attr);
				if (theOtherQueryCondition == null) return false;
				if (!theOtherQueryCondition.equals(getCondition(attr))) return false;
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}

	public List getConditions()
	{
		return conditions;
	}

	public Map getAttributeNames()
	{
		return attributeNames;
	}
	
	public int getConditionCount()
	{
		return conditions.size();
	}

}