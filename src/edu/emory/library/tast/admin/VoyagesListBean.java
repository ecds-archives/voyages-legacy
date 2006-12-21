package edu.emory.library.tast.admin;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.ui.GridColumn;
import edu.emory.library.tast.ui.GridRow;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class VoyagesListBean
{
	
	private VoyageBean voyageBean;
	private boolean dataValid = false;

	private GridRow[] rows;

	private int currentPage = 1;
	private int firstRecordIndex = 1;
	private int lastRecordIndex;
	private int pageSize = 30;

	private int yearFrom = 1500;
	private int yearTo = 1900;
	
	private String nationId;
	
	public String openVoyage()
	{
		return "edit";
	}
	
	private void loadDataIfNecessary()
	{

		if (dataValid) return;

		// open db
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		// prepare conditions
		Conditions conds = new Conditions(Conditions.JOIN_AND);
		
		// year to
		conds.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearFrom),
				Conditions.OP_GREATER_OR_EQUAL);

		// year to
		conds.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearTo),
				Conditions.OP_SMALLER_OR_EQUAL);

		// nation
		if (!StringUtils.isNullOrEmpty(nationId))
			conds.addCondition(
					new SequenceAttribute(new Attribute[] {Voyage.getAttribute("natinimp"), Nation.getAttribute("id")}),
					new Long(nationId),
					Conditions.OP_EQUALS);

		// load voyages
		QueryValue query = new QueryValue("Voyage", conds);
		query.setOrder(QueryValue.ORDER_ASC);
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
		QueryValue queryCount = new QueryValue("Voyage", conds);
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
			new GridColumn("Major place of purchase"),	
			new GridColumn("Major place of sell")	
		};
	}
	
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
		if (currentPage != this.currentPage)
		{
			dataValid = false;
			this.currentPage = currentPage;
		}
	}

	public VoyageBean getVoyageBean()
	{
		return voyageBean;
	}

	public void setVoyageBean(VoyageBean voyageBean)
	{
		this.voyageBean = voyageBean;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public String getNationId()
	{
		return nationId;
	}

	public void setNationId(String nationId)
	{
		if (!StringUtils.compareStrings(nationId, this.nationId))
		{
			dataValid = false;
			this.nationId = nationId;
			this.currentPage = 1;
		}
	}

	public int getYearFrom()
	{
		return yearFrom;
	}

	public void setYearFrom(int yearFrom)
	{
		if (yearFrom != this.yearFrom)
		{
			dataValid = false;
			this.yearFrom = yearFrom;
			this.currentPage = 1;
		}
	}

	public int getYearTo()
	{
		return yearTo;
	}

	public void setYearTo(int yearTo)
	{
		if (yearTo != this.yearTo)
		{
			dataValid = false;
			this.yearTo = yearTo;
			this.currentPage = 1;
		}
	}

}