package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.emory.library.tas.attrGroups.AbstractAttribute;

public class Query
{

	List conditions = new ArrayList();
	Map conditionsByAttributes = new Hashtable();
	
	public void addCondition(QueryCondition queryCondition)
	{
		if (queryCondition == null) return;
		conditions.add(queryCondition);
		conditionsByAttributes.put(queryCondition.getAttribute(), queryCondition);
	}
	
	public boolean addConditionOn(AbstractAttribute attribute)
	{
		
		if (containsConditionOn(attribute))
			return false;
		
		QueryCondition queryCondition = null;
		switch (attribute.getType().intValue())
		{
			case AbstractAttribute.TYPE_STRING:
				queryCondition = new QueryConditionText(attribute);
				break;
				
			case AbstractAttribute.TYPE_INTEGER:
			case AbstractAttribute.TYPE_LONG:
			case AbstractAttribute.TYPE_FLOAT:
			case AbstractAttribute.TYPE_DATE:
				queryCondition = new QueryConditionRange(attribute);
				break;
				
			case AbstractAttribute.TYPE_DICT:
				queryCondition = new QueryConditionDictionary(attribute);
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

	public List getConditions()
	{
		return conditions;
	}

	public Map getConditionsByAttributes()
	{
		return conditionsByAttributes;
	}
	
	public int getConditionCount()
	{
		return conditions.size();
	}
	
	public boolean containsConditionOn(AbstractAttribute attribute)
	{
		return conditionsByAttributes.containsKey(attribute);
	}

	public QueryCondition getCondition(AbstractAttribute attribute)
	{
		return (QueryCondition) conditionsByAttributes.get(attribute);
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
		if (obj == null || !(obj instanceof Query))
			return false;
		
		Query theOther = (Query) obj;
		
		if (getConditionCount() != theOther.getConditionCount())
			return false;
		
		for (Iterator iterAttr = conditionsByAttributes.keySet().iterator(); iterAttr.hasNext();)
		{
			AbstractAttribute attr = (AbstractAttribute) iterAttr.next();
			QueryCondition theOtherQueryCondition = theOther.getCondition(attr);
			
			if (theOtherQueryCondition == null)
				return false;
			
			if (!theOtherQueryCondition.equals(getCondition(attr)))
				return false;
		}
		
		return true;
	}

}