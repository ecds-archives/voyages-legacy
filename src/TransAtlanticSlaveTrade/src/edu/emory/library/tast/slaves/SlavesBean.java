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
import edu.emory.library.tast.common.MessageBarComponent;
import edu.emory.library.tast.common.listing.ShowDetailsEvent;
import edu.emory.library.tast.common.listing.SortChangeEvent;
import edu.emory.library.tast.common.listing.TableData;
import edu.emory.library.tast.common.listing.links.TableLinkManager;
import edu.emory.library.tast.common.voyage.VoyageDetailBean;
import edu.emory.library.tast.database.listing.formatters.AbstractAttributeFormatter;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Country;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Slave;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.CSVUtils;

/**
 * This bean coordinates everything in the names database. It is a session scope
 * bean. It connects the components representing the query on the left hand side
 * with the table on the right hand side of the page. It holds the current not
 * yet confirmed query (i.e. before the uses presses the Search button) in the
 * <p>
 * {@link #workingQuery} field. When the uses submits the query in the left
 * column on the page, the {@link #search(String)} method is called. If this is
 * not an AJAX submittion (see more on the below), this method compares
 * {@link #workingQuery} with {@link #currentQuery}, and if they differ it
 * replaces {@link #currentQuery}, and executes search.
 * <p>
 * The search itself is done by {@link #loadData(boolean, boolean)}. The first
 * parameter determines if the current total results count should be computed,
 * and the second parameter determines if the current query summary (which is in
 * the bottom in the left column of the page) should be also refreshed. The
 * current total number of results is held in {@link #pager}, and the current
 * summary is held in {@link #querySummary}.
 * {@link #loadData(boolean, boolean)} is called when the search is presses,
 * when the current page in the listing is changed, and when the number of
 * results displayed on the page is changed. In the second two cases, the total
 * number of results is not recomputed, as well as the summary of the query.
 * <p>
 * Current data of results visible on the page are held in {@link #tableData}.
 * It is refreshed by {@link #loadData(boolean, boolean)} as indicated above.
 * Current page a the number of results is held in {@link #pager}.
 * <p>
 * The values of the components in the left seach column are linked directly to
 * {@link #workingQuery} by exposing {@link #workingQuery} via
 * {@link #getWorkingQuery()}. The only two compoments which need some
 * additional data are the selection box for countries and the selection box
 * embarkation ports. The data for these two compoments are loaded everytime the
 * page is refreshed using {@link #getCountries()} and {@link #getExpPorts()}
 * respectively. The second compoment also needs to remeber which regions of the
 * ports are expanded. This is stored in {@link #expandedEmbPorts}.
 * </p>
 */

public class SlavesBean {
	
	private static final int AFRICA_ID = 60000;

	private static final String ATTRIBUTE = "Attribute_";

	private MessageBarComponent messageBar;
	
	private TableLinkManager pager;

	private TableData tableData;

	private SlavesQuery workingQuery = new SlavesQuery();

	private SlavesQuery currentQuery = new SlavesQuery();

	private List querySummary;

	private String[] expandedEmbPorts;

	private int expectedResults;

	private int firstVisibleRecord = -1;

	/**
	 * Reference to voyage bean
	 */
	private VoyageDetailBean voyageBean;

	public SlavesBean() {

		pager = new TableLinkManager(20);

		VisibleAttributeInterface[] visibleAttrs = {
				VisibleAttrSlave.getAttributeForTable("id"),
				VisibleAttrSlave.getAttributeForTable("name"),
				VisibleAttrSlave.getAttributeForTable("age"),
				VisibleAttrSlave.getAttributeForTable("height"),
				VisibleAttrSlave.getAttributeForTable("sexage"),
				VisibleAttrSlave.getAttributeForTable("country"),
				VisibleAttrSlave.getAttributeForTable("voyageId"),
				VisibleAttrSlave.getAttributeForTable("shipname"),
				VisibleAttrSlave.getAttributeForTable("datearr"),
				VisibleAttrSlave.getAttributeForTable("majbuypt"), 
				VisibleAttrSlave.getAttributeForTable("majselpt"),
				VisibleAttrSlave.getAttributeForTable("source") };

		tableData = new TableData();
		tableData.setKeyAttribute(Slave.getAttribute("voyageId"));
		tableData.setVisibleColumns(visibleAttrs);
		tableData.setOrderByColumn(visibleAttrs[0]);
		tableData.setFormatter(VisibleAttrSlave.getAttributeForTable("majselpt"), new AbstractAttributeFormatter() {

			public String format(VisibleAttributeInterface attr, Object object) {
				return Slave.getDisembarkationCode((Port) object);
			}

			public String[] format(VisibleAttributeInterface attr, Object[] object) {
				return null;
			}

		});

		expandedEmbPorts = new String[] { String.valueOf(AFRICA_ID) };

		loadData(true, true);

	}

	private VisibleAttributeInterface getVisibleAttribute(String attributeSort) {
		VisibleAttributeInterface ret = null;
		if (attributeSort.startsWith(ATTRIBUTE)) {
			String attrId = attributeSort.substring(ATTRIBUTE.length(), attributeSort.length());
			ret = VisibleAttrSlave.getAttributeForTable(attrId);
		}
		return ret;
	}

	/**
	 * Loads data to table.
	 * @param refreshCount - indicates if number of results should be recalculated
	 * @param refreshText
	 */
	private void loadData(boolean refreshCount, boolean refreshText) {

		this.firstVisibleRecord = this.pager.getCurrentFirstRecord();

		Session sess = HibernateConn.getSession();
		Transaction tran = sess.beginTransaction();
		
		if (refreshText) querySummary = new ArrayList();
		TastDbConditions conditions = currentQuery.createConditions(sess, refreshText ? querySummary : null);

		TastDbQuery queryTable = getQuery(conditions, this.pager.getCurrentFirstRecord(), this.pager.getStep());
	
		this.tableData.setData(queryTable.executeQuery(sess));

		this.tableData.setLinkStyle(TableData.LINK_ON_COLUMN);
		this.tableData.setLinkColumnIndex(6);

		if (refreshCount)
		{
			TastDbQuery queryValueCount = new TastDbQuery(new String[] { "Slave" }, new String[] { "e" }, conditions);
			queryValueCount.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] { Estimate.getAttribute("id") }));
			Object[] ret = queryValueCount.executeQuery();
			this.pager.setResultsNumber(((Number) ret[0]).intValue());
			expectedResults = ((Number) ret[0]).intValue();
		}

		tran.commit();
		sess.close();

	}

	
	/**
	 * Prepares query for slaves.
	 * @param conditions
	 * @param first
	 * @param limit
	 * @return
	 */
	private TastDbQuery getQuery(TastDbConditions conditions, int first, int limit) {
		TastDbQuery queryValueTable = new TastDbQuery(new String[] { "Slave" }, new String[] { "s" }, conditions);

		Attribute[] attrs = this.tableData.getAttributesForQuery();
		for (int i = 0; i < attrs.length; i++)
			queryValueTable.addPopulatedAttribute(attrs[i]);

		queryValueTable.setOrder(this.tableData.getOrder());
		queryValueTable.setOrderBy(this.tableData.getOrderByColumn().getAttributes());

		queryValueTable.setFirstResult(first);
		queryValueTable.setLimit(limit);
		return queryValueTable;
	}

	/**
	 * Updates count of expected results.
	 *
	 */
	private void updateExpectedCount() {

		Session sess = HibernateConn.getSession();
		Transaction tran = sess.beginTransaction();

		TastDbConditions conditions = workingQuery.createConditions(sess, null);

		TastDbQuery queryValueCount = new TastDbQuery(new String[] { "Slave" }, new String[] { "e" }, conditions);
		queryValueCount.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] { Estimate.getAttribute("id") }));
		Object[] ret = queryValueCount.executeQuery();
		expectedResults = ((Number) ret[0]).intValue();

		tran.commit();
		sess.close();

	}

	/**
	 * Bound to the search button in the left column.
	 * 
	 * @return
	 */
	public String search() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (AAUtils.isAjaxRequest(request)) {

			// indicate what should be rendered
			AAUtils.addZonesToRefresh(request, "totalBoxBasic");
			AAUtils.addZonesToRefresh(request, "totalBoxCountry");
			AAUtils.addZonesToRefresh(request, "totalBoxPlaces");

			// and update count
			updateExpectedCount();
		} else {

			// nothing has changed
			if (currentQuery.equals(workingQuery))
				return null;

			// reset pager
			this.pager.reset();

			// copy working query
			try {
				currentQuery = (SlavesQuery) workingQuery.clone();
			} catch (CloneNotSupportedException cnse) {
			}

			// refresh data, count and the current query text
			loadData(true, true);

		}

		return null;

	}

	/**
	 * Bound to the reset button in the left column.
	 * 
	 * @return
	 */
	public String reset() {

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

	/**
	 * Bound to the event of the table indicating the change of sorting.
	 * 
	 * @return
	 */
	public void sortChanged(SortChangeEvent event) {

		// get column that will be sorted
		VisibleAttributeInterface attr = this.getVisibleAttribute(event.getAttributeSort());

		// set appropriate order
		if (this.tableData.getOrderByColumn().getName().equals(attr.getName())) {
			switch (this.tableData.getOrder()) {
			case TastDbQuery.ORDER_ASC:
				this.tableData.setOrder(TastDbQuery.ORDER_DESC);
				break;
			case TastDbQuery.ORDER_DESC:
				this.tableData.setOrder(TastDbQuery.ORDER_DEFAULT);
				break;
			case TastDbQuery.ORDER_DEFAULT:
				this.tableData.setOrder(TastDbQuery.ORDER_ASC);
				break;
			}
		} else {
			this.tableData.setOrderByColumn(attr);
			this.tableData.setOrder(TastDbQuery.ORDER_ASC);
		}

		// reset pager
		this.pager.reset();

		// refresh data, but not count of query text
		loadData(false, false);

	}

	public void setStep(int newStep){
		if (newStep != this.pager.getStep() || this.firstVisibleRecord != this.pager.getCurrentFirstRecord()){
			this.pager.setStep(newStep);
			loadData(false, false);
		}
	}

	/**
	 * Bound to list of counties. Called every time the page is reloaded.
	 * 
	 * @return
	 */
	public LookupCheckboxItem[] getCountries() {

		String hsql = "from Country c " + "where c in (select s.country from Slave s group by s.country) " + "order by c.name";

		Session sess = HibernateConn.getSession();
		Transaction tran = sess.beginTransaction();

		Query query = sess.createQuery(hsql);
		List countries = query.list();

		LookupCheckboxItem[] countryUi = new LookupCheckboxItem[countries.size()];

		int i = 0;
		for (Iterator iter = countries.iterator(); iter.hasNext();) {
			Country country = (Country) iter.next();
			countryUi[i++] = new LookupCheckboxItem(String.valueOf(country.getId()), country.getName());
		}

		tran.commit();
		sess.close();

		return countryUi;

	}

	/**
	 * Bound to list of embarkation ports. Called every time the page is
	 * reloaded.
	 * 
	 * @return
	 */
	public LookupCheckboxItem[] getExpPorts() {

		String hsql =
			"from Port p " +
			"where p in (select s.majbuypt from Slave s group by s.majbuypt) " +
			"order by p.region.area.order, p.region.order, p.order";

		Session sess = HibernateConn.getSession();
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
		while (i < portsCount) {
			Area groupArea = area;

			LookupCheckboxItem areaItem = new LookupCheckboxItem();
			areaItem.setId(String.valueOf(area.getId()));
			areaItem.setText(area.getName());

			tmpAreas.add(areaItem);
			tmpRegions.clear();

			while (i < portsCount && groupArea.equals(area)) {
				Region groupRegion = region;

				LookupCheckboxItem regionItem = new LookupCheckboxItem();
				regionItem.setId(String.valueOf(region.getId()));
				regionItem.setText(region.getName());

				tmpRegions.add(regionItem);
				tmpPorts.clear();

				while (i < portsCount && groupRegion.equals(region)) {

					tmpPorts.add(new LookupCheckboxItem(String.valueOf(port.getId()), port.getName()));

					if (++i < portsCount) {
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

	/**
	 * Action invoked when row in table is clocked.
	 * @param event
	 * @return
	 */
	public String showDetails(ShowDetailsEvent event)
	{
		
		Session sess = HibernateConn.getSession();
		Transaction transaction = sess.beginTransaction();
		
		Voyage voyage = Voyage.loadByVoyageId(sess, event.getVoyageId().intValue());
		
		long iid = -1;
		if (voyage != null) iid = voyage.getIid().longValue();

		transaction.commit();
		sess.close();
		
		if (iid != -1)
		{

			voyageBean.openVoyageByIid(iid);

			FacesContext context = FacesContext.getCurrentInstance();
			context.getApplication().getNavigationHandler().handleNavigation(context, null, "voyage-detail");
			
		}
		
		return null;
	}

	/**
	 * Returns data which is placed in table.
	 * @return
	 */
	public TableData getTableData() {
		return this.tableData;
	}

	/**
	 * Gets link manager for table (the component which provides switching between results)
	 * @return
	 */
	public TableLinkManager getTableManager() {
		return this.pager;
	}

	/**
	 * Gets current working qurery (query which has been sent to DB)
	 * @return
	 */
	public SlavesQuery getWorkingQuery() {
		return workingQuery;
	}

	/**
	 * start record of currently visible data set
	 * @return
	 */
	public int getFirstDisplayed() {
		return this.pager.getCurrentFirstRecord() + 1;
	}

	/**
	 * last record currently visible.
	 * @return
	 */
	public int getLastDisplayed() {
		if (this.pager.getResultsNumber() == 0)
			return 0;
		else
			return this.pager.getCurrentFirstRecord() + 1 + (this.tableData.getData() != null ? this.tableData.getData().length - 1 : 0);
	}

	/**
	 * Gets step size (10, 20, 50 records...)
	 * @return
	 */
//	public String getStep() {
//		return String.valueOf(this.pager.getStep());
//	}
	public int getStep(){
		return this.pager.getStep();
	}

	/**
	 * Total number of records returned by query
	 * @return
	 */
	public int getTotalRows() {
		return this.pager.getResultsNumber();
	}

	/**
	 * Gets embarkation export ports.
	 * @return
	 */
	public String[] getExpandedEmbPorts() {
		return expandedEmbPorts;
	}

	/**
	 * Sets expanded export ports.
	 * @param expandedExpPorts
	 */
	public void setExpandedEmbPorts(String[] expandedExpPorts) {
		this.expandedEmbPorts = expandedExpPorts;
	}

	/**
	 * Gets query which is being built (this is query before search is pressed)
	 * @return
	 */
	public SlavesQuery getCurrentQuery() {
		return currentQuery;
	}

	/**
	 * Gets query summary
	 * @return
	 */
	public List getQuerySummary() {
		return querySummary;
	}

	public VoyageDetailBean getVoyageBean() {
		return voyageBean;
	}

	public void setVoyageBean(VoyageDetailBean voyageBean) {
		this.voyageBean = voyageBean;
	}

	/**
	 * Gets text for number of expected results.
	 * @return
	 */
	public String getNumberOfExpectedResultsText() {
		MessageFormat fmt = new MessageFormat(TastResource.getText("database_search_expected"));
		return fmt.format(new Object[] { new Integer(expectedResults) });
	}

	/**
	 * Saves currently visible results into table.
	 * @return
	 */
	public String getFileCurrentData() {
		Session sess = HibernateConn.getSession();
		Transaction t = sess.beginTransaction();
		TastDbConditions conditions = currentQuery.createConditions(sess, null);
		TastDbQuery q = this.getQuery(conditions, this.pager.getCurrentFirstRecord(), this.pager.getStep());
		CSVUtils.writeResponse(sess, q, false, false, "");
		t.commit();
		sess.close();
		return null;
	}

	/**
	 * Saves all the results returned by current query
	 * @return
	 */
	public String getFileAllData() {
		Session sess = HibernateConn.getSession();
		Transaction t = sess.beginTransaction();
		TastDbConditions conditions = currentQuery.createConditions(sess, null);
		TastDbQuery q = this.getQuery(conditions, 0, -1);
		CSVUtils.writeResponse(sess, q, false, false, "");
		t.commit();
		sess.close();
		return null;
	}
	/**
	 * Message bar to display link.
	 * @return
	 */
	public MessageBarComponent getMessageBar() {
		return messageBar;
	}

	public void setMessageBar(MessageBarComponent messageBar) {
		this.messageBar = messageBar;
	}

}