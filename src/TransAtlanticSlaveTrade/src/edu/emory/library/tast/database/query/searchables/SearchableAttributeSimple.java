package edu.emory.library.tast.database.query.searchables;

import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class SearchableAttributeSimple extends SearchableAttribute
{
	
	private Attribute[] attributes;

	public SearchableAttributeSimple(String id, String userLabel, UserCategories userCategories, Attribute[] attributes, String spssName, String listDescription, boolean inEstimates)
	{
		super(id, userLabel, userCategories, spssName, listDescription, inEstimates);
		if (attributes == null || attributes.length == 0)
		{
			this.attributes = new Attribute[0];
		}
		else
		{
			this.attributes = attributes;
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
	
	public String getNonNullSqlQuerySelectPart(String voyagePrefix)
	{
		StringBuffer select = new StringBuffer();
		select.append("COALESCE(");
		for (int i = 0; i < attributes.length; i++)
		{
			if (i > 0) select.append(", ");
			if (voyagePrefix != null) select.append(voyagePrefix).append(".");
			select.append(attributes[i].getName());
		}
		select.append(")");
		return select.toString();
	}

}
