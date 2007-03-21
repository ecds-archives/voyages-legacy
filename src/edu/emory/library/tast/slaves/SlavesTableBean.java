package edu.emory.library.tast.slaves;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.ajaxanywhere.AAUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.LookupCheckboxItem;
import edu.emory.library.tast.common.voyage.VoyageDetailBean;
import edu.emory.library.tast.database.table.ShowDetailsEvent;
import edu.emory.library.tast.database.table.SortChangeEvent;
import edu.emory.library.tast.database.table.TableData;
import edu.emory.library.tast.database.table.formatters.AbstractAttributeFormatter;
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
	private List querySummary;
	private int expectedResults;

	private SlavesQuery workingQuery = new SlavesQuery();
	private SlavesQuery currentQuery = new SlavesQuery();

	private String[] expandedEmbPorts;
	private VoyageDetailBean voyageBean;

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
		tableData.setKeyAttribute(Slave.getAttribute("voyageId"));
		tableData.setVisibleColumns(visibleAttrs);
		tableData.setOrderByColumn(visibleAttrs[0]);
		tableData.setFormatter(VisibleAttrSlave.getAttributeForTable("majselpt"), 
				new AbstractAttributeFormatter() {

					public String format(VisibleAttributeInterface attr, Object object) {
						return Slave.getDisembarkationCode((Port)object);
					}

					public String format(VisibleAttributeInterface attr, Object[] object) {						
						return null;
					}
			
		});
		
		
		expandedEmbPorts = new String[] {String.valueOf(AFRICA_ID)};
		
		loadData(true, true);
		
	}

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
		
		if (refreshText) querySummary = new ArrayList();
		Conditions conditions = currentQuery.createConditions(sess,
				refreshText ? querySummary : null);

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
			expectedResults = ((Number) ret[0]).intValue();
		}
		
		tran.commit();
		sess.close();

	}
	
	private void updateExpectedCount()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		Conditions conditions = workingQuery.createConditions(sess, null);
		
		QueryValue queryValueCount = new QueryValue(new String[] {"Slave"}, new String[] {"e"}, conditions);
		queryValueCount.addPopulatedAttribute(new  FunctionAttribute("count", new Attribute[] {Estimate.getAttribute("id")}));
		Object[] ret = queryValueCount.executeQuery();
		expectedResults = ((Number) ret[0]).intValue();
		
		tran.commit();
		sess.close();
		
	}
	
	public String searchFromBasicBox()
	{
		search("totalBoxBasic");
		return null;
	}
	
	public String searchFromCountryBox()
	{
		search("totalBoxCountry");
		return null;
	}

	public String searchFromPlacesBox()
	{
		search("totalBoxPlaces");
		return null;
	}

	private void search(String zoneName)
	{
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		if (AAUtils.isAjaxRequest(request))
		{

			// indicate what should be rendered
			AAUtils.addZonesToRefresh(request, zoneName);
			
			// and update count
			updateExpectedCount();
			
		}
		else
		{

			// nothing has changed
			if (currentQuery.equals(workingQuery))
				return;
			
			// reset pager
			this.pager.reset();

			// copy working query
			try
			{
				currentQuery = (SlavesQuery) workingQuery.clone();
			}
			catch (CloneNotSupportedException cnse) {}
			
			// refresh data, count and the current query text
			loadData(true, true);
			
		}
		
	}
	
	public String reset()
	{
		
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
		if (newStep != this.pager.getStep())
		{
			this.pager.setStep(newStep);
			loadData(false, false);
		}
		
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

		tran.commit();
		sess.close();

		return areaItems;

	}
	
	public String showDetails(ShowDetailsEvent event)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		voyageBean.openVoyage(event.getVoyageId().intValue());
		voyageBean.setVoyageAttr("voyageid");
		voyageBean.setBackPage("names-interface");
		context.getApplication().getNavigationHandler().handleNavigation(context, null, "voyage-detail");
		return null;
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

	public SlavesQuery getCurrentQuery()
	{
		return currentQuery;
	}

	public List getQuerySummary()
	{
		return querySummary;
	}

	public VoyageDetailBean getVoyageBean()
	{
		return voyageBean;
	}

	public void setVoyageBean(VoyageDetailBean voyageBean)
	{
		this.voyageBean = voyageBean;
	}
	
	public String getNumberOfExpectedResultsText()
	{
		MessageFormat fmt = new MessageFormat(TastResource.getText("database_search_expected"));
		return fmt.format(new Object[] {new Integer(expectedResults)});
	}
	
}