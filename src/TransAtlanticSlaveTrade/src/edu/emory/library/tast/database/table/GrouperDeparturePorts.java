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

import edu.emory.library.tast.common.table.GrouperSimpleDictionary;
import edu.emory.library.tast.common.table.Label;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

/**
 * Grouper for ports attribute.
 * Groupers are used in tables.
 *
 */
public class GrouperDeparturePorts extends GrouperSimpleDictionary
{
	
	private Label[] labels;
	private List ports;
	private Map lookupTable;
	
	public GrouperDeparturePorts(int resultIndex, boolean omitEmpty, List ports)
	{
		super(resultIndex, omitEmpty, ports);
		this.ports = ports;
	}
	
	public Attribute getGroupingAttribute()
	{
		 return new SequenceAttribute (new Attribute[] {
					Voyage.getAttribute("ptdepimp"),
					Port.getAttribute("id")});
	}

	
	public Attribute[] addExtraAttributes(int index)
	{
		return new Attribute[] {};
	}
	
	public void initSlots(Object[] dataTable)
	{

		Set portsIdsInTable = new HashSet();
		if (omitEmpty)
		{
			for (int i = 0; i < dataTable.length; i++)
			{
				Long regionId = getRowId(((Object[]) dataTable[i]));
				portsIdsInTable.add(regionId);
			}
		}
		
		lookupTable = new HashMap();
		
		List regions = new ArrayList();
		List portsInRegion = new ArrayList();
		
		int i = 0;
		long lastRegionId = 0;
		Label regionLabel = null;
		
		for (Iterator iter = ports.iterator(); iter.hasNext();)
		{
			Port port = (Port) iter.next();
			Long portId = port.getId();
			long regionId = port.getRegion().getId().longValue();
			
			if (!omitEmpty || portsIdsInTable.contains(portId))
			{
				
				if (regionId != lastRegionId)
				{
					if (regionLabel != null)
					{
						Label[] regionsArray = new Label[portsInRegion.size()];
						portsInRegion.toArray(regionsArray);
						regionLabel.setBreakdown(regionsArray);
					}
					regionLabel = new Label(port.getRegion().getName());
					regions.add(regionLabel);
					portsInRegion.clear();
					lastRegionId = regionId;
				}
				
				portsInRegion.add(new Label(port.getName()));
				lookupTable.put(portId, new Integer(i));
				i++;
				
			}

		}

		if (regionLabel != null)
		{
			Label[] regionsArray = new Label[portsInRegion.size()];
			portsInRegion.toArray(regionsArray);
			regionLabel.setBreakdown(regionsArray);
		}

		labels = new Label[regions.size()];
		regions.toArray(labels);

	}

	public int lookupIndex(Object[] dataRow)
	{
		Long portId = getRowId(dataRow);
		if (lookupTable.get(portId) == null) return 0;
		return ((Integer) lookupTable.get(portId)).intValue();
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