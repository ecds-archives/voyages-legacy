package edu.emory.library.tast.database.tableview;

import java.text.MessageFormat;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.SimpleTableCell;
import edu.emory.library.tast.common.tableview.Grouper;
import edu.emory.library.tast.common.tableview.Label;
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

/**
 * This bean is responsible for managing the cross-table in the database. It
 * has a reference to
 * {@link #edu.emory.library.tast.estimates.selection.EstimatesSelectionBean}.
 * It holds the entire table in {@link #table}. The table is refreshed every
 * time the query is changed or the user clicks to the refresh button. The main
 * method which generates the table is {@link #generateTableIfNecessary()}.
 * 
 */
public class DatabaseTableviewBean {

	private static class AvailableOption {
		
		private Attribute[] attributes;
		private String userLabel;
		private String id;
		private String[] labels;
		private MessageFormat format;
		private String zeroValue;
		
		public AvailableOption(String id, String userLabel, Attribute[] attrs, String[] colLabels, MessageFormat format, String zeroValue) {
			this.id = id;
			this.userLabel = userLabel;
			this.attributes = attrs;
			this.labels = colLabels;
			this.format = format;
			this.zeroValue = zeroValue;
		}

		public Attribute[] getAttributes() {
			return attributes;
		}

		public String getId() {
			return id;
		}

		public String getUserLabel() {
			return userLabel;
		}

		public String[] getLabels() {
			return labels;
		}

		public MessageFormat getFormat() {
			return format;
		}

		public String getZeroValue() {
			return zeroValue;
		}

		public void setZeroValue(String zeroValue) {
			this.zeroValue = zeroValue;
		}
		
	}
	
	private static final String CSS_CLASS_TD_LABEL = "tbl-label";

	private static final String CSS_CLASS_TD_TOTAL = "tbl-total";

	private SearchBean searchBean;

	private Conditions conditions;

	private boolean optionsChanged = true;

	private String rowGrouping = "years25";;

	private String colGrouping = "impRegion";

	private boolean omitEmptyRowsAndColumns = true;

	private SimpleTableCell[][] table;

	//private String aggregate = "sum";
	
	private static AvailableOption[] options = new AvailableOption[] {
			new AvailableOption("expSum", "Sum of embarked slaves", new Attribute[] {new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})}, new String[] {"Embarked"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			new AvailableOption("expAvg", "Average number of embarked slaves", new Attribute[] {new FunctionAttribute("avg", new Attribute[] {Voyage.getAttribute("slaximp")})}, new String[] {"Embarked"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			new AvailableOption("expCnt", "Number of voyages - embarked slaves", new Attribute[] {new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("slaximp")})}, new String[] {"Embarked"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			
			new AvailableOption("impSum", "Sum of disembarked slaves", new Attribute[] {new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})}, new String[] {"Disembarked"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			new AvailableOption("impAvg", "Average number of disembarked slaves", new Attribute[] {new FunctionAttribute("avg", new Attribute[] {Voyage.getAttribute("slamimp")})}, new String[] {"Disembarked"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			new AvailableOption("impCnt", "Number of voyages - disembarked slaves", new Attribute[] {new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("slamimp")})}, new String[] {"Disembarked"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			
			new AvailableOption("bothSum", "Sum of embarked/disembarked slaves", new Attribute[] {new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}), new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})}, new String[] {"Embarked", "Disembarked"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			new AvailableOption("bothAcg", "Average number of embarked/disembarked slaves", new Attribute[] {new FunctionAttribute("avg", new Attribute[] {Voyage.getAttribute("slaximp")}), new FunctionAttribute("avg", new Attribute[] {Voyage.getAttribute("slamimp")})}, new String[] {"Embarked", "Disembarked"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			new AvailableOption("bothCnt", "Number of voyages - embarked/disembarked slaves", new Attribute[] {new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("slaximp")}), new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("slamimp")})}, new String[] {"Embarked", "Disembarked"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			
			new AvailableOption("sexratioAvg", "Average percentage male", new Attribute[] {new FunctionAttribute("avg", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("malrat7")})})}, new String[] {"Percentage male"}, new MessageFormat("{0,number,#,###,##0.00}%"), ""),
			new AvailableOption("sexratioCnt", "Number of voyages - percentage male", new Attribute[] {new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("malrat7")})}, new String[] {"Percentage male"}, new MessageFormat("{0,number,#,###,###}"), ""),
			
			new AvailableOption("childratioAvg", "Average percentage children", new Attribute[] {new FunctionAttribute("avg", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("chilrat7")})})}, new String[] {"Percentage children"}, new MessageFormat("{0,number,#,###,##0.00}%"), ""),
			new AvailableOption("childratioCnt", "Number of voyages - percentage children", new Attribute[] {new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("chilrat7")})}, new String[] {"Percentage children"}, new MessageFormat("{0,number,#,###,###}"), ""),
			
			new AvailableOption("mortalityAvg", "Average percentage of slaves embarked who died during voyage", new Attribute[] {new FunctionAttribute("avg", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("vymrtrat")})})}, new String[] {"Percentage of slaves embarked who died during voyage"}, new MessageFormat("{0,number,#,###,##0.00}%"), ""),
			new AvailableOption("mortalityCnt", "Number of voyages - percentage of slaves embarked who died during voyage", new Attribute[] {new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("vymrtrat")})}, new String[] {"Percentage of slaves embarked who died during voyage"}, new MessageFormat("{0,number,#,###,###}"), ""),
			
			new AvailableOption("middlepassageAvg", "Average middle passage (days)", new Attribute[] {new FunctionAttribute("avg", new Attribute[] {Voyage.getAttribute("voy2imp")})}, new String[] {"Middle passage (days)"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			new AvailableOption("middlepassageCnt", "Number of voyages - middle passage (days)", new Attribute[] {new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("voy2imp")})}, new String[] {"Middle passage (days)"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			
			new AvailableOption("standarizedtonnageAvg", "Average standarized tonnage", new Attribute[] {new FunctionAttribute("avg", new Attribute[] {Voyage.getAttribute("tonmod")})}, new String[] {"Standarized tonnage"}, new MessageFormat("{0,number,#,###,###}"), "0"),
			new AvailableOption("standarizedtonnageCnt", "Number of voyages - standarized tonnage", new Attribute[] {new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("tonmod")})}, new String[] {"Standarized tonnage"}, new MessageFormat("{0,number,#,###,###}"), "0"),
	};
	
	private AvailableOption chosenOption = options[0];
	

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

	/**
	 * The main method of the bean. Generates the data for the table stores it
	 * in {@link #table}. First, it checks if it is really necessary. Then
	 * creates a grouper for columns and rows depending on the choice of values
	 * in columns and rows. Finally, it queries the database and uses the
	 * groupers to fill the table.
	 * <p>
	 * The main of a grouper is to find a row (or column) given the id of an
	 * item (port or nation). It also helps to construct the GROUP BY part of
	 * the query for the database. When the data for the table are loaded from
	 * the database they are not sorted in any particular way. The records are
	 * read one by one and the two groupes are used to determine the row and
	 * column which should hold the given value (the imported/exported slaves).
	 * <p>
	 * The groupers for nations and regions do not require any special
	 * initializations except providing the list of ports or nations which
	 * should appear in the rows/columns. The only exception is the grouper for
	 * years. It has to use the result from the database to determine the min
	 * and max year. That is why the {@link Grouper#initSlots(Object[])} needs
	 * to have an access to the data from the database. Also, when empty cells
	 * are supposed to be hidden, a grouper needs to first scan the data in
	 * order to determine the used nations and regions.
	 */
	private void generateTableIfNecessary() { 
		// conditions from the left column (i.e. from select bean)
		Conditions newConditions = (Conditions) searchBean.getSearchParameters().getConditions().clone();

		// check if we have to
		if (!optionsChanged && newConditions.equals(conditions))
			return;
		optionsChanged = false;
		conditions = newConditions;

		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		// from select bean
		// List selectedNations = selectionBean.loadSelectedNations(sess);
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

		// set grouping in the query
		query.setGroupBy(new Attribute[] { rowGrouper.getGroupingAttribute(), colGrouper.getGroupingAttribute() });

		// we want to see: row ID, row Name, col ID, col Name
		query.addPopulatedAttribute(rowGrouper.getGroupingAttribute());
		query.addPopulatedAttribute(colGrouper.getGroupingAttribute());

		// ... and populated attributes
		for (int i = 0; i < chosenOption.getAttributes().length; i++) {
			//query.addPopulatedAttribute(new FunctionAttribute(this.aggregate, new Attribute[] { chosenOption.getAttributes()[i] }));
			query.addPopulatedAttribute(chosenOption.getAttributes()[i]);
		}

		// row extra attributes
		Attribute[] rowExtraAttributes = rowGrouper.addExtraAttributes(2 + chosenOption.getAttributes().length);
		for (int i = 0; i < rowExtraAttributes.length; i++)
			query.addPopulatedAttribute(rowExtraAttributes[i]);

		// col extra attributes
		Attribute[] colExtraAttributes = rowGrouper.addExtraAttributes(2 + chosenOption.getAttributes().length + rowExtraAttributes.length);
		for (int i = 0; i < colExtraAttributes.length; i++)
			query.addPopulatedAttribute(colExtraAttributes[i]);

		// finally query the database
		System.out.println(query.toStringWithParams().conditionString);
		System.out.println("params: " + query.toStringWithParams().properties);
		Object[] result = query.executeQuery(sess);

		// init groupers
		rowGrouper.initSlots(result);
		colGrouper.initSlots(result);

		// close db
		transaction.commit();
		sess.close();

		// what to show
		int subCols = 2;
		int extraHeaderRows = 1;
		//int expColOffset = 0;
		//int impColOffset = 1;
		
		extraHeaderRows = chosenOption.attributes.length > 1 ? 1 : 0; 
		subCols = chosenOption.attributes.length;

		// dimensions of the table
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
		// of children is precalculated
		Label[] colLabels = colGrouper.getLabels();
		for (int j = 0; j < colLabels.length; j++)
			colLabels[j].calculateLeaves();

		// fill them using labels
		int colIdx = headerLeftColsCount;
		for (int j = 0; j < colLabels.length; j++) {
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
		for (int i = 0; i < rowLabels.length; i++) {
			addRowLabel(table, rowLabels[i], rowIdx, 0, 1, headerLeftColsCount);
			rowIdx += rowLabels[i].getLeavesCount();
		}

		// extra row with exported/imported labels
		if (extraHeaderRows != 0) {
			for (int j = 0; j < dataColCount; j++) {
				for (int i = 0; i < chosenOption.getAttributes().length; i++) {
					table[headerTopRowsCount][headerLeftColsCount + subCols * j + i] = new SimpleTableCell(
							chosenOption.getLabels()[i]).setCssClass(CSS_CLASS_TD_LABEL);
				}
			}
		}

		String aggregate = ((FunctionAttribute)chosenOption.getAttributes()[0]).getFunctionName();
		
		// labels for row totals
		if (!aggregate.equals("avg")) {
			table[0][headerLeftColsCount + subCols * dataColCount + 0] = new SimpleTableCell(TastResource
					.getText("database_tableview_totals")).setColspan(2).setRowspan(headerTopRowsCount).setCssClass(
					CSS_CLASS_TD_LABEL);
		} else {
			table[0][headerLeftColsCount + subCols * dataColCount + 0] = new SimpleTableCell(TastResource
					.getText("database_tableview_averages")).setColspan(2).setRowspan(headerTopRowsCount).setCssClass(
					CSS_CLASS_TD_LABEL);
		}
		
		for (int i = 0; i < chosenOption.attributes.length; i++) {
			table[headerTopRowsCount][headerLeftColsCount + subCols * dataColCount + i] = new SimpleTableCell(
					chosenOption.getLabels()[i]).setCssClass(CSS_CLASS_TD_LABEL);
		}

		// label for col totals
		if (!aggregate.equals("avg")) {
			table[headerTopRowsCount + extraHeaderRows + dataRowCount][0] = new SimpleTableCell(TastResource
					.getText("database_tableview_totals")).setCssClass(CSS_CLASS_TD_LABEL).setColspan(
					headerLeftColsCount);
		} else {
			table[headerTopRowsCount + extraHeaderRows + dataRowCount][0] = new SimpleTableCell(TastResource
					.getText("database_tableview_averages")).setCssClass(CSS_CLASS_TD_LABEL).setColspan(
					headerLeftColsCount);
		}

		// how we want to displat it
		MessageFormat valuesFormat = this.chosenOption.getFormat();

		//totals for average - vertical
		int[][] totalV = new int[dataColCount][chosenOption.attributes.length];
		
		//totals for average - horizontal
		int[][] totalH = new int[dataRowCount][chosenOption.attributes.length];
		
		// for totals
		double[][] rowTotals = new double[dataRowCount][chosenOption.attributes.length];
		double[][] colTotals = new double[dataColCount][chosenOption.attributes.length];
		double[] totals = new double[chosenOption.attributes.length];

		// fill in the data
		for (int i = 0; i < result.length; i++) {

			Object[] row = (Object[]) result[i];
			Number[] numbers = new Number[chosenOption.attributes.length];
			for (int j = 0; j < numbers.length; j++) {
				Number n = (Number) row[2 + j];
				numbers[j] = n;
				if (numbers[j] == null) {
					numbers[j] = new Double(0.0);
				}
			}
			

			int rowIndex = rowGrouper.lookupIndex(row);
			int colIndex = colGrouper.lookupIndex(row);

			for (int j = 0; j < chosenOption.attributes.length; j++) {
				rowTotals[rowIndex][j] += numbers[j].doubleValue();
				colTotals[colIndex][j] += numbers[j].doubleValue();
				
				if (aggregate.equals("avg") && row[2 + j] != null) {
					totalH[rowIndex][j] += 1;
					totalV[colIndex][j] += 1;
				}
				
				totals[j] += numbers[j].doubleValue();
				if (numbers[j].doubleValue() == 0.0) {
					table[headerTopRowsCount + extraHeaderRows + rowIndex][headerLeftColsCount + subCols * colIndex
					       				                               	+ j] = new SimpleTableCell(chosenOption.getZeroValue());
				} else {
					table[headerTopRowsCount + extraHeaderRows + rowIndex][headerLeftColsCount + subCols * colIndex
				                               	+ j] = new SimpleTableCell(valuesFormat.format(new Object[] { numbers[j] }));
				}
			}

		}

		if (aggregate.equals("avg")) {
			for (int i = 0; i < colTotals.length; i++) {
				for (int j = 0; j < colTotals[i].length; j++) {
					colTotals[i][j] /= (double) totalV[i][j];
				}
			}
			for (int i = 0; i < rowTotals.length; i++) {
				for (int j = 0; j < rowTotals[i].length; j++) {
					rowTotals[i][j] /= (double) totalH[i][j];
				}
			}
		}

		// fill gaps
		String zeroValue = chosenOption.getZeroValue();
		for (int i = 0; i < dataRowCount; i++) {
			for (int j = 0; j < subCols * dataColCount; j++) {
				if (table[headerTopRowsCount + extraHeaderRows + i][headerLeftColsCount + j] == null) {
					table[headerTopRowsCount + extraHeaderRows + i][headerLeftColsCount + j] = new SimpleTableCell(
							zeroValue);
				}
			}
		}

		// insert row totals
		for (int i = 0; i < dataRowCount; i++) {
			for (int j = 0; j < rowTotals[i].length; j++) {
				table[headerTopRowsCount + extraHeaderRows + i][headerLeftColsCount + subCols * dataColCount
				                        + j] = new SimpleTableCell(valuesFormat.format(new Object[] { new Double(
				                        rowTotals[i][j]) })).setCssClass(CSS_CLASS_TD_TOTAL);
			}
		}

		// insert col totals
		for (int j = 0; j < dataColCount; j++) {
			for (int i = 0; i < colTotals[j].length; i++) {
				table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols * j
				                        + i] = new SimpleTableCell(valuesFormat.format(new Object[] { new Double(
				                        colTotals[j][i]) })).setCssClass(CSS_CLASS_TD_TOTAL);
			}
		}

		// main totals
		if (!aggregate.equals("avg")) {
			for (int i = 0; i < totals.length; i++) {
				table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols * dataColCount
				                           + i] = new SimpleTableCell(valuesFormat
				                           .format(new Object[] { new Double(totals[i]) })).setCssClass(CSS_CLASS_TD_TOTAL);
			}
		} else {
			for (int i = 0; i < totals.length; i++) {
				table[headerTopRowsCount + extraHeaderRows + dataRowCount][headerLeftColsCount + subCols * dataColCount
				                               + i] = new SimpleTableCell("N/A").setCssClass(CSS_CLASS_TD_TOTAL);
			}
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
		return chosenOption.id;
	}

	/**
	 * Bound to UI. Wrapper for {@link #showMode}.
	 * 
	 * @return
	 */
	public void setShowMode(String showMode) {
		for (int i = 0; i < options.length; i++) {
			if (showMode.equals(options[i].id)) {
				this.chosenOption = options[i];
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
		SelectItem[] items = new SelectItem[options.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new SelectItem(options[i].getId(), options[i].getUserLabel());
		}
		return items;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}


}