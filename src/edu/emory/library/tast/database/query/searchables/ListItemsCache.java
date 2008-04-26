package edu.emory.library.tast.database.query.searchables;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.database.query.QueryConditionListItem;

public class ListItemsCache
{
	
	private Map lists = new HashMap();
	private static ListItemsCache instance;
	
	private ListItemsCache()
	{
	}
	
	public synchronized static ListItemsCache getInstance()
	{
		if (instance == null) instance = new ListItemsCache();
		return instance;
	}
	
	static synchronized public QueryConditionListItem[] getCachedListItems(String attrName)
	{
		return (QueryConditionListItem[]) getInstance().lists.get(attrName);
	}
	
	static synchronized public void setCachedListItems(String attrName, QueryConditionListItem[] items)
	{
		getInstance().lists.put(attrName, items);		
	}

}
