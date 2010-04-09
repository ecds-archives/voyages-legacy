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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import edu.emory.library.tast.database.query.searchables.ListItemsSource;
import edu.emory.library.tast.database.query.searchables.Searchables;
import edu.emory.library.tast.util.StringUtils;

public class QueryConditionList extends QueryCondition
{
	
	private static final long serialVersionUID = 6147345036427086382L;

	public static final String TYPE = "list";
	
	private Set selectedIds = new HashSet();
	private Set expandedIds = new HashSet();
	private boolean edit = false;
	private boolean autoSelection = false; 
	
	public QueryConditionList(String searchableAttributeId)
	{
		super(searchableAttributeId);
	}
	
	public boolean isEdit()
	{
		return edit;
	}

	public void setEdit(boolean edit)
	{
		this.edit = edit;
	}

	public boolean isAutoSelection() 
	{
		return autoSelection;
	}

	public void setAutoSelection(boolean autoSelection)
	{
		this.autoSelection = autoSelection;
	}

	public int getSelectedIdsCount()
	{
		if (selectedIds == null) return 0;
		return selectedIds.size();
	}

	public Set getSelectedIds()
	{
		return selectedIds;
	}

	public void setSelectedIds(Set values)
	{
		this.selectedIds = values;
	}
	
	public boolean containsId(String id)
	{
		if (selectedIds == null) return false;
		return selectedIds.contains(id);
	}

	public void addId(String id)
	{
		if (id == null) return;
		if (selectedIds == null) selectedIds = new HashSet();
		selectedIds.add(id);
	}
	
	public void setExpanded(String id, boolean state)
	{
		if (id == null) return;
		if (expandedIds == null) expandedIds = new HashSet();
		if (state)
		{
			expandedIds.add(id);
		}
		else
		{
			expandedIds.remove(id);
		}
	}
	
	public boolean isExpanded(String id)
	{
		if (expandedIds == null)
		{
			return false;
		}
		else
		{
			return expandedIds.contains(id);
		}
	}
	
	public void setExpandedIds(String[] ids)
	{
		if (expandedIds == null) expandedIds = new HashSet();
		expandedIds.clear();
		for (int i = 0; i < ids.length; i++) expandedIds.add(ids[i]);
	}

	public void setSelectedIds(String[] ids)
	{
		if (selectedIds == null) selectedIds = new HashSet();
		selectedIds.clear();
		for (int i = 0; i < ids.length; i++) selectedIds.add(ids[i]);
	}
	
	public Set getMinimalSelectedIds()
	{
		
		if (!autoSelection)
			return selectedIds;
		
		StringBuffer idPart = new StringBuffer(); 
		
		Set minSelectedIds = new HashSet();
		
		for (Iterator iterator = selectedIds.iterator(); iterator.hasNext();)
		{
			
			String id = (String) iterator.next();
			String[] idComponents = id.split(QueryBuilderComponent.ID_SEPARATOR);
			
			idPart.setLength(0);
			boolean isExtraneous = false;
			for (int i = 0; i < idComponents.length - 1 && !isExtraneous; i++)
			{
				if (i > 0) idPart.append(QueryBuilderComponent.ID_SEPARATOR);
				idPart.append(idComponents[i]);
				if (selectedIds.contains(idPart.toString())) isExtraneous = true;
			}
			
			if (!isExtraneous)
				minSelectedIds.add(id);
			
		}
		
		return minSelectedIds;
		
	}

	public UrlParam[] createUrlParamValue()
	{
		
		ListItemsSource listSource = (ListItemsSource) Searchables.getById(getSearchableAttributeId());
		
		Set minimalSelectedIds = getMinimalSelectedIds();

		Set realSelectedIds = new TreeSet();
		for (Iterator iterator = minimalSelectedIds.iterator(); iterator.hasNext();)
		{
			String id = (String) iterator.next();
			realSelectedIds.add(listSource.getItemRealId(id));
		}
		
		return new UrlParam[] {
				new UrlParam(
						getSearchableAttributeId(),
						StringUtils.join(".", realSelectedIds))};
		
	}

	public boolean equals(Object obj)
	{
		if (!super.equals(obj))
			return false;

		if (!(obj instanceof QueryConditionList))
			return false;
		
		QueryConditionList that = (QueryConditionList) obj;

		if (this.getSelectedIdsCount() != that.getSelectedIdsCount())
			return false;
		
		for (Iterator iter = selectedIds.iterator(); iter.hasNext();)
			if (!that.containsId((String) iter.next()))
				return false;
		
		return true;
	}
	
	protected Object clone()
	{
		QueryConditionList newQueryCondition =
			new QueryConditionList(getSearchableAttributeId());
		
		for (Iterator iterDict = selectedIds.iterator(); iterDict.hasNext();)
			newQueryCondition.addId((String) iterDict.next());
		
		return newQueryCondition;
	}

	public int hashCode()
	{
		String[] idsArr = new String[selectedIds.size()];
		Arrays.sort(idsArr);
		final int prime = 31;
		int result = 1;
		for (int i = 0; i < idsArr.length; i++)
		{
			String id = idsArr[i];
			result = prime * result + ((id == null) ? 0 : id.hashCode());			
		}
		return result;
	}
	
}