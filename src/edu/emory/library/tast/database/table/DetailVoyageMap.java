package edu.emory.library.tast.database.table;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.database.table.mapimpl.DetailQueryHolder;
import edu.emory.library.tast.database.table.mapimpl.DetailVoyageDataTransformer;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;

/**
 * Bean for map of detail voyage view.
 *
 */
public class DetailVoyageMap {

	//IID of presented voyage
	private long iid;

	//Indication if query is needed.
	private boolean queryNeeded = false;

	//Map data.
	private MapData mapData = new MapData();

	/**
	 * Invoked when new voyage is initialized.
	 * @param iid
	 */
	public void setVoyageIid(long iid) {
		if (iid != this.iid) {
			this.iid = iid;
			this.queryNeeded = true;
		}
		this.refreshData();
	}

	/**
	 * Refreshes data - queries DB
	 * Actually, it does not do much - the whole logic of map is stored in mapimpl subpackage.
	 * @return
	 */
	public boolean refreshData() {
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();

		if (!this.queryNeeded) {
			return true;
		}

		this.queryNeeded = false;

		Conditions conditions = new Conditions();
		conditions.addCondition(Voyage.getAttribute("iid"), new Long(iid), Conditions.OP_EQUALS);
		
		DetailQueryHolder queryHolder = new DetailQueryHolder(conditions);
		queryHolder.executeQuery(session, 0, -1);
		
		if (queryHolder.getRawQueryResponse().length > 0) {

			DetailVoyageDataTransformer transformer = new DetailVoyageDataTransformer(queryHolder.getAttributesMap());
			this.mapData.setMapData(queryHolder, transformer);
		}
		
		t.commit();
		session.close();
		
		return true;
	}
	
	/**
	 * Invoked when user hit refresh.
	 *
	 */
	public void refresh() {
		this.refreshData();
	}

	/**
	 * Returns points visible on the map.
	 * @return
	 */
	public PointOfInterest[] getPointsOfInterest() {
		this.refreshData();
		return this.mapData.getToolTip();
	}

	/**
	 * Return legend.
	 * @return
	 */
	public LegendItemsGroup[] getLegend() {
		return this.mapData.getLegend();
	}
}
