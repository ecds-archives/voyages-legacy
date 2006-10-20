package edu.emory.library.tast.estimates.listing;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.estimates.EstimatesBean;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.ui.search.table.SortChangeEvent;
import edu.emory.library.tast.ui.search.table.TableData;
import edu.emory.library.tast.ui.search.table.formatters.AbstractAttributeFormatter;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttrEstimate;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimateListingBean {
	
	private EstimatesSelectionBean estimatesBean;
	private TableData tableData;
	private Conditions conditions = null;
	private int firstResult = 0;
	private int packageSize = 10;
	
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
		tableData.setFormatter(visibleAttrs[0], new AbstractAttributeFormatter() {

			public String format(Object object) {
				// TODO Auto-generated method stub
				return ((Nation)object).getName();
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
		if (!this.getEstimatesBean().getConditions().equals(this.conditions)) {
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
		}
		return tableData;
	}

	public void setTableData(TableData tableData) {
		this.tableData = tableData;
	}
	
	public void sortChanged(SortChangeEvent event) {
		System.out.println("Sort: " + event.getAttributeSort());
	}
}
