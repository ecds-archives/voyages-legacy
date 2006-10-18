package edu.emory.library.tast.estimates.table;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesTableBean
{
	
	private EstimatesSelectionBean selectionBean;
	private String rowGrouping;
	private String colGrouping;
	private String[] rowLabels;
	private String[] colLabels;
	private String[][] values;
	
	public String loadTableData()
	{
		
		// from select bean
		List selectedNations = selectionBean.getSelectedNations();
		List selectedExpRegions = selectionBean.getSelectedExpRegions();
		List selectedImpRegions = selectionBean.getSelectedImpRegions();

		// conditions from the left column (i.e. from select bean)
		Conditions conditions = selectionBean.getConditions();

		// start query
		QueryValue query = new QueryValue(
				new String[] {"Estimate"},
				new String[] {"estimate"},
				conditions);
		
		// shorcut for nation ID
		Attribute nationId = new SequenceAttribute (new Attribute[] {
				Estimate.getAttribute("nation"),
				Nation.getAttribute("id")});

		// shorcut for exp region name
		Attribute expRegionId = new SequenceAttribute (new Attribute[] {
				Estimate.getAttribute("expRegion"),
				Region.getAttribute("id")});

		// shorcut for imp region ID
		Attribute impRegionId = new SequenceAttribute (new Attribute[] {
				Estimate.getAttribute("impRegion"),
				Region.getAttribute("id")});

		// determine columns by which we will group rows
		Attribute rowGroupingAttributeId = null;
		Map rowLookup = null;
		boolean rowsByYears = false;
		int period = 0;
		if ("nation".equals(rowGrouping))
		{
			rowGroupingAttributeId = nationId;
			rowLabels = Nation.nationNamesToArray(selectedNations);
			rowLookup = Nation.createIdIndexMap(selectedNations);
		}
		else if ("expRegion".equals(rowGrouping))
		{
			rowGroupingAttributeId = expRegionId;
			rowLabels = Region.regionNamesToArray(selectedExpRegions);
			rowLookup = Region.createIdIndexMap(selectedExpRegions);
		}
		else if ("impRegion".equals(rowGrouping))
		{
			rowGroupingAttributeId = impRegionId;
			rowLabels = Region.regionNamesToArray(selectedImpRegions);
			rowLookup = Region.createIdIndexMap(selectedImpRegions);
		}
		else if (rowGrouping != null && rowGrouping.startsWith("years"))
		{
			rowsByYears = true;
			period = Integer.parseInt(rowGrouping.substring(5));
			rowGroupingAttributeId =
				new FunctionAttribute("round_to_multiple", new Attribute[] {
						Estimate.getAttribute("year"),
						new DirectValueAttribute("period", new Integer(period))});
		}
		else
		{
			throw new RuntimeException("invalid rowGrouping value");
		}
		
		// determine columns by which we will group columns
		Attribute colGroupingAttributeId = null;
		Map colLookup = null;
		if ("nation".equals(colGrouping))
		{
			colGroupingAttributeId = nationId;
			colLabels = Nation.nationNamesToArray(selectedNations);
			colLookup = Nation.createIdIndexMap(selectedNations);
		}
		else if ("expRegion".equals(colGrouping))
		{
			colGroupingAttributeId = expRegionId;
			colLabels = Region.regionNamesToArray(selectedExpRegions);
			colLookup = Region.createIdIndexMap(selectedExpRegions);
		}
		else if ("impRegion".equals(colGrouping))
		{
			colGroupingAttributeId = impRegionId;
			colLabels = Region.regionNamesToArray(selectedImpRegions);
			colLookup = Region.createIdIndexMap(selectedImpRegions);
		}
		else
		{
			throw new RuntimeException("invalid colGrouping value");
		}
		
		// set grouping in the query
		query.setGroupBy(new Attribute[] {
				rowGroupingAttributeId,
				colGroupingAttributeId});
		
		// sort by rows so that we are able
		// to find max and min year later
		query.setOrder(QueryValue.ORDER_ASC);
		query.setOrderBy(new Attribute[] {rowGroupingAttributeId});

		// we want to see: row ID, row Name, col ID, col Name 
		query.addPopulatedAttribute(rowGroupingAttributeId);
		query.addPopulatedAttribute(colGroupingAttributeId);

		// ... and number of slaves exported
		query.addPopulatedAttribute(
				new FunctionAttribute("sum",
						new Attribute[] {Estimate.getAttribute("slavExported")}));

		// ... and number of slaves imported
		query.addPopulatedAttribute(
				new FunctionAttribute("sum",
						new Attribute[] {Estimate.getAttribute("slavImported")}));

		// finally query the database 
		Object[] result = query.executeQuery();
		
		// find max and min year
		if (rowsByYears)
		{

			int minYear = ((Integer) ((Object[])result[0])[0]).intValue();
			int maxYear = ((Integer) ((Object[])result[result.length - 1])[0]).intValue();

			int periods = (maxYear - minYear) / period + 1;
			rowLabels = new String[periods];
			for (int i = 0; i < periods; i++)
			{
				if (period == 1)
				{
					rowLabels[i] =
						String.valueOf(minYear + i * period);
				}
				else
				{
					rowLabels[i] =
						String.valueOf(minYear + i * period) + "<br>" + 
						String.valueOf(minYear + (i+1) * period - 1);
				}
			}
			
			rowLookup = new HashMap();
			for (int i = 0; i < periods; i++)
				rowLookup.put(
						new Integer(minYear + i * period),
						new Integer(i));

		}
		
		// how we want to displat it
		MessageFormat valuesFormat = new MessageFormat(
				"<div class=\"exp\">{0,number,#}</div>" +
				"<div class=\"imp\">{1,number,#}</div>");
		
		// prepate the final array of values
		int rowCount = rowLabels.length;
		int colCount = colLabels.length;
		values = new String[rowCount][colCount];
		for (int i = 0; i < rowCount; i++)
		{
			for (int j = 0; j < colCount; j++)
			{
				values[i][j] = valuesFormat.format(
						new Object[]{new Double(0), new Double(0)});
			}
		}
		
		// fill in the data
		for (int i = 0; i < result.length; i++)
		{
			
			Object[] row = (Object[]) result[i];
			Integer rowId = (Integer) row[0];
			Integer colId = (Integer) row[1];
			Double imp = (Double) row[2];
			Double exp = (Double) row[3];
			
			Integer rowIndex = (Integer) rowLookup.get(rowId);
			Integer colIndex = (Integer) colLookup.get(colId);
			
			if (rowIndex != null && colIndex != null)
			{
				if (imp == null) imp = new Double (0.0);
				if (exp == null) exp = new Double (0.0);
				values[rowIndex.intValue()][colIndex.intValue()] =
					valuesFormat.format(new Object[]{exp, imp});
			}
			
		}
		
		// stay on the same page
		return null;
		
	}

	public EstimatesSelectionBean getSelectionBean()
	{
		return selectionBean;
	}

	public void setSelectionBean(EstimatesSelectionBean selectionBean)
	{
		this.selectionBean = selectionBean;
	}

	public String getColGrouping()
	{
		return colGrouping;
	}

	public void setColGrouping(String colGrouping)
	{
		this.colGrouping = colGrouping;
	}

	public String getRowGrouping()
	{
		return rowGrouping;
	}

	public void setRowGrouping(String rowGrouping)
	{
		this.rowGrouping = rowGrouping;
	}

	public String[] getColLabels()
	{
		return colLabels;
	}

	public String[] getRowLabels()
	{
		return rowLabels;
	}

	public String[][] getValues()
	{
		return values;
	}
	
//	public static void main(String[] args)
//	{
//		EstimatesTableBean bean = new EstimatesTableBean();
//		EstimatesSelectionBean selectionBean = new EstimatesSelectionBean();
//		bean.setSelectionBean(selectionBean);
//		bean.getTableData();
//	}

}
