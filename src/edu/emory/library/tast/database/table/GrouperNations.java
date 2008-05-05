package edu.emory.library.tast.database.table;

import java.util.List;

import edu.emory.library.tast.common.tableview.GrouperSimpleDictionary;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

/**
 * Grouper for Nations.
 * Groupers are used in tables.
 *
 */
public class GrouperNations extends GrouperSimpleDictionary
{
	
	public GrouperNations(int resultIndex, boolean omitEmpty, List nations)
	{
		super(resultIndex, omitEmpty, nations);
	}
	
	public Attribute getGroupingAttribute()
	{
		 return new SequenceAttribute (new Attribute[] {
					Voyage.getAttribute("natinimp"),
					Nation.getAttribute("id")});	
	}

}