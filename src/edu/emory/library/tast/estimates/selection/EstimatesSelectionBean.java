package edu.emory.library.tast.estimates.selection;

import java.util.List;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.query.Conditions;

public class EstimatesSelectionBean
{
	
	private Conditions conditions;
	private List selectedNations;
	private List selectedExpRegions;
	private List selectedImpRegions;
	
	public EstimatesSelectionBean()
	{
		// buildTestConditions();
		conditions = new Conditions();
	}
	
	private void buildTestConditions()
	{
		
		Conditions expRegionConditions = new Conditions(Conditions.JOIN_OR);
		
		expRegionConditions.addCondition(
				new SequenceAttribute(new Attribute[] {
						Estimate.getAttribute("expRegion"),
						Region.getAttribute("id")}),
						new Long(1), Conditions.OP_EQUALS);
		
		expRegionConditions.addCondition(
				new SequenceAttribute(new Attribute[] {
						Estimate.getAttribute("expRegion"),
						Region.getAttribute("id")}),
						new Long(12), Conditions.OP_EQUALS);

		Conditions impRegionConditions = new Conditions(Conditions.JOIN_OR);
		
		impRegionConditions.addCondition(
				new SequenceAttribute(new Attribute[] {
						Estimate.getAttribute("impRegion"),
						Region.getAttribute("id")}),
						new Long(52), Conditions.OP_EQUALS);
		
		impRegionConditions.addCondition(
				new SequenceAttribute(new Attribute[] {
						Estimate.getAttribute("impRegion"),
						Region.getAttribute("id")}),
						new Long(55), Conditions.OP_EQUALS);
		
		conditions = new Conditions(Conditions.JOIN_AND);
		conditions.addCondition(expRegionConditions);
		conditions.addCondition(impRegionConditions);
		
	}

	public Conditions getConditions()
	{
		return conditions;
	}

	public void setConditions(Conditions conditions)
	{
		this.conditions = conditions;
	}
	
}
