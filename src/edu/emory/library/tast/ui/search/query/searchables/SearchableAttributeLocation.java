package edu.emory.library.tast.ui.search.query.searchables;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
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

		// check
		if (!(queryCondition instanceof QueryConditionList))
			throw new IllegalArgumentException("expected QueryConditionList"); 
		
		// cast
		QueryConditionList queryConditionList =
			(QueryConditionList) queryCondition;
		
		// is empty -> no db condition
		if (queryConditionList.getSelectedIdsCount() == 0)
			return true;
		
		// add locations to the query
		Conditions subCond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = queryConditionList.getSelectedIds().iterator(); iter.hasNext();)
		{
			String[] ids = ((String) iter.next()).split(":");
			if (ids.length == 3)
			{
				long id = Long.parseLong(ids[2]);
				for (int j = 0; j < locations.length; j++)
					subCond.addCondition(
							new SequenceAttribute(new Attribute[] {locations[j].getPort(), Port.getAttribute("id")}),
							new Long(id), Conditions.OP_EQUALS);
			}
		}
		conditions.addCondition(subCond);
		
		return true;

	}
	
	public QueryConditionListItem[] getAvailableItems(Session session)
	{
		
		List areas = Area.loadAll(session);
		QueryConditionListItem[] areasItems = new QueryConditionListItem[areas.size()];

		int k = 0;
		for (Iterator areaIter = areas.iterator(); areaIter.hasNext();)
		{
			Area area = (Area) areaIter.next();
			
			QueryConditionListItem areaItem = new QueryConditionListItem(
					String.valueOf(area.getId()),
					area.getName());
			
			QueryConditionListItem[] regionsItems =
				new QueryConditionListItem[area.getRegions().size()];

			int i = 0;
			for (Iterator regionIter = area.getRegions().iterator(); regionIter.hasNext();)
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
				regionsItems[i++] = regionItem;

			}
			
			areaItem.setChildren(regionsItems);
			areasItems[k++] = areaItem;

		}
		
		return areasItems;

	}

	public QueryConditionListItem getItemByFullId(Session session, String id)
	{
		String[] ids = id.split(":");
		if (ids.length == 3)
		{
			Port port = Port.loadById(session, Long.parseLong(ids[2]));
			return new QueryConditionListItem(
					port.getId().toString(),
					port.getRegion().getName() + " / " + port.getName());
		}
		else
		{
			return null;
		}
	}

}
