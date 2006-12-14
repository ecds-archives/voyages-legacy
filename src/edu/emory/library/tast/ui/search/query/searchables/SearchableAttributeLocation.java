package edu.emory.library.tast.ui.search.query.searchables;

import java.util.Iterator;
import java.util.List;

import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionList;
import edu.emory.library.tast.ui.search.query.QueryConditionListItem;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeLocation extends SearchableAttribute implements ListItemsSource
{

	private Location[] locations;

	public SearchableAttributeLocation(String id, String userLabel, UserCategories userCategories, Location[] locations)
	{
		super(id, userLabel, userCategories);
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
		
		List regions = Region.loadAll(null);
		QueryConditionListItem[] regionItems = new QueryConditionListItem[regions.size()];
		
		int i = 0;
		for (Iterator regionIter = regions.iterator(); regionIter.hasNext();)
		{

			Region region = (Region) regionIter.next();
			
			QueryConditionListItem regionItem = new QueryConditionListItem(
					String.valueOf(region.getId()),
					region.getName());
			
			QueryConditionListItem[] portItems =
				new QueryConditionListItem[region.getPorts().size()];

			int j = 0;
			for (Iterator iterPort = region.getPorts().iterator(); iterPort.hasNext();)
			{
				Port port = (Port) iterPort.next();
				portItems[j++] = new QueryConditionListItem(
						String.valueOf(port.getId()),
						port.getName());
			}
			
			regionItem.setChildren(portItems);
			regionItems[i++] = regionItem;

		}
		
		return regionItems;

//		return new QueryConditionListItem[] {
//				new QueryConditionListItem("a", "Europe", new QueryConditionListItem[] {
//						new QueryConditionListItem("1", "Germany"),
//						new QueryConditionListItem("2", "Great Britain", new QueryConditionListItem[] {
//								new QueryConditionListItem("x", "England"),
//								new QueryConditionListItem("y", "Ireland"),
//								new QueryConditionListItem("z", "Scottland")
//						}),
//						new QueryConditionListItem("3", "France")
//				}),
//				new QueryConditionListItem("b", "Africa",
//						new QueryConditionListItem[] {
//						new QueryConditionListItem("1", "Kenya"),
//						new QueryConditionListItem("2", "Angola"),
//						new QueryConditionListItem("3", "Congo"),
//						new QueryConditionListItem("4", "Uganda"),
//						new QueryConditionListItem("5", "Zambia"),
//						new QueryConditionListItem("6", "Zimbabwe")
//				}),
//				new QueryConditionListItem("c", "America",
//						new QueryConditionListItem[] {
//						new QueryConditionListItem("a", "USA"),
//						new QueryConditionListItem("b", "Mexico"),
//						new QueryConditionListItem("c", "Canada")
//				})
//		};
	}

	public QueryConditionListItem getItemByFullId(String id)
	{
		return null;
	}

}
