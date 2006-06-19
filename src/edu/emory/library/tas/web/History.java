package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class History
{
	
	private List items = new ArrayList();
	
	public Query getLatestQuery()
	{
		if (isEmpty()) return null;
		return ((HistoryItem)items.get(0)).getQuery();
	}
	
	public boolean isEmpty()
	{
		return items.size() == 0;
	}
	
	public void addQuery(Query query)
	{
		HistoryItem historyItem = new HistoryItem();
		historyItem.setQuery(query);
		addItem(historyItem);
	}

	public void addItem(HistoryItem item)
	{
		
		int newId = 0;
		for (Iterator iterHistoryItem = items.iterator(); iterHistoryItem.hasNext();)
		{
			HistoryItem historyItem = (HistoryItem) iterHistoryItem.next();
			int id = Integer.parseInt(historyItem.getId());
			if (newId <= id) newId = id + 1;
		}
		
		item.setId(String.valueOf(newId));
		items.add(0, item);
		
	}
	
	public HistoryItem getHistoryItem(String historyId)
	{
		for (Iterator iterHistoryItem = items.iterator(); iterHistoryItem.hasNext();)
		{
			HistoryItem historyItem = (HistoryItem) iterHistoryItem.next();
			if (historyId.equals(historyItem.getId()))
			{
				return historyItem;
			}
		}
		return null;
	}
	
	public boolean deleteItem(String historyId)
	{
		HistoryItem toDelete = getHistoryItem(historyId);
		if (toDelete != null)
		{
			items.remove(toDelete);
			return true;
		}
		else
		{
			return false;
		}
	}

	public List getItems()
	{
		return items;
	}

}
