package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class SearchableAttributeSimpleRange extends SearchableAttributeSimple
{

	public SearchableAttributeSimpleRange(String id, String userLabel, UserCategories userCategories, Attribute[] attributes)
	{
		super(id, userLabel, userCategories, attributes);
	}

	public abstract String getLabelFrom();
	public abstract String getLabelTo();
	public abstract String getLabelEquals();
	public abstract String getLabelBetween();

}
