package edu.emory.library.tas.web.components.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

public class TableData {

	private static final AbstractAttributeFormatter DEFAULT_FORMATTER = new SimpleAttributeFormatter();

	private List columns = new ArrayList();

	private Map columnFormatters = new HashMap();

	private List data = new ArrayList();

	private VisibleColumn orderByColumn = Voyage.getAttribute("voyageId");

	private int order = QueryValue.ORDER_ASC;

	private List additionalColumns = new ArrayList();

	private List additionalData = new ArrayList();

	public class DataTableItem {
		public Long voyageId;

		public Object[] dataRow;

		public DataTableItem(Long voyageId, Object[] row) {
			this.voyageId = voyageId;
			this.dataRow = row;
		}

	}

	public void setVisibleColumns(List columns) {
		this.columns.clear();
		this.columns.addAll(columns);
		if (this.columns.size() > 0) {
			this.setOrderByColumn((VisibleColumn) this.columns.get(0));
			this.setOrder(QueryValue.ORDER_ASC);
		}
	}

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

	public VisibleColumn[] getVisibleAttributes() {
		return (VisibleColumn[]) this.columns.toArray(new VisibleColumn[] {});
	}

	public void setVisibleAdditionalColumns(List columns) {
		this.additionalColumns.clear();
		this.additionalColumns.addAll(columns);
	}

	public void setVisibleAdditionalColumns(VisibleColumn[] columns) {
		this.additionalColumns.clear();
		for (int i = 0; i < columns.length; i++) {
			this.additionalColumns.add(columns[i]);
		}
	}

	public VisibleColumn[] getVisibleAdditionalAttributes() {
		return (VisibleColumn[]) this.additionalColumns.toArray(new VisibleColumn[] {});
	}

	public void setFormatter(VisibleColumn column, AbstractAttributeFormatter formatter) {
		this.columnFormatters.put(column, formatter);
	}

	private AbstractAttributeFormatter getFormatter(VisibleColumn column) {
		if (this.columnFormatters.containsKey(column)) {
			return (AbstractAttributeFormatter) this.columnFormatters.get(column);
		} else {
			return DEFAULT_FORMATTER;
		}
	}

	public List getAttrForCAttribute(CompoundAttribute cAttr) {
		ArrayList list = new ArrayList();
		Set attributes = cAttr.getAttributes();
		list.addAll(attributes);
		AbstractAttribute.sortByName(list);
		return list;
	}

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

	private Attribute[] getAttributesForColumn(List columns, int column) {
		ArrayList attrs = new ArrayList();
		if (columns.get(column) instanceof Group) {
			Group group = (Group) columns.get(column);
			attrs.addAll(this.getAttrForGroup(group));
		} else if (columns.get(column) instanceof CompoundAttribute) {
			CompoundAttribute cAttr = (CompoundAttribute) columns.get(column);
			attrs.addAll(this.getAttrForCAttribute(cAttr));
		} else {
			Attribute attr = (Attribute) columns.get(column);
			attrs.add(attr);
		}
		return (Attribute[]) attrs.toArray(new Attribute[] {});
	}

	private int[] getRangeOfAttribute(int attr) {
		int i = 0;
		int count = 0;
		Attribute[] attrs = null;
		for (i = 0; i < attr && i < this.columns.size(); i++) {
			count += this.getAttributesForColumn(this.columns, i).length;
		}
		if (i > this.columns.size() - 1) {
			for (; i < this.additionalColumns.size() + this.columns.size() - 1 && i < attr; i++) {
				count += this.getAttributesForColumn(this.additionalColumns, i - this.columns.size()).length;
			}
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

	private String executeFormatter(VisibleColumn column, Object[] rawRow, int[] rawCols) {
		Object[] tmp = getObjectTable(rawRow, rawCols);
		if (tmp.length > 1) {
			return this.getFormatter(column).format(tmp);
		} else {
			return this.getFormatter(column).format(tmp[0]);
		}
	}

	public void setData(Object[] rawData) {
		this.data.clear();
		this.additionalData.clear();
		for (int i = 0; i < rawData.length; i++) {
			Object[] dataColumn = new Object[columns.size()];
			Object[] additionalDataColumn = new Object[additionalColumns.size()];
			Object[] rawRow = (Object[]) rawData[i];
			for (int j = 0; j < columns.size(); j++) {
				int[] rawCols = this.getRangeOfAttribute(j);
				dataColumn[j] = this.executeFormatter((VisibleColumn) columns.get(j), rawRow, rawCols);
			}
			for (int j = 0; j < additionalColumns.size(); j++) {
				int[] rawCols = this.getRangeOfAttribute(j + this.columns.size());
				additionalDataColumn[j] = this.executeFormatter((VisibleColumn) additionalColumns.get(j), rawRow,
						rawCols);
			}
			this.data.add(new DataTableItem((Long) ((Object[]) rawData[i])[0], dataColumn));
			if (additionalDataColumn.length > 0) {
				this.additionalData.add(new DataTableItem((Long) ((Object[]) rawData[i])[0], additionalDataColumn));
			}
		}
	}

	private Object[] getObjectTable(Object[] rawRow, int[] rawCols) {
		Object[] tmp = new Object[rawCols.length];
		for (int k = 0; k < tmp.length; k++) {
			tmp[k] = rawRow[rawCols[k]];
		}
		return tmp;
	}

	public DataTableItem[] getData() {
		return (DataTableItem[]) this.data.toArray(new DataTableItem[] {});
	}

	public DataTableItem[] getAdditionalData() {
		return (DataTableItem[]) this.additionalData.toArray(new DataTableItem[] {});
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public VisibleColumn getOrderByColumn() {
		return orderByColumn;
	}

	public void setOrderByColumn(VisibleColumn orderByColumn) {
		this.orderByColumn = orderByColumn;
	}
}
