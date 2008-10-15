package edu.emory.library.tast.database.query;


import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.db.TastDbConditions;

/**
 * Used by the bean {@link edu.emory.library.tast.database.query.SearchBean} in order to
 * pass search parameters collected from
 * {@link edu.emory.library.tast.database.query.QueryBuilderComponent}. It contains the
 * current query, the list of attributes (since we need the columns to the
 * results) and the type of the user category.
 * 
 * @author Jan Zich
 * 
 */
public class SearchParameters
{
	
	private TastDbConditions conditions;
	private int numberOfResults;
	private UserCategory category = UserCategory.Beginners;
	
	public SearchParameters()
	{
		this.conditions = null;
		this.numberOfResults = 0;
	}

	public TastDbConditions getConditions()
	{
		return conditions;
	}
	
	public void setConditions(TastDbConditions conditions)
	{
		this.conditions = conditions;
	}

	public UserCategory getCategory()
	{
		return category;
	}

	public void setCategory(UserCategory category)
	{
		this.category = category;
	}

	public int getNumberOfResults()
	{
		return numberOfResults;
	}

	public void setNumberOfResults(int numberOfResults)
	{
		this.numberOfResults = numberOfResults;
	}

	public Object clone()
	{
		SearchParameters params = new SearchParameters();
		params.setConditions((TastDbConditions) this.conditions.clone());
		params.setNumberOfResults(numberOfResults);
		params.setCategory(category);
		return params;
	}

}
