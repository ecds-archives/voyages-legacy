package edu.emory.library.tast.estimates.listing;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.estimates.EstimatesBean;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.ui.search.table.SortChangeEvent;
import edu.emory.library.tast.ui.search.table.TableData;
import edu.emory.library.tast.ui.search.table.formatters.AbstractAttributeFormatter;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttrEstimate;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttribute;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimateListingBean {
	
	private static final String ATTRIBUTE = "Attribute_";
	
	private EstimatesSelectionBean estimatesBean;
	private TableData tableData;
	private Conditions conditions = null;
	private int firstResult = 0;
	private int packageSize = 10;
	private int resultLen = 0;
	private boolean requery = false;
	
	public EstimateListingBean() {
		
		VisibleAttributeInterface[] visibleAttrs = new VisibleAttributeInterface[6];
		visibleAttrs[0] = VisibleAttrEstimate.getAttributeForTable("nation");
		visibleAttrs[1] = VisibleAttrEstimate.getAttributeForTable("year");
		visibleAttrs[2] = VisibleAttrEstimate.getAttributeForTable("expRegion");
		visibleAttrs[3] = VisibleAttrEstimate.getAttributeForTable("impRegion");
		visibleAttrs[4] = VisibleAttrEstimate.getAttributeForTable("slavExported");
		visibleAttrs[5] = VisibleAttrEstimate.getAttributeForTable("slavImported");
		
		tableData = new TableData();
		tableData.setKeyAttribute(Estimate.getAttribute("id"));
		tableData.setVisibleColumns(visibleAttrs);
		tableData.setOrderByColumn(visibleAttrs[0]);
		
		tableData.setFormatter(visibleAttrs[4], new AbstractAttributeFormatter() {

			public String format(Object object) {
				return String.valueOf(Math.round((((Number)object).doubleValue() * 100)) / (double)100);
			}

			public String format(Object[] object) {
				return "";
			}
			
		});

		tableData.setFormatter(visibleAttrs[5], new AbstractAttributeFormatter() {

			public String format(Object object) {
				return String.valueOf(Math.round((((Number)object).doubleValue() * 100)) / (double)100);
			}

			public String format(Object[] object) {
				return "";
			}
			
		});
		
	}
	
	public EstimatesSelectionBean getEstimatesBean() {
		return estimatesBean;
	}

	public void setEstimatesBean(EstimatesSelectionBean estimatesBean) {
		this.estimatesBean = estimatesBean;
	}

	public TableData getTableData() {
		if (!this.getEstimatesBean().getConditions().equals(this.conditions) || requery) {
			this.conditions = this.getEstimatesBean().getConditions();
			QueryValue qValue = new QueryValue(new String[] {"Estimate"}, new String[] {"e"}, this.conditions);
			Attribute[] attrs = this.tableData.getAttributesForQuery();
			for (int i = 0; i < attrs.length; i++) {
				qValue.addPopulatedAttribute(attrs[i]);
			}
			qValue.setOrder(this.tableData.getOrder());
			qValue.setOrderBy(this.tableData.getOrderByColumn().getAttributes());
			qValue.setFirstResult(this.firstResult);
			qValue.setLimit(this.packageSize);
			this.tableData.setData(qValue.executeQuery());
			this.setNumberOfResults();
			this.requery = false;
		}
		return tableData;
	}
	
	private void setNumberOfResults() {
		this.conditions = this.getEstimatesBean().getConditions();
		QueryValue qValue = new QueryValue(new String[] {"Estimate"}, new String[] {"e"}, this.conditions);
		qValue.addPopulatedAttribute(new  FunctionAttribute("count", new Attribute[] {Estimate.getAttribute("id")}));
		Object[] ret = qValue.executeQuery();
		this.resultLen = ((Integer) ret[0]).intValue();
	}

	public void setTableData(TableData tableData) {
		this.tableData = tableData;
	}
	
	public void sortChanged(SortChangeEvent event) {
//		 Get column that will be sorted
		System.out.println(event.getAttributeSort());
		VisibleAttributeInterface attr = this.getVisibleAttribute(event.getAttributeSort());

		// Set appropriate order
		if (this.getTableData().getOrderByColumn().getName().equals(attr.getName())) {
			switch (this.getTableData().getOrder()) {
			case QueryValue.ORDER_ASC:
				this.getTableData().setOrder(QueryValue.ORDER_DESC);
				break;
			case QueryValue.ORDER_DESC:
				this.getTableData().setOrder(QueryValue.ORDER_DEFAULT);
				break;
			case QueryValue.ORDER_DEFAULT:
				this.getTableData().setOrder(QueryValue.ORDER_ASC);
				break;
			}
		} else {
			this.getTableData().setOrderByColumn(attr);
			this.getTableData().setOrder(QueryValue.ORDER_ASC);
		}

		// Indicate need of query
		this.firstResult = 0;
		this.requery = true;
	}
	
	private VisibleAttributeInterface getVisibleAttribute(String attributeSort) {
		VisibleAttributeInterface ret = null;
		if (attributeSort.startsWith(ATTRIBUTE)) {
			String attrId = attributeSort.substring(ATTRIBUTE.length(), attributeSort.length());
			ret = VisibleAttrEstimate.getAttributeForTable(attrId);
		} 
		return ret;
	}

	public String prev() {
		this.requery = true;
		this.firstResult -= this.packageSize;
		if (this.firstResult < 0) {
			this.firstResult = 0;
		}
		return null;
	}
	
	public String next() {
		this.requery = true;
		if (this.firstResult + this.packageSize < this.resultLen) {
			this.firstResult += this.packageSize;
		}
		return null;
	}
	
	public int getFirstDisplayed() {
		return this.firstResult + 1;
	}
	
	public int getLastDisplayed() {
		int number = this.getTableData().getData().length;
		return this.firstResult + number;
	}
	
	public String getStep() {
		return String.valueOf(this.packageSize);
	}
	
	public int getTotalRows() {
		return this.resultLen;
	}
	
	public void setStep(String step) {
		if ("all".equals(step)) {
			this.packageSize = Integer.MAX_VALUE;
		} else {
			this.packageSize = Integer.parseInt(step);
		}
		this.requery = true;
	}
}
