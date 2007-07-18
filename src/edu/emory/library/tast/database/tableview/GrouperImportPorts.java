package edu.emory.library.tast.database.tableview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.common.tableview.GrouperSimpleDictionary;
import edu.emory.library.tast.common.tableview.Label;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

public class GrouperImportPorts extends GrouperSimpleDictionary
{
	
	private Label[] labels;
	private List ports;
	private Map lookupTable;
	
	public GrouperImportPorts(int resultIndex, boolean omitEmpty, List regions)
	{
		super(resultIndex, omitEmpty, regions);
		this.ports = regions;
	}
	
	public Attribute getGroupingAttribute()
	{
		 return new SequenceAttribute (new Attribute[] {
					Voyage.getAttribute("mjslptimp"),
					Region.getAttribute("id")});
	}

	public Attribute[] addExtraAttributes(int index)
	{
		return new Attribute[] {};
	}
	
	public void initSlots(Object[] dataTable)
	{

		Set regionsIdsInTable = new HashSet();
		if (omitEmpty)
		{
			for (int i = 0; i < dataTable.length; i++)
			{
				Object regionId = ((Object[]) dataTable[i])[resultIndex];
				regionsIdsInTable.add(regionId);
			}
		}
		
		lookupTable = new HashMap();
		
		List areas = new ArrayList();
		List regionsInArea = new ArrayList();
		
		int i = 0;
		long lastAreaId = 0;
		Label areaLabel = null;
		
		for (Iterator iter = ports.iterator(); iter.hasNext();)
		{
			
			Port port = (Port) iter.next();
			long areaId = port.getRegion().getId().longValue();
			Long regionId = port.getId();
			
			if (!omitEmpty || regionsIdsInTable.contains(regionId))
			{
				
				if (areaId != lastAreaId)
				{
					if (areaLabel != null)
					{
						if (regionsInArea.size() > 1)
						{
							Label[] regionsArray = new Label[regionsInArea.size()];
							regionsInArea.toArray(regionsArray);
							areaLabel.setBreakdown(regionsArray);
						}
					}
					areaLabel = new Label(port.getRegion().getName());
					areas.add(areaLabel);
					regionsInArea.clear();
					lastAreaId = areaId;
				}
				
				regionsInArea.add(new Label(port.getName()));
				lookupTable.put(regionId, new Integer(i));
				i++;
				
			}

		}

		if (areaLabel != null)
		{
			if (regionsInArea.size() > 1)
			{
				Label[] regionsArray = new Label[regionsInArea.size()];
				regionsInArea.toArray(regionsArray);
				areaLabel.setBreakdown(regionsArray);
			}
		}

		labels = new Label[areas.size()];
		areas.toArray(labels);

	}

	public int lookupIndex(Object[] dataRow)
	{
		Object regionId = dataRow[resultIndex];
		if (lookupTable.get(regionId) == null) {
			return 0;
		}
		return ((Integer) lookupTable.get(regionId)).intValue();
	}

	public int getLeaveLabelsCount()
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