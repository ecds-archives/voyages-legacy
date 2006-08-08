package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.dm.attributes.Attribute;

public class SearchableAttributeSimple extends SearchableAttribute
{
	
	private Attribute[] attributes;

	public SearchableAttributeSimple(String userLabel, Attribute[] attributes)
	{
		super(userLabel);
		this.attributes = attributes != null ? attributes : new Attribute[0];
	}
	
	public int getAtrributesCount()
	{
		return attributes.length;
	}

	public Attribute[] getAttributes()
	{
		return attributes;
	}

}
