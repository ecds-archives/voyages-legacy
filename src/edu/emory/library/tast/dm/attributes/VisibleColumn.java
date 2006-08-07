package edu.emory.library.tast.dm.attributes;

public interface VisibleColumn
{
	public String getName();
	public String toString();
	public Long getId();
	public String encodeToString();
	public Integer getType();
	public String getUserLabelOrName();
}
