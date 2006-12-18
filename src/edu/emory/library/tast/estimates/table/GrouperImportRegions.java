package edu.emory.library.tast.estimates.table;

import java.util.List;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesImportArea;
import edu.emory.library.tast.dm.EstimatesImportRegion;
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
					Estimate.getAttribute("impRegion"),
					EstimatesImportRegion.getAttribute("area"),
					EstimatesImportArea.getAttribute("id")});
					
	}

}