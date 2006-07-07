package edu.emory.library.tas.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.emory.library.tas.attrGroups.AbstractAttribute;

public class Query implements Serializable
{

	private static final long serialVersionUID = 5986829888479480030L;

	private List conditions = new ArrayList();
	private transient Map conditionsByAttributes = null;
	
	private void ensureMap()
	{
		if (conditionsByAttributes == null)
		{
			conditionsByAttributes = new HashMap();
			for (Iterator iter = conditions.iterator(); iter.hasNext();)
			{
				QueryCondition queryCondition = (QueryCondition) iter.next();
				conditionsByAttributes.put(queryCondition.getAttribute(), queryCondition);
			}
		}
	}
	
	public void addCondition(QueryCondition queryCondition)
	{
		
		if (queryCondition == null) return;
		
		conditions.add(queryCondition);
		
		ensureMap();
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
				queryCondition = new QueryConditionNumeric(attribute);
				break;
				
			case AbstractAttribute.TYPE_DATE:
				queryCondition = new QueryConditionDate(attribute);
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
		ensureMap();
		return conditionsByAttributes;
	}
	
	public int getConditionCount()
	{
		return conditions.size();
	}
	
	public boolean containsConditionOn(AbstractAttribute attribute)
	{
		ensureMap();
		return conditionsByAttributes.containsKey(attribute);
	}

	public QueryCondition getCondition(AbstractAttribute attribute)
	{
		ensureMap();
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
		
		for (Iterator iterAttr = getConditionsByAttributes().keySet().iterator(); iterAttr.hasNext();)
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