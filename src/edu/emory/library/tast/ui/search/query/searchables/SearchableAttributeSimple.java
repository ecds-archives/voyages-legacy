package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class SearchableAttributeSimple extends SearchableAttribute
{
	
	private Attribute[] attributes;
	private String attributeType;

	public SearchableAttributeSimple(String id, String userLabel, UserCategory userCategory, Attribute[] attributes)
	{
		super(id, userLabel, userCategory);
		if (attributes == null || attributes.length == 0)
		{
			this.attributes = new Attribute[0];
			this.attributeType = null;
		}
		else
		{
			this.attributes = attributes;
			this.attributeType = Attribute.decodeType(attributes[0]);
		}
	}
	
	public int getAtrributesCount()
	{
		return attributes.length;
	}

	public Attribute[] getAttributes()
	{
		return attributes;
	}

	public String getAttributeType()
	{
		return attributeType;
	}

}
