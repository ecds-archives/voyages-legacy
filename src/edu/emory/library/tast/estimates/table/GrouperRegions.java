package edu.emory.library.tast.estimates.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

public class GrouperRegions extends Grouper
{
	
	private String[] labels;
	private List regions;
	private Map lookupTable;
	
	public GrouperRegions(int resultIndex, boolean omitEmpty, boolean exportRegions, List regions)
	{
		super(resultIndex, omitEmpty, new SequenceAttribute (new Attribute[] {
				Estimate.getAttribute(exportRegions ? "expRegion" : "impRegion"),
				Region.getAttribute("id")}));
		this.regions = regions;
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
		
		int noOfRegions = omitEmpty ? 
				regionsIdsInTable.size() :
					regions.size();
		
		labels = new String[noOfRegions];
		lookupTable = new HashMap();
		
		int i = 0;
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			Long regionId = region.getId();
			if (!omitEmpty || regionsIdsInTable.contains(regionId))
			{
				labels[i] = region.getName();
				lookupTable.put(regionId, new Integer(i));
				i++;
			}
		}

	}

	public int lookupIndex(Object[] dataRow)
	{
		Object regionId = dataRow[resultIndex];
		return ((Integer) lookupTable.get(regionId)).intValue();
	}

	public int getSlotsCount()
	{
		return labels.length;
	}

	public String[] getLabels()
	{
		return labels;
	}

}