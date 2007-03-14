package edu.emory.library.tast.slaves;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.LookupCheckboxItem;
import edu.emory.library.tast.database.table.SortChangeEvent;
import edu.emory.library.tast.database.table.TableData;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.database.tabscommon.links.TableLinkManager;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Country;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Slave;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class SlavesTableBean
{
	
	private static final int AFRICA_ID = 60000;
	private static final String ATTRIBUTE = "Attribute_";
	
	private TableLinkManager pager;
	private TableData tableData;
	private String queryText;

	private SlavesQuery workingQuery = new SlavesQuery();
	private SlavesQuery currentQuery = new SlavesQuery();

	private String[] expandedEmbPorts;

	/*
	private Integer queryAgeFrom;
	private Integer queryAgeTo;
	private Integer queryHeightFrom;
	private Integer queryHeightTo;
	private Integer queryYearFrom;
	private Integer queryYearTo;
	private String querySlaveName;
	private String queryShipName;
	private Boolean queryBoy;
	private Boolean queryMan;
	private Boolean queryMale;
	private Boolean queryGirl;
	private Boolean queryWoman;
	private Boolean queryFemale;
	private String queryCountry;
	private String queryExpPort;
	private String[] selectedCountries;
	private String[] selectedExpPorts;
	private Boolean querySierraLeone;
	private Boolean queryHavana;
	*/
	
	public SlavesTableBean()
	{
		
		pager = new TableLinkManager(20);
		
		VisibleAttributeInterface[] visibleAttrs = {
			VisibleAttrSlave.getAttributeForTable("id"),
			VisibleAttrSlave.getAttributeForTable("voyageId"),
			VisibleAttrSlave.getAttributeForTable("name"),
			VisibleAttrSlave.getAttributeForTable("shipname"),
			VisibleAttrSlave.getAttributeForTable("age"),
			VisibleAttrSlave.getAttributeForTable("height"),
			VisibleAttrSlave.getAttributeForTable("datearr"),
			VisibleAttrSlave.getAttributeForTable("source"),
			VisibleAttrSlave.getAttributeForTable("sexage"),
			VisibleAttrSlave.getAttributeForTable("country"),
			VisibleAttrSlave.getAttributeForTable("majselpt"),
			VisibleAttrSlave.getAttributeForTable("majbuypt")};
			
		tableData = new TableData();
		tableData.setKeyAttribute(Slave.getAttribute("id"));
		tableData.setVisibleColumns(visibleAttrs);
		tableData.setOrderByColumn(visibleAttrs[0]);
		
		expandedEmbPorts = new String[] {String.valueOf(AFRICA_ID)};
		
		loadData(true, true);
		
	}

	/*
	private void resetToDefaultQuery()
	{
		
		queryAgeFrom = null;
		queryAgeTo = null;
		queryHeightFrom = null;
		queryHeightTo = null;
		queryYearFrom = null;
		queryYearTo = null;
		querySlaveName = null;
		queryShipName = null;
		queryBoy = new Boolean(true);
		queryMan = new Boolean(true);
		queryMale = new Boolean(true);
		queryGirl = new Boolean(true);
		queryWoman = new Boolean(true);
		queryFemale = new Boolean(true);
		queryCountry = null;
		queryExpPort = null;
		selectedCountries = new String[] {};
		selectedExpPorts = new String[] {};
		expandedExpPorts = new String[] {};
		querySierraLeone = new Boolean(true);
		queryHavana = new Boolean(true);
		
		expandedExpPorts = new String[] {String.valueOf(AFRICA_ID)};
		
	}
	
	public TableData getTableData()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		Conditions conditions = currentQuery.createConditions(sess, null);
		
		QueryValue queryValueTable = new QueryValue(new String[] {"Slave"}, new String[] {"s"}, conditions);
		Attribute[] attrs = this.tableData.getAttributesForQuery();
		for (int i = 0; i < attrs.length; i++)
		{
			queryValueTable.addPopulatedAttribute(attrs[i]);
		}
		queryValueTable.setOrder(this.tableData.getOrder());
		queryValueTable.setOrderBy(this.tableData.getOrderByColumn().getAttributes());
		queryValueTable.setFirstResult(this.linkManager.getCurrentFirstRecord());
		queryValueTable.setLimit(this.linkManager.getStep());
		this.tableData.setData(queryValueTable.executeQuery(sess));
		
		QueryValue queryValueCount = new QueryValue(new String[] {"Slave"}, new String[] {"e"}, conditions);
		queryValueCount.addPopulatedAttribute(new  FunctionAttribute("count", new Attribute[] {Estimate.getAttribute("id")}));
		Object[] ret = queryValueCount.executeQuery();
		this.linkManager.setResultsNumber(((Number) ret[0]).intValue());
		
		tran.commit();
		sess.close();
		
		return tableData;
		
	}

	private Conditions prepareMultiselectConditions(Attribute attr, Boolean[] states, Object[] values, StringBuffer queryTextBuff, String variableName, String[] labels)
	{

		boolean allSelected = true;
		boolean allDeselected = true;
		
		for (int i = 0; i < states.length; i++)
		{
			if (!states[i].booleanValue())
			{
				allSelected = false;
			}
			else
			{
				allDeselected = false;
			}
		}
		
		if (allSelected || allDeselected)
			return null;
		
		Conditions conditions = new Conditions(Conditions.JOIN_OR);

		queryTextBuff.append("<div class=\"current-query-condition\">");
		queryTextBuff.append("<span class=\"current-query-variable\">");
		queryTextBuff.append(variableName);
		queryTextBuff.append("</span>: ");
		
		int j = 0;
		for (int i = 0; i < states.length; i++)
		{
			if (states[i].booleanValue())
			{
				if (j > 0) queryTextBuff.append(", ");
				queryTextBuff.append(labels[i]);
				conditions.addCondition(attr, values[i], Conditions.OP_EQUALS);
				j++;
			}
		}
		
		return conditions;

	}

	private Conditions prepareConditions(Session sess)
	{
		
		StringBuffer queryTextBuff = new StringBuffer(); 
		
		Conditions c = new Conditions();
		
		FunctionAttribute slaveNameUpperAttr = new FunctionAttribute("upper", new Attribute[] {Slave.getAttribute("name")});
		if (!StringUtils.isNullOrEmpty(this.querySlaveName, true))
		{
			String[] s = StringUtils.extractQueryKeywords(this.querySlaveName, true);
			for (int i = 0; i < s.length; i++)
			{
				c.addCondition(slaveNameUpperAttr, "%" + s[i] + "%", Conditions.OP_LIKE);
			}
			if (s.length > 0)
			{
				queryTextBuff.append("<div class=\"current-query-condition\">");
				queryTextBuff.append("<span class=\"current-query-variable\">");
				queryTextBuff.append(TastResource.getText("slaves_query_slave_name"));
				queryTextBuff.append("</span>: ");
				queryTextBuff.append("<span class=\"current-query-value\">");
				queryTextBuff.append(this.querySlaveName.trim());
				queryTextBuff.append("</span>");
				queryTextBuff.append("</div>");
			}
		}
		
		FunctionAttribute shipNameUpperAttr = new FunctionAttribute("upper", new Attribute[] {Slave.getAttribute("shipname")});
		if (!StringUtils.isNullOrEmpty(this.queryShipName, true))
		{
			String[] s = StringUtils.extractQueryKeywords(this.queryShipName, true);
			for (int i = 0; i < s.length; i++)
			{
				c.addCondition(shipNameUpperAttr, "%" + s[i] + "%", Conditions.OP_LIKE);
			}
			if (s.length > 0)
			{
				queryTextBuff.append("<div class=\"current-query-condition\">");
				queryTextBuff.append("<span class=\"current-query-variable\">");
				queryTextBuff.append(TastResource.getText("slaves_query_ship_name"));
				queryTextBuff.append("</span>: ");
				queryTextBuff.append("<span class=\"current-query-value\">");
				queryTextBuff.append(this.queryShipName.trim());
				queryTextBuff.append("</span>");
				queryTextBuff.append("</div>");
			}
		}
		
		if (this.queryYearFrom != null)
		{
			c.addCondition(Slave.getAttribute("datearr"), this.queryYearFrom, Conditions.OP_GREATER_OR_EQUAL);
		}
		
		if (this.queryYearTo != null)
		{
			c.addCondition(Slave.getAttribute("datearr"), this.queryYearTo, Conditions.OP_SMALLER_OR_EQUAL);
		}
		
		if (this.queryYearFrom != null || this.queryYearTo != null)
		{
			queryTextBuff.append("<div class=\"current-query-condition\">");
			queryTextBuff.append("<span class=\"current-query-variable\">");
			queryTextBuff.append(TastResource.getText("slaves_query_year"));
			queryTextBuff.append("</span>: ");
			queryTextBuff.append("<span class=\"current-query-value\">");
			if (this.queryYearFrom == null)
			{
				queryTextBuff.append(TastResource.getText("slaves_query_year_to"));
				queryTextBuff.append(" ");
				queryTextBuff.append(this.queryYearTo);
			}
			else if (this.queryYearTo == null)
			{
				queryTextBuff.append(TastResource.getText("slaves_query_year_from"));
				queryTextBuff.append(" ");
				queryTextBuff.append(this.queryYearFrom);
			}
			else
			{
				queryTextBuff.append(this.queryYearFrom);
				queryTextBuff.append(" - ");
				queryTextBuff.append(this.queryYearTo);
			}
			queryTextBuff.append("</span>");
			queryTextBuff.append("</div>");
			
		}

		if (this.queryAgeFrom != null)
		{
			c.addCondition(Slave.getAttribute("age"), this.queryAgeFrom, Conditions.OP_GREATER_OR_EQUAL);
		}
		
		if (this.queryAgeTo != null)
		{
			c.addCondition(Slave.getAttribute("age"), this.queryAgeTo, Conditions.OP_SMALLER_OR_EQUAL);
		}
		
		if (this.queryAgeFrom != null || this.queryAgeTo != null)
		{
			queryTextBuff.append("<div class=\"current-query-condition\">");
			queryTextBuff.append("<span class=\"current-query-variable\">");
			queryTextBuff.append(TastResource.getText("slaves_query_age"));
			queryTextBuff.append("</span>: ");
			queryTextBuff.append("<span class=\"current-query-value\">");
			if (this.queryAgeFrom == null)
			{
				queryTextBuff.append(TastResource.getText("slaves_query_age_to"));
				queryTextBuff.append(" ");
				queryTextBuff.append(this.queryAgeTo);
			}
			else if (this.queryAgeTo == null)
			{
				queryTextBuff.append(TastResource.getText("slaves_query_age_from"));
				queryTextBuff.append(" ");
				queryTextBuff.append(this.queryAgeFrom);
			}
			else
			{
				queryTextBuff.append(this.queryAgeFrom);
				queryTextBuff.append(" - ");
				queryTextBuff.append(this.queryAgeTo);
			}
			queryTextBuff.append("</span>");
			queryTextBuff.append("</div>");
			
		}

		if (this.queryHeightFrom != null)
		{
			c.addCondition(Slave.getAttribute("height"), new Double(this.queryHeightFrom.intValue()), Conditions.OP_GREATER_OR_EQUAL);
		}
		
		if (this.queryHeightTo != null)
		{
			c.addCondition(Slave.getAttribute("height"), new Double(this.queryHeightTo.intValue()), Conditions.OP_SMALLER_OR_EQUAL);
		}
		
		if (this.queryHeightFrom != null || this.queryHeightTo != null)
		{
			queryTextBuff.append("<div class=\"current-query-condition\">");
			queryTextBuff.append("<span class=\"current-query-variable\">");
			queryTextBuff.append(TastResource.getText("slaves_query_height"));
			queryTextBuff.append("</span>: ");
			queryTextBuff.append("<span class=\"current-query-value\">");
			if (this.queryHeightFrom == null)
			{
				queryTextBuff.append(TastResource.getText("slaves_query_height_to"));
				queryTextBuff.append(" ");
				queryTextBuff.append(this.queryAgeTo);
			}
			else if (this.queryHeightTo == null)
			{
				queryTextBuff.append(TastResource.getText("slaves_query_height_from"));
				queryTextBuff.append(" ");
				queryTextBuff.append(this.queryHeightFrom);
			}
			else
			{
				queryTextBuff.append(this.queryHeightFrom);
				queryTextBuff.append(" - ");
				queryTextBuff.append(this.queryHeightTo);
			}
			queryTextBuff.append("</span>");
			queryTextBuff.append("</div>");
			
		}
		
		if (selectedExpPorts != null && selectedExpPorts.length > 0)
		{
			
			Conditions condPorts = new Conditions(Conditions.JOIN_OR);
			Pattern idSplitter = Pattern.compile(LookupCheckboxListComponent.ID_PARTS_SEPARATOR);
			
			queryTextBuff.append("<div class=\"current-query-condition\">");
			queryTextBuff.append("<span class=\"current-query-variable\">");
			queryTextBuff.append(TastResource.getText("slaves_query_embarkation"));
			queryTextBuff.append("</span>: ");
			queryTextBuff.append("<span class=\"current-query-value\">");
			
			for (int i = 0; i < selectedExpPorts.length; i++)
			{
				String[] idParts = idSplitter.split(selectedExpPorts[i]);
				if (idParts.length == 3)
				{
					Port port = Port.loadById(sess, Long.parseLong(idParts[2]));
					
					if (i > 0) queryTextBuff.append(", ");
					queryTextBuff.append(port.getName());
					
					condPorts.addCondition(Slave.getAttribute("majbuypt"), port, Conditions.OP_EQUALS);
				}
			}

			queryTextBuff.append("</span>");
			queryTextBuff.append("</div>");
			
			c.addCondition(condPorts);

		}
		
		if (selectedCountries != null && selectedCountries.length > 0)
		{

			Conditions condCountries = new Conditions(Conditions.JOIN_OR);
			
			queryTextBuff.append("<div class=\"current-query-condition\">");
			queryTextBuff.append("<span class=\"current-query-variable\">");
			queryTextBuff.append(TastResource.getText("slaves_query_country"));
			queryTextBuff.append("</span>: ");
			queryTextBuff.append("<span class=\"current-query-value\">");
			
			for (int i = 0; i < selectedCountries.length; i++)
			{
				
				Country country = Country.loadById(sess, Long.parseLong(selectedCountries[i]));
				
				if (i > 0) queryTextBuff.append(", ");
				queryTextBuff.append(country.getName());
				
				condCountries.addCondition(Slave.getAttribute("country"), country, Conditions.OP_EQUALS);

			}

			queryTextBuff.append("</span>");
			queryTextBuff.append("</div>");

			c.addCondition(condCountries);

		}

		Conditions subGender = prepareMultiselectConditions(
				Slave.getAttribute("sexage"),
				new Boolean[] {
					queryBoy,
					queryMan,
					queryMale,
					queryGirl,
					queryWoman,
					queryFemale},
				new Object[] {
					genderBoy,
					genderMan,
					genderMale,
					genderGirl,
					genderWoman,
					genderFemale},
				queryTextBuff,
				TastResource.getText("slaves_query_sexage"),
				new String[] {
					TastResource.getText("slaves_checkbox_boys"),
					TastResource.getText("slaves_checkbox_man"),
					TastResource.getText("slaves_checkbox_males"),
					TastResource.getText("slaves_checkbox_girls"),
					TastResource.getText("slaves_checkbox_woman"),
					TastResource.getText("slaves_checkbox_females")});
		
		if (subGender != null)
			c.addCondition(subGender);
		
		Conditions subDisembarkation = prepareMultiselectConditions(
				Slave.getAttribute("majselpt"),
				new Boolean[] {
					queryHavana,
					querySierraLeone},
				new Object[] {
					portHavana,
					portSierraLeone},
				queryTextBuff,
				TastResource.getText("slaves_query_captured"),
				new String[] {
					TastResource.getText("slaves_captured_havana"),
					TastResource.getText("slaves_captured_sierra_leone")});

		if (subDisembarkation != null)
			c.addCondition(subDisembarkation);
		
		if (queryTextBuff.length() == 0)
			queryTextBuff.append(TastResource.getText("slaves_current_no_query"));
		
		currentQueryText = queryTextBuff.toString();
		
		return c;
	}
	*/
	
	private VisibleAttributeInterface getVisibleAttribute(String attributeSort)
	{
		VisibleAttributeInterface ret = null;
		if (attributeSort.startsWith(ATTRIBUTE))
		{
			String attrId = attributeSort.substring(ATTRIBUTE.length(), attributeSort.length());
			ret = VisibleAttrSlave.getAttributeForTable(attrId);
		} 
		return ret;
	}
	
	private void loadData(boolean refreshCount, boolean refreshText)
	{
			
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		StringBuffer textBuff = null;
		if (refreshText) textBuff = new StringBuffer();
		
		Conditions conditions = currentQuery.createConditions(sess, textBuff);

		if (refreshText) queryText = textBuff.toString();

		QueryValue queryValueTable = new QueryValue(new String[] {"Slave"}, new String[] {"s"}, conditions);

		Attribute[] attrs = this.tableData.getAttributesForQuery();
		for (int i = 0; i < attrs.length; i++)
			queryValueTable.addPopulatedAttribute(attrs[i]);
		
		queryValueTable.setOrder(this.tableData.getOrder());
		queryValueTable.setOrderBy(this.tableData.getOrderByColumn().getAttributes());
		
		queryValueTable.setFirstResult(this.pager.getCurrentFirstRecord());
		queryValueTable.setLimit(this.pager.getStep());
		
		this.tableData.setData(queryValueTable.executeQuery(sess));
		
		if (refreshCount)
		{
			QueryValue queryValueCount = new QueryValue(new String[] {"Slave"}, new String[] {"e"}, conditions);
			queryValueCount.addPopulatedAttribute(new  FunctionAttribute("count", new Attribute[] {Estimate.getAttribute("id")}));
			Object[] ret = queryValueCount.executeQuery();
			this.pager.setResultsNumber(((Number) ret[0]).intValue());
		}
		
		tran.commit();
		sess.close();

	}
	
	public String search() throws CloneNotSupportedException
	{
		
		// nothing has changed
		if (currentQuery.equals(workingQuery))
			return null;
		
		System.out.println("not equal");
		
		// reset pager
		this.pager.reset();
		
		// copy working query
		currentQuery = (SlavesQuery) workingQuery.clone();
		
		// refresh data, count and the current query text
		loadData(true, true);

		// done
		return null;
		
	}
	
	public String reset()
	{
		
		System.out.println("he?");
		
		// restore default query
		workingQuery = new SlavesQuery();
		currentQuery = new SlavesQuery();
		
		// reset pager
		this.pager.reset();
		
		// refresh data, count and the current query text
		loadData(true, true);
		
		// done
		return null;
		
	}
	
	public void sortChanged(SortChangeEvent event)
	{
		
		// get column that will be sorted
		VisibleAttributeInterface attr = this.getVisibleAttribute(event.getAttributeSort());

		// set appropriate order
		if (this.tableData.getOrderByColumn().getName().equals(attr.getName()))
		{
			switch (this.tableData.getOrder())
			{
			case QueryValue.ORDER_ASC:
				this.tableData.setOrder(QueryValue.ORDER_DESC);
				break;
			case QueryValue.ORDER_DESC:
				this.tableData.setOrder(QueryValue.ORDER_DEFAULT);
				break;
			case QueryValue.ORDER_DEFAULT:
				this.tableData.setOrder(QueryValue.ORDER_ASC);
				break;
			}
		}
		else
		{
			this.tableData.setOrderByColumn(attr);
			this.tableData.setOrder(QueryValue.ORDER_ASC);
		}

		// reset pager
		this.pager.reset();
		
		// refresh data, but not count of query text
		loadData(false, false);
		
	}
	
	public void setStep(String step)
	{
		
		// get value
		int newStep = "all".equals(step) ? Integer.MAX_VALUE : Integer.parseInt(step);

		// changed?
		if (newStep == this.pager.getStep())
			return;
		
		// refresh data, but not count of query text
		loadData(false, false);
		
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
	
	public TableData getTableData()
	{
		return this.tableData;
	}
	
	public TableLinkManager getTableManager()
	{
		return this.pager;
	}
	
	public SlavesQuery getWorkingQuery()
	{
		return workingQuery;
	}

	public String getQueryText()
	{
		return queryText;
	}

	public int getFirstDisplayed()
	{
		return this.pager.getCurrentFirstRecord() + 1;
	}
	
	public int getLastDisplayed()
	{
		if (this.pager.getResultsNumber() == 0)
			return 0;
		else
			return this.pager.getCurrentFirstRecord() + 1 +
			(this.tableData.getData() != null ? this.tableData.getData().length - 1 : 0);
	}
	
	public String getStep()
	{
		return String.valueOf(this.pager.getStep());
	}
	
	public int getTotalRows()
	{
		return this.pager.getResultsNumber();
	}

	public String[] getExpandedEmbPorts()
	{
		return expandedEmbPorts;
	}

	public void setExpandedEmbPorts(String[] expandedExpPorts)
	{
		this.expandedEmbPorts = expandedExpPorts;
	}
	
}