package edu.emory.library.tast.ui.search.query;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.emory.library.tast.util.query.Conditions;

public class QueryConditionList extends QueryCondition
{
	
	private static final long serialVersionUID = 6147345036427086382L;

	private Set selectedIds = new HashSet();
	private boolean edit = false;
	
	public QueryConditionList(String searchableAttributeId)
	{
		super(searchableAttributeId);
	}

	public boolean addToConditions(Conditions conditions, boolean markErrors)
	{
		return true;
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

	public boolean isEdit()
	{
		return edit;
	}

	public void setEdit(boolean edit)
	{
		this.edit = edit;
	}

}