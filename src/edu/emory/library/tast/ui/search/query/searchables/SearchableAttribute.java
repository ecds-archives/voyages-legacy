package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.util.query.Conditions;

public abstract class SearchableAttribute
{
	
	private String userLabel;
	private String id;
	private UserCategory userCategory;
	
	public SearchableAttribute(String id, String userLabel, UserCategory userCategory)
	{
		this.id = id;
		this.userLabel = userLabel;
		this.userCategory = userCategory;
	}

	public String getUserLabel()
	{
		return userLabel;
	}

	public String getId()
	{
		return id;
	}

	public UserCategory getUserCategory()
	{
		return userCategory;
	}
	
	public abstract QueryCondition createQueryCondition();
	public abstract boolean addToConditions(boolean markErrors, Conditions conditions, QueryCondition queryCondition);

}