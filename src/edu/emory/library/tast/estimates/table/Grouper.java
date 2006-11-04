package edu.emory.library.tast.estimates.table;

import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class Grouper
{
	
	protected Attribute groupingAttribute;
	protected int resultIndex;
	protected boolean omitEmpty;
	
	public abstract void initSlots(Object[] dataTable);
	public abstract int lookupIndex(Object[] dataRow);
	public abstract int getSlotsCount();
	public abstract String[] getLabels();
	
	public Grouper(int resultIndex, boolean omitEmpty, Attribute groupingAttribute)
	{
		this.groupingAttribute = groupingAttribute;
		this.resultIndex = resultIndex;
		this.omitEmpty = omitEmpty;
	}

	public Attribute getGroupingAttribute()
	{
		return groupingAttribute;
	}

}