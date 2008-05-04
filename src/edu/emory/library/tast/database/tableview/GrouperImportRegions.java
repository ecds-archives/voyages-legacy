package edu.emory.library.tast.database.tableview;

import java.util.List;

import edu.emory.library.tast.common.tableview.GrouperSimpleDictionary;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

public class GrouperImportRegions extends GrouperSimpleDictionary
{
	
	public GrouperImportRegions(int resultIndex, boolean omitEmpty, List areas)
	{
		super(resultIndex, omitEmpty, areas);
	}
	
	public Attribute getGroupingAttribute()
	{
		 return new SequenceAttribute (new Attribute[] {
					Voyage.getAttribute("mjselimp"),
					Region.getAttribute("area"),
					Area.getAttribute("id")});
					
	}

}