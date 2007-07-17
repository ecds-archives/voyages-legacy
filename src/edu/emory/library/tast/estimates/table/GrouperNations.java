package edu.emory.library.tast.estimates.table;

import java.util.List;

import edu.emory.library.tast.common.tableview.GrouperSimpleDictionary;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesNation;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

public class GrouperNations extends GrouperSimpleDictionary
{
	
	public GrouperNations(int resultIndex, boolean omitEmpty, List nations)
	{
		super(resultIndex, omitEmpty, nations);
	}
	
	public Attribute getGroupingAttribute()
	{
		 return new SequenceAttribute (new Attribute[] {
				 Estimate.getAttribute("nation"),
				 EstimatesNation.getAttribute("id")});
	}

}