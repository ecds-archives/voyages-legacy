package edu.emory.library.tast.database.query.searchables;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.util.query.Conditions;

public abstract class SearchableAttribute
{
	
	private String userLabel;
	private String id;
	private UserCategories userCategories;
	private String spssName;
	private String listDescription;
	private boolean inEstimates;
	
	public SearchableAttribute(String id, String userLabel, UserCategories userCategories, String spssName, String listDescription, boolean inEstimates)
	{
		this.id = id;
		this.userLabel = userLabel;
		this.userCategories = userCategories;
		this.inEstimates = inEstimates;
		this.listDescription = listDescription;
		this.spssName = spssName;
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

	public String getSpssName()
	{
		return spssName;
	}

	public String getListDescription()
	{
		return listDescription;
	}

	public boolean isInEstimates()
	{
		return inEstimates;
	}

	public abstract QueryCondition createQueryCondition();
	public abstract boolean addToConditions(boolean markErrors, Conditions conditions, QueryCondition queryCondition);
	public abstract QueryCondition restoreFromUrl(Session session, Map params);
	public abstract String getNonNullSqlQuerySelectPart(String voyagePrefix);

}