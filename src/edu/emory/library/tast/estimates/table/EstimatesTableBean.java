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
	private boolean optionsChanged = true;
	private Conditions conditions;
	private String rowGrouping = "years25";;
	private String colGrouping = "nation";
	private SimpleTableCell[][] table;
	private boolean omitEmptyRowsAndColumns;
	private String showMode = "both";
	
	private Grouper createGrouper(String groupBy, int resultIndex, List nations, List expRegions, List impRegions, List impAreas)
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
			return new GrouperExportRegions(
					resultIndex,
					omitEmptyRowsAndColumns,
					expRegions);
		}
		else if ("impRegion".equals(groupBy))
		{
			return new GrouperImportRegions(
					resultIndex,
					omitEmptyRowsAndColumns,
					impAreas);
		}
		else if ("impRegionBreakdowns".equals(groupBy))
		{
			return new GrouperImportRegionsWithBreakdowns(
					resultIndex,
					omitEmptyRowsAndColumns,
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
	
	private void addColumnLabel(SimpleTableCell table[][], Label label, int rowIdx, int colIdx, int depth, int maxDepth, int subCols)
	{
		
		SimpleTableCell cell = new SimpleTableCell(label.getText());
		cell.setColspan(subCols*label.getLeavesCount());
		cell.setCssClass(CSS_CLASS_TD_LABEL);
		if (!label.hasBreakdown()) cell.setRowspan(maxDepth - depth + 1);
		table[rowIdx][colIdx] = cell;
		
		if (label.hasBreakdown())
		{
			int colOffset = 0;
			Label[] breakdown = label.getBreakdown();
			for (int j = 0; j < breakdown.length; j++)
			{
				addColumnLabel(table, breakdown[j], rowIdx + 1, colIdx + colOffset, depth + 1, maxDepth, subCols);
				colOffset += subCols*breakdown[j].getLeavesCount();
			}
		}

	}
	
	private void addRowLabel(SimpleTableCell table[][], Label label, int rowIdx, int colIdx, int depth, int maxDepth)
	{
		
		SimpleTableCell cell = new SimpleTableCell(label.getText());
		cell.setRowspan(label.getLeavesCount());
		cell.setCssClass(CSS_CLASS_TD_LABEL);
		if (!label.hasBreakdown()) cell.setColspan(maxDepth - depth + 1);
		table[rowIdx][colIdx] = cell;
		
		if (label.hasBreakdown())
		{
			int rowOffset = 0;
			Label[] breakdown = label.getBreakdown();
			for (int i = 0; i < breakdown.length; i++)
			{
				addRowLabel(table, breakdown[i], rowIdx + rowOffset, colIdx + 1, depth + 1, maxDepth);
				rowOffset += breakdown[i].getLeavesCount();
			}
		}

	}

	private void generateTableIfNecessary()
	{
		
		// conditions from the left column (i.e. from select bean)
		Conditions newConditions = selectionBean.getConditions();
		
		// check if we have to
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
		List selectedImpAreas = selectionBean.loadSelectedImpAreas(sess);
		
		// for grouping rows
		Grouper rowGrouper = createGrouper(rowGrouping, 0,
				selectedNations,
				selectedExpRegions,
				selectedImpRegions,
				selectedImpAreas);
		
		// for grouping cols
		Grouper colGrouper = createGrouper(colGrouping, 1,
				selectedNations,
				selectedExpRegions,
				selectedImpRegions,
				selectedImpAreas);

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
		
		// init groupers
		rowGrouper.initSlots(result);
		colGrouper.initSlots(result);

		// close db
		transaction.commit();
		sess.close();
		
		// what to show
		boolean showExp = true;
		boolean showImp = true;
		int subCols = 2;
		int extraHeaderRows = 1;
		int expColOffset = 0;
		int impColOffset = 1;
		if ("exp".equals(showMode))
		{
			showExp = true;
			showImp = false;
			subCols = 1;
			extraHeaderRows = 0;
			expColOffset = 0;
			impColOffset = 0;
		}
		else if ("imp".equals(showMode))
		{
			showExp = false;
			showImp = true;
			subCols = 1;
			extraHeaderRows = 0;
			expColOffset = 0;
			impColOffset = 0;
		}
		else
		{
			showExp = true;
			showImp = true;
			subCols = 2;
			extraHeaderRows = 1;
			expColOffset = 0;
			impColOffset = 1;
		}

		// dimensions of the table
		int dataRowCount = rowGrouper.getLeaveLabelsCount();
		int dataColCount = colGrouper.getLeaveLabelsCount();
		int headerTopRowsCount = colGrouper.getBreakdownDepth();
		int headerLeftColsCount = rowGrouper.getBreakdownDepth();
		int totalRows = headerTopRowsCount + extraHeaderRows + dataRowCount + 1;
		int totalCols = headerLeftColsCount + subCols*(dataColCount + 1);
		
		// create table
		table = new SimpleTableCell[totalRows][totalCols];

		// (0,0) cell
		SimpleTableCell topLeftCell = new SimpleTableCell("");
		topLeftCell.setColspan(headerLeftColsCount);
		topLeftCell.setRowspan(headerTopRowsCount + extraHeaderRows);
		table[0][0] = topLeftCell;

		// get column labels and make sure that the number
		// of children is precalculated
		Label[] colLabels = colGrouper.getLabels();
		for (int j = 0; j < colLabels.length; j++)
			colLabels[j].calculateLeaves();
		
		// fill them using labels
		int colIdx = headerLeftColsCount;
		for (int j = 0; j < colLabels.length; j++)
		{
			addColumnLabel(table, colLabels[j], 0, colIdx, 1, headerTopRowsCount, subCols);
			colIdx += subCols*colLabels[j].getLeavesCount();
		}

		// get row labels and make sure that the number
		// of children is precalculated
		Label[] rowLabels = rowGrouper.getLabels();
		for (int j = 0; j < rowLabels.length; j++)
			rowLabels[j].calculateLeaves();

		// fill them using labels
		int rowIdx = headerTopRowsCount + extraHeaderRows;
		for (int i = 0; i < rowLabels.length; i++)
		{
			addRowLabel(table, rowLabels[i], rowIdx, 0, 1, headerLeftColsCount);
			rowIdx += rowLabels[i].getLeavesCount();
		}

		// extra row with exported/imported labels
		if (showExp && showImp)
		{
			for (int j = 0; j < dataColCount; j++)
			{
				table[headerTopRowsCount][headerLeftColsCount + subCols*j + expColOffset] = new SimpleTableCell("Exported").setCssClass(CSS_CLASS_TD_LABEL);
				table[headerTopRowsCount][headerLeftColsCount + subCols*j + impColOffset] = new SimpleTableCell("Imported").setCssClass(CSS_CLASS_TD_LABEL);
			}
		}
		
		// labels for row totals
		table[0][headerLeftColsCount + subCols*dataColCount + 0] = new SimpleTableCell("Totals").setColspan(2).setRowspan(headerTopRowsCount).setCssClass(CSS_CLASS_TD_LABEL);
		if (showExp) table[headerTopRowsCount][headerLeftColsCount + subCols*dataColCount + expColOffset] = new SimpleTableCell("Exported").setCssClass(CSS_CLASS_TD_LABEL); 
		if (showImp) table[headerTopRowsCount][headerLeftColsCount + subCols*dataColCount + impColOffset] = new SimpleTableCell("Imported").setCssClass(CSS_CLASS_TD_LABEL);
		
		// label for col totals
		table[headerTopRowsCount + extraHeaderRows + dataRowCount][0] = new SimpleTableCell("Totals").setCssClass(CSS_CLASS_TD_LABEL).setColspan(headerLeftColsCount);

		// how we want to displat it
		MessageFormat valuesFormat = new MessageFormat("{0,number,#,###,###}");
		
		// for totals
		double[] rowExpTotals = new double[dataRowCount]; 
		double[] rowImpTotals = new double[dataRowCount]; 
		double[] colExpTotals = new double[dataColCount]; 
		double[] colImpTotals = new double[dataColCount];
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
			
			if (showExp) table[headerTopRowsCount + extraHeaderRows + rowIndex][headerLeftColsCount + subCols*colIndex + expColOffset] = new SimpleTableCell(valuesFormat.format(new Object[]{exp})); 
			if (showImp) table[headerTopRowsCount + extraHeaderRows + rowIndex][headerLeftColsCount + subCols*colIndex + impColOffset] = new SimpleTableCell(valuesFormat.format(new Object[]{imp}));
			
		}
		
		// fill gaps
		String zeroValue = valuesFormat.format(new Object[]{new Double(0)});
		for (int i = 0; i < dataRowCount; i++)
		{
			for (int j = 0; j < subCols*dataColCount; j++)
			{
				if (table[headerTopRowsCount + extraHeaderRows + i][headerLeftColsCount + j] == null)
				{
					table[headerTopRowsCount + extraHeaderRows + i][headerLeftColsCount + j] = new SimpleTableCell(zeroValue);
				}
			}
		}
		
		// insert row totals
		for (int i = 0; i < dataRowCount; i++)
		{
			if (showExp) table[headerTopRowsCount + extraHeaderRows + i][headerLeftColsCount + subCols*dataColCount + expColOffset] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(rowExpTotals[i])})).setCssClass(CSS_CLASS_TD_TOTAL);
			if (showImp) table[headerTopRowsCount + extraHeaderRows + i][headerLeftColsCount + subCols*dataColCount + impColOffset] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(rowImpTotals[i])})).setCssClass(CSS_CLASS_TD_TOTAL);
		}
		
		// insert col totals
		for (int j = 0; j < dataColCount; j++)
		{
			if (showExp) table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols*j + expColOffset] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(colExpTotals[j])})).setCssClass(CSS_CLASS_TD_TOTAL);
			if (showImp) table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols*j + impColOffset] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(colImpTotals[j])})).setCssClass(CSS_CLASS_TD_TOTAL);
		}
		
		// main totals
		if (showExp) table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols*dataColCount + expColOffset] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(expTotals)})).setCssClass(CSS_CLASS_TD_TOTAL);
		if (showImp) table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols*dataColCount + impColOffset] = new SimpleTableCell(valuesFormat.format(new Object[]{new Double(impTotals)})).setCssClass(CSS_CLASS_TD_TOTAL);

	}
	
	public String refreshTable()
	{
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

	public String getShowMode()
	{
		return showMode;
	}

	public void setShowMode(String showMode)
	{
		this.showMode = showMode;
	}

}