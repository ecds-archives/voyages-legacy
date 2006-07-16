package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represent the entire history of searches in the search UI.
 * Basically, it is just a holder for a list of classes of type
 * {@link edu.emory.library.tas.web.HistoryItem}. On top of it, it provides
 * convenience methods for accessing and manipulating the list of history items.
 * It is the data format used by the history list component
 * {@link edu.emory.library.tas.web.HistoryListComponent}. The
 * {@link edu.emory.library.tas.web.HistoryListComponent} does not, however,
 * hold internally the history list, since it does not need to modify it.
 * 
 * @author Jan Zich
 * 
 */
public class History
{
	
	private List items = new ArrayList();
	
	public void clear()
	{
		items.clear();
	}
	
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
