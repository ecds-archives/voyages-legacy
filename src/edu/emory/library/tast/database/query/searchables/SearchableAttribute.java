package edu.emory.library.tast.database.query.searchables;

import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.util.query.Conditions;

public abstract class SearchableAttribute
{
	
	private String userLabel;
	private String id;
	private UserCategories userCategories;
	
	public SearchableAttribute(String id, String userLabel, UserCategories userCategories)
	{
		this.id = id;
		this.userLabel = userLabel;
		this.userCategories = userCategories;
	}

	public String getUserLabel()
	{
		return userLabel;
	}

	public String getId()
	{
		return id;
	}

	public UserCategories getUserCategories()
	{
		return userCategories;
	}

	public boolean isInUserCategory(UserCategory category)
	{
		return userCategories.isIn(category);
	}

	public abstract QueryCondition createQueryCondition();
	public abstract boolean addToConditions(boolean markErrors, Conditions conditions, QueryCondition queryCondition);

}