package edu.emory.library.tast.ui.names;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Country;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.SexAge;
import edu.emory.library.tast.dm.Slave;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.ui.LookupCheckboxItem;
import edu.emory.library.tast.ui.LookupCheckboxListComponent;
import edu.emory.library.tast.ui.search.table.SortChangeEvent;
import edu.emory.library.tast.ui.search.table.TableData;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.ui.search.tabscommon.links.TableLinkManager;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class SlavesTableBean {
	
	private static final String ATTRIBUTE = "Attribute_";
	
	private TableData tableData;
	private TableLinkManager linkManager = new TableLinkManager(20);
	MessageFormat valuesFormat = new MessageFormat("{0,number,#,###,###}");
	
	private Integer queryAgeFrom;
	private Integer queryAgeTo;
	private Integer queryHeightFrom;
	private Integer queryHeightTo;
	private String querySlaveName;
	private String queryShipName;
	private boolean queryBoy = true;
	private boolean queryMan = true;
	private boolean queryMale = true;
	private boolean queryGirl = true;
	private boolean queryWoman = true;
	private boolean queryFemail = true;
	private String queryCountry;
	private String queryExpPort;
	private String[] selectedCountries = new String[] {};
	private String[] selectedExpPorts = new String[] {};
	private String[] expandedExpPorts = new String[] {};
	private boolean querySierraLeone = true;
	private boolean queryHavana = true;
	private Port havanaPort;
	private Port slPort;
	private SexAge sBoy;
	private SexAge sGirl;
	private SexAge sMale;
	private SexAge sFemale;
	private SexAge sMan;
	private SexAge sWoman;
	
	public SlavesTableBean()
	{
		
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
		
		expandedExpPorts = new String[] {"60000"};
		
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		havanaPort = Port.loadById(sess, 30112);
		slPort = Port.loadById(sess, 60220);
		sBoy = SexAge.loadById(sess, 2);
		sGirl = SexAge.loadById(sess, 6);
		sMale = SexAge.loadById(sess, 3);
		sFemale = SexAge.loadById(sess, 4);
		sMan = SexAge.loadById(sess, 1);
		sWoman = SexAge.loadById(sess, 5);
		
		tran.commit();
		sess.close();
		
	}
	
	public TableData getTableData()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		Conditions conditions = this.prepareConditions(sess);
		
		QueryValue qValue = new QueryValue(new String[] {"Slave"}, new String[] {"s"}, conditions);
		Attribute[] attrs = this.tableData.getAttributesForQuery();
		for (int i = 0; i < attrs.length; i++)
		{
			qValue.addPopulatedAttribute(attrs[i]);
		}
		qValue.setOrder(this.tableData.getOrder());
		qValue.setOrderBy(this.tableData.getOrderByColumn().getAttributes());
		qValue.setFirstResult(this.linkManager.getCurrentFirstRecord());
		qValue.setLimit(this.linkManager.getStep());
		this.tableData.setData(qValue.executeQuery(sess));
		this.setNumberOfResults(sess);
		
		tran.commit();
		sess.close();
		
		return tableData;
		
	}
	
	private void setNumberOfResults(Session sess)
	{
		//this.conditions = this.getEstimatesBean().getConditions();
		QueryValue qValue = new QueryValue(new String[] {"Slave"}, new String[] {"e"}, this.prepareConditions(sess));
		qValue.addPopulatedAttribute(new  FunctionAttribute("count", new Attribute[] {Estimate.getAttribute("id")}));
		Object[] ret = qValue.executeQuery();
		this.linkManager.setResultsNumber(((Number) ret[0]).intValue());
	}

	private Conditions prepareConditions(Session sess)
	{
		
		Conditions c = new Conditions();
		if (this.queryAgeFrom != null) {
			c.addCondition(Slave.getAttribute("age"), this.queryAgeFrom, Conditions.OP_GREATER_OR_EQUAL);
		}
		if (this.queryAgeTo != null) {
			c.addCondition(Slave.getAttribute("age"), this.queryAgeTo, Conditions.OP_SMALLER_OR_EQUAL);
		}
		if (this.queryHeightFrom != null) {
			c.addCondition(Slave.getAttribute("height"), new Double(this.queryHeightFrom.intValue()), Conditions.OP_GREATER_OR_EQUAL);
		}
		if (this.queryHeightTo != null) {
			c.addCondition(Slave.getAttribute("height"), new Double(this.queryHeightTo.intValue()), Conditions.OP_SMALLER_OR_EQUAL);
		}
		if (!StringUtils.isNullOrEmpty(this.querySlaveName)) {
			String[] s = StringUtils.extractQueryKeywords(this.querySlaveName, true);
			for (int i = 0; i < s.length; i++) {
				c.addCondition(new FunctionAttribute("upper", new Attribute[] {Slave.getAttribute("name")}), "%" + s[i] + "%", Conditions.OP_LIKE);
			}
		}
		if (!StringUtils.isNullOrEmpty(this.queryShipName)) {
			String[] s = StringUtils.extractQueryKeywords(this.queryShipName, true);
			for (int i = 0; i < s.length; i++) {
				c.addCondition(new FunctionAttribute("upper", new Attribute[] {Slave.getAttribute("shipname")}), "%" + s[i] + "%", Conditions.OP_LIKE);
			}
		}
		
		if (selectedExpPorts != null && selectedExpPorts.length > 0)
		{
			Conditions condPorts = new Conditions(Conditions.JOIN_OR);
			Pattern idSplitter = Pattern.compile(LookupCheckboxListComponent.ID_PARTS_SEPARATOR); 
			for (int i = 0; i < selectedExpPorts.length; i++)
			{
				String[] idParts = idSplitter.split(selectedExpPorts[i]);
				if (idParts.length == 3)
				{
					condPorts.addCondition(
							Slave.getAttribute("majbuypt"),
							Port.loadById(sess, Long.parseLong(idParts[2])),
							Conditions.OP_EQUALS);
				}
			}
			c.addCondition(condPorts);
		}
		
		if (selectedCountries != null && selectedCountries.length > 0)
		{
			Conditions condCountries = new Conditions(Conditions.JOIN_OR);
			for (int i = 0; i < selectedCountries.length; i++)
			{
				condCountries.addCondition(
						Slave.getAttribute("country"),
						Country.loadById(sess, Long.parseLong(selectedCountries[i])),
						Conditions.OP_EQUALS);
			}
			c.addCondition(condCountries);
		}

		if (!StringUtils.isNullOrEmpty(this.queryCountry)) {
			String[] s = StringUtils.extractQueryKeywords(this.queryCountry, true);
			for (int i = 0; i < s.length; i++) {
				c.addCondition(new FunctionAttribute("upper", new Attribute[] {new SequenceAttribute(new Attribute[] {Slave.getAttribute("country"), Country.getAttribute("name")})}), "%" + s[i] + "%", Conditions.OP_LIKE);
			}
		}
		if (!StringUtils.isNullOrEmpty(this.queryExpPort)) {
			String[] s = StringUtils.extractQueryKeywords(this.queryExpPort, true);
			for (int i = 0; i < s.length; i++) {
				c.addCondition(new FunctionAttribute("upper", new Attribute[] {new SequenceAttribute(new Attribute[] {Slave.getAttribute("majbuypt"), Port.getAttribute("name")})}), "%" + s[i] + "%", Conditions.OP_LIKE);
			}
		}
		
		Conditions subGender = null;
		if (!queryBoy && !queryFemail && !queryGirl && !queryMale && !queryMan && !queryWoman) {
			subGender = new Conditions(Conditions.JOIN_AND);
			subGender.addCondition(Slave.getAttribute("sexage"), sBoy, Conditions.OP_NOT_EQUALS);
			subGender.addCondition(Slave.getAttribute("sexage"), sGirl, Conditions.OP_NOT_EQUALS);
			subGender.addCondition(Slave.getAttribute("sexage"), sMale, Conditions.OP_NOT_EQUALS);
			subGender.addCondition(Slave.getAttribute("sexage"), sFemale, Conditions.OP_NOT_EQUALS);
			subGender.addCondition(Slave.getAttribute("sexage"), sMan, Conditions.OP_NOT_EQUALS);
			subGender.addCondition(Slave.getAttribute("sexage"), sWoman, Conditions.OP_NOT_EQUALS);
		} else {
			subGender = new Conditions(Conditions.JOIN_OR);
			if (queryBoy) subGender.addCondition(Slave.getAttribute("sexage"), sBoy, Conditions.OP_EQUALS);
			if (queryGirl) subGender.addCondition(Slave.getAttribute("sexage"), sGirl, Conditions.OP_EQUALS);
			if (queryMale) subGender.addCondition(Slave.getAttribute("sexage"), sMale, Conditions.OP_EQUALS);
			if (queryFemail) subGender.addCondition(Slave.getAttribute("sexage"), sFemale, Conditions.OP_EQUALS);
			if (queryMan) subGender.addCondition(Slave.getAttribute("sexage"), sMan, Conditions.OP_EQUALS);
			if (queryWoman) subGender.addCondition(Slave.getAttribute("sexage"), sWoman, Conditions.OP_EQUALS);
		}
		if (subGender != null) {
			c.addCondition(subGender);
		}
		
		Conditions subSell = null;
		if (!this.queryHavana && !this.querySierraLeone)  {
			subSell = new Conditions(Conditions.JOIN_AND);
			subSell.addCondition(Slave.getAttribute("majselpt"), havanaPort, Conditions.OP_NOT_EQUALS);
			subSell.addCondition(Slave.getAttribute("majselpt"), slPort, Conditions.OP_NOT_EQUALS);
		} else if (!this.queryHavana || !this.querySierraLeone) {
			subSell = new Conditions(Conditions.JOIN_AND);
			if (this.queryHavana) {
				subSell.addCondition(Slave.getAttribute("majselpt"), havanaPort, Conditions.OP_EQUALS);
			} else {
				subSell.addCondition(Slave.getAttribute("majselpt"), slPort, Conditions.OP_EQUALS);
			}
		} 
		if (subSell != null) {
			c.addCondition(subSell);
		}
		return c;
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
	
	public String search() {
		this.linkManager.reset();
		return null;
	}
	
	public LookupCheckboxItem[] getCountries()
	{

		String hsql =
			"from Country c " +
			"where c in (select s.country from Slave s group by s.country) " +
			"order by c.name";

		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		Query query = sess.createQuery(hsql);
		List countries = query.list();

		LookupCheckboxItem[] countryUi = new LookupCheckboxItem[countries.size()];
		
		int i = 0;
		for (Iterator iter = countries.iterator(); iter.hasNext();)
		{
			Country country = (Country) iter.next();
			countryUi[i++] = new LookupCheckboxItem(
					String.valueOf(country.getId()),
					country.getName());
		}

		tran.commit();
		sess.close();

		return countryUi;

	}
	
	public LookupCheckboxItem[] getExpPorts()
	{

		String hsql =
			"from Port p " +
			"where p in (select s.majbuypt from Slave s group by s.majbuypt) " +
			"order by p.region.area.order, p.region.order, p.order";

		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		Query query = sess.createQuery(hsql);
		List portsDb = query.list();
		
		int portsCount = portsDb.size();

		Port port = (Port) portsDb.get(0);
		Region region = port.getRegion();
		Area area = region.getArea();
		
		List tmpAreas = new ArrayList();
		List tmpRegions = new ArrayList();
		List tmpPorts = new ArrayList();

		int i = 0;
		while (i < portsCount)
		{
			Area groupArea = area;
			
			LookupCheckboxItem areaItem = new LookupCheckboxItem();
			areaItem.setId(String.valueOf(area.getId()));
			areaItem.setText(area.getName());
			
			tmpAreas.add(areaItem);
			tmpRegions.clear();

			while (i < portsCount && groupArea.equals(area))
			{
				Region groupRegion = region;
				
				LookupCheckboxItem regionItem = new LookupCheckboxItem();
				regionItem.setId(String.valueOf(region.getId()));
				regionItem.setText(region.getName());
				
				tmpRegions.add(regionItem);
				tmpPorts.clear();

				while (i < portsCount && groupRegion.equals(region))
				{
					
					tmpPorts.add(new LookupCheckboxItem(
							String.valueOf(port.getId()),
							port.getName()));

					if (++i < portsCount)
					{
						port = (Port) portsDb.get(i);
						region = port.getRegion();
						area = region.getArea();
					}

				}
				
				LookupCheckboxItem portItems[] = new LookupCheckboxItem[tmpPorts.size()];
				tmpPorts.toArray(portItems);
				regionItem.setChildren(portItems);

			}
			
			LookupCheckboxItem regionItems[] = new LookupCheckboxItem[tmpRegions.size()];
			tmpRegions.toArray(regionItems);
			areaItem.setChildren(regionItems);
			
		}
		
		LookupCheckboxItem areaItems[] = new LookupCheckboxItem[tmpAreas.size()];
		tmpAreas.toArray(areaItems);

		/*
		Query query = sess.createQuery(hsql);
		List countries = query.list();

		LookupCheckboxItem[] countryUi = new LookupCheckboxItem[countries.size()];
		
		int i = 0;
		for (Iterator iter = countries.iterator(); iter.hasNext();)
		{
			Country country = (Country) iter.next();
			countryUi[i++] = new LookupCheckboxItem(
					String.valueOf(country.getId()),
					country.getName());
		}
		*/

		tran.commit();
		sess.close();

		return areaItems;

	}

	public TableLinkManager getTableManager() {
		return this.linkManager;
	}

	public Integer getQueryAgeFrom()
	{
		return queryAgeFrom;
	}

	public void setQueryAgeFrom(Integer queryAgeFrom)
	{
		this.queryAgeFrom = queryAgeFrom;
	}

	public Integer getQueryAgeTo()
	{
		return queryAgeTo;
	}

	public void setQueryAgeTo(Integer queryAgeTo)
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

	public String getQueryExpPort()
	{
		return queryExpPort;
	}

	public void setQueryExpPort(String queryExpRegion)
	{
		this.queryExpPort = queryExpRegion;
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

	public Integer getQueryHeightFrom()
	{
		return queryHeightFrom;
	}

	public void setQueryHeightFrom(Integer queryHeightFrom)
	{
		this.queryHeightFrom = queryHeightFrom;
	}

	public Integer getQueryHeightTo()
	{
		return queryHeightTo;
	}

	public void setQueryHeightTo(Integer queryHeightTo)
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

	public boolean isQuerySierraLeone()
	{
		return querySierraLeone;
	}

	public void setQuerySierraLeone(boolean querySierraLeona)
	{
		this.querySierraLeone = querySierraLeona;
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

	public String[] getSelectedCountries()
	{
		return selectedCountries;
	}

	public void setSelectedCountries(String[] selectedCountries)
	{
		this.selectedCountries = selectedCountries;
	}

	public String[] getExpandedExpPorts()
	{
		return expandedExpPorts;
	}

	public void setExpandedExpPorts(String[] expandedExpRegions)
	{
		this.expandedExpPorts = expandedExpRegions;
	}

	public String[] getSelectedExpPorts()
	{
		return selectedExpPorts;
	}

	public void setSelectedExpPorts(String[] selectedExpRegions)
	{
		this.selectedExpPorts = selectedExpRegions;
	}

}