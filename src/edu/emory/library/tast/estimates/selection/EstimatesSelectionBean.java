package edu.emory.library.tast.estimates.selection;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesSelectionBean
{
	
	private Conditions conditions;
	private List selectedNations;
	private List selectedExpRegions;
	private List selectedImpRegions;
	
	public EstimatesSelectionBean()
	{
		
		conditions = new Conditions();
		
		Attribute areaAmericaAtrribute = new SequenceAttribute(new Attribute[] {
				Region.getAttribute("area"),
				Area.getAttribute("america")});
		
		QueryValue nationsQuery = new QueryValue("edu.emory.library.tast.dm.Nation");
		nationsQuery.setOrderBy(new Attribute[] {Nation.getAttribute("name")});
		nationsQuery.setOrder(QueryValue.ORDER_ASC);
		
		Conditions arficanRegionsCond = new Conditions();
		arficanRegionsCond.addCondition(areaAmericaAtrribute,
				new Boolean(false), Conditions.OP_EQUALS);

		QueryValue africanRegionsQuery = new QueryValue("Region", arficanRegionsCond);
		africanRegionsQuery.setOrderBy(new Attribute[] {Region.getAttribute("name")});
		africanRegionsQuery.setOrder(QueryValue.ORDER_ASC);

		Conditions americanRegionsCond = new Conditions();
		americanRegionsCond.addCondition(areaAmericaAtrribute,
				new Boolean(true), Conditions.OP_EQUALS);
		
		QueryValue americanRegionsQuery = new QueryValue("Region", americanRegionsCond);
		americanRegionsQuery.setOrderBy(new Attribute[] {Region.getAttribute("name")});
		americanRegionsQuery.setOrder(QueryValue.ORDER_ASC);
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		selectedNations = nationsQuery.executeQueryList(sess);
		selectedExpRegions = africanRegionsQuery.executeQueryList(sess);
		selectedImpRegions = americanRegionsQuery.executeQueryList(sess);

		transaction.commit();
		sess.close();
		
//		System.out.println("Nations ...");
//		for (Iterator iter = selectedNations.iterator(); iter.hasNext();)
//		{
//			Nation nation = (Nation) iter.next();
//			System.out.println(nation.getName());
//		}
//
//		System.out.println("Regions AFRICA ...");
//		for (Iterator iter = selectedExpRegions.iterator(); iter.hasNext();)
//		{
//			Region nation = (Region) iter.next();
//			System.out.println(nation.getName());
//		}
//
//		System.out.println("Regions AMERICA ...");
//		for (Iterator iter = selectedImpRegions.iterator(); iter.hasNext();)
//		{
//			Region nation = (Region) iter.next();
//			System.out.println(nation.getName());
//		}

	}
	
//	private void buildTestConditions()
//	{
//		
//		Conditions expRegionConditions = new Conditions(Conditions.JOIN_OR);
//		
//		expRegionConditions.addCondition(
//				new SequenceAttribute(new Attribute[] {
//						Estimate.getAttribute("expRegion"),
//						Region.getAttribute("id")}),
//						new Long(1), Conditions.OP_EQUALS);
//		
//		expRegionConditions.addCondition(
//				new SequenceAttribute(new Attribute[] {
//						Estimate.getAttribute("expRegion"),
//						Region.getAttribute("id")}),
//						new Long(12), Conditions.OP_EQUALS);
//
//		Conditions impRegionConditions = new Conditions(Conditions.JOIN_OR);
//		
//		impRegionConditions.addCondition(
//				new SequenceAttribute(new Attribute[] {
//						Estimate.getAttribute("impRegion"),
//						Region.getAttribute("id")}),
//						new Long(52), Conditions.OP_EQUALS);
//		
//		impRegionConditions.addCondition(
//				new SequenceAttribute(new Attribute[] {
//						Estimate.getAttribute("impRegion"),
//						Region.getAttribute("id")}),
//						new Long(55), Conditions.OP_EQUALS);
//		
//		conditions = new Conditions(Conditions.JOIN_AND);
//		conditions.addCondition(expRegionConditions);
//		conditions.addCondition(impRegionConditions);
//		
//	}

	public Conditions getConditions()
	{
		return conditions;
	}

	public List getSelectedExpRegions()
	{
		return selectedExpRegions;
	}

	public List getSelectedImpRegions()
	{
		return selectedImpRegions;
	}

	public List getSelectedNations()
	{
		return selectedNations;
	}
	
	public static void main(String[] args)
	{
		new EstimatesSelectionBean();
	}

}
