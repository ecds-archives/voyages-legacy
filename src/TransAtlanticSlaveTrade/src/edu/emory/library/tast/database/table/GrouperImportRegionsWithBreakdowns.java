/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.database.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.common.table.Grouper;
import edu.emory.library.tast.common.table.Label;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

/**
 * Grouper for import regions (import regions are broken down to ports).
 * Groupers are used in tables.
 *
 */
public class GrouperImportRegionsWithBreakdowns extends Grouper
{
	
	private Label[] labels;
	private List regions;
	private Map lookupTable;
	
	public GrouperImportRegionsWithBreakdowns(int resultIndex, boolean omitEmpty, List regions)
	{
		super(resultIndex, omitEmpty);
		this.regions = regions;
	}
	
	public Attribute getGroupingAttribute()
	{
		 return new SequenceAttribute (new Attribute[] {
					Voyage.getAttribute("mjselimp"),
					Region.getAttribute("id")});
	}

	public Attribute[] addExtraAttributes(int index)
	{
		return new Attribute[] {};
	}
	
	private Long getRowId(Object[] dataRow)
	{
		return new Long(((Number)dataRow[resultIndex]).longValue());
	}

	public void initSlots(Object[] dataTable)
	{

		Set regionsIdsInTable = new HashSet();
		if (omitEmpty)
		{
			for (int i = 0; i < dataTable.length; i++)
			{
				Long id = getRowId((Object[]) dataTable[i]);
				regionsIdsInTable.add(id);
			}
		}
		
		lookupTable = new HashMap();
		
		List areas = new ArrayList();
		List regionsInArea = new ArrayList();
		
		int i = 0;
		long lastAreaId = 0;
		Label areaLabel = null;
		
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			
			Region region = (Region) iter.next();
			long areaId = region.getArea().getId().longValue();
			Long regionId = region.getId();
			
			if (!omitEmpty || regionsIdsInTable.contains(regionId))
			{
				
				if (areaId != lastAreaId)
				{
					if (areaLabel != null)
					{
						Label[] regionsArray = new Label[regionsInArea.size()];
						regionsInArea.toArray(regionsArray);
						areaLabel.setBreakdown(regionsArray);
					}
					areaLabel = new Label(region.getArea().getName());
					areas.add(areaLabel);
					regionsInArea.clear();
					lastAreaId = areaId;
				}
				
				regionsInArea.add(new Label(region.getName()));
				lookupTable.put(regionId, new Integer(i));
				i++;
				
			}

		}

		if (areaLabel != null)
		{
			Label[] regionsArray = new Label[regionsInArea.size()];
			regionsInArea.toArray(regionsArray);
			areaLabel.setBreakdown(regionsArray);
		}

		labels = new Label[areas.size()];
		areas.toArray(labels);

	}

	public int lookupIndex(Object[] dataRow)
	{
		Long regionId = getRowId(dataRow);
		if (lookupTable.get(regionId) == null) return 0;
		return ((Integer) lookupTable.get(regionId)).intValue();
	}

	public int getLeafLabelsCount()
	{
		return lookupTable.size();
	}

	public int getBreakdownDepth()
	{
		return 2;
	}

	public Label[] getLabels()
	{
		return labels;
	}

}