package edu.emory.library.tast.ui.search.query;

import edu.emory.library.tast.dm.attributes.AbstractAttribute;
import edu.emory.library.tast.dm.attributes.VisibleColumn;
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
	private VisibleColumn[] columns = new VisibleColumn[0]; 
	private int category = AbstractAttribute.CATEGORY_GENERAL;
	
	
	public SearchParameters() {
		this.conditions = null;
	}
	
	public SearchParameters(Conditions conditions2) {
		this.conditions = conditions2;
	}

	public VisibleColumn[] getColumns()
	{
		return columns;
	}
	
	public void setColumns(VisibleColumn[] columns)
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

	public int getCategory()
	{
		return category;
	}

	public void setCategory(int category)
	{
		this.category = category;
	}

}
