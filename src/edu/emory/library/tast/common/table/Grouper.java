package edu.emory.library.tast.common.table;

import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class Grouper
{
	
	protected int resultIndex;
	protected boolean omitEmpty;
	
	public abstract Attribute getGroupingAttribute();
	public abstract Attribute[] addExtraAttributes(int index);
	public abstract void initSlots(Object[] dataTable);
	public abstract int lookupIndex(Object[] dataRow);

	public abstract Label[] getLabels();
	public abstract int getLeafLabelsCount();
	public abstract int getBreakdownDepth();
	
	public Grouper(int resultIndex, boolean omitEmpty)
	{
		this.resultIndex = resultIndex;
		this.omitEmpty = omitEmpty;
	}

}