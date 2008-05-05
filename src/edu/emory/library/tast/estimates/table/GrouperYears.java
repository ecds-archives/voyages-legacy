package edu.emory.library.tast.estimates.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.common.table.Grouper;
import edu.emory.library.tast.common.table.Label;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;

public class GrouperYears extends Grouper
{
	
	private int period;
	private Label[] labels;
	private Map lookupTable;
	private int minYearInPeriodResultIndex = -1;
	private int maxYearInPeriodResultIndex = -1;
	
	public GrouperYears(int resultIndex, boolean omitEmpty, int period)
	{
		super(resultIndex, omitEmpty);
		this.period = period;
	}
	
	public Attribute getGroupingAttribute()
	{
		if (period > 1)
			return new FunctionAttribute("round_to_multiple",
					new Attribute[] {Estimate.getAttribute("year"),
					new DirectValueAttribute("period", new Integer(period))});
		else
			return Estimate.getAttribute("year");
	}

	public Attribute[] addExtraAttributes(int index)
	{
		minYearInPeriodResultIndex = index + 0;
		maxYearInPeriodResultIndex = index + 1;
		return new Attribute[] {
				new FunctionAttribute("min", new Attribute[] {Estimate.getAttribute("year")}),
				new FunctionAttribute("max", new Attribute[] {Estimate.getAttribute("year")})};
	}

	public void initSlots(Object[] dataTable)
	{
		
		int minYearRounded = Integer.MAX_VALUE;
		int maxYearRounded = Integer.MIN_VALUE;
		int minYear = Integer.MAX_VALUE;
		int maxYear = Integer.MIN_VALUE;

		Set yearsInTable = new HashSet();
		for (int i = 0; i < dataTable.length; i++)
		{
			Object[] row = (Object[]) dataTable[i];
			
			Integer yearRounded = (Integer) row[resultIndex];
			Integer minYearInPeriod = (Integer) row[minYearInPeriodResultIndex];
			Integer maxYearInPeriod = (Integer) row[maxYearInPeriodResultIndex];
			
			int yearIntRounded = yearRounded.intValue(); 
			int minYearInPeriodInt = minYearInPeriod.intValue(); 
			int maxYearInPeriodInt = maxYearInPeriod.intValue(); 
			
			if (omitEmpty) yearsInTable.add(yearRounded);
			
			if (yearIntRounded < minYearRounded) minYearRounded = yearIntRounded;
			if (maxYearRounded < yearIntRounded) maxYearRounded = yearIntRounded;
			
			if (minYearInPeriodInt < minYear) minYear = minYearInPeriodInt;
			if (maxYear < maxYearInPeriodInt) maxYear = maxYearInPeriodInt;

		}
		
		int periods = dataTable.length == 0 ?
				0 : (maxYearRounded - minYearRounded) / period + 1;

		int slots = omitEmpty ?
				yearsInTable.size() : periods;
				
		labels = new Label[slots];
		lookupTable = new HashMap();

		int j = 0;
		for (int i = 0; i < periods; i++)
		{
			int year = minYearRounded + i * period;
			if (!omitEmpty || yearsInTable.contains(new Integer(year)))
			{
				lookupTable.put(new Integer(year), new Integer(j));
				if (period == 1)
				{
					labels[j] = new Label(String.valueOf(year));
				}
				else
				{
					if (i == 0)
					{
						if (minYear == year + period)
						{
							labels[j] = new Label(String.valueOf(minYear)); 
						}
						else
						{
							labels[j] = new Label(minYear + "-" + (year + period));
						}
					}
					else if (i == periods - 1)
					{
						if (year + 1 == maxYear)
						{
							labels[j] = new Label(String.valueOf(maxYear)); 
						}
						else
						{
							labels[j] = new Label((year + 1) + "-" + maxYear);
						}
					}
					else
					{
						labels[j] = new Label((year + 1) + "-" +  (year + period));
					}
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
