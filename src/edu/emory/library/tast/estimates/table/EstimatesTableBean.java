package edu.emory.library.tast.estimates.table;

import java.text.MessageFormat;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.ui.SimpleTableCell;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesTableBean
{
	
	private static final String CSS_CLASS_TD_LABEL = "tbl-label";
	private static final String CSS_CLASS_TD_TOTAL = "tbl-total";
	
	private EstimatesSelectionBean selectionBean;
	private boolean optionsSelected = false;
	private boolean optionsChanged = true;
	private Conditions conditions;
	private String rowGrouping;
	private String colGrouping;
	private SimpleTableCell[][] table;
	private boolean omitEmptyRowsAndColumns;
	
	private Grouper createGrouper(String groupBy, int resultIndex, List nations, List expRegions, List impRegions)
	{
		if ("nation".equals(groupBy))
		{
			return new GrouperNations(
					resultIndex,
					omitEmptyRowsAndColumns,
					nations);
		}
		else if ("expRegion".equals(groupBy))
		{
			return new GrouperRegions(
					resultIndex,
					omitEmptyRowsAndColumns,
					true,
					expRegions);
		}
		else if ("impRegion".equals(groupBy))
		{
			return new GrouperRegions(
					resultIndex,
					omitEmptyRowsAndColumns,
					false,
					impRegions);
		}
		else if (groupBy != null && groupBy.startsWith("years"))
		{
			int period = Integer.parseInt(rowGrouping.substring(5));
			return new GrouperYears(
					resultIndex,
					omitEmptyRowsAndColumns,
					period);
		}
		else
		{
			throw new RuntimeException("invalid group by value");
		}
	}
	
	private void generateTableIfNecessary()
	{
		
		// conditions from the left column (i.e. from select bean)
		Conditions newConditions = selectionBean.getConditions();
		
		// check if we have to
		if (!optionsSelected) return;
		if (!optionsChanged && newConditions.equals(conditions)) return;
		optionsChanged = false;
		conditions = newConditions;
		
		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// from select bean
		List selectedNations = selectionBean.loadSelectedNations(sess);
		List selectedExpRegions = selectionBean.loadSelectedExpRegions(sess);
		List selectedImpRegions = selectionBean.loadSelectedImpRegions(sess);

		// for grouping rows
		Grouper rowGrouper = createGrouper(rowGrouping, 0,
				selectedNations, selectedExpRegions, selectedImpRegions);
		
		// for grouping cols
		Grouper colGrouper = createGrouper(colGrouping, 1,
				selectedNations, selectedExpRegions, selectedImpRegions);

		// start query
		QueryValue query = new QueryValue(
				new String[] {"Estimate"},
				new String[] {"estimate"},
				conditions);

		// set grouping in the query
		query.setGroupBy(new Attribute[] {
				rowGrouper.getGroupingAttribute(),
				colGrouper.getGroupingAttribute()});
		
		// we want to see: row ID, row Name, col ID, col Name 
		query.addPopulatedAttribute(rowGrouper.getGroupingAttribute());
		query.addPopulatedAttribute(colGrouper.getGroupingAttribute());
		
		// ... and number of slaves exported
		query.addPopulatedAttribute(
				new FunctionAttribute("sum",
						new Attribute[] {Estimate.getAttribute("slavExported")}));

		// ... and number of slaves imported
		query.addPopulatedAttribute(
				new FunctionAttribute("sum",
						new Attribute[] {Estimate.getAttribute("slavImported")}));

		// row extra attributes
		Attribute[] rowExtraAttributes = rowGrouper.addExtraAttributes(4);
		for (int i = 0; i < rowExtraAttributes.length; i++)
			query.addPopulatedAttribute(rowExtraAttributes[i]);
		
		// col extra attributes
		Attribute[] colExtraAttributes = rowGrouper.addExtraAttributes(4 + rowExtraAttributes.length);
		for (int i = 0; i < colExtraAttributes.length; i++)
			query.addPopulatedAttribute(colExtraAttributes[i]);

		// finally query the database 
		Object[] result = query.executeQuery(sess);
		
		// close db
		transaction.commit();
		sess.close();
		
		// init groupers
		rowGrouper.initSlots(result);
		colGrouper.initSlots(result);
		
		// allocate table
		int rowCount = rowGrouper.getSlotsCount();
		int colCount = colGrouper.getSlotsCount();
		table = new SimpleTableCell[rowCount + 3][];
		
		// first two rows with titles
		table[0] = new SimpleTableCell[1 + colCount + 1];
		table[1] = new SimpleTableCell[1 + 2*colCount + 2];
		
		// rows for data
		for (int i = 0; i < rowCount; i++)
		{	
			table[i+2] = new SimpleTableCell[1 + 2*colCount + 2];
		}

		// last row with totals
		table[rowCount+2] = new SimpleTableCell[1 + 2*colCount + 2];

		// the column labels
		String[] colLabels = colGrouper.getLabels();
		for (int j = 0; j < colCount; j++)
		{	
			table[0][j+1] = new SimpleTableCell(colLabels[j]).setColspan(2).setCssClass(CSS_CLASS_TD_LABEL);
			table[1][2*j+1] = new SimpleTableCell("Exported").setCssClass(CSS_CLASS_TD_LABEL);
			table[1][2*j+2] = new SimpleTableCell("Imported").setCssClass(CSS_CLASS_TD_LABEL);
		}
		
		// labels for row totals
		table[0][colCount+1] = new SimpleTableCell("Totals").setColspan(2).setCssClass(CSS_CLASS_TD_LABEL);
		table[1][2*colCount+1] = new SimpleTableCell("Exported").setCssClass(CSS_CLASS_TD_LABEL); 
		table[1][2*colCount+2] = new SimpleTableCell("Imported").setCssClass(CSS_CLASS_TD_LABEL);
		
		// the row labels
		String[] rowLabels = rowGrouper.getLabels();
		for (int i = 0; i < rowCount; i++)
		{	
			table[i+2][0] = new SimpleTableCell(rowLabels[i]).setCssClass(CSS_CLASS_TD_LABEL);
		}
		
		// label for col totals
		table[rowCount+2][0] = new SimpleTableCell("Totals").setCssClass(CSS_CLASS_TD_LABEL);

		// how we want to displat it
		MessageFormat valuesFormat = new MessageFormat("{0,number,#,###,###}");
		
		// for totals
		double[] rowExpTotals = new double[rowCount]; 
		double[] rowImpTotals = new double[rowCount]; 
		double[] colExpTotals = new double[colCount]; 
		double[] colImpTotals = new double[colCount];
		double expTotals = 0; 
		double impTotals = 0;
		
		// fill in the data
		for (int i = 0; i < result.length; i++)
		{
			
			Object[] row = (Object[]) result[i];
			Double exp = (Double) row[2];
			Double imp = (Double) row[3];
			
			int rowIndex = rowGrouper.lookupIndex(row);
			int colIndex = colGrouper.lookupIndex(row);
			
			if (imp == null) imp = new Double (0.0);
			if (exp == null) exp = new Double (0.0);
			
			rowExpTotals[rowIndex] += exp.doubleValue();
			rowImpTotals[rowIndex] += imp.doubleValue();
			colExpTotals[colIndex] += exp.doubleValue();
			colImpTotals[colIndex] += imp.doubleValue();
			expTotals += exp.doubleValue();
			impTotals += imp.doubleValue();
			
			table[rowIndex+2][2*colIndex+1] = new SimpleTableCell(valuesFormat.format(new Object[]{exp}));
			table[rowIndex+2][2*colIndex+2] = new SimpleTableCell(valuesFormat.format(new Object[]{imp}));
			
		}
		
		// fill gaps
		String zeroValue = valuesFormat.format(new Object[]{new Double(0)});
		for (int i = 0; i < rowCount; i++)
		{
			for (int j = 0; j < 2*colCount+1; j++)
			{
				if (table[i+2][j+1] == null)
				{
					table[i+2][j+1] = new SimpleTableCell(zeroValue);
				}
			}
		}
		
		// insert row totals
		for (int i = 0; i < rowCount; i++)
		{
			table[i+2][2*colCount+1] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(rowExpTotals[i])})).setCssClass(CSS_CLASS_TD_TOTAL);
			table[i+2][2*colCount+2] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(rowImpTotals[i])})).setCssClass(CSS_CLASS_TD_TOTAL);
		}
		
		// insert col totals
		for (int j = 0; j < colCount; j++)
		{
			table[rowCount+2][2*j+1] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(colExpTotals[j])})).setCssClass(CSS_CLASS_TD_TOTAL);
			table[rowCount+2][2*j+2] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(colImpTotals[j])})).setCssClass(CSS_CLASS_TD_TOTAL);
		}
		
		// main totals
		table[rowCount+2][2*colCount+1] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(expTotals)})).setCssClass(CSS_CLASS_TD_TOTAL);
		table[rowCount+2][2*colCount+2] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(impTotals)})).setCssClass(CSS_CLASS_TD_TOTAL);

	}

	public String refreshTable()
	{
		optionsSelected = true;
		optionsChanged = true;
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

	public SimpleTableCell[][] getTable()
	{
		generateTableIfNecessary();
		return table;
	}

	public boolean isOmitEmptyRowsAndColumns()
	{
		return omitEmptyRowsAndColumns;
	}

	public void setOmitEmptyRowsAndColumns(boolean omitEmptyRowsAndColumns)
	{
		this.omitEmptyRowsAndColumns = omitEmptyRowsAndColumns;
	}

}