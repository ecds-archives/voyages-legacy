/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.database.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Holds the list of conditions, i.e. objects of types
 * {@link edu.emory.library.tast.database.query.QueryCondition}. Represents the currently
 * built query. Used by {@link edu.emory.library.tast.database.query.QueryBuilderComponent}.
 * Also, it is important that this class implements {@link #clone()} and
 * {@link #equals(Object)} since is the two methods are used when storing and
 * restoring the query in the history list.
 * 
 * @author Jan Zich
 * 
 */
public class QueryBuilderQuery implements Cloneable
{

	private static final long serialVersionUID = 5986829888479480030L;

	private List conditions = new ArrayList();
	private transient Map conditionsByAttributes = null;
	
	private synchronized void ensureMap()
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

	public int hashCode()
	{
		return (conditions == null) ? 0 : conditions.hashCode();
	}

}