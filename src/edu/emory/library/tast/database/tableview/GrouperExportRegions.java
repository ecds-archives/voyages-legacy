package edu.emory.library.tast.database.tableview;

import java.util.List;

import edu.emory.library.tast.common.tableview.GrouperSimpleDictionary;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

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