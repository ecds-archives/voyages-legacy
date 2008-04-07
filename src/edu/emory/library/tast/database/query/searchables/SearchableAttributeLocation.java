package edu.emory.library.tast.database.query.searchables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.emory.library.tast.database.query.QueryBuilderComponent;
import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.database.query.QueryConditionList;
import edu.emory.library.tast.database.query.QueryConditionListItem;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeLocation extends SearchableAttribute implements ListItemsSource
{

	private Location[] locations;

	public SearchableAttributeLocation(String id, String userLabel, UserCategories userCategories, Location[] locations, String spssName, String listDescription, boolean inEstimates)
	{
		super(id, userLabel, userCategories, spssName, listDescription, inEstimates);
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
	
	private Long extractPortId(String fullId)
	{
		if (fullId == null || fullId.length() == 0)
		{
			return null;
		}
		else
		{
			String[] ids = fullId.split(QueryBuilderComponent.ID_SEPARATOR);
			String lastId = ids[ids.length - 1];
			if (lastId.startsWith("P"))
			{
				return new Long(lastId.substring(1));  
			}
			else
			{
				return null;
			}
		}
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
			Long portId = extractPortId((String) iter.next());
			if (portId != null)
			{
				for (int j = 0; j < locations.length; j++)
				{
					subCond.addCondition(
							new SequenceAttribute(new Attribute[] {locations[j].getPort(), Port.getAttribute("id")}),
							portId, Conditions.OP_EQUALS);
				}
			}
		}
		conditions.addCondition(subCond);
		
		return true;

	}
	
	public QueryConditionListItem[] getAvailableItems(Session session)
	{
		
		String attrName = locations[0].getPort().getName();

		String hsql =
			"from Port p " +
			"where p in (select v." + attrName + " from Voyage v) " +
			"order by p.region.area.order, p.region.order, p.order";
		
		Query query = session.createQuery(hsql);
		List portsDb = query.list();
		
		int portsCount = portsDb.size();

		Port port = (Port) portsDb.get(0);
		Region region = port.getRegion();
		Area area = region.getArea();
		
		List tmpAreas = new ArrayList();
		List tmpRegions = new ArrayList();
		List tmpPorts = new ArrayList();

		int i = 0;
		while (i < portsCount)
		{
			Area groupArea = area;
			
			QueryConditionListItem areaItem = new QueryConditionListItem();
			areaItem.setId("A" + area.getId());
			areaItem.setText(area.getName());
			areaItem.setExpandable(true);
			areaItem.setExpanded(false);
			
			tmpAreas.add(areaItem);
			tmpRegions.clear();

			while (i < portsCount && groupArea.equals(area))
			{
				Region groupRegion = region;
				
				QueryConditionListItem regionItem = new QueryConditionListItem();
				regionItem.setId("R" + region.getId());
				regionItem.setText(region.getName());
				regionItem.setExpandable(true);
				regionItem.setExpanded(false);
				
				tmpRegions.add(regionItem);
				tmpPorts.clear();

				while (i < portsCount && groupRegion.equals(region))
				{
					
					tmpPorts.add(new QueryConditionListItem(
							"P" + port.getId(),
							port.getName()));

					if (++i < portsCount)
					{
						port = (Port) portsDb.get(i);
						region = port.getRegion();
						area = region.getArea();
					}

				}
				
				// 6/12/2006 disabled
//				if (tmpPorts.size() == 1 && false) 
//				{
//					QueryConditionListItem singlePort = (QueryConditionListItem) tmpPorts.get(0);
//					regionItem.setText(singlePort.getText());
//					regionItem.setId(singlePort.getId());
//				}
//				else
//				{
//					QueryConditionListItem portItems[] = new QueryConditionListItem[tmpPorts.size()];
//					tmpPorts.toArray(portItems);
//					regionItem.setChildren(portItems);
//				}

				QueryConditionListItem portItems[] = new QueryConditionListItem[tmpPorts.size()];
				tmpPorts.toArray(portItems);
				regionItem.setChildren(portItems);

			}
			
			QueryConditionListItem regionItems[] = new QueryConditionListItem[tmpRegions.size()];
			tmpRegions.toArray(regionItems);
			areaItem.setChildren(regionItems);
			
		}
		
		QueryConditionListItem areaItems[] = new QueryConditionListItem[tmpAreas.size()];
		tmpAreas.toArray(areaItems);

		return areaItems;

	}
	
/*	
	public QueryConditionListItem[] getAvailableItems1(Session session)
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
*/
	public QueryConditionListItem getItemByFullId(Session session, String fullId)
	{
		Long portId = extractPortId(fullId);
		if (portId != null)
		{
			Port port = Port.loadById(session, portId.longValue());
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
