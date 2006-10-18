package edu.emory.library.tast.estimates.table;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesTableBean
{
	
	private EstimatesSelectionBean selectionBean;
	
	public void getTableData()
	{
		
		Conditions conditions = selectionBean.getConditions();
		
		QueryValue query = new QueryValue(
				new String[] {"Estimate"},
				new String[] {"estimate"},
				conditions);
		
		Attribute nationName = new SequenceAttribute (new Attribute[] {
				Estimate.getAttribute("nation"),
				Nation.getAttribute("name")});
		
		Attribute expRegionName = new SequenceAttribute (new Attribute[] {
				Estimate.getAttribute("expRegion"),
				Region.getAttribute("name")});

		Attribute impRegionName = new SequenceAttribute (new Attribute[] {
				Estimate.getAttribute("impRegion"),
				Region.getAttribute("name")});

		query.setGroupBy(new Attribute[] {nationName, expRegionName});
		query.setOrderBy(new Attribute[] {nationName, expRegionName});

		query.addPopulatedAttribute(nationName);
		query.addPopulatedAttribute(expRegionName);
		query.addPopulatedAttribute(impRegionName);

		query.addPopulatedAttribute(
				new FunctionAttribute("sum",
						new Attribute[] {Estimate.getAttribute("slavExported")}));

		query.addPopulatedAttribute(
				new FunctionAttribute("sum",
						new Attribute[] {Estimate.getAttribute("slavImported")}));

		query.setOrder(QueryValue.ORDER_ASC);
		
		Object[] result = query.executeQuery();
		
		for (int i = 0; i < result.length; i++)
		{
			
			Object[] row = (Object[])result[i];
			String nation = (String) row[0]; 
			String expRegion = (String) row[1]; 
			double exported = ((Double) row[2]).doubleValue();
			double imported = ((Double) row[3]).doubleValue();
			
			System.out.println(
					nation + ", " +  
					expRegion + " -> " +
					"exported = " + exported + " : " +
					"imported = " + imported);
			
		}
		
	}

	public EstimatesSelectionBean getSelectionBean()
	{
		return selectionBean;
	}

	public void setSelectionBean(EstimatesSelectionBean selectionBean)
	{
		this.selectionBean = selectionBean;
	}
	
	public static void main(String[] args)
	{
		EstimatesTableBean bean = new EstimatesTableBean();
		EstimatesSelectionBean selectionBean = new EstimatesSelectionBean();
		bean.setSelectionBean(selectionBean);
		bean.getTableData();
	}

}
