package edu.emory.library.tast.estimates.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;

public class GrouperYears extends Grouper
{
	
	private int period;
	private String[] labels;
	private Map lookupTable;
	
	public GrouperYears(int resultIndex, boolean omitEmpty, int period)
	{
		super(resultIndex, omitEmpty, getGroupingAttribute(period));
		this.period = period;
	}
	
	private static Attribute getGroupingAttribute(int period)
	{
		if (period > 1)
			return new FunctionAttribute("round_to_multiple", new Attribute[] {
				Estimate.getAttribute("year"), new DirectValueAttribute("period", new Integer(period))});
		else
			return Estimate.getAttribute("year");
	}

	public void initSlots(Object[] dataTable)
	{
		
		int minYear = Integer.MAX_VALUE;
		int maxYear = Integer.MIN_VALUE;

		Set yearsInTable = new HashSet();
		for (int i = 0; i < dataTable.length; i++)
		{
			Integer year = (Integer) ((Object[]) dataTable[i])[resultIndex];
			int yearInt = year.intValue(); 
			if (omitEmpty) yearsInTable.add(year);
			if (yearInt < minYear) minYear = yearInt;
			if (maxYear < yearInt) maxYear = yearInt;
		}
		
		int periods = (maxYear - minYear) / period + 1;
		int slots = omitEmpty ? yearsInTable.size() : periods;
				
		labels = new String[slots];
		lookupTable = new HashMap();

		int j = 0;
		for (int i = 0; i < periods; i++)
		{
			int year = minYear + i * period;
			if (!omitEmpty || yearsInTable.contains(new Integer(year)))
			{
				lookupTable.put(new Integer(year), new Integer(j));
				if (period == 1)
				{
					labels[j] =
						String.valueOf(year);
				}
				else
				{
					labels[j] =
						String.valueOf(year + 1) + "<br>" + 
						String.valueOf(year + period);
				}
				j++;
			}
		}
		
	}

	public int lookupIndex(Object[] dataRow)
	{
		Object year = dataRow[resultIndex];
		return ((Integer) lookupTable.get(year)).intValue();
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
