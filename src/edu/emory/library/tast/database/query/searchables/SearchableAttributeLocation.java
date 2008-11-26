package edu.emory.library.tast.database.query.searchables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.emory.library.tast.SimpleCache;
import edu.emory.library.tast.database.query.QueryBuilderComponent;
import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.database.query.QueryConditionList;
import edu.emory.library.tast.database.query.QueryConditionListItem;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.StringUtils;

public class SearchableAttributeLocation extends SearchableAttribute implements ListItemsSource
{

	private Location[] locations;
	
	private static final boolean USE_DENORMALIZED_COLUMNS = true;
	private static final boolean USE_IN_OPERATOR = true;

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
		
		if (StringUtils.isNullOrEmpty(fullId))
			return null;

		String[] ids = fullId.split(QueryBuilderComponent.ID_SEPARATOR);
		String lastId = ids[ids.length - 1];
		
		if (!lastId.startsWith("P"))
			return null;
		
		try {return new Long(lastId.substring(1));}
		catch (NumberFormatException nfe) {return null;}
		
	}
	
	private Long[] extractLocationIds(String fullId)
	{

		if (StringUtils.isNullOrEmpty(fullId))
			return null;
		
		String[] idComponents = fullId.split(QueryBuilderComponent.ID_SEPARATOR);
		
		if (idComponents.length < 1 || 3 < idComponents.length)
			return null;
		
		Long[] splitIds = new Long[idComponents.length];
		
		if (idComponents[0] != null && idComponents[0].startsWith("A"))
		{
			try {splitIds[0] = new Long(idComponents[0].substring(1));}
			catch (NumberFormatException nfe) {return null;}
		}
		else
		{
			return null;
		}

		if (idComponents.length > 1)
		{
			if (idComponents[1] != null && idComponents[1].startsWith("R"))
			{
				try {splitIds[1] = new Long(idComponents[1].substring(1));}
				catch (NumberFormatException nfe) {return null;}
			}
			else
			{
				return null;
			}
		}
		
		if (idComponents.length > 2)
		{
			if (idComponents[2] != null && idComponents[2].startsWith("P"))
			{
				try {splitIds[2] = new Long(idComponents[2].substring(1));}
				catch (NumberFormatException nfe) {return null;}
			}
			else
			{
				return null;
			}
		}

		return splitIds;

	}

	public boolean addToConditions(boolean markErrors, TastDbConditions conditions, QueryCondition queryCondition)
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
		
		// extra just the minimal set to create reasonable queries
		Set minimalSelectedIds = queryConditionList.getMinimalSelectedIds();

		// add locations to the query
		TastDbConditions subCond = new TastDbConditions(TastDbConditions.OR);
		if (USE_IN_OPERATOR)
		{
			
			ArrayList portIdsList = new ArrayList();
			ArrayList regionIdsList = new ArrayList();
			ArrayList areaIdsList = new ArrayList();
			
			for (Iterator iter = minimalSelectedIds.iterator(); iter.hasNext();)
			{
				String fullId = (String) iter.next();
				Long[] idComponents = extractLocationIds(fullId);
				if (idComponents != null && idComponents.length == 3)
				{
					portIdsList.add(idComponents[2]);
				}
				else if (idComponents != null && idComponents.length == 2)
				{
					regionIdsList.add(idComponents[1]);
				}
				else if (idComponents != null && idComponents.length == 1)
				{
					areaIdsList.add(idComponents[0]);
				}
			}
			
			Long[] portIds = new Long[portIdsList.size()];
			Long[] regionIds = new Long[regionIdsList.size()];
			Long[] areaIds = new Long[areaIdsList.size()];

			portIdsList.toArray(portIds);
			regionIdsList.toArray(regionIds);
			areaIdsList.toArray(areaIds);
			
			for (int j = 0; j < locations.length; j++)
			{
				
				if (!USE_DENORMALIZED_COLUMNS)
				{
				
					subCond.addCondition(
							new SequenceAttribute(new Attribute[] {
									locations[j].getPort(),
									Port.getAttribute("region"),
									Region.getAttribute("area"),
									Area.getAttribute("id")}),
									areaIds,
									TastDbConditions.OP_IN);
					
					subCond.addCondition(
							new SequenceAttribute(new Attribute[] {
									locations[j].getPort(),
									Port.getAttribute("region"),
									Region.getAttribute("id")}),
									regionIds,
									TastDbConditions.OP_IN);
					
					subCond.addCondition(
							new SequenceAttribute(new Attribute[] {
									locations[j].getPort(),
									Port.getAttribute("id")}),
									areaIds,
									TastDbConditions.OP_IN);
					
				}
				else
				{
					
					if (areaIds.length != 0)
						subCond.addCondition(
								Voyage.getAttribute(locations[j].getPort().getName() + "_area"),
								areaIds,
								TastDbConditions.OP_IN);					
					
					if (regionIds.length != 0)
						subCond.addCondition(
								Voyage.getAttribute(locations[j].getPort().getName() + "_region"),
								regionIds,
								TastDbConditions.OP_IN);
					
					if (portIds.length != 0)
						subCond.addCondition(
								Voyage.getAttribute(locations[j].getPort().getName() + "_port"),
								portIds,
								TastDbConditions.OP_IN);						
					
				}

			}

		}
		else
		{
			for (Iterator iter = minimalSelectedIds.iterator(); iter.hasNext();)
			{
				String fullId = (String) iter.next();
				Long[] idComponents = extractLocationIds(fullId);
				if (idComponents != null && idComponents.length == 1)
				{
					for (int j = 0; j < locations.length; j++)
					{
						if (!USE_DENORMALIZED_COLUMNS)
						{
							subCond.addCondition(
									new SequenceAttribute(new Attribute[] {
											locations[j].getPort(),
											Port.getAttribute("region"),
											Region.getAttribute("area"),
											Area.getAttribute("id")}),
											idComponents[0],
											TastDbConditions.OP_EQUALS);
						}
						else
						{
							subCond.addCondition(
									Voyage.getAttribute(locations[j].getPort().getName() + "_area"),
									idComponents[0],
									TastDbConditions.OP_EQUALS);						
						}
					}
				}
				else if (idComponents != null && idComponents.length == 2)
				{
					for (int j = 0; j < locations.length; j++)
					{
						if (!USE_DENORMALIZED_COLUMNS)
						{
							subCond.addCondition(
									new SequenceAttribute(new Attribute[] {
											locations[j].getPort(),
											Port.getAttribute("region"),
											Region.getAttribute("id")}),
											idComponents[1],
											TastDbConditions.OP_EQUALS);
						}
						else
						{
							subCond.addCondition(
									Voyage.getAttribute(locations[j].getPort().getName() + "_region"),
									idComponents[1],
									TastDbConditions.OP_EQUALS);						
						}
					}
				}
				else if (idComponents != null && idComponents.length == 3)
				{
					for (int j = 0; j < locations.length; j++)
					{
						if (!USE_DENORMALIZED_COLUMNS)
						{
							subCond.addCondition(
									new SequenceAttribute(new Attribute[] {
											locations[j].getPort(),
											Port.getAttribute("id")}),
											idComponents[2],
											TastDbConditions.OP_EQUALS);
						}
						else
						{
							subCond.addCondition(
									Voyage.getAttribute(locations[j].getPort().getName() + "_port"),
									idComponents[2],
									TastDbConditions.OP_EQUALS);						
						}
					}
				}
			}
		}
		if (!subCond.isEmpty()) conditions.addCondition(subCond);
		
		return true;

	}
	
	public synchronized QueryConditionListItem[] getAvailableItems(Session session)
	{
		
		QueryConditionListItem[] queryConditionItems =
			(QueryConditionListItem[]) SimpleCache.get(
					SimpleCache.VOYAGES_PREFIX + getId());
		
		if (queryConditionItems == null)
		{
			
			StringBuffer hql = new StringBuffer();
			
			hql.append("from Port p ");
			hql.append("where ");
			hql.append("(");
			for (int i = 0; i < locations.length; i++)
			{
				if (i > 0) hql.append(" or ");
				hql.append("p in (select v.");
				hql.append(locations[i].getPort().getName());
				hql.append(" from Voyage v)");
			}
			hql.append(") ");
			hql.append("order by p.region.area.order, p.region.order, p.order");
			
			Query query = session.createQuery(hql.toString());
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
			
			queryConditionItems = areaItems;
			SimpleCache.set(
					SimpleCache.VOYAGES_PREFIX + getId(),
					queryConditionItems);
			
		}
		
		return queryConditionItems;

	}
	
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

	public String getNonNullSqlQuerySelectPart(String prefix)
	{
		StringBuffer select = new StringBuffer();
		select.append("COALESCE(");
		for (int i = 0; i < locations.length; i++)
		{
			if (i > 0) select.append(" + ");
			Location location = locations[i];
			if (location.getRegion() != null)
			{
				if (prefix != null) select.append(prefix).append(".");
				select.append(location.getRegion().getName());
			}
			if (location.getRegion() != null && location.getPort() != null)
			{
				select.append(", ");
			}
			if (location.getPort() != null)
			{
				if (prefix != null) select.append(prefix).append(".");
				select.append(location.getPort().getName());
			}
		}
		select.append(")");
		return select.toString();
	}

	public QueryCondition restoreFromUrl(Session session, Map params)
	{
		
		String urlValue = StringUtils.getFirstElement((String[]) params.get(getId()));
		if (StringUtils.isNullOrEmpty(urlValue))
			return null;
		
		QueryConditionList queryCondition = (QueryConditionList) createQueryCondition();
		
		String[] idsStrArr = urlValue.split("\\s*[,\\.]\\s*");
		for (int i = 0; i < idsStrArr.length; i++)
		{
			long id = -1;
			try {id = Long.parseLong(idsStrArr[i]);}
			catch (NumberFormatException nfe){}
			if (0 <= id && id <= 999999)
			{
				if (id % 10000 == 0)
				{
					queryCondition.addId("A" + id);
				}
				else if (id % 100 == 0)
				{
					queryCondition.addId(
							"A" + (id / 10000) * 10000 +
							QueryBuilderComponent.ID_SEPARATOR +
							"R" + id);
				}
				else
				{
					queryCondition.addId(
						"A" + (id / 10000) * 10000 +
						QueryBuilderComponent.ID_SEPARATOR +
						"R" + (id / 100) * 100 +
						QueryBuilderComponent.ID_SEPARATOR +
						"P" + id);
				}
			}
		}
		
		return queryCondition;

	}

	public Long getItemRealId(String fullId)
	{
		
		if (StringUtils.isNullOrEmpty(fullId))
			return null;
		
		String[] ids = fullId.split(QueryBuilderComponent.ID_SEPARATOR);
		String lastId = ids[ids.length - 1];

		if (lastId.startsWith("P") || lastId.startsWith("R") || lastId.startsWith("A"))
			return new Long(lastId.substring(1));
		
		return null;
		
	}
	
}
