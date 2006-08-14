package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionList;
import edu.emory.library.tast.ui.search.query.QueryConditionListItem;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeLocation extends SearchableAttribute implements ListItemsSource
{

	private Location[] locations;

	public SearchableAttributeLocation(String id, String userLabel, UserCategory userCategory, Location[] locations)
	{
		super(id, userLabel, userCategory);
		this.locations = locations != null ? locations : new Location[0];
	}

	public int getLocationsCount()
	{
		return locations.length;
	}

	public Location[] getLocations()
	{
		return locations;
	}

	public QueryCondition createQueryCondition()
	{
		QueryConditionList queryConditionList = new QueryConditionList(getId());
		queryConditionList.setAutoSelection(true);
		return queryConditionList;
	}

	public boolean addToConditions(boolean markErrors, Conditions conditions, QueryCondition queryCondition)
	{
		return false;
	}
	
	public QueryConditionListItem[] getAvailableItems()
	{
		return new QueryConditionListItem[] {
				new QueryConditionListItem("a", "Europe", new QueryConditionListItem[] {
						new QueryConditionListItem("1", "Germany"),
						new QueryConditionListItem("2", "Great Britain", new QueryConditionListItem[] {
								new QueryConditionListItem("x", "England"),
								new QueryConditionListItem("y", "Ireland"),
								new QueryConditionListItem("z", "Scottland")
						}),
						new QueryConditionListItem("3", "France")
				}),
				new QueryConditionListItem("b", "Africa",
						new QueryConditionListItem[] {
						new QueryConditionListItem("1", "Kenya"),
						new QueryConditionListItem("2", "Angola"),
						new QueryConditionListItem("3", "Congo"),
						new QueryConditionListItem("4", "Uganda"),
						new QueryConditionListItem("5", "Zambia"),
						new QueryConditionListItem("6", "Zimbabwe")
				}),
				new QueryConditionListItem("c", "America",
						new QueryConditionListItem[] {
						new QueryConditionListItem("a", "USA"),
						new QueryConditionListItem("b", "Mexico"),
						new QueryConditionListItem("c", "Canada")
				})
		};
	}

	public QueryConditionListItem getItemByFullId(String id)
	{
		return null;
	}

}
