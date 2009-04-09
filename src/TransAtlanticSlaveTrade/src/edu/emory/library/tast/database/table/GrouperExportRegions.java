package edu.emory.library.tast.database.table;

import java.util.List;

import edu.emory.library.tast.common.table.GrouperSimpleDictionary;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

/**
 * Grouper for export regions attribute.
 * Groupers are used in tables.
 *
 */
public class GrouperExportRegions extends GrouperSimpleDictionary
{
	
	public GrouperExportRegions(int resultIndex, boolean omitEmpty, List regions)
	{
		super(resultIndex, omitEmpty, regions);
	}
	
	public Attribute getGroupingAttribute()
	{
		 return new SequenceAttribute (new Attribute[] {
					Voyage.getAttribute("majbyimp"),
					Region.getAttribute("id")});
	}

}