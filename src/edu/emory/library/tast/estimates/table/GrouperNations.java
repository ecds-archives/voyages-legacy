package edu.emory.library.tast.estimates.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

public class GrouperNations extends Grouper
{
	
	private String[] labels;
	private List nations;
	private Map lookupTable;
	
	public GrouperNations(int resultIndex, boolean omitEmpty, List nations)
	{
		super(resultIndex, omitEmpty, new SequenceAttribute (new Attribute[] {
				Estimate.getAttribute("nation"),
				Nation.getAttribute("id")}));
		this.nations = nations;
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
		labels = new String[noOfNations];
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			Long nationId = new Long(nation.getId());
			if (!omitEmpty || listedNationIds.contains(nationId))
			{
				labels[i] = nation.getName();
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

	public int getSlotsCount()
	{
		return labels.length;
	}

	public String[] getLabels()
	{
		return labels;
	}

}