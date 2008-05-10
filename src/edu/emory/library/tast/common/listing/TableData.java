package edu.emory.library.tast.common.listing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.database.listing.formatters.AbstractAttributeFormatter;
import edu.emory.library.tast.database.listing.formatters.SimpleAttributeFormatter;
import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.Group;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Data that is presented in results (Result table or detail table).
 * 
 * @author Pawel Jurczyk
 * 
 */
public class TableData {

	/**
	 * Default formatter - calls to string (for lists creates ['val a', ....,
	 * 'val n'])
	 */
	private static final AbstractAttributeFormatter DEFAULT_FORMATTER = new SimpleAttributeFormatter();

	/**
	 * List of visible columns - basic group.
	 */
	private List columns = new ArrayList();

	/**
	 * Specific Formatters.
	 */
	private Map columnFormatters = new HashMap();

	/**
	 * Data for basic group of columns.
	 */
	private List data = new ArrayList();

	/**
	 * Column used to order results.
	 */
	private VisibleAttributeInterface orderByColumn = VisibleAttribute.getAttribute("voyageId");

	/**
	 * Current order.
	 */
	private int order = QueryValue.ORDER_ASC;

	/**
	 * Additional columns (optional).
	 */
	private List additionalColumns = new ArrayList();

	/**
	 * Data for additional columns.
	 */
	private List additionalData = new ArrayList();

	private int lastAddedToQuery = 1;
	
	private Attribute keyAttribute = null;
	
	private Map rollovers = new HashMap();
	
	/**
	 * Container for raw data that is stored in data and additionalData.
	 * 
	 * @author Pawel Jurczyk
	 * 
	 */
	public class DataTableItem {

		/**
		 * VoyageID.
		 */
		public Object voyageId;

		/**
		 * Voyage attributes.
		 */
		public Object[] dataRow;

		/**
		 * Constructor.
		 * 
		 * @param voyageId
		 * @param row
		 */
		public DataTableItem(Object voyageId, Object[] row) {
			this.voyageId = voyageId;
			this.dataRow = row;
		}

	}

	/**
	 * A class that represents visible data in the table.
	 * The class has three important fields: data (data that will be visible), attribute (attribute name/configuration)
	 * and formatter (that will be used to render data visible for user).
	 * The class also features tooltip creation which should be visible when mouse is over given table cell.
	 */
	public class ColumnData {

		/**
		 * Raw data in column
		 */
		private Object[] data;

		/**
		 * Attribute visible in column
		 */
		private VisibleAttributeInterface attribute;

		/**
		 * formatter used for rendering visible data
		 */
		private AbstractAttributeFormatter formatter;

		public ColumnData(VisibleAttributeInterface attribute, Object[] data, AbstractAttributeFormatter formatter) {
			this.attribute = attribute;
			this.data = data;
			this.formatter = formatter;
		}

		/**
		 * Gets tool tip that will be visible "onmouseover".
		 * @param table
		 * @return
		 */
		public String getToolTipText(TableData table) {

			List list = new ArrayList();
			list.add(attribute);
			Attribute[] attributes = table.getAttributesForColumn(list, 0);
			StringBuffer buffer = new StringBuffer();

			if (attributes.length > 1) {

				buffer.append("<table class=\"tooltip-class\">");
				buffer.append("<tr><td><b>" + TastResource.getText("components_table_compoundattr") + ": ");
				buffer.append(attribute.getUserLabelOrName()).append("</b></td></tr>");
				for (int i = 0; i < attributes.length; i++) {
					if (data[i] == null || "".equals(data[i])) {
						break;
					}
					//Attribute attribute = attributes[i];
					//buffer.append("<tr><td>").append(attribute.getUserLabelOrName()).append(":</td>");
					//buffer.append("<td>").append(data[i] == null ? "" : data[i]).append("</td></tr>");
					buffer.append("<tr><td>").append(data[i]).append("</td></tr>");
				}
				buffer.append("</table>");
			} else {
				buffer.append(attribute.getUserLabelOrName()).append(": ");
				buffer.append(data[0] == null ? "" : data[0]);
			}
			return buffer.toString();

		}

		public String[] getDataToDisplay() {
			if (this.data.length == 1) {
				return new String[] {formatter.format(attribute, data[0])};
			} else {
				return formatter.format(attribute, data);
			}
		}

		public String[] getRollovers() {
			String[] rollovers = new String[data.length];
			for (int i = 0; i < data.length; i++) {
				rollovers[i] = TableData.this.getRollover(data[i]);
			}
			return rollovers;
		}
	}

	/**
	 * Sets visible columns.
	 * 
	 * @param columns
	 */
	public void setVisibleColumns(List columns) {
		this.columns.clear();
		this.columns.addAll(columns);
		if (this.columns.size() > 0) {
			this.setOrderByColumn((VisibleAttributeInterface) this.columns.get(0));
			this.setOrder(QueryValue.ORDER_ASC);
		}
	}

	/**
	 * Sets visible columns.
	 * 
	 * @param columns
	 */
	public void setVisibleColumns(VisibleAttributeInterface[] columns) {
		this.columns.clear();
		for (int i = 0; i < columns.length; i++) {
			this.columns.add(columns[i]);
		}
		if (this.columns.size() > 0) {
			this.setOrderByColumn((VisibleAttributeInterface) this.columns.get(0));
			this.setOrder(QueryValue.ORDER_ASC);
		}
	}

	/**
	 * Gets visible columns.
	 * 
	 * @return
	 */
	public VisibleAttributeInterface[] getVisibleAttributes() {
		return (VisibleAttributeInterface[]) this.columns.toArray(new VisibleAttributeInterface[] {});
	}

	/**
	 * Sets optional visible columns.
	 * 
	 * @param columns
	 */
	public void setVisibleAdditionalColumns(List columns) {
		this.additionalColumns.clear();
		this.additionalColumns.addAll(columns);
	}

	/**
	 * Sets optional visible columns.
	 * 
	 * @param columns
	 */
	public void setVisibleAdditionalColumns(VisibleAttributeInterface[] columns) {
		this.additionalColumns.clear();
		for (int i = 0; i < columns.length; i++) {
			this.additionalColumns.add(columns[i]);
		}
	}

	/**
	 * Gets optional visible columns.
	 * 
	 * @return
	 */
	public VisibleAttributeInterface[] getVisibleAdditionalAttributes() {
		return (VisibleAttributeInterface[]) this.additionalColumns.toArray(new VisibleAttributeInterface[] {});
	}

	/**
	 * Sets specific formatter for any column in data table.
	 * 
	 * @param column
	 * @param formatter
	 */
	public void setFormatter(VisibleAttributeInterface column, AbstractAttributeFormatter formatter) {
		this.columnFormatters.put(column, formatter);
	}

	/**
	 * Gets formatter that should be used in rendering column.
	 * 
	 * @param column
	 * @return
	 */
	private AbstractAttributeFormatter getFormatter(VisibleAttributeInterface column) {
		if (this.columnFormatters.containsKey(column)) {
			// Registered specific formatter
			return (AbstractAttributeFormatter) this.columnFormatters.get(column);
		} else {
			// Use default formatter
			return DEFAULT_FORMATTER;
		}
	}

	/**
	 * Gets attributes that should be used in query.
	 * 
	 * @return
	 */
	public Attribute[] getAttributesForQuery() {
		ArrayList attributes = new ArrayList();
		
		attributes.add(keyAttribute);
		for (int i = 0; i < columns.size(); i++) {
			VisibleAttributeInterface attr = (VisibleAttributeInterface)columns.get(i);
			attributes.addAll(Arrays.asList(attr.getAttributes()));
		}
		return (Attribute[]) attributes.toArray(new Attribute[] {});
	}

	/**
	 * Gets attributes for optional columns that should be used in query
	 * 
	 * @return
	 */
	public Attribute[] getAdditionalAttributesForQuery() {
		ArrayList attributes = new ArrayList();
		for (int i = 0; i < this.additionalColumns.size(); i++) {
			VisibleAttributeInterface attr = (VisibleAttributeInterface)additionalColumns.get(i);
			attributes.addAll(Arrays.asList(attr.getAttributes()));
		}
		return (Attribute[]) attributes.toArray(new Attribute[] {});
	}

	/**
	 * Gets array of attributes needed for specific column.
	 * 
	 * @param columns
	 *            all columns
	 * @param column
	 *            index of element in columns
	 * @return array of attributes
	 */
	private Attribute[] getAttributesForColumn(List columns, int column) {
		ArrayList attrs = new ArrayList();
		VisibleAttributeInterface attr = (VisibleAttributeInterface)columns.get(column);
		attrs.addAll(Arrays.asList(attr.getAttributes()));
		return (Attribute[]) attrs.toArray(new Attribute[] {});
	}

	/**
	 * Gets array of indexes of data for specified column.
	 * 
	 * @param col
	 *            column number
	 * @return
	 */
	private int[] getRangeOfAttribute(int col) {
		int i = 0;
		int count = 0;
		Attribute[] attrs = null;
		// Check columns
		for (i = 0; i < col && i < this.columns.size(); i++) {
			count += this.getAttributesForColumn(this.columns, i).length;
		}
		if (i > this.columns.size() - 1) {
			// col refers additional columns
			for (; i < this.additionalColumns.size() + this.columns.size() - 1 && i < col; i++) {
				count += this.getAttributesForColumn(this.additionalColumns, i - this.columns.size()).length;
			}
			// Array of attributes refering given column
			attrs = this.getAttributesForColumn(this.additionalColumns, i - this.columns.size());
			int[] ret = new int[attrs.length];
			for (int j = 0; j < attrs.length; j++) {
				ret[j] = count + j + lastAddedToQuery;
			}
			return ret;
		} else {
			attrs = this.getAttributesForColumn(this.columns, i);
			int[] ret = new int[attrs.length];
			for (int j = 0; j < attrs.length; j++) {
				ret[j] = count + j + lastAddedToQuery;
			}
			return ret;
		}
	}

	/**
	 * Executes formatter on given column.
	 * 
	 * @param column
	 * @param rawRow
	 * @param rawCols
	 * @return
	 */
	private Object getSubItem(VisibleAttributeInterface column, Object[] rawRow, int[] rawCols) {

		// Build tmp object array
		Object[] tmp = getObjectTable(rawRow, rawCols);

		return new ColumnData(column, tmp, this.getFormatter(column));
	}

	/**
	 * Sets query response for this TableData
	 * 
	 * @param rawData
	 *            query response
	 */
	public void setData(Object[] rawData) {

		// Clear old data
		this.data.clear();
		this.additionalData.clear();

		// Parse all rows
		for (int i = 0; i < rawData.length; i++) {
			Object[] dataColumn = new Object[columns.size()];
			Object[] additionalDataColumn = new Object[additionalColumns.size()];
			Object[] rawRow = (Object[]) rawData[i];

			// Get basic columns
			for (int j = 0; j < columns.size(); j++) {
				int[] rawCols = this.getRangeOfAttribute(j);
				dataColumn[j] = this.getSubItem((VisibleAttributeInterface) columns.get(j), rawRow, rawCols);
			}

			// Get additional columns
			for (int j = 0; j < additionalColumns.size(); j++) {
				int[] rawCols = this.getRangeOfAttribute(j + this.columns.size());
				additionalDataColumn[j] = this.getSubItem((VisibleAttributeInterface) additionalColumns.get(j), rawRow, rawCols);
			}

			// Set data and additionalData if needed.
			this.data.add(new DataTableItem(((Object[]) rawData[i])[0], dataColumn));
			if (additionalDataColumn.length > 0) {
				this.additionalData.add(new DataTableItem(((Object[]) rawData[i])[0], additionalDataColumn));
			}
		}
	}

	/**
	 * Builds subarray from given object array.
	 * 
	 * @param rawRow
	 *            initial object array
	 * @param rawCols
	 *            array of indexes in rawRow that will be placed in returned
	 *            array
	 * @return array of object
	 */
	private Object[] getObjectTable(Object[] rawRow, int[] rawCols) {
		Object[] tmp = new Object[rawCols.length];
		for (int k = 0; k < tmp.length; k++) {
			tmp[k] = rawRow[rawCols[k]];
		}
		return tmp;
	}

	/**
	 * Gets data from table data.
	 * 
	 * @return
	 */
	public DataTableItem[] getData() {
		return (DataTableItem[]) this.data.toArray(new DataTableItem[] {});
	}

	/**
	 * Gets optional data from table data.
	 * 
	 * @return
	 */
	public DataTableItem[] getAdditionalData() {
		if (this.additionalData.size() == 0) {
			return null;
		}
		return (DataTableItem[]) this.additionalData.toArray(new DataTableItem[] {});
	}

	/**
	 * Gets current order for column that is being sorted.
	 * 
	 * @return
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Sets sort order.
	 * 
	 * @param order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Gets ordered column.
	 * 
	 * @return
	 */
	public VisibleAttributeInterface getOrderByColumn() {
		return orderByColumn;
	}

	/**
	 * Sets ordered columns.
	 * 
	 * @param orderByColumn
	 */
	public void setOrderByColumn(VisibleAttributeInterface orderByColumn) {
		this.orderByColumn = orderByColumn;
	}

	public void setKeyAttribute(Attribute keyAttribute) {
		this.keyAttribute = keyAttribute;
	}

	public Group[] loadAttrGroups() {
		return Group.getGroups();
	}

	public void setRollover(Object object, String information) {
		this.rollovers.put(object, information);
	}
	
	public String getRollover(Object o) {
		return (String)this.rollovers.get(o);
	}

}
