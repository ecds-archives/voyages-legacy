package edu.emory.library.tas.web.schema;

import java.util.Comparator;

public class AbstractAttributeComparator implements Comparator
{

	private int sortBy = AbstractAttributeForDisplay.SORT_BY_NAME;
	private boolean sortAsc = true;
	
	public AbstractAttributeComparator(int sortBy, boolean sortAsc)
	{
		this.sortBy = sortBy;
		this.sortAsc = sortAsc;
	}
	
	public int compare(Object o1, Object o2)
	{
		AbstractAttributeForDisplay a1 = (AbstractAttributeForDisplay) o1;
		AbstractAttributeForDisplay a2 = (AbstractAttributeForDisplay) o2;
		int result = 0;
		switch (sortBy)
		{
			case AbstractAttributeForDisplay.SORT_BY_NAME:
				result = a1.getName().compareToIgnoreCase(a2.getName());
				break;
			
			case AbstractAttributeForDisplay.SORT_BY_LABEL:
				result = a1.getUserLabel().compareToIgnoreCase(a2.getUserLabel());
				break;
			
			case AbstractAttributeForDisplay.SORT_BY_TYPE:
				result = a1.getTypeDisplayName().compareToIgnoreCase(a2.getTypeDisplayName());
				if (result == 0)
					result = result = a1.getName().compareToIgnoreCase(a2.getName());
				break;
			
			case AbstractAttributeForDisplay.SORT_BY_CATEGORY:
				int c1 = a1.getCategory();
				int c2 = a2.getCategory();
				result = c1 == c2 ? 0 : (c1 < c2 ? 1 : -1);
				break;

			case AbstractAttributeForDisplay.SORT_BY_VISIBILITY:
				boolean v1 = a1.isVisible();
				boolean v2 = a2.isVisible();
				if ((v1 && v2) || (!v1 && !v2))
					result = a1.getName().compareToIgnoreCase(a2.getName());
				else if (v1 && !v2)
					result = 1;
				else
					result = -1;
				break;
			
			default:
				result = 0;
		}
		if (!sortAsc) result *= -1;
		return result;
	}

}
