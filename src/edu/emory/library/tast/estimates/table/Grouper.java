package edu.emory.library.tast.estimates.table;

import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class Grouper
{
	
	protected int resultIndex;
	protected boolean omitEmpty;
	
	public abstract Attribute getGroupingAttribute();
	public abstract Attribute[] addExtraAttributes(int index);
	public abstract void initSlots(Object[] dataTable);
	public abstract int lookupIndex(Object[] dataRow);
	public abstract int getSlotsCount();
	public abstract String[] getLabels();
	
	public Grouper(int resultIndex, boolean omitEmpty)
	{
		this.resultIndex = resultIndex;
		this.omitEmpty = omitEmpty;
	}

}