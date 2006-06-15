package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class History
{
	
	private List items = new ArrayList();
	
	public void addItem(HistoryItem item)
	{
		
		int newId = 0;
		for (Iterator iterHistoryItem = items.iterator(); iterHistoryItem.hasNext();)
		{
			HistoryItem historyItem = (HistoryItem) iterHistoryItem.next();
			int id = Integer.parseInt(historyItem.getId());
			if (newId <= id) newId ++;
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
