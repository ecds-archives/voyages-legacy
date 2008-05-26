package edu.emory.library.tast.common.table;

import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class TableBuilder
{
	private String colLabel;
	
	public TableBuilder(String colLabel)
	{
		this.colLabel = colLabel;
	}

	public String getColLabel()
	{
		return colLabel;
	}
	public void setColLabel(String colLabel)
	{
		this.colLabel = colLabel;
	}
	
	public abstract Table formTable(Object[] data, int dataIdxCol, Grouper rowGrouper, Grouper colGrouper);
	public abstract Attribute[] getAttributes();
	public abstract int getAttributeCount();
	public abstract String getTotalLabel();
}
