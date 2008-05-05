package edu.emory.library.tast.database.table;

import java.util.List;

import edu.emory.library.tast.common.tableview.GrouperSimpleDictionary;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

/**
 * Grouper for import regions.
 * Groupers are used in tables.
 *
 */
public class GrouperBroadDepartureRegions extends GrouperSimpleDictionary
{
	
	public GrouperBroadDepartureRegions(int resultIndex, boolean omitEmpty, List areas)
	{
		super(resultIndex, omitEmpty, areas);
	}
	
	public Attribute getGroupingAttribute()
	{
		 return new SequenceAttribute (new Attribute[] {
					Voyage.getAttribute("deptregimp"),
					Region.getAttribute("area"),
					Area.getAttribute("id")});
					
	}

}