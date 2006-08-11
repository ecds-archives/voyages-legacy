package edu.emory.library.tast.ui.search.query;

import java.io.Serializable;

import edu.emory.library.tast.ui.search.query.searchables.SearchableAttribute;
import edu.emory.library.tast.ui.search.query.searchables.Searchables;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;

/**
 * This class represents one condition in the list of conditions, represented by
 * {@link edu.emory.library.tast.ui.search.query.Query}, in the currently built query in the
 * search UI. It is abstract. It provides only the necessary methods and
 * properties for specialized conditions. It is important that this class and,
 * in particular, its descendants implement {@link #clone()} and
 * {@link #equals(Object)} since is the two methods are used when storing and
 * restoring the query in the history list.
 * 
 * @author Jan Zich
 * 
 */
public abstract class QueryCondition implements Serializable
{
	
	private String searchableAttributeId;
	private transient boolean errorFlag;

	protected abstract Object clone();

	public boolean addToConditions(Conditions conditions, boolean markErrors)
	{
		return getSearchableAttribute().addToConditions(markErrors, conditions, this);
	}

	public QueryCondition(String searchableAttributeId)
	{
		this.searchableAttributeId = searchableAttributeId;
		this.errorFlag = false;
	}

	public boolean isErrorFlag()
	{
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag)
	{
		this.errorFlag = errorFlag;
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof QueryCondition)) return false;
		QueryCondition that = (QueryCondition) obj;
		return StringUtils.compareStrings(this.searchableAttributeId, that.searchableAttributeId);
	}
	
	public String getSearchableAttributeId()
	{
		return searchableAttributeId;
	}
	
	public SearchableAttribute getSearchableAttribute()
	{
		return Searchables.getCurrent().getSearchableAttributeById(searchableAttributeId);
	}

}