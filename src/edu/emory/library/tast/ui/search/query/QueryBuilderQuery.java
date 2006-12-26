package edu.emory.library.tast.ui.search.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Holds the list of conditions, i.e. objects of types
 * {@link edu.emory.library.tast.ui.search.query.QueryCondition}. Represents the currently
 * built query. Used by {@link edu.emory.library.tast.ui.search.query.QueryBuilderComponent}.
 * Also, it is important that this class implements {@link #clone()} and
 * {@link #equals(Object)} since is the two methods are used when storing and
 * restoring the query in the history list.
 * 
 * @author Jan Zich
 * 
 */
public class QueryBuilderQuery implements Serializable, Cloneable
{

	private static final long serialVersionUID = 5986829888479480030L;

	private List conditions = new ArrayList();

	private transient Map conditionsByAttributes = null;
	
	private void ensureMap()
	{
		if (conditionsByAttributes == null)
		{
			Map newMap = new HashMap();
			for (Iterator iter = conditions.iterator(); iter.hasNext();)
			{
				QueryCondition queryCondition = (QueryCondition) iter.next();
				newMap.put(queryCondition.getSearchableAttributeId(), queryCondition);
			}
			conditionsByAttributes = newMap;
		}
	}
	
	public void addCondition(QueryCondition queryCondition)
	{
		
		if (queryCondition == null) return;
		
		conditions.add(queryCondition);
		
		ensureMap();
		conditionsByAttributes.put(queryCondition.getSearchableAttributeId(), queryCondition);
	
	}
	
//	public boolean addConditionOn(String searchableAttributeId)
//	{
//		
//		if (containsConditionOn(searchableAttributeId))
//			return false;
//		
//		SearchableAttribute searchableAttribute = Searchables.getCurrent().getSearchableAttributeById(searchableAttributeId);
//		QueryCondition queryCondition = searchableAttribute.createQueryCondition();
//		
//		if (queryCondition != null)
//		{
//			addCondition(queryCondition);
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//		
//	}

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
	
	public boolean containsConditionOn(String searchableAttributeId)
	{
		ensureMap();
		return conditionsByAttributes.containsKey(searchableAttributeId);
	}

	public QueryCondition getCondition(String searchableAttributeId)
	{
		ensureMap();
		return (QueryCondition) conditionsByAttributes.get(searchableAttributeId);
	}

	protected Object clone()
	{
		QueryBuilderQuery newQuery = new QueryBuilderQuery();
		for (Iterator iterQueryCondition = conditions.iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			newQuery.addCondition((QueryCondition) queryCondition.clone());
		}
		return newQuery;
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof QueryBuilderQuery))
			return false;
		
		QueryBuilderQuery that = (QueryBuilderQuery) obj;
		
		if (this.getConditionCount() != that.getConditionCount())
			return false;
		
		for (Iterator iterAttr = getConditionsByAttributes().keySet().iterator(); iterAttr.hasNext();)
		{
			String attrId = (String) iterAttr.next();
			QueryCondition thisQueryCondition = this.getCondition(attrId);
			QueryCondition thatQueryCondition = that.getCondition(attrId);
			
			if (thatQueryCondition == null)
				return false;
			
			if (!thatQueryCondition.equals(thisQueryCondition))
				return false;
		}
		
		return true;
	}

}