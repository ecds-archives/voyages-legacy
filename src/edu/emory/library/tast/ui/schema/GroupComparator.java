package edu.emory.library.tast.ui.schema;

import java.util.Comparator;

public class GroupComparator implements Comparator
{
	
	private int sortBy = GroupForDisplay.SORT_BY_NAME;
	private boolean sortAsc = true;
	
	public GroupComparator(int sortBy, boolean sortAsc)
	{
		this.sortBy = sortBy;
		this.sortAsc = sortAsc;
	}
	
	public int compare(Object o1, Object o2)
	{
		GroupForDisplay g1 = (GroupForDisplay) o1;
		GroupForDisplay g2 = (GroupForDisplay) o2;
		int result = 0;
		switch (sortBy)
		{
			case GroupForDisplay.SORT_BY_NAME:
				result = g1.getName().compareToIgnoreCase(g2.getName());
				break;

			case GroupForDisplay.SORT_BY_LABEL:
				result = g1.getUserLabel().compareToIgnoreCase(g2.getUserLabel());
				break;

			default:
				result = 0;
		}
		if (!sortAsc) result *= -1;
		return result;
	}
}
