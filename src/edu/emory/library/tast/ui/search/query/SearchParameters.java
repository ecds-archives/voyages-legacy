package edu.emory.library.tast.ui.search.query;


import edu.emory.library.tast.ui.search.query.searchables.UserCategory;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttribute;
import edu.emory.library.tast.util.query.Conditions;

/**
 * Used by the bean {@link edu.emory.library.tast.ui.search.query.SearchBean} in order to
 * pass search parameters collected from
 * {@link edu.emory.library.tast.ui.search.query.QueryBuilderComponent}. It contains the
 * current query, the list of attributes (since we need the columns to the
 * results) and the type of the user category.
 * 
 * @author Jan Zich
 * 
 */
public class SearchParameters
{
	
	private Conditions conditions;
	private VisibleAttribute[] columns = new VisibleAttribute[0]; 
	private UserCategory category = UserCategory.Beginners;
	
	
	public SearchParameters() {
		this.conditions = null;
	}
	
	public SearchParameters(Conditions conditions) {
		this.conditions = conditions;
	}

	public VisibleAttribute[] getColumns()
	{
		return columns;
	}
	
	public void setColumns(VisibleAttribute[] columns)
	{
		this.columns = columns;
	}
	
	public Conditions getConditions()
	{
		return conditions;
	}
	
	public void setConditions(Conditions conditions)
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

}
