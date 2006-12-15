package edu.emory.library.tast.estimates.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesNation;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

public class GrouperNations extends Grouper
{
	
	private Label[] labels;
	private List nations;
	private Map lookupTable;
	
	public GrouperNations(int resultIndex, boolean omitEmpty, List nations)
	{
		super(resultIndex, omitEmpty);
		this.nations = nations;
	}
	
	public Attribute getGroupingAttribute()
	{
		 return new SequenceAttribute (new Attribute[] {
				 Estimate.getAttribute("nation"),
				 EstimatesNation.getAttribute("id")});
	}

	public Attribute[] addExtraAttributes(int index)
	{
		return new Attribute[] {};
	}

	public void initSlots(Object[] dataTable)
	{

		Set listedNationIds = new HashSet();
		for (int i = 0; i < dataTable.length; i++)
		{
			Object nationId = ((Object[]) dataTable[i])[resultIndex];
			listedNationIds.add(nationId);
		}
		
		int noOfNations = omitEmpty ? 
				listedNationIds.size() :
					nations.size();

		lookupTable = new HashMap();
		labels = new Label[noOfNations];
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			EstimatesNation nation = (EstimatesNation) iter.next();
			Long nationId = nation.getId();
			if (!omitEmpty || listedNationIds.contains(nationId))
			{
				labels[i] = new Label(nation.getName());
				lookupTable.put(nationId, new Integer(i));
				i++;
			}
		}

	}

	public int lookupIndex(Object[] dataRow)
	{
		Long nationId = (Long) dataRow[resultIndex];
		return ((Integer) lookupTable.get(nationId)).intValue();
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