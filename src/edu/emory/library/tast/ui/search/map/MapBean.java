package edu.emory.library.tast.ui.search.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.AttributesRange;
import edu.emory.library.tast.ui.maps.LegendItemsGroup;
import edu.emory.library.tast.ui.maps.MapData;
import edu.emory.library.tast.ui.maps.component.PointOfInterest;
import edu.emory.library.tast.ui.maps.mapfile.MapFileCreator;
import edu.emory.library.tast.ui.search.map.mapimpl.GlobalMapDataTransformer;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;


/**
 * ./configure --with-proj --with-ogr --with-gdal --with-postgis=yes --with-threads --verbose=yes
 * 
 * @author juri
 * 
 */
public class MapBean {

	public static int PORT_DEPARTURE = 1;

	public static int PORT_ARRIVAL = 2;

	public static int PORT_BOTH = 3;

	private static final String MAP_OBJECT_ATTR_NAME = "__map__file_";

	/**
	 * Reference to Search bean.
	 */
	private SearchBean searchBean = null;

	/**
	 * Conditions used in query.
	 */
	private Conditions conditions = null;

	private boolean neededQuery = false;

	private MapFileCreator creator;

	private String sessionParam;

	private List pointsOfInterest = new ArrayList();

	private MapData mapData = new MapData();

	private void setMapData() {

		if (!this.searchBean.getSearchParameters().getConditions().equals(this.conditions)) {
			this.conditions = (Conditions) this.searchBean.getSearchParameters().getConditions().clone();
			neededQuery = true;
		}

		if (this.neededQuery) {
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			this.pointsOfInterest.clear();
			Conditions localCondition = this.searchBean.getSearchParameters().getConditions().addAttributesPrefix("v.");
			localCondition.addCondition(VoyageIndex.getRecent().addAttributesPrefix("vi."));

			// We will need join condition (to join VoyageIndex and Voyage).
			localCondition.addCondition("vi.remoteVoyageId", new DirectValue("v.id"), Conditions.OP_EQUALS);

			AttributesMap map = new AttributesMap();
			List col1 = new ArrayList();
			List col2 = new ArrayList();
			ArrayList response = new ArrayList();
			
			double[] minmax = executeMapQuery(response, localCondition, new String[] {
					"v.majbuypt.name", "case when sum(v.slaximp) is null then 0 else sum(v.slaximp) end", "1" },
					new String[] { "v.majbuypt.name" }, new String[] { "case when sum(v.slaximp) is null then 0 else sum(v.slaximp) end" });
			col1.add(new AttributesRange(Voyage.getAttribute("majbuypt"), 0, response.size() - 1));
			col2.add(new AttributesRange(Voyage.getAttribute("slaximp"), 0, response.size() - 1));
			min = minmax[1];
			max = minmax[0];
			
			int beginSize = response.size();
			minmax = executeMapQuery(response, localCondition, new String[] { "v.majselpt.name",
					"case when sum(v.slamimp) is null then 0 else sum(v.slamimp) end", "2" },
					new String[] { "v.majselpt.name" }, new String[] { "case when sum(v.slamimp) is null then 0 else sum(v.slamimp) end"});
			col1.add(new AttributesRange(Voyage.getAttribute("majselpt"), beginSize, beginSize + response.size() - 1));
			col2.add(new AttributesRange(Voyage.getAttribute("slamimp"), beginSize, beginSize + response.size() - 1));
			if (min > minmax[1]) {
				min = minmax[1];
			}
			if (max < minmax[0]) {
				max = minmax[0];
			}

			map.addColumn(col1);
			map.addColumn(col2);
			
			GlobalMapDataTransformer transformer = new GlobalMapDataTransformer(map);						
			this.mapData.setMapData(response.toArray(), min, max, transformer);
			
			this.neededQuery = false;
		}
	}

	private double[] executeMapQuery(List response, Conditions localCondition,
			String[] populatedAttrs, String[] groupBy, String[] orderBy) {

		QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v", localCondition);

		for (int i = 0; i < populatedAttrs.length; i++) {
			qValue.addPopulatedAttribute(populatedAttrs[i], false);
		}

		qValue.setGroupBy(groupBy);
		qValue.setOrderBy(orderBy);
		qValue.setOrder(QueryValue.ORDER_ASC);

		Object[] voyages = qValue.executeQuery();

		response.addAll(Arrays.asList(voyages));

		return new double[] { ((Number) ((Object[]) voyages[voyages.length - 1])[1]).doubleValue(),
				((Number) ((Object[]) voyages[0])[1]).doubleValue() };

	}

	public String getMapPath() {

		try {

			this.setMapData();
			AbstractMapItem[] items = this.mapData.getItems();
			
			if (this.creator == null) {
				this.creator = new MapFileCreator();
			}

			if (items.length > 0) {
				this.creator.setMapData(items);
			}
			if (this.creator.createMapFile()) {
				sessionParam = MAP_OBJECT_ATTR_NAME + System.currentTimeMillis();
				ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
				((HttpSession) servletContext.getSession(true)).setAttribute(sessionParam, creator.getFilePath());

			} else {
				return null;
			}

			return sessionParam;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public PointOfInterest[] getPointsOfInterest() {
		
		if (this.mapData.getToolTip().length != 0) {
			System.out.println(this.mapData.getToolTip()[0].getX());
		}
		
		return this.mapData.getToolTip();
	}
	
	public LegendItemsGroup[] getLegend() {
		return this.mapData.getLegend();
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}

}
