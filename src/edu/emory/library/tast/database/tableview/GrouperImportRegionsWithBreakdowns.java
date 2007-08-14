package edu.emory.library.tast.database.tableview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.common.tableview.Grouper;
import edu.emory.library.tast.common.tableview.Label;
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
						if (regionsInArea.size() > 1)
						{
							Label[] regionsArray = new Label[regionsInArea.size()];
							regionsInArea.toArray(regionsArray);
							areaLabel.setBreakdown(regionsArray);
						}
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