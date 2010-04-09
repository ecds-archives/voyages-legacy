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
import java.util.Iterator;
import java.util.List;

/**
 * This class represents the history of searches in the search UI.
 * Basically, it is just a holder for a list of classes of type
 * {@link edu.emory.library.tast.database.query.HistoryItem}. On top of it, it provides
 * convenience methods for accessing and manipulating the list of history items.
 * It is the data format used by the history list component
 * {@link edu.emory.library.tast.database.query.HistoryListComponent}. The
 * {@link edu.emory.library.tast.database.query.HistoryListComponent} does not, however,
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
		return getLatestItem().getQuery();
	}
	
	public HistoryItem getLatestItem()
	{
		if (isEmpty()) return null;
		return (HistoryItem) items.get(0);
	}

	public boolean isEmpty()
	{
		return items.size() == 0;
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
