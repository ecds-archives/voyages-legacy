package edu.emory.library.tast.ui.search.query;


import edu.emory.library.tast.ui.search.query.searchables.UserCategory;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;
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
	
	public static final int NOT_SPECIFIED = 0;
	public static final int MAP_PORTS = 1;
	public static final int MAP_REGIONS = 2;
	public static final int VALUES_ADJUSTED = 1;
	public static final int VALUES_RAW = 2;
	
	private Conditions conditions;
	private VisibleAttributeInterface[] columns = new VisibleAttributeInterface[0]; 
	private UserCategory category = UserCategory.Beginners;
	private int valuesType = NOT_SPECIFIED;
	private int mapElements = NOT_SPECIFIED;
	
	public SearchParameters()
	{
		this.conditions = null;
	}
	
	public SearchParameters(Conditions conditions)
	{
		this.conditions = conditions;
	}

	public VisibleAttributeInterface[] getColumns()
	{
		return columns;
	}
	
	public void setColumns(VisibleAttributeInterface[] columns)
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

	public int getValuesType()
	{
		return valuesType;
	}

	public void setValuesType(int displayAdjusted)
	{
		this.valuesType = displayAdjusted;
	}

	public int getMapElements()
	{
		return mapElements;
	}

	public void setMapElements(int mapElements)
	{
		this.mapElements = mapElements;
	}

}
