package edu.emory.library.tast.ui.names;

import java.text.MessageFormat;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Slave;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.CountryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.PortAttribute;
import edu.emory.library.tast.dm.attributes.SexAgeAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.search.table.SortChangeEvent;
import edu.emory.library.tast.ui.search.table.TableData;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.ui.search.tabscommon.links.TableLinkManager;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class SlavesTableBean {
	
	private static final String ATTRIBUTE = "Attribute_";
	
	private TableData tableData;
	private Conditions conditions = new Conditions();
	//private boolean requery = false;
	private TableLinkManager linkManager = new TableLinkManager(10);
	MessageFormat valuesFormat = new MessageFormat("{0,number,#,###,###}");
	
	private int queryAgeFrom;
	private int queryAgeTo;
	private int queryHeightFrom;
	private int queryHeightTo;
	private String querySlaveName;
	private String queryShipName;
	private boolean queryBoy;
	private boolean queryMan;
	private boolean queryMale;
	private boolean queryGirl;
	private boolean queryWoman;
	private boolean queryFemail;
	private String queryCountry;
	private String queryExpRegion;
	private boolean querySierraLeona;
	private boolean queryHavana;
	
	public SlavesTableBean() {
		VisibleAttributeInterface[] visibleAttrs = new VisibleAttributeInterface[12];
		visibleAttrs[0] = VisibleAttrSlave.getAttributeForTable("id");
		visibleAttrs[1] = VisibleAttrSlave.getAttributeForTable("voyageId");
		visibleAttrs[2] = VisibleAttrSlave.getAttributeForTable("name");
		visibleAttrs[3] = VisibleAttrSlave.getAttributeForTable("shipname");
		visibleAttrs[4] = VisibleAttrSlave.getAttributeForTable("age");
		visibleAttrs[5] = VisibleAttrSlave.getAttributeForTable("height");
		visibleAttrs[6] = VisibleAttrSlave.getAttributeForTable("datearr");
		visibleAttrs[7] = VisibleAttrSlave.getAttributeForTable("source");
		visibleAttrs[8] = VisibleAttrSlave.getAttributeForTable("sexage");
		visibleAttrs[9] = VisibleAttrSlave.getAttributeForTable("country");
		visibleAttrs[10] = VisibleAttrSlave.getAttributeForTable("majselpt");
		visibleAttrs[11] = VisibleAttrSlave.getAttributeForTable("majbuypt");
		
		tableData = new TableData();
		tableData.setKeyAttribute(Slave.getAttribute("id"));
		tableData.setVisibleColumns(visibleAttrs);
		tableData.setOrderByColumn(visibleAttrs[0]);
	}
	
	public TableData getTableData() {
//		if (!this.getEstimatesBean().getConditions().equals(this.conditions) || requery) {
//			this.conditions = this.getEstimatesBean().getConditions();
//			QueryValue qValue = new QueryValue(new String[] {"Estimate"}, new String[] {"e"}, this.conditions);
//			Attribute[] attrs = this.tableData.getAttributesForQuery();
//			for (int i = 0; i < attrs.length; i++) {
//				qValue.addPopulatedAttribute(attrs[i]);
//			}
//			qValue.setOrder(this.tableData.getOrder());
//			qValue.setOrderBy(this.tableData.getOrderByColumn().getAttributes());
//			qValue.setFirstResult(this.linkManager.getCurrentFirstRecord());
//			qValue.setLimit(this.linkManager.getStep());
//			this.tableData.setData(qValue.executeQuery());
//			this.setNumberOfResults();
//			this.requery = false;
//		}
		
		QueryValue qValue = new QueryValue(new String[] {"Slave"}, new String[] {"s"}, this.conditions);
		Attribute[] attrs = this.tableData.getAttributesForQuery();
		for (int i = 0; i < attrs.length; i++) {
			qValue.addPopulatedAttribute(attrs[i]);
		}
		qValue.setOrder(this.tableData.getOrder());
		qValue.setOrderBy(this.tableData.getOrderByColumn().getAttributes());
		qValue.setFirstResult(this.linkManager.getCurrentFirstRecord());
		qValue.setLimit(this.linkManager.getStep());
		this.tableData.setData(qValue.executeQuery());
		this.setNumberOfResults();
		
		return tableData;
	}
	
	private void setNumberOfResults() {
		//this.conditions = this.getEstimatesBean().getConditions();
		QueryValue qValue = new QueryValue(new String[] {"Slave"}, new String[] {"e"}, this.conditions);
		qValue.addPopulatedAttribute(new  FunctionAttribute("count", new Attribute[] {Estimate.getAttribute("id")}));
		Object[] ret = qValue.executeQuery();
		this.linkManager.setResultsNumber(((Number) ret[0]).intValue());
	}

	public void setTableData(TableData tableData) {
		this.tableData = tableData;
	}
	
	public void sortChanged(SortChangeEvent event) {
		//Get column that will be sorted
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
		this.linkManager.reset();
		//this.requery = true;
	}
	
	private VisibleAttributeInterface getVisibleAttribute(String attributeSort) {
		VisibleAttributeInterface ret = null;
		if (attributeSort.startsWith(ATTRIBUTE)) {
			String attrId = attributeSort.substring(ATTRIBUTE.length(), attributeSort.length());
			ret = VisibleAttrSlave.getAttributeForTable(attrId);
		} 
		return ret;
	}
	
	public int getFirstDisplayed() {
		return this.linkManager.getCurrentFirstRecord() + 1;
	}
	
	public int getLastDisplayed() {
		if (this.linkManager.getResultsNumber() == 0)
			return 0;
		else
			return this.linkManager.getCurrentFirstRecord() + 1 + 
					(this.tableData.getData() != null ? this.tableData.getData().length - 1 : 0);
	}
	
	public String getStep() {
		return String.valueOf(this.linkManager.getStep());
	}
	
	public int getTotalRows() {
		return this.linkManager.getResultsNumber();
	}
	
	public void setStep(String step) {
		if ("all".equals(step)) {
			this.linkManager.setStep(Integer.MAX_VALUE);
		} else {
			this.linkManager.setStep(Integer.parseInt(step));
		}
		//this.requery = true;
	}
	
	public TableLinkManager getTableManager() {
		return this.linkManager;
	}

	public int getQueryAgeFrom()
	{
		return queryAgeFrom;
	}

	public void setQueryAgeFrom(int queryAgeFrom)
	{
		this.queryAgeFrom = queryAgeFrom;
	}

	public int getQueryAgeTo()
	{
		return queryAgeTo;
	}

	public void setQueryAgeTo(int queryAgeTo)
	{
		this.queryAgeTo = queryAgeTo;
	}

	public boolean isQueryBoy()
	{
		return queryBoy;
	}

	public void setQueryBoy(boolean queryBoy)
	{
		this.queryBoy = queryBoy;
	}

	public String getQueryCountry()
	{
		return queryCountry;
	}

	public void setQueryCountry(String queryCountry)
	{
		this.queryCountry = queryCountry;
	}

	public String getQueryExpRegion()
	{
		return queryExpRegion;
	}

	public void setQueryExpRegion(String queryExpRegion)
	{
		this.queryExpRegion = queryExpRegion;
	}

	public boolean isQueryFemail()
	{
		return queryFemail;
	}

	public void setQueryFemail(boolean queryFemail)
	{
		this.queryFemail = queryFemail;
	}

	public boolean isQueryGirl()
	{
		return queryGirl;
	}

	public void setQueryGirl(boolean queryGirl)
	{
		this.queryGirl = queryGirl;
	}

	public boolean isQueryHavana()
	{
		return queryHavana;
	}

	public void setQueryHavana(boolean queryHavana)
	{
		this.queryHavana = queryHavana;
	}

	public int getQueryHeightFrom()
	{
		return queryHeightFrom;
	}

	public void setQueryHeightFrom(int queryHeightFrom)
	{
		this.queryHeightFrom = queryHeightFrom;
	}

	public int getQueryHeightTo()
	{
		return queryHeightTo;
	}

	public void setQueryHeightTo(int queryHeightTo)
	{
		this.queryHeightTo = queryHeightTo;
	}

	public boolean isQueryMale()
	{
		return queryMale;
	}

	public void setQueryMale(boolean queryMale)
	{
		this.queryMale = queryMale;
	}

	public boolean isQueryMan()
	{
		return queryMan;
	}

	public void setQueryMan(boolean queryMan)
	{
		this.queryMan = queryMan;
	}

	public String getQueryShipName()
	{
		return queryShipName;
	}

	public void setQueryShipName(String queryShipName)
	{
		this.queryShipName = queryShipName;
	}

	public boolean isQuerySierraLeona()
	{
		return querySierraLeona;
	}

	public void setQuerySierraLeona(boolean querySierraLeona)
	{
		this.querySierraLeona = querySierraLeona;
	}

	public String getQuerySlaveName()
	{
		return querySlaveName;
	}

	public void setQuerySlaveName(String querySlaveName)
	{
		this.querySlaveName = querySlaveName;
	}

	public boolean isQueryWoman()
	{
		return queryWoman;
	}

	public void setQueryWoman(boolean queryWoman)
	{
		this.queryWoman = queryWoman;
	}
}