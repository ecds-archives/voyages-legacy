package edu.emory.library.tast.database.table;

import java.text.MessageFormat;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.SimpleTableCell;
import edu.emory.library.tast.common.table.Grouper;
import edu.emory.library.tast.common.table.Label;
import edu.emory.library.tast.common.table.Table;
import edu.emory.library.tast.common.table.TableBuilder;
import edu.emory.library.tast.common.table.TableBuilderAverage;
import edu.emory.library.tast.common.table.TableBuilderBreakdown;
import edu.emory.library.tast.common.table.TableBuilderSimple;
import edu.emory.library.tast.common.table.TableUtils;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.CSVUtils;

public class TableBean
{
	
	private static final String CSS_CLASS_TD_LABEL = "tbl-label";
	private static final String CSS_CLASS_TD_TOTAL = "tbl-total";
	private static final String CSS_CLASS_TD_TOTAL_ROW_LABEL = "tbl-total-row-label";
	private static final String CSS_CLASS_TD_TOTAL_COL_LABEL = "tbl-total-col-label";

	private SearchBean searchBean;

	private TastDbConditions conditions;

	private boolean optionsChanged;

	private String rowGrouping;
	private String colGrouping;

	private boolean omitEmptyRowsAndColumns;

	private SimpleTableCell[][] table;

	private CellVariable cellValuesDesc = variables[0];
	
	private static final CellVariable[] variables = new CellVariable[] {
		
			CellVariable.createSingleSum("Sum of embarked slaves", "slaximp"),
			CellVariable.createSingleAverage("Average number of embarked slaves", "slaximp"),
			CellVariable.createSingleCount("Number of voyages - embarked slaves", "slaximp"),

			new CellVariable(
				"expByRows",
				"Percent of embarked slaves (row total)",
				TastResource.getText("database_tableview_averages"),
				new TableBuilder[] {
						new TableBuilderBreakdown(
								"Embarked",
								TableBuilderBreakdown.BREAKDOWN_ROWS,
								new FunctionAttribute("sum", Voyage.getAttribute("slaximp")))},
				new Attribute[] {Voyage.getAttribute("slaximp")},
				new MessageFormat("{0,number,#,###,##0.0}%")),
				
			new CellVariable(
				"expByColumns",
				"Percent of embarked slaves (column total)",
				TastResource.getText("database_tableview_averages"),
				new TableBuilder[] {
						new TableBuilderBreakdown(
								"Embarked",
								TableBuilderBreakdown.BREAKDOWN_COLUMNS,
								new FunctionAttribute("sum", Voyage.getAttribute("slaximp")))},
				new Attribute[] {Voyage.getAttribute("slaximp")},
				new MessageFormat("{0,number,#,###,##0.0}%")),
				
			CellVariable.createSingleSum("Sum of disembarked slaves", "slamimp"),
			CellVariable.createSingleAverage("Average number of disembarked slaves", "slamimp"),
			CellVariable.createSingleCount("Number of voyages - disembarked slaves", "slamimp"),
				
			new CellVariable(
				"impByRows",
				"Percent of disembarked slaves (row total)",
				TastResource.getText("database_tableview_averages"),
				new TableBuilder[] {
						new TableBuilderBreakdown(
								"Disembarked",
								TableBuilderBreakdown.BREAKDOWN_ROWS,
								new FunctionAttribute("sum", Voyage.getAttribute("slamimp")))},
				new Attribute[] {Voyage.getAttribute("slamimp")},
				new MessageFormat("{0,number,#,###,##0.0}%")),
				
			new CellVariable(
				"impByColumns",
				"Percent of disembarked slaves (column total)",
				TastResource.getText("database_tableview_averages"),
				new TableBuilder[] {
						new TableBuilderBreakdown(
								"Disembarked",
								TableBuilderBreakdown.BREAKDOWN_COLUMNS,
								new FunctionAttribute("sum", Voyage.getAttribute("slamimp")))},
				new Attribute[] {Voyage.getAttribute("slamimp")},
				new MessageFormat("{0,number,#,###,##0.0}%")),
				
			new CellVariable(
				"bothSum",
				"Sum of embarked/disembarked slaves",
				TastResource.getText("database_tableview_totals"),
				new TableBuilder[] {
						new TableBuilderSimple(
								"Embarked",
								new FunctionAttribute("sum", Voyage.getAttribute("slaximp"))),
						new TableBuilderSimple(
								"Disembarked",
								new FunctionAttribute("sum", Voyage.getAttribute("slamimp")))},
				new Attribute[] {Voyage.getAttribute("slaximp"), Voyage.getAttribute("slamimp")},
				new MessageFormat("{0,number,#,###,###}")),
				
			new CellVariable(
				"bothAvg",
				"Average number of embarked/disembarked slaves",
				TastResource.getText("database_tableview_averages"),
				new TableBuilder[] {
						new TableBuilderAverage(
								"Embarked",
								new FunctionAttribute("sum", Voyage.getAttribute("slaximp")),
								new FunctionAttribute("count", Voyage.getAttribute("slaximp"))),
						new TableBuilderAverage(
								"Disembarked",
								new FunctionAttribute("sum", Voyage.getAttribute("slamimp")),
								new FunctionAttribute("count", Voyage.getAttribute("slamimp")))},
				new Attribute[] {Voyage.getAttribute("slaximp"), Voyage.getAttribute("slamimp")},
				new MessageFormat("{0,number,#,###,###}")),
				
			new CellVariable(
				"bothCnt",
				"Number of voyages - embarked/disembarked slaves",
				TastResource.getText("database_tableview_totals"),
				new TableBuilder[] {
						new TableBuilderSimple(
								"Embarked",
								new FunctionAttribute("count", Voyage.getAttribute("slaximp"))),
						new TableBuilderSimple(
								"Disembarked",
								new FunctionAttribute("count", Voyage.getAttribute("slamimp")))},
				new Attribute[] {Voyage.getAttribute("slaximp"), Voyage.getAttribute("slamimp")},
				new MessageFormat("{0,number,#,###,###}")),

			CellVariable.createSinglePercentageAverage("Average percentage male", "malrat7"),
			CellVariable.createSingleCount("Number of voyages - percentage male", "malrat7"),
			
			CellVariable.createSinglePercentageAverage("Average percentage children", "chilrat7"),
			CellVariable.createSingleCount("Number of voyages - percentage children", "chilrat7"),

			CellVariable.createSinglePercentageAverage("Average percentage of slaves embarked who died during voyage", "vymrtrat"),
			CellVariable.createSingleCount("Number of voyages - percentage of slaves embarked who died during voyage", "vymrtrat"),
				
			CellVariable.createSingleAverage("Average middle passage (days)", "voy2imp"),
			CellVariable.createSingleCount("Number of voyages - middle passage (days)", "voy2imp"),

			CellVariable.createSingleAverage("Average standarized tonnage", "tonmod"),
			CellVariable.createSingleCount("Number of voyages - standarized tonnage", "tonmod"),

			CellVariable.createSingleAverage("Sterling cash price in Jamaica", "jamcaspr"),
			CellVariable.createSingleCount("Number of voyages - sterling cash price in Jamaica", "jamcaspr"),
			
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
	 * An internal method called by {@link #refreshTable()}.
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
	private Grouper createGrouper(String groupBy, int resultIndex, List expRegions, List impRegions, List impAreas, List ports, List nations)
	{
		if ("expRegion".equals(groupBy))
		{
			return new GrouperExportRegions(resultIndex, omitEmptyRowsAndColumns, expRegions);
		}
		else if ("impRegion".equals(groupBy))
		{
			return new GrouperImportRegions(resultIndex, omitEmptyRowsAndColumns, impAreas);
		}
		else if ("impRegionBreakdowns".equals(groupBy))
		{
			return new GrouperImportRegionsWithBreakdowns(resultIndex, omitEmptyRowsAndColumns, impRegions);
		}
		else if (groupBy != null && groupBy.startsWith("years"))
		{
			int period = Integer.parseInt(rowGrouping.substring(5));
			return new GrouperYears(resultIndex, omitEmptyRowsAndColumns, period);
		}
		else if ("impPorts".equals(groupBy))
		{
			return new GrouperImportPorts(resultIndex, omitEmptyRowsAndColumns, ports);
		}
		else if ("expPorts".equals(groupBy))
		{
			return new GrouperExportPorts(resultIndex, omitEmptyRowsAndColumns, ports);
		}
		else if ("departureBroad".equals(groupBy))
		{
			return new GrouperBroadDepartureRegions(resultIndex, omitEmptyRowsAndColumns, impAreas);
		}
		else if ("departureRegion".equals(groupBy))
		{
			return new GrouperDepartureRegions(resultIndex, omitEmptyRowsAndColumns, expRegions);
		}
		else if ("departure".equals(groupBy))
		{
			return new GrouperDeparturePorts(resultIndex, omitEmptyRowsAndColumns, ports);
		}
		else if ("flagStar".equals(groupBy))
		{
			return new GrouperNations(resultIndex, omitEmptyRowsAndColumns, nations);
		}
		else
		{
			throw new RuntimeException("invalid group by value");
		}
	}

	private void generateTableIfNecessary()
	{ 
		
		// conditions from the left column (i.e. from select bean)
		TastDbConditions newConditions = (TastDbConditions) searchBean.getSearchParameters().getConditions().clone();

		// check if we have to
		if (!optionsChanged && newConditions.equals(conditions)) return;
		optionsChanged = false;
		conditions = newConditions;

		// open db
		Session sess = HibernateConn.getSession();
		Transaction transaction = sess.beginTransaction();

		// ugly!
		List expRegions = Region.loadAll(sess);
		List impRegions = expRegions;
		List impAreas = Area.loadAll(sess);
		List ports = Port.loadAll(sess);
		List nations = Nation.loadAll(sess);

		// table builders
		TableBuilder[] tableBuilders = cellValuesDesc.getTableBuilders();

		// for grouping rows
		Grouper rowGrouper = createGrouper(rowGrouping, 0, expRegions, impRegions, impAreas, ports, nations);

		// for grouping cols
		Grouper colGrouper = createGrouper(colGrouping, 1, expRegions, impRegions, impAreas, ports, nations);

		// start query
		TastDbQuery query = new TastDbQuery(new String[] { "Voyage" }, new String[] { "voyage" }, conditions);

		// we want to restrict results so that it does not have nulls
		conditions.addCondition(rowGrouper.getGroupingAttribute(), null, TastDbConditions.OP_IS_NOT);
		conditions.addCondition(colGrouper.getGroupingAttribute(), null, TastDbConditions.OP_IS_NOT);
		
		// also eliminate rows from the result which would produce empty rows or columns
		Attribute[] nonNullAttributes = cellValuesDesc.getNonNullAttributes();
		if (nonNullAttributes != null && nonNullAttributes.length > 0)
		{
			TastDbConditions condNonNull = new TastDbConditions(TastDbConditions.OR); 
			for (int i = 0; i < nonNullAttributes.length; i++)
			{
				condNonNull.addCondition(nonNullAttributes[i], null, TastDbConditions.OP_IS_NOT);
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
		int noOfDataColumns = 0;
		
		for (int i = 0; i < tableBuilders.length; i++)
		{
			Attribute[] attributes = tableBuilders[i].getAttributes();
			noOfDataColumns += attributes.length;
			for (int j = 0; j < attributes.length; j++)
			{
				query.addPopulatedAttribute(attributes[j]);
			
			}
		}

		// row extra attributes
		Attribute[] rowExtraAttributes = rowGrouper.addExtraAttributes(2 + noOfDataColumns);
		for (int i = 0; i < rowExtraAttributes.length; i++){
			query.addPopulatedAttribute(rowExtraAttributes[i]);		
		}

		// col extra attributes
		Attribute[] colExtraAttributes = rowGrouper.addExtraAttributes(2 + noOfDataColumns + rowExtraAttributes.length);
		for (int i = 0; i < colExtraAttributes.length; i++)
			query.addPopulatedAttribute(colExtraAttributes[i]);

		// finally query the database
		boolean useSQL = AppConfig.getConfiguration().getBoolean(AppConfig.DATABASE_USE_SQL);
		System.out.println(query.toSQLStringWithParams().conditionString.toString());
		Object[] result = query.executeQuery(sess, useSQL);

		// init groupers
		rowGrouper.initSlots(result);
		colGrouper.initSlots(result);

		// close db
		transaction.commit();
		sess.close();

		// dimensions of the table
		int subCols = tableBuilders.length;
		int extraHeaderRows = subCols > 1 ? 1 : 0; 
		int dataRowCount = rowGrouper.getLeafLabelsCount();
		int dataColCount = colGrouper.getLeafLabelsCount();
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
			TableUtils.addColumnLabel(table, colLabels[j], 0, colIdx, 1, headerTopRowsCount, subCols, CSS_CLASS_TD_LABEL);
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
			TableUtils.addRowLabel(table, rowLabels[i], rowIdx, 0, 1, headerLeftColsCount, CSS_CLASS_TD_LABEL);
			rowIdx += rowLabels[i].getLeavesCount();
		}
		
		// extra second row with labels 
		if (extraHeaderRows != 0)
		{
			for (int j = 0; j < dataColCount; j++)
			{
				for (int i = 0; i < tableBuilders.length; i++)
				{
					table[headerTopRowsCount][headerLeftColsCount + subCols * j + i] =
						new SimpleTableCell(tableBuilders[i].getColLabel()).setCssClass(CSS_CLASS_TD_LABEL);
				}
			}
		}
		
		// last column label 
		for (int i = 0; i < tableBuilders.length; i++)
		{
			table[0][headerLeftColsCount + subCols * dataColCount + i] =
				new SimpleTableCell(tableBuilders[i].getTotalLabel()).
				setRowspan(headerTopRowsCount).
				setCssClass(CSS_CLASS_TD_TOTAL_COL_LABEL);
		}
		
		// last column extra row label 
		for (int i = 0; i < tableBuilders.length; i++)
		{
			table[headerTopRowsCount][headerLeftColsCount + subCols * dataColCount + i] =
				new SimpleTableCell(tableBuilders[i].getColLabel()).
				setCssClass(CSS_CLASS_TD_TOTAL_COL_LABEL);
		}

		// label for column totals
		table[headerTopRowsCount + extraHeaderRows + dataRowCount][0] =
			new SimpleTableCell(cellValuesDesc.getLastRowLabel()).
			setColspan(headerLeftColsCount).
			setCssClass(CSS_CLASS_TD_TOTAL_ROW_LABEL);

		// how we want to displat it
		MessageFormat valuesFormat = this.cellValuesDesc.getFormat();
		
		// construct the tables
		int dataColOffset = 2;
		for (int k = 0; k < tableBuilders.length; k++)
		{
			
			// construct the table
			TableBuilder tableBuilder = tableBuilders[k]; 
			Table dataTable = tableBuilder.formTable(result, dataColOffset, rowGrouper, colGrouper);
			
			// inside cell of the table
			Double[][] tableValues = dataTable.getTable(); 
			for (int i = 0; i < dataRowCount; i++)
			{
				int innerRowIdx = headerTopRowsCount + extraHeaderRows + i;
				for (int j = 0; j < dataColCount; j++)
				{
					int innerColIdx = headerLeftColsCount + subCols * j + k;
					Double cellValue = tableValues[i][j];
					String cellStrValue = cellValue != null ? valuesFormat.format(new Object[] {cellValue}) : "";
					table[innerRowIdx][innerColIdx] = new SimpleTableCell(cellStrValue);
				}
			}
			
			// last row
			Double[] lastRow = dataTable.getLastRow();
			for (int j = 0; j < dataColCount; j++)
			{
				Double cellValue = lastRow[j];
				String cellStrValue = cellValue != null ? valuesFormat.format(new Object[] {cellValue}) : "";
				SimpleTableCell cell = new SimpleTableCell(cellStrValue, CSS_CLASS_TD_TOTAL);
				table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols * j + k] = cell;
			}
			
			// last column
			Double[] lastCol = dataTable.getLastCol();
			for (int i = 0; i < dataRowCount; i++)
			{
				Double cellValue = lastCol[i];
				String cellStrValue = cellValue != null ? valuesFormat.format(new Object[] {cellValue}) : "";
				SimpleTableCell cell = new SimpleTableCell(cellStrValue, CSS_CLASS_TD_TOTAL);
				table[headerTopRowsCount + extraHeaderRows + i][headerLeftColsCount + subCols * dataColCount + k] = cell;
			}
			
			// bottom right cell
			Double bottomRight = dataTable.getBottomRight();
			String cellStrValue = bottomRight != null ? valuesFormat.format(new Object[] {bottomRight}) : "";
			SimpleTableCell cell = new SimpleTableCell(cellStrValue, CSS_CLASS_TD_TOTAL);
			table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols * dataColCount + k] = cell;
			
			// update offset 
			dataColOffset += tableBuilder.getAttributeCount();
			
		}

	}

	/**
	 * Bound to UI. Marks the data as invalid in order to trigger
	 * {@link #generateTableIfNecessary()}
	 * 
	 * @return
	 */
	public String refreshTable()
	{
		optionsChanged = true;
		return null;
	}

	/**
	 * Bound to a HTML select which determines what should be in columns.
	 * Wrapper for {@link #colGrouping}.
	 * 
	 * @return
	 */
	public String getColGrouping()
	{
		return colGrouping;
	}

	/**
	 * Bound to a HTML select which determines what should be in columns.
	 * Wrapper for {@link #colGrouping}.
	 * 
	 * @return
	 */
	public void setColGrouping(String colGrouping)
	{
		this.colGrouping = colGrouping;
	}

	/**
	 * Bound to a HTML select which determines what should be in rows. Wrapper
	 * for {@link #rowGrouping}.
	 * 
	 * @return
	 */
	public String getRowGrouping()
	{
		return rowGrouping;
	}

	/**
	 * Bound to a HTML select which determines what should be in rows. Wrapper
	 * for {@link #rowGrouping}.
	 * 
	 * @return
	 */
	public void setRowGrouping(String rowGrouping)
	{
		this.rowGrouping = rowGrouping;
	}

	/**
	 * Bound to UI. Wrapper for {@link #table}. Also, calles
	 * {@link #generateTableIfNecessary()}. This is the only place where
	 * {@link #generateTableIfNecessary()} is called.
	 * 
	 * @return
	 */
	public SimpleTableCell[][] getTable()
	{
		generateTableIfNecessary();
		return table;
	}

	/**
	 * Bound to UI. Wrapper for {@link #omitEmptyRowsAndColumns}.
	 * 
	 * @return
	 */
	public boolean isOmitEmptyRowsAndColumns()
	{
		return omitEmptyRowsAndColumns;
	}

	/**
	 * Bound to UI. Wrapper for {@link #omitEmptyRowsAndColumns}.
	 * 
	 * @return
	 */
	public void setOmitEmptyRowsAndColumns(boolean omitEmptyRowsAndColumns)
	{
		this.omitEmptyRowsAndColumns = omitEmptyRowsAndColumns;
	}

	/**
	 * Bound to UI. Wrapper for {@link #showMode}.
	 * 
	 * @return
	 */
	public String getShowMode()
	{
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
		Session session = HibernateConn.getSession();
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
	
	public SelectItem[] getAvailableAttributes() {
		SelectItem[] items = new SelectItem[variables.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new SelectItem(variables[i].getId(), variables[i].getUserLabel());
		}
		return items;
	}
	
	public SearchBean getSearchBean()
	{
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean)
	{
		this.searchBean = searchBean;
	}

}
