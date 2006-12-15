package edu.emory.library.tast.estimates.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.DictionaryOrdered;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

public class GrouperExportRegions extends Grouper
{
	
	private Label[] labels;
	private List regions;
	private Map lookupTable;
	
	public GrouperExportRegions(int resultIndex, boolean omitEmpty, List regions)
	{
		super(resultIndex, omitEmpty);
		this.regions = regions;
	}
	
	public Attribute getGroupingAttribute()
	{
		 return new SequenceAttribute (new Attribute[] {
					Estimate.getAttribute("expRegion"),
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
		
		int noOfRegions = omitEmpty ? 
				regionsIdsInTable.size() :
					regions.size();
		
		labels = new Label[noOfRegions];
		lookupTable = new HashMap();
		
		int i = 0;
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			DictionaryOrdered region = (DictionaryOrdered) iter.next();
			Long regionId = region.getId();
			if (!omitEmpty || regionsIdsInTable.contains(regionId))
			{
				labels[i] = new Label(region.getName());
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

	public int getLeaveLabelsCount()
	{
		return labels.length;
	}

	public int getBreakdownDepth()
	{
		return 1;
	}

	public Label[] getLabels()
	{
		return labels;
	}

}