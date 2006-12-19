package edu.emory.library.tast.admin;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.GridColumn;
import edu.emory.library.tast.ui.GridRow;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.QueryValue;

public class VoyagesListBean
{
	
	private VoyageBean voyageBean;
	private boolean dataValid = false;

	private GridRow[] rows;

	private int currentPage = 0;
	private int firstRecordIndex = 1;
	private int lastRecordIndex;
	private int pageSize = 20;
	
	private void loadDataIfNecessary()
	{
		
		if (dataValid) return;

		// open db
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		// load voyages
		QueryValue query = new QueryValue("Voyage");
		query.setOrderBy(new Attribute[] {Voyage.getAttribute("voyageid")});
		query.setFirstResult(currentPage * pageSize);
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
			String nation = voyage.getNatinimp() == null ?
					null : voyage.getNatinimp().getName();
			
			rows[i++] = new GridRow(new String[] {
					String.valueOf(voyage.getVoyageid()),
					voyage.getShipname(),
					String.valueOf(voyage.getYearam()),
					nation,
					slaveNumFmt.format(voyage.getSlaximp()),
					slaveNumFmt.format(voyage.getSlamimp())});

		}

		// determine total count
		QueryValue queryCount = new QueryValue("Voyage");
		queryCount.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("iid")}));		
		Object[] ret = queryCount.executeQuery();
		lastRecordIndex = ((Number)ret[0]).intValue();

		// done with db
		tran.commit();
		sess.close();
		
		// mark a loaded
		dataValid = true;

	}

	public GridColumn[] getColumns()
	{
		return new GridColumn[] {
			new GridColumn("Voyage ID"),	
			new GridColumn("Ship"),	
			new GridColumn("Year"),	
			new GridColumn("Nation"),	
			new GridColumn("Exported"),	
			new GridColumn("Imported"),	
		};
	}

	public GridRow[] getRows()
	{
		loadDataIfNecessary();
		return rows;
	}

	public int getFirstRecordIndex()
	{
		return firstRecordIndex;
	}

	public int getLastRecordIndex()
	{
		loadDataIfNecessary();
		return lastRecordIndex;
	}
	
	public int getCurrentPage()
	{
		loadDataIfNecessary();
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		if (currentPage != this.currentPage) dataValid = false;
		this.currentPage = currentPage;
	}

	public VoyageBean getVoyageBean()
	{
		return voyageBean;
	}

	public void setVoyageBean(VoyageBean voyageBean)
	{
		this.voyageBean = voyageBean;
	}

}