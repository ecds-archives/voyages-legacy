package edu.emory.library.tast.common.voyage;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.emory.library.tast.common.table.TableData;
import edu.emory.library.tast.database.table.DetailVoyageInfo;
import edu.emory.library.tast.database.table.DetailVoyageMap;
import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapLayer;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class VoyageDetailBean {

	private static final String MAP_SESSION_KEY = "detail_map__";
	
	private int voyageId;

	private TableData detailData = new TableData();

	private DetailVoyageMap detailVoyageMap = new DetailVoyageMap();

	private DetailVoyageInfo[] detailVoyageInfo = new DetailVoyageInfo[] {};

	private String backLink;

	private String voyageAttr;
	
	public void openVoyage(int voyageId) {
		this.voyageId = voyageId;
	}

	public int getTestValue() {
		return voyageId;
	}
	
	public void back() {
		System.out.println(this.backLink);
		FacesContext context = FacesContext.getCurrentInstance();
		context.getApplication().getNavigationHandler().handleNavigation(context, null, this.backLink);
	}

	/**
	 * Queries DB for detail voyage info.
	 * 
	 */
	private void getResultsDetailDB(String fieldName) {

		Conditions c = new Conditions();
		// c.addCondition(VoyageIndex.getApproved());
		c.addCondition(Voyage.getAttribute(fieldName), new Long(this.voyageId),
				Conditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("revision"), new Integer(Voyage.getCurrentRevision()), Conditions.OP_EQUALS);

		List validAttrs = new ArrayList();
		VisibleAttributeInterface[] attrs = VisibleAttribute.getAllAttributes();
		for (int i = 0; i < attrs.length; i++) {
			VisibleAttributeInterface column = attrs[i];
			validAttrs.add(column);
		}
		detailData.setVisibleColumns(validAttrs);

		Object[][] info = this.queryAndFillInData(c, this.detailData, -1, -1,
				true);

		this.detailVoyageInfo = new DetailVoyageInfo[info[0].length];
		for (int i = 0; i < info[0].length; i++) {
			this.detailVoyageInfo[i] = new DetailVoyageInfo(
					(VisibleAttribute) info[0][i], info[1][i]);
		}

	}

	private Object[][] queryAndFillInData(Conditions subCondition,
			TableData dataTable, int start, int length, boolean returnBasicInfo) {

		// localCond.addCondition(VoyageIndex.getAttribute("remoteVoyageId"),
		// new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);

		// Build query
		QueryValue qValue = new QueryValue("Voyage", subCondition);
		if (length != -1) {
			qValue.setLimit(length);
		}
		if (start != -1) {
			qValue.setFirstResult(start);
		}

		// Dictionaries - list of columns with dictionaries.
		dataTable.setKeyAttribute(Voyage.getAttribute("iid"));
		Attribute[] populatedAttributes = dataTable.getAttributesForQuery();
		if (populatedAttributes != null) {
			for (int i = 0; i < populatedAttributes.length; i++) {
				if (populatedAttributes[i] != null) {
					qValue.addPopulatedAttribute(populatedAttributes[i]);
				}
			}
		}

		// Add populated attributes
		Attribute[] populatedAdditionalAttributes = dataTable
				.getAdditionalAttributesForQuery();
		if (populatedAdditionalAttributes != null) {
			for (int i = 0; i < populatedAdditionalAttributes.length; i++) {
				qValue.addPopulatedAttribute(populatedAdditionalAttributes[i]);
			}
		}

		if (returnBasicInfo) {
			qValue.addPopulatedAttribute(VisibleAttribute.getAttribute(
					"voyageid").getAttributes()[0]);
		}

		VisibleAttributeInterface vattr = dataTable.getOrderByColumn();
		if (dataTable.getOrderByColumn() == null) {
			qValue
					.setOrderBy(new Attribute[] { Voyage
							.getAttribute("voyageid") });
		} else {

			Attribute[] attr = vattr.getAttributes();

			if (attr != null) {
				Attribute[] order = new Attribute[attr.length];
				for (int i = 0; i < attr.length; i++) {
					if (!(attr[i] instanceof DictionaryAttribute)) {
						order[i] = attr[i];
					} else {
						order[i] = new SequenceAttribute(new Attribute[] {
								attr[i],
								((DictionaryAttribute) attr[i])
										.getAttribute("name") });
					}
				}
				qValue.setOrderBy(order);
				qValue.setOrder(dataTable.getOrder());
			}
		}

		// Execute query
		Object[] ret = qValue.executeQuery();
		dataTable.setData(ret);

		if (returnBasicInfo && ret.length > 0) {
			int len = ((Object[]) ret[0]).length;
			return new Object[][] {
					{ VisibleAttribute.getAttribute("voyageid") },
					{ ((Object[]) ret[0])[len - 1] } };
		} else {
			return new Object[][] {};
		}
	}
	
	/**
	 * Gets detail voyage table data.
	 * 
	 * @return
	 */
	public TableData getDetailData() {
		this.getResultsDetailDB(this.voyageAttr);
		return detailData;
	}

	/**
	 * Sets detail voyage data.
	 * 
	 * @param detailData
	 */
	public void setDetailData(TableData detailData) {
		this.detailData = detailData;
	}
	
	public String getMapPath() {

		this.detailVoyageMap.setVoyageId(new Long(this.voyageId));
		this.detailVoyageMap.setAttribute(this.voyageAttr);
		
		if (this.detailVoyageMap.prepareMapFile()) {

			long time = System.currentTimeMillis();
			
			ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
			((HttpSession) servletContext.getSession(true)).setAttribute(MAP_SESSION_KEY + time, this.detailVoyageMap
					.getCurrentMapFilePath());
			return MAP_SESSION_KEY + time;
		}
		return "";
	}

	public void setMapPath(String path) {
	}

	public PointOfInterest[] getPointsOfInterest() {
		return this.detailVoyageMap.getPointsOfInterest();
	}

	public DetailVoyageInfo[] getDetailVoyageInfo() {
		getResultsDetailDB(this.voyageAttr);
		return this.detailVoyageInfo;
	}
	public LegendItemsGroup[] getLegend() {
		return this.detailVoyageMap.getLegend();
	}
	
	public MapLayer[] getLayers() {
		return this.detailVoyageMap.getLayers();
	}

	public void setBackPage(String viewId) {
		this.backLink = viewId;
	}

	public void setVoyageAttr(String voyageAttr) {
		this.voyageAttr = voyageAttr;
	}

	public String refresh() {
		this.detailVoyageMap.refresh();
		return null;
	}

}