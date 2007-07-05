package edu.emory.library.tast.estimates.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.DictionaryOrdered;
import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class GrouperSimpleDictionary extends Grouper
{
	
	private Label[] labels;
	private List dictionary;
	private Map lookupTable;
	
	public GrouperSimpleDictionary(int resultIndex, boolean omitEmpty, List nations)
	{
		super(resultIndex, omitEmpty);
		this.dictionary = nations;
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
		
		int noOfDictItems = omitEmpty ? 
				listedNationIds.size() :
					dictionary.size();

		lookupTable = new HashMap();
		labels = new Label[noOfDictItems];
		
		int i = 0;
		for (Iterator iter = dictionary.iterator(); iter.hasNext();)
		{
			DictionaryOrdered nation = (DictionaryOrdered) iter.next();
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
		if (nationId == null || lookupTable.get(nationId) == null) {
			return 0;
		}
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