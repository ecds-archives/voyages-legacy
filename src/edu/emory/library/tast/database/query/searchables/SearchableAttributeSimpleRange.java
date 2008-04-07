package edu.emory.library.tast.database.query.searchables;

import edu.emory.library.tast.database.query.QueryConditionNumeric;
import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class SearchableAttributeSimpleRange extends SearchableAttributeSimple
{
	
	protected int defaultSearchType = QueryConditionNumeric.TYPE_BETWEEN;
	
	public SearchableAttributeSimpleRange(String id, String userLabel, UserCategories userCategories, Attribute[] attributes, int defaultSearchType, String spssName, String listDescription, boolean inEstimates)
	{
		super(id, userLabel, userCategories, attributes, spssName, listDescription, inEstimates);
		this.defaultSearchType = defaultSearchType;
	}

	public abstract String getLabelFrom();
	public abstract String getLabelTo();
	public abstract String getLabelEquals();
	public abstract String getLabelBetween();

}
