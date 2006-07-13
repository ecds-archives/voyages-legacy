package edu.emory.library.tas.web.components.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.attrGroups.VisibleColumn;
import edu.emory.library.tas.attrGroups.formatters.AbstractAttributeFormatter;
import edu.emory.library.tas.attrGroups.formatters.SimpleAttributeFormatter;
import edu.emory.library.tas.util.query.QueryValue;

/**
 * Data that is presented in results (Result table or detail table).
 * @author Pawel Jurczyk
 *
 */
public class TableData {

	/**
	 * Default formatter - calls to string (for lists creates ['val a', ...., 'val n'])
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
	private VisibleColumn orderByColumn = Voyage.getAttribute("voyageId");

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

	/**
	 * Container for raw data that is stored in data and additionalData.
	 * @author Pawel Jurczyk
	 *
	 */
	public class DataTableItem {
		
		/**
		 * VoyageID.
		 */
		public Long voyageId;

		/**
		 * Voyage attributes.
		 */
		public Object[] dataRow;

		/**
		 * Constructor.
		 * @param voyageId
		 * @param row
		 */
		public DataTableItem(Long voyageId, Object[] row) {
			this.voyageId = voyageId;
			this.dataRow = row;
		}

	}

	/**
	 * Sets visible columns.
	 * @param columns
	 */
	public void setVisibleColumns(List columns) {
		this.columns.clear();
		this.columns.addAll(columns);
		if (this.columns.size() > 0) {
			this.setOrderByColumn((VisibleColumn) this.columns.get(0));
			this.setOrder(QueryValue.ORDER_ASC);
		}
	}

	/**
	 * Sets visible columns.
	 * @param columns
	 */
	public void setVisibleColumns(VisibleColumn[] columns) {
		this.columns.clear();
		for (int i = 0; i < columns.length; i++) {
			this.columns.add(columns[i]);
		}
		if (this.columns.size() > 0) {
			this.setOrderByColumn((VisibleColumn) this.columns.get(0));
			this.setOrder(QueryValue.ORDER_ASC);
		}
	}

	/**
	 * Gets visible columns.
	 * @return
	 */
	public VisibleColumn[] getVisibleAttributes() {
		return (VisibleColumn[]) this.columns.toArray(new VisibleColumn[] {});
	}

	/**
	 * Sets optional visible columns.
	 * @param columns
	 */
	public void setVisibleAdditionalColumns(List columns) {
		this.additionalColumns.clear();
		this.additionalColumns.addAll(columns);
	}

	/**
	 * Sets optional visible columns.
	 * @param columns
	 */
	public void setVisibleAdditionalColumns(VisibleColumn[] columns) {
		this.additionalColumns.clear();
		for (int i = 0; i < columns.length; i++) {
			this.additionalColumns.add(columns[i]);
		}
	}

	/**
	 * Gets optional visible columns.
	 * @return
	 */
	public VisibleColumn[] getVisibleAdditionalAttributes() {
		return (VisibleColumn[]) this.additionalColumns.toArray(new VisibleColumn[] {});
	}

	/**
	 * Sets specific formatter for any column in data table.
	 * @param column
	 * @param formatter
	 */
	public void setFormatter(VisibleColumn column, AbstractAttributeFormatter formatter) {
		this.columnFormatters.put(column, formatter);
	}

	/**
	 * Gets formatter that should be used in rendering column.
	 * @param column
	 * @return
	 */
	private AbstractAttributeFormatter getFormatter(VisibleColumn column) {
		if (this.columnFormatters.containsKey(column)) {
			//Registered specific formatter
			return (AbstractAttributeFormatter) this.columnFormatters.get(column);
		} else {
			//Use default formatter
			return DEFAULT_FORMATTER;
		}
	}

	/**
	 * Gets all attributes for given compound attribute.
	 * @param cAttr
	 * @return
	 */
	public List getAttrForCAttribute(CompoundAttribute cAttr) {
		ArrayList list = new ArrayList();
		Set attributes = cAttr.getAttributes();
		list.addAll(attributes);
		AbstractAttribute.sortByName(list);
		return list;
	}

	/**
	 * Gets all attributes for given group.
	 * @param group
	 * @return
	 */
	public List getAttrForGroup(Group group) {
		ArrayList list = new ArrayList();
		Set cAttrs = group.getCompoundAttributes();
		for (Iterator iter = cAttrs.iterator(); iter.hasNext();) {
			CompoundAttribute element = (CompoundAttribute) iter.next();
			list.addAll(this.getAttrForCAttribute(element));
		}
		Set attributes = group.getAttributes();
		list.addAll(attributes);
		return list;
	}

	/**
	 * Gets attributes that should be used in query.
	 * @return
	 */
	public Attribute[] getAttributesForQuery() {
		ArrayList attributes = new ArrayList();
		attributes.add(Voyage.getAttribute("voyageId"));
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i) instanceof Group) {
				Group group = (Group) columns.get(i);
				attributes.addAll(this.getAttrForGroup(group));
			} else if (columns.get(i) instanceof CompoundAttribute) {
				CompoundAttribute cAttr = (CompoundAttribute) columns.get(i);
				attributes.addAll(this.getAttrForCAttribute(cAttr));
			} else {
				Attribute attr = (Attribute) columns.get(i);
				attributes.add(attr);
			}
		}
		return (Attribute[]) attributes.toArray(new Attribute[] {});
	}

	/**
	 * Gets attributes for optional columns that should be used in query
	 * @return
	 */
	public Attribute[] getAdditionalAttributesForQuery() {
		ArrayList attributes = new ArrayList();
		for (int i = 0; i < this.additionalColumns.size(); i++) {
			if (this.additionalColumns.get(i) instanceof Group) {
				Group group = (Group) this.additionalColumns.get(i);
				attributes.addAll(this.getAttrForGroup(group));
			} else if (this.additionalColumns.get(i) instanceof CompoundAttribute) {
				CompoundAttribute cAttr = (CompoundAttribute) this.additionalColumns.get(i);
				attributes.addAll(this.getAttrForCAttribute(cAttr));
			} else {
				Attribute attr = (Attribute) this.additionalColumns.get(i);
				attributes.add(attr);
			}
		}
		return (Attribute[]) attributes.toArray(new Attribute[] {});
	}

	/**
	 * Gets array of attributes needed for specific column.
	 * @param columns all columns
	 * @param column  index of element in columns
	 * @return array of attributes
	 */
	private Attribute[] getAttributesForColumn(List columns, int column) {
		ArrayList attrs = new ArrayList();
		if (columns.get(column) instanceof Group) {
			//Group of attributes
			Group group = (Group) columns.get(column);
			attrs.addAll(this.getAttrForGroup(group));
		} else if (columns.get(column) instanceof CompoundAttribute) {
			//Compound attribute
			CompoundAttribute cAttr = (CompoundAttribute) columns.get(column);
			attrs.addAll(this.getAttrForCAttribute(cAttr));
		} else {
			//Simple attribute
			Attribute attr = (Attribute) columns.get(column);
			attrs.add(attr);
		}
		return (Attribute[]) attrs.toArray(new Attribute[] {});
	}

	/**
	 * Gets array of indexes of data for specified column.
	 * @param col column number
	 * @return
	 */
	private int[] getRangeOfAttribute(int col) {
		int i = 0;
		int count = 0;
		Attribute[] attrs = null;
		//Check columns
		for (i = 0; i < col && i < this.columns.size(); i++) {
			count += this.getAttributesForColumn(this.columns, i).length;
		}
		if (i > this.columns.size() - 1) {
			//col refers additional columns
			for (; i < this.additionalColumns.size() + this.columns.size() - 1 && i < col; i++) {
				count += this.getAttributesForColumn(this.additionalColumns, i - this.columns.size()).length;
			}
			//Array of attributes refering given column
			attrs = this.getAttributesForColumn(this.additionalColumns, i - this.columns.size());
			int[] ret = new int[attrs.length];
			for (int j = 0; j < attrs.length; j++) {
				ret[j] = count + j + 1;
			}
			return ret;
		} else {
			attrs = this.getAttributesForColumn(this.columns, i);
			int[] ret = new int[attrs.length];
			for (int j = 0; j < attrs.length; j++) {
				ret[j] = count + j + 1;
			}
			return ret;
		}
	}

	/**
	 * Executes formatter on given column.
	 * @param column
	 * @param rawRow
	 * @param rawCols
	 * @return
	 */
	private String executeFormatter(VisibleColumn column, Object[] rawRow, int[] rawCols) {
		
		//Build tmp object array
		Object[] tmp = getObjectTable(rawRow, rawCols);
		
		//Execute appropriate formatter
		if (tmp.length > 1) {
			return this.getFormatter(column).format(tmp);
		} else {
			return this.getFormatter(column).format(tmp[0]);
		}
	}

	/**
	 * Sets query response for this TableData
	 * @param rawData query response
	 */
	public void setData(Object[] rawData) {
		
		//Clear old data
		this.data.clear();
		this.additionalData.clear();
		
		//Parse all rows
		for (int i = 0; i < rawData.length; i++) {
			Object[] dataColumn = new Object[columns.size()];
			Object[] additionalDataColumn = new Object[additionalColumns.size()];
			Object[] rawRow = (Object[]) rawData[i];
			
			//Get basic columns
			for (int j = 0; j < columns.size(); j++) {
				int[] rawCols = this.getRangeOfAttribute(j);
				dataColumn[j] = this.executeFormatter((VisibleColumn) columns.get(j), rawRow, rawCols);
			}
			
			//Get additional columns
			for (int j = 0; j < additionalColumns.size(); j++) {
				int[] rawCols = this.getRangeOfAttribute(j + this.columns.size());
				additionalDataColumn[j] = this.executeFormatter((VisibleColumn) additionalColumns.get(j), rawRow,
						rawCols);
			}
			
			//Set data and additionalData if needed.
			this.data.add(new DataTableItem((Long) ((Object[]) rawData[i])[0], dataColumn));
			if (additionalDataColumn.length > 0) {
				this.additionalData.add(new DataTableItem((Long) ((Object[]) rawData[i])[0], additionalDataColumn));
			}
		}
	}

	/**
	 * Builds subarray from given object array.
	 * @param rawRow  initial object array
	 * @param rawCols array of indexes in rawRow that will be placed in returned array 
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
	 * @return
	 */
	public DataTableItem[] getData() {
		return (DataTableItem[]) this.data.toArray(new DataTableItem[] {});
	}

	/**
	 * Gets optional data from table data.
	 * @return
	 */
	public DataTableItem[] getAdditionalData() {
		return (DataTableItem[]) this.additionalData.toArray(new DataTableItem[] {});
	}

	/**
	 * Gets current order for column that is being sorted.
	 * @return
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Sets sort order.
	 * @param order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Gets ordered column.
	 * @return
	 */
	public VisibleColumn getOrderByColumn() {
		return orderByColumn;
	}

	/**
	 * Sets ordered columns.
	 * @param orderByColumn
	 */
	public void setOrderByColumn(VisibleColumn orderByColumn) {
		this.orderByColumn = orderByColumn;
	}
}
