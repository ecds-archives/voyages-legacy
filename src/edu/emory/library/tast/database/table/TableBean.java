package edu.emory.library.tast.database.table;

import java.text.MessageFormat;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.SimpleTableCell;
import edu.emory.library.tast.common.table.Grouper;
import edu.emory.library.tast.common.table.Label;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.CSVUtils;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class TableBean
{
	
	private static final String CSS_CLASS_TD_LABEL = "tbl-label";
	private static final String CSS_CLASS_TD_TOTAL = "tbl-total";

	private SearchBean searchBean;

	private Conditions conditions;

	private boolean optionsChanged;

	private String rowGrouping;
	private String colGrouping;

	private boolean omitEmptyRowsAndColumns;

	private SimpleTableCell[][] table;

	private CellVariable cellValuesDesc = variables[0];
	
	private static final CellVariable[] variables = new CellVariable[] {
		
			new CellVariable(
				"expSum",
				CellVariable.SUM,
				"Sum of embarked slaves",
				new Attribute[] {new FunctionAttribute("sum", Voyage.getAttribute("slaximp"))},
				new Attribute[] {Voyage.getAttribute("slaximp")},
				new String[] {"Embarked"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),

			new CellVariable(
				"expAvg",
				CellVariable.AVG,
				"Average number of embarked slaves",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("slaximp")), new FunctionAttribute("sum", Voyage.getAttribute("slaximp"))},
				new Attribute[] {Voyage.getAttribute("slaximp")},
				new String[] {"Embarked"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),
				
			new CellVariable(
				"expCnt",
				CellVariable.COUNT,
				"Number of voyages - embarked slaves",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("slaximp"))},
				new Attribute[] {Voyage.getAttribute("slaximp")},
				new String[] {"Embarked"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),
				
			new CellVariable(
				"impSum",
				CellVariable.SUM,
				"Sum of disembarked slaves",
				new Attribute[] {new FunctionAttribute("sum", Voyage.getAttribute("slamimp"))},
				new Attribute[] {Voyage.getAttribute("slamimp")},
				new String[] {"Disembarked"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),
				
			new CellVariable(
				"impAvg",
				CellVariable.AVG,
				"Average number of disembarked slaves",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("slamimp")), new FunctionAttribute("sum", Voyage.getAttribute("slamimp"))},
				new Attribute[] {Voyage.getAttribute("slamimp")},
				new String[] {"Disembarked"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),
				
			new CellVariable(
				"impCnt",
				CellVariable.COUNT,
				"Number of voyages - disembarked slaves",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("slamimp"))},
				new Attribute[] {Voyage.getAttribute("slamimp")},
				new String[] {"Disembarked"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),
				
			new CellVariable(
				"bothSum",
				CellVariable.SUM,
				"Sum of embarked/disembarked slaves",
				new Attribute[] {new FunctionAttribute("sum", Voyage.getAttribute("slaximp")), new FunctionAttribute("sum", Voyage.getAttribute("slamimp"))},
				new Attribute[] {Voyage.getAttribute("slaximp")},
				new String[] {"Embarked", "Disembarked"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),
				
			new CellVariable(
				"bothAcg",
				CellVariable.AVG,
				"Average number of embarked/disembarked slaves",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("slaximp")), new FunctionAttribute("sum", Voyage.getAttribute("slaximp")), new FunctionAttribute("count", Voyage.getAttribute("slamimp")), new FunctionAttribute("sum", Voyage.getAttribute("slamimp"))},
				new Attribute[] {Voyage.getAttribute("slaximp"), Voyage.getAttribute("slamimp")},
				new String[] {"Embarked", "Disembarked"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),
				
			new CellVariable(
				"bothCnt",
				CellVariable.COUNT,
				"Number of voyages - embarked/disembarked slaves",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("slaximp")), new FunctionAttribute("count", Voyage.getAttribute("slamimp"))},
				new Attribute[] {Voyage.getAttribute("slaximp"), Voyage.getAttribute("slamimp")},
				new String[] {"Embarked", "Disembarked"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),
				
			new CellVariable(
				"sexratioAvg",
				CellVariable.AVG,
				"Average percentage male",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("malrat7")), new FunctionAttribute("sum", new FunctionAttribute("crop_to_0_100", Voyage.getAttribute("malrat7")))},
				new Attribute[] {Voyage.getAttribute("malrat7")},
				new String[] {"Percentage male"}, new MessageFormat("{0,number,#,###,##0}%"),
				""),
				
			new CellVariable(
				"sexratioCnt",
				CellVariable.COUNT,
				"Number of voyages - percentage male",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("malrat7"))},
				new Attribute[] {Voyage.getAttribute("malrat7")},
				new String[] {"Percentage male"}, new MessageFormat("{0,number,#,###,###}"),
				""),
				
			new CellVariable(
				"childratioAvg",
				CellVariable.AVG,
				"Average percentage children",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("chilrat7")), new FunctionAttribute("sum", new FunctionAttribute("crop_to_0_100", Voyage.getAttribute("chilrat7")))},
				new Attribute[] {Voyage.getAttribute("chilrat7")},
				new String[] {"Percentage children"}, new MessageFormat("{0,number,#,###,##0.0}%"),
				""),
				
			new CellVariable(
				"childratioCnt",
				CellVariable.COUNT,
				"Number of voyages - percentage children",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("chilrat7"))},
				new Attribute[] {Voyage.getAttribute("chilrat7")},
				new String[] {"Percentage children"}, new MessageFormat("{0,number,#,###,###}"), ""),
				
			new CellVariable(
				"mortalityAvg",
				CellVariable.AVG,
				"Average percentage of slaves embarked who died during voyage",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("vymrtrat")), new FunctionAttribute("sum", new FunctionAttribute("crop_to_0_100", Voyage.getAttribute("vymrtrat")))},
				new Attribute[] {Voyage.getAttribute("vymrtrat")},
				new String[] {"Percentage of slaves embarked who died during voyage"}, new MessageFormat("{0,number,#,###,##0.0}%"),
				""),
				
			new CellVariable(
				"mortalityCnt",
				CellVariable.COUNT,
				"Number of voyages - percentage of slaves embarked who died during voyage",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("vymrtrat"))},
				new Attribute[] {Voyage.getAttribute("vymrtrat")},
				new String[] {"Percentage of slaves embarked who died during voyage"}, new MessageFormat("{0,number,#,###,###}"),
				""),
				
			new CellVariable(
				"middlepassageAvg",
				CellVariable.AVG,
				"Average middle passage (days)",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("voy2imp")), new FunctionAttribute("sum", Voyage.getAttribute("voy2imp"))},
				new Attribute[] {Voyage.getAttribute("voy2imp")},
				new String[] {"Middle passage (days)"}, new MessageFormat("{0,number,#,###,###}"),
				""),
				
			new CellVariable(
				"middlepassageCnt",
				CellVariable.COUNT,
				"Number of voyages - middle passage (days)",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("voy2imp"))},
				new Attribute[] {Voyage.getAttribute("voy2imp")},
				new String[] {"Middle passage (days)"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),
				
			new CellVariable(
				"standarizedtonnageAvg",
				CellVariable.AVG,
				"Average standarized tonnage",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("tonmod")), new FunctionAttribute("sum", Voyage.getAttribute("tonmod"))},
				new Attribute[] {Voyage.getAttribute("tonmod")},
				new String[] {"Standarized tonnage"}, new MessageFormat("{0,number,#,###,###}"),
				""),
				
			new CellVariable(
				"standarizedtonnageCnt",
				CellVariable.COUNT,
				"Number of voyages - standarized tonnage",
				new Attribute[] {new FunctionAttribute("count", Voyage.getAttribute("tonmod"))},
				new Attribute[] {Voyage.getAttribute("tonmod")},
				new String[] {"Standarized tonnage"}, new MessageFormat("{0,number,#,###,###}"),
				"0"),
				
	};
	
	public TableBean()
	{
		
		resetToDefault();

	}
	
	public void resetToDefault()
	{
		
		optionsChanged = true;

		rowGrouping = "years25";;
		colGrouping = "impRegion";
		
		omitEmptyRowsAndColumns = true;
		
	}

	/**
	 * An internal method calle by {@link #refreshTable()}.
	 * {@link #refreshTable()} needs to create two groupers: one for columns and
	 * one for rows. This method creates the instances a grouper based on the
	 * given parameters.
	 * 
	 * @param groupBy
	 * @param resultIndex
	 * @param nations
	 * @param expRegions
	 * @param impRegions
	 * @param impAreas
	 * @return
	 */
	private Grouper createGrouper(String groupBy, int resultIndex, List expRegions, List impRegions, List impAreas, List ports, List nations) {
		if ("expRegion".equals(groupBy)) {
			return new GrouperExportRegions(resultIndex, omitEmptyRowsAndColumns, expRegions);
		} else if ("impRegion".equals(groupBy)) {
			return new GrouperImportRegions(resultIndex, omitEmptyRowsAndColumns, impAreas);
		} else if ("impRegionBreakdowns".equals(groupBy)) {
			return new GrouperImportRegionsWithBreakdowns(resultIndex, omitEmptyRowsAndColumns, impRegions);
		} else if (groupBy != null && groupBy.startsWith("years")) {
			int period = Integer.parseInt(rowGrouping.substring(5));
			return new GrouperYears(resultIndex, omitEmptyRowsAndColumns, period);
		} else if ("impPorts".equals(groupBy)) {
			return new GrouperImportPorts(resultIndex, omitEmptyRowsAndColumns, ports);
		} else if ("expPorts".equals(groupBy)) {
			return new GrouperExportPorts(resultIndex, omitEmptyRowsAndColumns, ports);
		} else if ("departureBroad".equals(groupBy)) {
			return new GrouperBroadDepartureRegions(resultIndex, omitEmptyRowsAndColumns, impAreas);
		} else if ("departureRegion".equals(groupBy)) {
			return new GrouperDepartureRegions(resultIndex, omitEmptyRowsAndColumns, expRegions);
		} else if ("departure".equals(groupBy)) {
			return new GrouperDeparturePorts(resultIndex, omitEmptyRowsAndColumns, ports);
		} else if ("flagStar".equals(groupBy)){
			return new GrouperNations(resultIndex, omitEmptyRowsAndColumns, nations);
		}else {
			throw new RuntimeException("invalid group by value");
		}
	}

	/**
	 * Adds column labels to {@link #table}. It is recursive because label can
	 * be generally broken down to sub-labels.
	 * 
	 * @param table
	 * @param label
	 * @param rowIdx
	 * @param colIdx
	 * @param depth
	 * @param maxDepth
	 * @param subCols
	 */
	private void addColumnLabel(SimpleTableCell table[][], Label label, int rowIdx, int colIdx, int depth,
			int maxDepth, int subCols) {

		SimpleTableCell cell = new SimpleTableCell(label.getText());
		cell.setColspan(subCols * label.getLeavesCount());
		cell.setCssClass(CSS_CLASS_TD_LABEL);
		if (!label.hasBreakdown())
			cell.setRowspan(maxDepth - depth + 1);
		table[rowIdx][colIdx] = cell;

		if (label.hasBreakdown()) {
			int colOffset = 0;
			Label[] breakdown = label.getBreakdown();
			for (int j = 0; j < breakdown.length; j++) {
				addColumnLabel(table, breakdown[j], rowIdx + 1, colIdx + colOffset, depth + 1, maxDepth, subCols);
				colOffset += subCols * breakdown[j].getLeavesCount();
			}
		}

	}

	/**
	 * Adds column labels to {@link #table}. It is recursive because label can
	 * be generally broken down to sub-labels.
	 * 
	 * @param table
	 * @param label
	 * @param rowIdx
	 * @param colIdx
	 * @param depth
	 * @param maxDepth
	 */
	private void addRowLabel(SimpleTableCell table[][], Label label, int rowIdx, int colIdx, int depth, int maxDepth) {

		SimpleTableCell cell = new SimpleTableCell(label.getText());
		cell.setRowspan(label.getLeavesCount());
		cell.setCssClass(CSS_CLASS_TD_LABEL);
		if (!label.hasBreakdown())
			cell.setColspan(maxDepth - depth + 1);
		table[rowIdx][colIdx] = cell;

		if (label.hasBreakdown()) {
			int rowOffset = 0;
			Label[] breakdown = label.getBreakdown();
			for (int i = 0; i < breakdown.length; i++) {
				addRowLabel(table, breakdown[i], rowIdx + rowOffset, colIdx + 1, depth + 1, maxDepth);
				rowOffset += breakdown[i].getLeavesCount();
			}
		}

	}

	private void generateTableIfNecessary()
	
	{ 
		// conditions from the left column (i.e. from select bean)
		Conditions newConditions = (Conditions) searchBean.getSearchParameters().getConditions().clone();

		// check if we have to
		if (!optionsChanged && newConditions.equals(conditions)) return;
		optionsChanged = false;
		conditions = newConditions;

		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		// ugly!
		List expRegions = Region.loadAll(sess);
		List impRegions = expRegions;
		List impAreas = Area.loadAll(sess);
		List ports = Port.loadAll(sess);
		List nations = Nation.loadAll(sess);

		// for grouping rows
		Grouper rowGrouper = createGrouper(rowGrouping, 0, expRegions, impRegions, impAreas, ports, nations);

		// for grouping cols
		Grouper colGrouper = createGrouper(colGrouping, 1, expRegions, impRegions, impAreas, ports, nations);

		// start query
		QueryValue query = new QueryValue(new String[] { "Voyage" }, new String[] { "voyage" }, conditions);

		// we want to restrict results so that it does not have nulls
		conditions.addCondition(rowGrouper.getGroupingAttribute(), null, Conditions.OP_IS_NOT);
		conditions.addCondition(colGrouper.getGroupingAttribute(), null, Conditions.OP_IS_NOT);
		
		// also eliminate rows from the result which would produce empty rows or columns
		Attribute[] nonNullAttributes = cellValuesDesc.getNonNullAttributes();
		if (nonNullAttributes != null && nonNullAttributes.length > 0)
		{
			Conditions condNonNull = new Conditions(Conditions.OR); 
			for (int i = 0; i < nonNullAttributes.length; i++)
			{
				condNonNull.addCondition(nonNullAttributes[i], null, Conditions.OP_IS_NOT);
			}
			conditions.addCondition(condNonNull);
		}

		// set grouping in the query
		query.setGroupBy(new Attribute[] {
				rowGrouper.getGroupingAttribute(),
				colGrouper.getGroupingAttribute() });

		// first comes: row ID, col ID
		query.addPopulatedAttribute(rowGrouper.getGroupingAttribute());
		query.addPopulatedAttribute(colGrouper.getGroupingAttribute());
		
		// and the real values depending on the selection
		Attribute[] attributes = cellValuesDesc.getAttributes();
		for (int i = 0; i < attributes.length; i++)
			query.addPopulatedAttribute(attributes[i]);

		// row extra attributes
		Attribute[] rowExtraAttributes = rowGrouper.addExtraAttributes(2 + cellValuesDesc.getAttributes().length);
		for (int i = 0; i < rowExtraAttributes.length; i++)
			query.addPopulatedAttribute(rowExtraAttributes[i]);

		// col extra attributes
		Attribute[] colExtraAttributes = rowGrouper.addExtraAttributes(2 + cellValuesDesc.getAttributes().length + rowExtraAttributes.length);
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

		// dimensions of the table
		int subCols = cellValuesDesc.getValuesCount();
		int extraHeaderRows = cellValuesDesc.getValuesCount() > 1 ? 1 : 0; 
		int dataRowCount = rowGrouper.getLeaveLabelsCount();
		int dataColCount = colGrouper.getLeaveLabelsCount();
		int headerTopRowsCount = colGrouper.getBreakdownDepth();
		int headerLeftColsCount = rowGrouper.getBreakdownDepth();
		int totalRows = headerTopRowsCount + extraHeaderRows + dataRowCount + 1;
		int totalCols = headerLeftColsCount + subCols * (dataColCount + 1);

		// create table
		table = new SimpleTableCell[totalRows][totalCols];

		// (0,0) cell
		SimpleTableCell topLeftCell = new SimpleTableCell("");
		topLeftCell.setColspan(headerLeftColsCount);
		topLeftCell.setRowspan(headerTopRowsCount + extraHeaderRows);
		table[0][0] = topLeftCell;

		// get column labels and make sure that the number
		// of children is pre-calculated
		Label[] colLabels = colGrouper.getLabels();
		for (int j = 0; j < colLabels.length; j++)
			colLabels[j].calculateLeaves();

		// fill them using labels
		int colIdx = headerLeftColsCount;
		for (int j = 0; j < colLabels.length; j++)
		{
			addColumnLabel(table, colLabels[j], 0, colIdx, 1, headerTopRowsCount, subCols);
			colIdx += subCols * colLabels[j].getLeavesCount();
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
		if (extraHeaderRows != 0)
		{
			for (int j = 0; j < dataColCount; j++)
			{
				for (int i = 0; i < cellValuesDesc.getValuesCount(); i++)
				{
					table[headerTopRowsCount][headerLeftColsCount + subCols * j + i] =
						new SimpleTableCell(cellValuesDesc.getLabels()[i]).setCssClass(CSS_CLASS_TD_LABEL);
				}
			}
		}

		boolean isAvg = cellValuesDesc.isAvg();
		if (!isAvg)
		{
			table[0][headerLeftColsCount + subCols * dataColCount + 0] =
				new SimpleTableCell(TastResource.getText("database_tableview_totals")).
				setColspan(2).
				setRowspan(headerTopRowsCount).
				setCssClass(CSS_CLASS_TD_LABEL);
		}
		else
		{
			table[0][headerLeftColsCount + subCols * dataColCount + 0] =
				new SimpleTableCell(TastResource.getText("database_tableview_averages")).
				setColspan(2).
				setRowspan(headerTopRowsCount).
				setCssClass(CSS_CLASS_TD_LABEL);
		}
		
		for (int i = 0; i < cellValuesDesc.getValuesCount(); i++)
		{
			table[headerTopRowsCount][headerLeftColsCount + subCols * dataColCount + i] =
				new SimpleTableCell(cellValuesDesc.getLabels()[i]).
				setCssClass(CSS_CLASS_TD_LABEL);
		}

		// label for col totals
		if (!isAvg)
		{
			table[headerTopRowsCount + extraHeaderRows + dataRowCount][0] =
				new SimpleTableCell(TastResource.getText("database_tableview_totals")).
				setColspan(headerLeftColsCount).
				setCssClass(CSS_CLASS_TD_LABEL);
		}
		else
		{
			table[headerTopRowsCount + extraHeaderRows + dataRowCount][0] =
				new SimpleTableCell(TastResource.getText("database_tableview_averages")).
				setColspan(headerLeftColsCount).
				setCssClass(CSS_CLASS_TD_LABEL);
		}

		// how we want to displat it
		MessageFormat valuesFormat = this.cellValuesDesc.getFormat();

		// count totals (used for average)
		int[][] colTotalCount = new int[dataColCount][cellValuesDesc.getValuesCount()];
		int[][] rowTotalCounts = new int[dataRowCount][cellValuesDesc.getValuesCount()];
		int[] totalCounts = new int[cellValuesDesc.getValuesCount()];
		
		// for totals
		double[][] rowTotals = new double[dataRowCount][cellValuesDesc.getValuesCount()];
		double[][] colTotals = new double[dataColCount][cellValuesDesc.getValuesCount()];
		double[] totals = new double[cellValuesDesc.getValuesCount()];

		// fill in the data
		for (int i = 0; i < result.length; i++)
		{
			Object[] row = (Object[]) result[i];

			int rowIndex = rowGrouper.lookupIndex(row);
			int colIndex = colGrouper.lookupIndex(row);

			for (int k = 0; k < cellValuesDesc.getValuesCount(); k++)
			{
				
				double val = 0.0; 
				
				if (!isAvg)
				{
				
					Number valObj = (Number) row[2 + k];
					val = valObj == null ? 0.0 : valObj.doubleValue();
					
					rowTotals[rowIndex][k] += val;
					colTotals[colIndex][k] += val;
					totals[k] += val;
					
				}
				else
				{

					Long cntObj = (Long) row[2 + 2*k + 0];
					Number valObj = (Number) row[2 + 2*k + 1];

					long cnt = cntObj == null ? 0 : cntObj.longValue();
					val = valObj == null ? 0.0 : valObj.doubleValue();
					
					if (cnt != 0)
					{
						rowTotals[rowIndex][k] += val;
						colTotals[colIndex][k] += val;
						totals[k] += val;
						rowTotalCounts[rowIndex][k] += cnt;
						colTotalCount[colIndex][k] += cnt;
						totalCounts[k] += cnt;
						val /= cnt;
					}
					
				}

				int innerRowIdx = headerTopRowsCount + extraHeaderRows + rowIndex;
				int innerColIdx = headerLeftColsCount + subCols * colIndex + k;

				String cellValue = valuesFormat.format(new Object[] {new Double(val)});
				
				table[innerRowIdx][innerColIdx] = new SimpleTableCell(cellValue);
				
			}

		}

		// adjust total in case of averages
		if (isAvg)
		{
			for (int j = 0; j < colTotals.length; j++)
			{
				for (int k = 0; k <  cellValuesDesc.getValuesCount(); k++)
				{
					if (colTotalCount[j][k] > 0)
						colTotals[j][k] /= (double) colTotalCount[j][k];
				}
			}
			for (int i = 0; i < rowTotals.length; i++)
			{
				for (int k = 0; k < cellValuesDesc.getValuesCount(); k++)
				{
					if (rowTotalCounts[i][k] > 0)
						rowTotals[i][k] /= (double) rowTotalCounts[i][k];
				}
			}
			for (int k = 0; k < cellValuesDesc.getValuesCount(); k++)
			{
				if (totalCounts[k] > 0)
					totals[k] /= (double) totalCounts[k];
			}
		}

		// fill gaps
		// String zeroValue = cellValuesDesc.getZeroValue();
		for (int i = 0; i < dataRowCount; i++)
		{
			int innerRowIdx = headerTopRowsCount + extraHeaderRows + i;
			for (int j = 0; j < subCols * dataColCount; j++)
			{
				int innerColIdx = headerLeftColsCount + j;
				if (table[innerRowIdx][innerColIdx] == null)
				{
					table[innerRowIdx][innerColIdx] = new SimpleTableCell("");
				}
			}
		}

		// insert row totals
		for (int i = 0; i < dataRowCount; i++)
		{
			for (int j = 0; j < rowTotals[i].length; j++)
			{
				table[headerTopRowsCount + extraHeaderRows + i][headerLeftColsCount + subCols * dataColCount + j] =
					new SimpleTableCell(valuesFormat.format(new Object[] { new Double(rowTotals[i][j]) })).
				    setCssClass(CSS_CLASS_TD_TOTAL);
			}
		}

		// insert col totals
		for (int j = 0; j < dataColCount; j++)
		{
			for (int i = 0; i < colTotals[j].length; i++)
			{
				table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols * j + i] =
					new SimpleTableCell(valuesFormat.format(new Object[] { new Double(colTotals[j][i]) })).
					setCssClass(CSS_CLASS_TD_TOTAL);
			}
		}

		// main totals
		for (int i = 0; i < totals.length; i++)
		{
			table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols * dataColCount + i] =
		    	  new SimpleTableCell(valuesFormat.format(new Object[] { new Double(totals[i]) })).
		    	  setCssClass(CSS_CLASS_TD_TOTAL);
		}

	}

	/**
	 * Bound to UI. Marks the data as invalid in order to trigger
	 * {@link #generateTableIfNecessary()}
	 * 
	 * @return
	 */
	public String refreshTable() {
		optionsChanged = true;
		return null;
	}

	/**
	 * Bound to a HTML select which determines what should be in columns.
	 * Wrapper for {@link #colGrouping}.
	 * 
	 * @return
	 */
	public String getColGrouping() {
		return colGrouping;
	}

	/**
	 * Bound to a HTML select which determines what should be in columns.
	 * Wrapper for {@link #colGrouping}.
	 * 
	 * @return
	 */
	public void setColGrouping(String colGrouping) {
		this.colGrouping = colGrouping;
	}

	/**
	 * Bound to a HTML select which determines what should be in rows. Wrapper
	 * for {@link #rowGrouping}.
	 * 
	 * @return
	 */
	public String getRowGrouping() {
		return rowGrouping;
	}

	/**
	 * Bound to a HTML select which determines what should be in rows. Wrapper
	 * for {@link #rowGrouping}.
	 * 
	 * @return
	 */
	public void setRowGrouping(String rowGrouping) {
		this.rowGrouping = rowGrouping;
	}

	/**
	 * Bound to UI. Wrapper for {@link #table}. Also, calles
	 * {@link #generateTableIfNecessary()}. This is the only place where
	 * {@link #generateTableIfNecessary()} is called.
	 * 
	 * @return
	 */
	public SimpleTableCell[][] getTable() {
		generateTableIfNecessary();
		return table;
	}

	/**
	 * Bound to UI. Wrapper for {@link #omitEmptyRowsAndColumns}.
	 * 
	 * @return
	 */
	public boolean isOmitEmptyRowsAndColumns() {
		return omitEmptyRowsAndColumns;
	}

	/**
	 * Bound to UI. Wrapper for {@link #omitEmptyRowsAndColumns}.
	 * 
	 * @return
	 */
	public void setOmitEmptyRowsAndColumns(boolean omitEmptyRowsAndColumns) {
		this.omitEmptyRowsAndColumns = omitEmptyRowsAndColumns;
	}

	/**
	 * Bound to UI. Wrapper for {@link #showMode}.
	 * 
	 * @return
	 */
	public String getShowMode() {
		return cellValuesDesc.getId();
	}

	/**
	 * Bound to UI. Wrapper for {@link #showMode}.
	 * 
	 * @return
	 */
	public void setShowMode(String showMode) {
		for (int i = 0; i < variables.length; i++) {
			if (showMode.equals(variables[i].getId())) {
				this.cellValuesDesc = variables[i];
				break;
			}
		}		
	}

	/**
	 * Gets zip file with data visible in table.
	 * @return
	 */
	public String getFileAllData() {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();

		String[][] data = new String[this.table.length][this.table[0].length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < this.table[i].length; j++) {
				if (this.table[i][j] != null) {
					data[i][j] = this.table[i][j].getText();
				} else {
					data[i][j] = "";
				}
			}
		}
		CSVUtils.writeResponse(session, data);

		t.commit();
		session.close();
		return null;
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}
	
	public SelectItem[] getAvailableAttributes() {
		SelectItem[] items = new SelectItem[variables.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new SelectItem(variables[i].getId(), variables[i].getUserLabel());
		}
		return items;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}

}
