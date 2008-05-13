package edu.emory.library.tast.admin;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.GridColumn;
import edu.emory.library.tast.common.GridRow;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Revision;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.TastDbConditions;
import edu.emory.library.tast.util.query.TastDbQuery;

public class VoyagesListBean
{
	
	private VoyageBean voyageBean;
	private boolean filterChanged = true;
	private boolean currentPageChanged = true;
	private boolean dataValid = false;

	private GridRow[] rows;

	private int currentPage = 1;
	private int firstRecordIndex = 1;
	private int lastRecordIndex;
	private int pageSize = 30;

	private int yearFrom = -1;
	private int yearTo = -1;
	
	private int voyageIdFrom = -1;
	private int voyageIdTo = -1;
	
	private int revision = -1;
	
	private String nationId;
	
	public VoyagesListBean()
	{
		restoreDefaultOptions();
	}
	
	public String restoreDefaultOptions()
	{
		
		nationId = "";
		this.yearFrom = -1;
		this.yearTo = -1;
		this.voyageIdFrom = -1;
		this.voyageIdTo = -1;
		
		currentPage = 1;
		
		return null;
		
	}
	
	public String openVoyage()
	{
		return "edit";
	}
	
	private void loadDataIfNecessary()
	{
		// no need
		if (dataValid && !filterChanged && !currentPageChanged)
			return;
		
		// reset page if some filter changed
		if (filterChanged)
			currentPage = 1;
		
		// clean states
		filterChanged = false;
		currentPageChanged = false;
		dataValid = true;

		// open db
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		// prepare conditions
		TastDbConditions conds = new TastDbConditions(TastDbConditions.AND);

		// year to
		if (yearFrom != -1) {
			conds.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearFrom),
				TastDbConditions.OP_GREATER_OR_EQUAL);
		}
		
		// year to
		if (yearTo != -1) {
			conds.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearTo),
				TastDbConditions.OP_SMALLER_OR_EQUAL);
		}
		
		//voyage id From
		if (voyageIdFrom != -1) {
			conds.addCondition(
				Voyage.getAttribute("voyageid"),
				new Integer(voyageIdFrom),
				TastDbConditions.OP_GREATER_OR_EQUAL);
		}
		
		// voyage id to
		if (voyageIdTo != -1) {
			conds.addCondition(
				Voyage.getAttribute("voyageid"),
				new Integer(voyageIdTo),
				TastDbConditions.OP_SMALLER_OR_EQUAL);
		}
		
		// nation
		if (!StringUtils.isNullOrEmpty(nationId))
			conds.addCondition(
					new SequenceAttribute(new Attribute[] {Voyage.getAttribute("natinimp"), Nation.getAttribute("id")}),
					new Long(nationId),
					TastDbConditions.OP_EQUALS);

		// load voyages
		conds.addCondition(Voyage.getAttribute("revision"), new Integer(this.revision), TastDbConditions.OP_EQUALS);
		conds.addCondition(Voyage.getAttribute("suggestion"), new Boolean(false), TastDbConditions.OP_EQUALS);
		TastDbQuery query = new TastDbQuery("Voyage", conds);
		query.setOrder(TastDbQuery.ORDER_ASC);
		query.setOrderBy(new Attribute[] {Voyage.getAttribute("voyageid")});
		query.setFirstResult((currentPage-1) * pageSize);
		query.setLimit(pageSize);
		List voyages = query.executeQueryList(sess);
		
		// for numbers of slaves
		MessageFormat slaveNumFmt = new MessageFormat("{0,number,#,###,###}");

		// more voyages to UI
		int i = 0;
		rows = new GridRow[voyages.size()];
		for (Iterator iter = voyages.iterator(); iter.hasNext();)
		{
			
			Voyage voyage = (Voyage) iter.next();
			
			String rowId = voyage.getIid().toString();
			
			String nation = voyage.getNatinimp() == null ? null :
				voyage.getNatinimp().getName();
			
			String slaximp = voyage.getSlaximp() == null ? null :
				slaveNumFmt.format(new Object[] {voyage.getSlaximp()});
			
			String slamimp = voyage.getSlamimp() == null ? null :
				slaveNumFmt.format(new Object[] {voyage.getSlamimp()});
			
			String yearam = voyage.getYearam() == null ? null :
				String.valueOf(voyage.getYearam());

			String mjPlacePurchase = voyage.getMjbyptimp() == null ? null :
				voyage.getMjbyptimp().getRegion().getName() + " / " + voyage.getMjbyptimp().getName();

			String mjPlaceSell = voyage.getMjslptimp() == null ? null :
				voyage.getMjslptimp().getRegion().getName() + " / " + voyage.getMjslptimp().getName();

			rows[i++] = new GridRow(rowId, new String[] {
					String.valueOf(voyage.getVoyageid()),
					voyage.getShipname(), yearam, nation, slaximp, slamimp, 
					mjPlacePurchase, mjPlaceSell});

		}

		// determine total count
		TastDbQuery queryCount = new TastDbQuery("Voyage", conds);
		queryCount.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("iid")}));		
		Object[] ret = queryCount.executeQuery();
		lastRecordIndex = ((Number)ret[0]).intValue();

		// done with db
		tran.commit();
		sess.close();
		
	}

	/**
	 * Columns visible in the list of all voyages
	 * @return
	 */
	public GridColumn[] getColumns()
	{
		return new GridColumn[] {
			new GridColumn("Voyage ID"),	
			new GridColumn("Ship"),	
			new GridColumn("Year"),	
			new GridColumn("Nation"),	
			new GridColumn("Exported"),	
			new GridColumn("Imported"),	
			new GridColumn("Major place of purchase"),	
			new GridColumn("Major place of sell")	
		};
	}
	
	/**
	 * Available nations.
	 * @return
	 */
	public SelectItem[] getNations()
	{
		
		List nationsDb = Nation.loadAll(null);
		SelectItem[] nationsUi = new SelectItem[nationsDb.size() + 1];
		
		nationsUi[0] = new SelectItem("", "[all nations]"); 

		int i = 1;
		for (Iterator iter = nationsDb.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			nationsUi[i++] = new SelectItem(nation.getId().toString(), nation.getName()); 
		}
		
		return nationsUi;
		
	}
	
	public void invalidateList()
	{
		dataValid = false;
	}
	
	/**
	 * Gets rows containing voyages.
	 * @return
	 */
	public GridRow[] getRows()
	{
		loadDataIfNecessary();
		return rows;
	}

	/**
	 * Gets first visible voyage
	 * @return
	 */
	public int getFirstRecordIndex()
	{
		return firstRecordIndex;
	}

	/**
	 * Gets last visible voyage.
	 * @return
	 */
	public int getLastRecordIndex()
	{
		loadDataIfNecessary();
		return lastRecordIndex;
	}
	
	/**
	 * Gets index of current page.
	 * @return
	 */
	public int getCurrentPage()
	{
		loadDataIfNecessary();
		return currentPage;
	}

	/**
	 * Sets current visible page.
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage)
	{
		if (currentPage != this.currentPage)
		{
			currentPageChanged = true;
			this.currentPage = currentPage;
		}
	}

	/**
	 * Gets reference to voyage bean
	 * @return
	 */
	public VoyageBean getVoyageBean()
	{
		return voyageBean;
	}

	/**
	 * Sets reference to vouyage bean
	 * @param voyageBean
	 */
	public void setVoyageBean(VoyageBean voyageBean)
	{
		this.voyageBean = voyageBean;
	}

	/**
	 * Gets number of voyages per page.
	 * @return
	 */
	public int getPageSize()
	{
		return pageSize;
	}

	/**
	 * Gets chosen nation
	 * @return
	 */
	public String getNationId()
	{
		return nationId;
	}

	/**
	 * Sets chosen nation
	 * @param nationId
	 */
	public void setNationId(String nationId)
	{
		if (!StringUtils.compareStrings(nationId, this.nationId))
		{
			filterChanged = true;
			this.nationId = nationId;
		}
	}

	/**
	 * Gets chosen year from.
	 * @return
	 */
	public String getYearFrom()
	{
		if (yearFrom == -1) {
			return "";
		}
		return String.valueOf(yearFrom);
	}

	/**
	 * Sets chosen year from
	 */
	public void setYearFrom(String yearFrom)
	{
		if (StringUtils.isNullOrEmpty(yearFrom)){
			this.yearFrom = -1;
			filterChanged = true;
		} else {
			int yearFromI = Integer.parseInt(yearFrom);
			if (yearFromI != this.yearFrom)
			{
				filterChanged = true;
				this.yearFrom = yearFromI;
			}
		}
	}

	/**
	 * Gets year to
	 * @return
	 */
	public String getYearTo()
	{
		if (yearTo == -1) {
			return "";
		}
		return String.valueOf(yearTo);
	}

	/**
	 * Sets year to.
	 * @param yearTo
	 */
	public void setYearTo(String yearTo)
	{
		if (StringUtils.isNullOrEmpty(yearTo)) {
			this.yearTo = -1;
			this.filterChanged = true;
		} else {
			int yearToI = Integer.parseInt(yearTo);
			if (yearToI != this.yearTo)
			{
				filterChanged = true;
				this.yearTo = yearToI;
				this.currentPage = 1;
			}
		}
	}

	/**
	 * Gets voyage id from
	 * @return
	 */
	public String getVoyageIdFrom() {
		if (voyageIdFrom == -1) {
			return "";
		}
		return String.valueOf(voyageIdFrom);
	}

	/**
	 * Sets voyage id from
	 * @param voyageIdFrom
	 */
	public void setVoyageIdFrom(String voyageIdFrom) {
		if (StringUtils.isNullOrEmpty(voyageIdFrom)) {
			this.voyageIdFrom = -1;
			return;
		}
		this.voyageIdFrom = Integer.parseInt(voyageIdFrom);
	}

	public String getVoyageIdTo() {
		if (voyageIdTo == -1) {
			return "";
		}
		return String.valueOf(voyageIdTo);
	}

	/**
	 * Sets voyage id to
	 * @param voyageIdTo
	 */
	public void setVoyageIdTo(String voyageIdTo) {
		if (StringUtils.isNullOrEmpty(voyageIdTo)) {
			this.voyageIdTo = -1;
			return;
		}
		this.voyageIdTo = Integer.parseInt(voyageIdTo);
	}
	
	/**
	 * Gets available revisions.
	 * @return
	 */
	public SelectItem[] getRevisions() {
		TastDbQuery qValue = new TastDbQuery("Revision");
		qValue.setOrderBy(new Attribute[] {Revision.getAttribute("id")});
		Object[] revisions = qValue.executeQuery();
		SelectItem[] revs = new SelectItem[revisions.length + 1];
		revs[0] = new SelectItem("-1", "*Future revision*");
		for (int i = 0; i < revisions.length; i++) {
			revs[i + 1] = new SelectItem(((Revision)revisions[i]).getId().toString(), ((Revision)revisions[i]).getName());
		}
		return revs;
	}

	/**
	 * Gets chosen revision
	 * @return
	 */
	public String getRevision() {
		return String.valueOf(revision);
	}

	/**
	 * Sets chosen revision
	 * @param revision
	 */
	public void setRevision(String revision) {
		this.revision = Integer.parseInt(revision);
	}
	
}