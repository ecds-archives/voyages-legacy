package edu.emory.library.tast.estimates.selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.common.MessageBarComponent;
import edu.emory.library.tast.common.SelectItem;
import edu.emory.library.tast.common.SelectItemWithImage;
import edu.emory.library.tast.database.query.HistoryItem;
import edu.emory.library.tast.database.query.Query;
import edu.emory.library.tast.dm.Configuration;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesExportRegion;
import edu.emory.library.tast.dm.EstimatesImportArea;
import edu.emory.library.tast.dm.EstimatesImportRegion;
import edu.emory.library.tast.dm.EstimatesNation;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.XMLExportable;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.XMLUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * This bean is responsible for managing the query on the left hand side in the
 * estimates. It does not do the actuall searching, it is only connected to the
 * other beans (i.e.
 * {@link edu.emory.library.tast.estimates.table.EstimatesTableBean},
 * {@link edu.emory.library.tast.estimates.map.EstimatesMapBean} and
 * {@link edu.emory.library.tast.estimates.listing.EstimateListingBean} and
 * {@link edu.emory.library.tast.estimates.timeline.EstimatesTimelineBean}) via
 * a configuration in faces-config.xml.
 * <p>
 * The bean stores the currently checked nations and regions in
 * {@link #checkedNations}, {@link #checkedExpRegions},
 * {@link #checkedImpRegions}. The bean uses it to compose the query. Whenever
 * the query is composed the bean also stores the currently selected nations and
 * ports to {@link #selectedExpRegionIds}, {@link #selectedExpRegionIds},
 * {@link #selectedImpRegionIds} and {@link selectedImpAreaIds}. The reason for
 * this is that we need the names of the selected regions and ports for the
 * table. Also, during the query composition, the text fields
 * {@link #selectedNationsAsText}, {@link #selectedExpRegionsAsText} and
 * {@link #selectedImpRegionsAsText} are recalculated. They hold the textual
 * description of the query which is displayed in the left bottom part of the
 * page.
 * <p>
 * The prepared databases query are stored in two fields:
 * {@link #timeFrameConditions} and {@link #geographicConditions}, and they are
 * recomputed whenever the user presess the Change Selection button. The reason
 * for this splitting is that the timeline tab should not be restricted by the
 * choise of the time frame. So it gets only the geographical part of the query.
 * <p>
 * The ids of import regions in {@link #checkedImpRegions} are stored in the
 * following format. If the item is an area which has more than one region, its
 * id is just A followed by the id of the area, i.e. A1, A2, ... If the item is
 * an area which has exactly one region, its id is A followed by the id of the
 * area, followed by _ and then R followed by the id of the region, i.e. A5_R9,
 * A6_R8 ... Finally, if the item is a region which (is in and area with more
 * than one region), its id is just R followed by the id of the region, i.e. R4,
 * R6, ... The reason for this encoding is that, in the interface, we need don't
 * want to have areas which would expand only to one region. So areas with only
 * one regions appear as one non-expadable item, and we need to decode from it
 * (without going to the database) which region does it represent.
 */

public class EstimatesSelectionBean {

	private static final int TIME_SPAN_INITIAL_FROM = 1500;

	private static final int TIME_SPAN_INITIAL_TO = 1900;

	public static class EstimatesSelection implements XMLExportable {

		private int yearFrom;

		private int yearTo;

		private Set selectedNationIds;

		private Set selectedExpRegionIds;

		private Set selectedImpRegionIds;

		private Set selectedImpAreaIds;
		
		private String selectedTab;

		public EstimatesSelection() {
		}

		public EstimatesSelection(EstimatesSelectionBean bean) {
			this.yearFrom = bean.yearFrom;
			this.yearTo = bean.yearTo;
			this.selectedNationIds = bean.selectedNationIds;
			this.selectedExpRegionIds = bean.selectedExpRegionIds;
			this.selectedImpRegionIds = bean.selectedImpRegionIds;
			this.selectedImpAreaIds = bean.selectedImpAreaIds;
			this.selectedTab = bean.selectedTab;
		}

		public void restoreFromXML(Node entry) {

			Node estimatesSelection = XMLUtils.getChildNode(entry, "estimatesSelection");
			if (estimatesSelection != null) {
				this.yearFrom = Integer.parseInt(XMLUtils.getXMLProperty(estimatesSelection, "yearFrom"));
				this.yearTo = Integer.parseInt(XMLUtils.getXMLProperty(estimatesSelection, "yearTo"));
				this.selectedTab = XMLUtils.getXMLProperty(estimatesSelection, "selectedTab");
				NodeList list = estimatesSelection.getChildNodes();
				for (int i = 0; i < list.getLength(); i++) {
					Node child = list.item(i);
					if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("set")) {
						String setName = XMLUtils.getXMLProperty(child, "name");
						Set set = XMLUtils.restoreSetOfLongs(child);
						if (setName.equals("nations")) {
							this.selectedNationIds = set;
						} else if (setName.equals("exported")) {
							this.selectedExpRegionIds = set;
						} else if (setName.equals("impAreas")) {
							this.selectedImpAreaIds = set;
						} else {
							this.selectedImpRegionIds = set;
						}
					}
				}
			}

		}

		public String toXML() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<estimatesSelection ");
			XMLUtils.appendAttribute(buffer, "yearFrom", String.valueOf(this.yearFrom));
			XMLUtils.appendAttribute(buffer, "yearTo", String.valueOf(this.yearTo));
			XMLUtils.appendAttribute(buffer, "selectedTab", this.selectedTab);
			buffer.append(">/n");
			buffer.append(XMLUtils.encodeSet("nations", this.selectedNationIds));
			buffer.append(XMLUtils.encodeSet("exported", this.selectedExpRegionIds));
			buffer.append(XMLUtils.encodeSet("impAreas", this.selectedImpAreaIds));
			buffer.append(XMLUtils.encodeSet("impRegions", this.selectedImpRegionIds));
			buffer.append("</estimatesSelection>\n");
			return buffer.toString();
		}
	}

	private MessageBarComponent messageBar;

	private Conditions timeFrameConditions;

	private Conditions geographicConditions;

	private String[] checkedNations;

	private String[] checkedExpRegions;

	private String[] checkedImpRegions;

	private String[] expandedExpRegions;

	private String[] expandedImpRegions;

	private int totalNationsCount;

	private int totalExpRegionsCount;

	private int totalImpRegionsCount;

	private Set selectedNationIds;

	private Set selectedExpRegionIds;

	private Set selectedImpRegionIds;

	private Set selectedImpAreaIds;

	private String selectedNationsAsText;

	private String selectedExpRegionsAsText;

	private String selectedImpRegionsAsText;

	private int yearFrom = TIME_SPAN_INITIAL_FROM;

	private int yearTo = TIME_SPAN_INITIAL_TO;

	private String selectedTab = "table";
	
	public EstimatesSelectionBean() {
		initDefaultValues();
	}

	/**
	 * Used only during inicialization and when user presses the Reset button.
	 * Load the default values, i.e. all nations and regions are selected and
	 * the time frame is set to the min and max year determined from the
	 * database. It uses {@link #checkAllNationsAndRegions} and
	 * {@link #initDefaultTimeFrame(Session)} to do it.
	 * 
	 * @param sess
	 */
	private void initDefaultValues() {

		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		checkAllNationsAndRegions(sess);
		initDefaultTimeFrame(sess);

		transaction.commit();
		sess.close();

		changeSelection();

	}

	/**
	 * Used only during inicialization and when user presses the Reset button.
	 * 
	 * @param sess
	 */
	private void initDefaultTimeFrame(Session sess) {

		QueryValue query = new QueryValue("edu.emory.library.tast.dm.Estimate");

		query.addPopulatedAttribute(new FunctionAttribute("min", new Attribute[] { Estimate.getAttribute("year") }));

		query.addPopulatedAttribute(new FunctionAttribute("max", new Attribute[] { Estimate.getAttribute("year") }));

		Object[] result = query.executeQuery();
		Object[] firsrRow = (Object[]) result[0];

		yearFrom = ((Integer) firsrRow[0]).intValue();
		yearTo = ((Integer) firsrRow[1]).intValue();

	}

	/**
	 * General method, which changes {@link #checkedNations} by the given list
	 * of nations from the database. Now used only during inicialization and
	 * when user presses the Reset button.
	 * 
	 * @param nations
	 *            List of nations.
	 */
	private void checkNations(List nations) {

		totalNationsCount = nations.size();
		checkedNations = new String[nations.size()];

		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();) {
			EstimatesNation nation = (EstimatesNation) iter.next();
			checkedNations[i++] = String.valueOf(nation.getId());
		}

	}

	/**
	 * General method, which changes {@link #checkedExpRegions} by the given
	 * list of regions from the database. Now used only during inicialization
	 * and when user presses the Reset button.
	 * 
	 * @param nations
	 *            List of nations.
	 */
	private void checkExpRegions(List regions) {

		checkedExpRegions = new String[regions.size()];

		int i = 0;
		for (Iterator iter = regions.iterator(); iter.hasNext();) {
			EstimatesExportRegion region = (EstimatesExportRegion) iter.next();
			checkedExpRegions[i] = String.valueOf(region.getId());
			i++;
		}

	}

	/**
	 * General method, which changes {@link #checkedImpRegions} by the given
	 * list of regions from the database. Now used only during inicialization
	 * and when user presses the Reset button.
	 * 
	 * @param nations
	 *            List of nations.
	 */
	private void checkImpRegions(List regions) {

		int i = 0;
		int regionsCount = regions.size();

		List tempIds = new ArrayList();

		while (i < regionsCount) {

			EstimatesImportRegion region = (EstimatesImportRegion) regions.get(i);
			EstimatesImportArea area = region.getArea();
			Long lastRegionId = region.getId();

			int regionsInArea = 0;
			while (area.equals(region.getArea())) {
				lastRegionId = region.getId();
				tempIds.add("R" + lastRegionId);
				regionsInArea++;
				if (++i == regionsCount)
					break;
				region = (EstimatesImportRegion) regions.get(i);
			}

			if (regionsInArea > 1) {
				tempIds.add("A" + area.getId());
			} else {
				tempIds.add("A" + area.getId() + "_" + "R" + lastRegionId);
			}

		}

		checkedImpRegions = new String[tempIds.size()];
		tempIds.toArray(checkedImpRegions);

	}

	/**
	 * Used only during inicialization and when user presses the Reset button.
	 * 
	 * @param sess
	 */
	private void checkAllNationsAndRegions(Session sess) {

		List allNations = EstimatesNation.loadAll(sess);
		List allExpRegions = EstimatesExportRegion.loadAll(sess);
		List allImpRegions = EstimatesImportRegion.loadAll(sess);

		totalNationsCount = allNations.size();
		totalExpRegionsCount = allExpRegions.size();
		totalImpRegionsCount = allImpRegions.size();

		checkNations(allNations);
		checkExpRegions(allExpRegions);
		checkImpRegions(allImpRegions);

	}

	/**
	 * Used by {@link edu.emory.library.tast.estimates.table.EstimatesTableBean}
	 * to load the names of the selected nations to the table.
	 * 
	 * @param session
	 * @return
	 */
	public List loadSelectedNations(Session session) {

		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedNationIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesNation.getAttribute("id"), iter.next(), Conditions.OP_EQUALS);

		QueryValue query = new QueryValue("edu.emory.library.tast.dm.EstimatesNation", cond);
		query.setOrderBy(new Attribute[] { EstimatesNation.getAttribute("order") });
		query.setOrder(QueryValue.ORDER_ASC);

		return query.executeQueryList(session);

	}

	/**
	 * Used by {@link edu.emory.library.tast.estimates.table.EstimatesTableBean}
	 * to load the names of the selected export regions to the table.
	 * 
	 * @return
	 */
	public List loadSelectedExpRegions(Session session) {

		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedExpRegionIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesExportRegion.getAttribute("id"), iter.next(), Conditions.OP_EQUALS);

		QueryValue query = new QueryValue("edu.emory.library.tast.dm.EstimatesExportRegion", cond);
		query.setOrderBy(new Attribute[] { EstimatesExportRegion.getAttribute("order") });
		query.setOrder(QueryValue.ORDER_ASC);

		return query.executeQueryList(session);

	}

	/**
	 * Used by {@link edu.emory.library.tast.estimates.table.EstimatesTableBean}
	 * to load the names of the selected import regions to the table.
	 * 
	 * @return
	 */
	public List loadSelectedImpRegions(Session session) {

		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedImpRegionIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesImportRegion.getAttribute("id"), iter.next(), Conditions.OP_EQUALS);

		QueryValue query = new QueryValue("edu.emory.library.tast.dm.EstimatesImportRegion", cond);
		query.setOrderBy(new Attribute[] { EstimatesImportRegion.getAttribute("order") });
		query.setOrder(QueryValue.ORDER_ASC);

		return query.executeQueryList(session);

	}

	/**
	 * Used by {@link edu.emory.library.tast.estimates.table.EstimatesTableBean}
	 * to load the names of the selected import areas to the table.
	 * 
	 * @return
	 */
	public List loadSelectedImpAreas(Session session) {

		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedImpAreaIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesImportArea.getAttribute("id"), iter.next(), Conditions.OP_EQUALS);

		QueryValue query = new QueryValue("edu.emory.library.tast.dm.EstimatesImportArea", cond);
		query.setOrderBy(new Attribute[] { EstimatesImportRegion.getAttribute("order") });
		query.setOrder(QueryValue.ORDER_ASC);

		return query.executeQueryList(session);

	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	public boolean isAllNationsSelected() {
		return totalNationsCount == selectedNationIds.size();
	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	public boolean isAllExpRegionsSelected() {
		return totalExpRegionsCount == selectedExpRegionIds.size();
	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	public boolean isAllImpRegionsSelected() {
		return totalImpRegionsCount == selectedImpRegionIds.size();
	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	public Set getSelectedExpRegionIds() {
		return selectedExpRegionIds == null ? new HashSet() : selectedExpRegionIds;
	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	public Set getSelectedImpAreaIds() {
		return selectedImpAreaIds == null ? new HashSet() : selectedImpRegionIds;
	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	public Set getSelectedImpRegionIds() {
		return selectedImpRegionIds == null ? new HashSet() : selectedImpRegionIds;
	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	public Set getSelectedNationIds() {
		return selectedNationIds == null ? new HashSet() : selectedNationIds;
	}

	/**
	 * Bound to the Change Selection buttons. Calles {@link #createConditions()}
	 * and {@link #updateSelectionInfo()}.
	 * 
	 * @return
	 */
	public String changeSelection() {
		createConditions();
		updateSelectionInfo();
		return null;
	}

	/**
	 * Uses {@link #checkedNations}, {@link #checkedExpRegions},
	 * {@link #checkedImpRegions}, {@link #yearFrom} and {@link #yearTo} to
	 * create the current query.
	 */
	private void createConditions() {
		timeFrameConditions = new Conditions(Conditions.JOIN_AND);

		timeFrameConditions.addCondition(Estimate.getAttribute("year"), new Integer(yearFrom), Conditions.OP_GREATER_OR_EQUAL);

		timeFrameConditions.addCondition(Estimate.getAttribute("year"), new Integer(yearTo), Conditions.OP_SMALLER_OR_EQUAL);

		Conditions conditionNations = new Conditions(Conditions.JOIN_OR);
		Conditions conditionExpRegions = new Conditions(Conditions.JOIN_OR);
		Conditions conditionImpRegions = new Conditions(Conditions.JOIN_OR);
		Conditions conditionRegions = new Conditions(Conditions.JOIN_AND);

		geographicConditions = new Conditions(Conditions.JOIN_AND);
		geographicConditions.addCondition(conditionNations);
		geographicConditions.addCondition(conditionRegions);
		conditionRegions.addCondition(conditionExpRegions);
		conditionRegions.addCondition(conditionImpRegions);

		selectedNationIds = new HashSet();
		selectedExpRegionIds = new HashSet();
		selectedImpRegionIds = new HashSet();
		selectedImpAreaIds = new HashSet();

		Attribute nationIdAttr = new SequenceAttribute(new Attribute[] { Estimate.getAttribute("nation"),
				EstimatesNation.getAttribute("id") });

		for (int i = 0; i < checkedNations.length; i++) {
			Long nationId = new Long(checkedNations[i]);
			selectedNationIds.add(nationId);
			conditionNations.addCondition(nationIdAttr, nationId, Conditions.OP_EQUALS);
		}

		Attribute regionExpIdAttr = new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"),
				EstimatesExportRegion.getAttribute("id") });

		for (int i = 0; i < checkedExpRegions.length; i++) {
			Long regionId = new Long(checkedExpRegions[i]);
			selectedExpRegionIds.add(regionId);
			conditionExpRegions.addCondition(regionExpIdAttr, regionId, Conditions.OP_EQUALS);
		}

		Attribute regionImpIdAttr = new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"),
				EstimatesImportRegion.getAttribute("id") });

		for (int i = 0; i < checkedImpRegions.length; i++) {
			String[] ids = checkedImpRegions[i].split("_");
			for (int j = 0; j < ids.length; j++) {
				Long id = new Long(ids[j].substring(1));
				if (ids[j].startsWith("A")) {
					selectedImpAreaIds.add(id);
				} else {
					selectedImpRegionIds.add(id);
					conditionImpRegions.addCondition(regionImpIdAttr, id, Conditions.OP_EQUALS);
				}
			}
		}
	}

	/**
	 * Restores proper conditions after using permanent link
	 * 
	 */
	public void restoreConditions(Session session) {
		timeFrameConditions = new Conditions(Conditions.JOIN_AND);

		timeFrameConditions.addCondition(Estimate.getAttribute("year"), new Integer(yearFrom), Conditions.OP_GREATER_OR_EQUAL);

		timeFrameConditions.addCondition(Estimate.getAttribute("year"), new Integer(yearTo), Conditions.OP_SMALLER_OR_EQUAL);

		Conditions conditionNations = new Conditions(Conditions.JOIN_OR);
		Conditions conditionExpRegions = new Conditions(Conditions.JOIN_OR);
		Conditions conditionImpRegions = new Conditions(Conditions.JOIN_OR);

		geographicConditions = new Conditions(Conditions.JOIN_AND);
		geographicConditions.addCondition(conditionNations);
		geographicConditions.addCondition(conditionExpRegions);
		geographicConditions.addCondition(conditionImpRegions);

		Attribute nationIdAttr = new SequenceAttribute(new Attribute[] { Estimate.getAttribute("nation"),
				EstimatesNation.getAttribute("id") });

		Iterator iter = this.selectedNationIds.iterator();
		this.checkedNations = new String[this.selectedNationIds.size()];
		int i = 0;
		while (iter.hasNext()) {
			Long nationId = (Long) iter.next();
			this.checkedNations[i++] = String.valueOf(nationId);
			conditionNations.addCondition(nationIdAttr, nationId, Conditions.OP_EQUALS);
		}

		Attribute regionExpIdAttr = new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"),
				EstimatesExportRegion.getAttribute("id") });

		iter = this.selectedExpRegionIds.iterator();
		this.checkedExpRegions = new String[this.selectedExpRegionIds.size()];
		i = 0;
		while (iter.hasNext()) {
			Long regionId = (Long) iter.next();
			this.checkedExpRegions[i++] = regionId.toString();
			conditionExpRegions.addCondition(regionExpIdAttr, regionId, Conditions.OP_EQUALS);
		}

		Attribute regionImpIdAttr = new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"),
				EstimatesImportRegion.getAttribute("id") });

		iter = this.selectedImpRegionIds.iterator();
		List tmpList = new ArrayList();
		List addedAreas = new ArrayList();
		while (iter.hasNext()) {
			Long regionId = (Long) iter.next();
			EstimatesImportRegion region = EstimatesImportRegion.loadById(session, regionId.longValue());
			EstimatesImportArea area = region.getArea();
			if (area.getRegions().size() == 1) {
				tmpList.add("A" + area.getId() + "_R" + regionId);
				addedAreas.add(area.getId());
			} else {
				tmpList.add("R" + regionId);
			}
			conditionImpRegions.addCondition(regionImpIdAttr, regionId, Conditions.OP_EQUALS);
		}
		
		iter = this.selectedImpAreaIds.iterator();
		while (iter.hasNext()) {
			Long areaId = (Long) iter.next();
			if (!addedAreas.contains(areaId)) {
				tmpList.add("A" + areaId);
			}
		}
		
		this.checkedImpRegions = (String[]) tmpList.toArray(new String[] {});
	}

	/**
	 * Recalculates {@link #selectedNationsAsText},
	 * {@link #selectedExpRegionsAsText} and {@link #selectedImpRegionsAsText}
	 * based on {@link #selectedExpRegionIds}, {@link #selectedExpRegionIds},
	 * {@link #selectedImpRegionIds} and {@link #selectedImpAreaIds}.
	 */
	private void updateSelectionInfo() {

		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		updateSelectedNationsInfo(sess);
		updateSelectedExpRegionsInfo(sess);
		updateSelectedImpRegionsInfo(sess);

		transaction.commit();
		sess.close();

	}

	/**
	 * Part of {@link #updateSelectionInfo()}. Split for clarity.
	 * 
	 * @param sess
	 */
	private void updateSelectedNationsInfo(Session sess) {

		StringBuffer selectedNationsBuff = new StringBuffer();

		if (totalNationsCount > selectedNationIds.size()) {
			int i = 0;
			List selectedNations = loadSelectedNations(sess);
			for (Iterator iter = selectedNations.iterator(); iter.hasNext();) {
				EstimatesNation nation = (EstimatesNation) iter.next();
				if (i > 0)
					selectedNationsBuff.append(", ");
				selectedNationsBuff.append(nation.getName());
				i++;
			}
		} else {
			selectedNationsBuff.append("<i>all</i>");
		}

		selectedNationsAsText = selectedNationsBuff.toString();

	}

	/**
	 * Part of {@link #updateSelectionInfo()}. Split for clarity.
	 * 
	 * @param sess
	 */
	private void updateSelectedExpRegionsInfo(Session sess) {

		StringBuffer selectedExpRegionsBuff = new StringBuffer();

		if (totalExpRegionsCount > selectedExpRegionIds.size()) {
			int i = 0;
			List selectedExpRegions = loadSelectedExpRegions(sess);
			for (Iterator iter = selectedExpRegions.iterator(); iter.hasNext();) {
				EstimatesExportRegion region = (EstimatesExportRegion) iter.next();
				if (i > 0)
					selectedExpRegionsBuff.append(", ");
				selectedExpRegionsBuff.append(region.getName());
				i++;
			}
		} else {
			selectedExpRegionsBuff.append("<i>all</i>");
		}

		selectedExpRegionsAsText = selectedExpRegionsBuff.toString();

	}

	/**
	 * Part of {@link #updateSelectionInfo()}. Split for clarity.
	 * 
	 * @param sess
	 */
	private void updateSelectedImpRegionsInfo(Session sess) {

		StringBuffer selectedImpRegionsBuff = new StringBuffer();

		if (totalImpRegionsCount > selectedImpRegionIds.size()) {

			int i = 0;

			List selectedImpRegions = EstimatesImportRegion.loadAll(sess);

			Iterator iter = selectedImpRegions.iterator();
			EstimatesImportRegion region = (EstimatesImportRegion) iter.next();
			int lastAreaId = region.getArea().getId().intValue();
			String lastAreaName = region.getArea().getName();

			StringBuffer selectedImpRegionsInAreaBuff = new StringBuffer();

			while (iter.hasNext()) {

				int j = 0;
				boolean allSelected = true;
				boolean noSelected = true;

				lastAreaName = region.getArea().getName();

				selectedImpRegionsInAreaBuff.setLength(0);

				while (iter.hasNext()) {

					if (selectedImpRegionIds.contains(region.getId())) {
						if (j > 0)
							selectedImpRegionsInAreaBuff.append(", ");
						selectedImpRegionsInAreaBuff.append(region.getName());
						noSelected = false;
						j++;
					} else {
						allSelected = false;
					}

					region = (EstimatesImportRegion) iter.next();

					int areaId = region.getArea().getId().intValue();
					if (lastAreaId != areaId) {
						lastAreaId = areaId;
						break;
					}

				}

				if (allSelected || !noSelected) {

					if (i > 0)
						selectedImpRegionsBuff.append("<br>");

					selectedImpRegionsBuff.append("<i>");
					selectedImpRegionsBuff.append(lastAreaName);
					selectedImpRegionsBuff.append("</i>: ");

					if (allSelected) {
						selectedImpRegionsBuff.append("all regions");
					} else {
						selectedImpRegionsBuff.append(selectedImpRegionsInAreaBuff);
					}

					i++;

				}

			}
		} else {
			selectedImpRegionsBuff.append("<i>all</i>");
		}

		selectedImpRegionsAsText = selectedImpRegionsBuff.toString();

	}

	/**
	 * Bound to the reset button.
	 * 
	 * @return null
	 */
	public String resetSelection() {
		initDefaultValues();
		return null;
	}

	/**
	 * Used by the other beans.
	 * 
	 * @return
	 */
	public Conditions getTimeFrameConditions() {
		return timeFrameConditions;
	}

	/**
	 * Used by the other beans.
	 * 
	 * @return
	 */
	public Conditions getGeographicConditions() {
		return geographicConditions;
	}

	/**
	 * Used by the other beans.
	 * 
	 * @return
	 */
	public Conditions getConditions() {
		Conditions conds = new Conditions(Conditions.JOIN_AND);
		conds.addCondition(geographicConditions);
		conds.addCondition(timeFrameConditions);
		return conds;
	}

	/**
	 * Bound to UI. It loads all export regions from the database and convert
	 * them to an array or {@link #edu.emory.library.tast.common.SelectItem}.
	 * 
	 * @return
	 */
	public SelectItem[] getAllExpRegions() {
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		List regionsDb = EstimatesExportRegion.loadAll(sess);
		SelectItem[] regionsUi = new SelectItem[regionsDb.size()];

		int i = 0;
		for (Iterator iter = regionsDb.iterator(); iter.hasNext();) {
			EstimatesExportRegion region = (EstimatesExportRegion) iter.next();
			regionsUi[i++] = new SelectItem(region.getName(), String.valueOf(region.getId()));
		}

		transaction.commit();
		sess.close();

		return regionsUi;
	}

	/**
	 * Bound to UI. It loads all import regions from the database and convert
	 * them to an array or {@link #edu.emory.library.tast.common.SelectItem}.
	 * See {@link EstimatesSelectionBean} for more information about endocoding
	 * of ids.
	 * 
	 * @return
	 */
	public SelectItem[] getAllImpRegions() {
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		List regionsDb = EstimatesImportRegion.loadAll(sess);

		List areasTemp = new ArrayList();
		List regionsTemp = new ArrayList();

		int i = 0;
		int regionsCount = regionsDb.size();

		while (i < regionsCount) {

			EstimatesImportRegion region = (EstimatesImportRegion) regionsDb.get(i);
			EstimatesImportArea area = region.getArea();

			regionsTemp.clear();

			while (area.equals(region.getArea())) {
				regionsTemp
						.add(new SelectItemWithImage(region.getName(), "R" + region.getId(), "regions/region-" + region.getId() + ".png"));
				if (++i == regionsCount)
					break;
				region = (EstimatesImportRegion) regionsDb.get(i);
			}

			SelectItemWithImage areaItem = new SelectItemWithImage(area.getName(), null, "regions/area-" + area.getId() + ".png");

			areaItem.setSelectable(true);
			areasTemp.add(areaItem);

			if (regionsTemp.size() > 1) {
				SelectItemWithImage[] regionsUi = new SelectItemWithImage[regionsTemp.size()];
				regionsTemp.toArray(regionsUi);
				areaItem.setSubItems(regionsUi);
				areaItem.setValue("A" + area.getId());
			} else {
				SelectItemWithImage singleRegion = (SelectItemWithImage) regionsTemp.get(0);
				areaItem.setValue("A" + area.getId() + "_" + singleRegion.getValue());
			}

		}

		transaction.commit();
		sess.close();

		SelectItemWithImage[] areasUi = new SelectItemWithImage[areasTemp.size()];
		areasTemp.toArray(areasUi);

		return areasUi;
	}

	/**
	 * Bound to UI. It loads all nations from the database and convert them to
	 * an array or {@link #edu.emory.library.tast.common.SelectItem}.
	 * 
	 * @return
	 */
	public SelectItem[] getAllNations() {
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		List nationsDb = EstimatesNation.loadAll(sess);
		SelectItem[] nationsUi = new SelectItem[nationsDb.size()];

		int i = 0;
		for (Iterator iter = nationsDb.iterator(); iter.hasNext();) {
			EstimatesNation nation = (EstimatesNation) iter.next();
			nationsUi[i++] = new SelectItem(nation.getName(), String.valueOf(nation.getId()));
		}

		transaction.commit();
		sess.close();

		return nationsUi;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public String[] getCheckedExpRegions() {
		return checkedExpRegions;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public void setCheckedExpRegions(String[] checkedExpRegionValues) {
		this.checkedExpRegions = checkedExpRegionValues;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public String[] getCheckedImpRegions() {
		return checkedImpRegions;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public void setCheckedImpRegions(String[] checkedImpRegionValues) {
		this.checkedImpRegions = checkedImpRegionValues;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public String[] getCheckedNations() {
		return checkedNations;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public void setCheckedNations(String[] checkedNationValues) {
		this.checkedNations = checkedNationValues;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public String[] getExpandedExpRegions() {
		return expandedExpRegions;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public void setExpandedExpRegions(String[] expandedExpRegions) {
		this.expandedExpRegions = expandedExpRegions;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public String[] getExpandedImpRegions() {
		return expandedImpRegions;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public void setExpandedImpRegions(String[] expandedImpRegions) {
		this.expandedImpRegions = expandedImpRegions;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public String getSelectedExpRegionsAsText() {
		return selectedExpRegionsAsText;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public void setSelectedExpRegionsAsText(String selectedExpRegionsAsText) {
		this.selectedExpRegionsAsText = selectedExpRegionsAsText;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public String getSelectedImpRegionsAsText() {
		return selectedImpRegionsAsText;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public void setSelectedImpRegionsAsText(String selectedImpRegionsAsText) {
		this.selectedImpRegionsAsText = selectedImpRegionsAsText;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public String getSelectedNationsAsText() {
		return selectedNationsAsText;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public void setSelectedNationsAsText(String selectedNationsAsText) {
		this.selectedNationsAsText = selectedNationsAsText;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public int getYearFrom() {
		return yearFrom;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public void setYearFrom(int yearFrom) {
		this.yearFrom = yearFrom;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public int getYearTo() {
		return yearTo;
	}

	/**
	 * Bound to UI. Wrapper.
	 * 
	 * @return
	 */
	public void setYearTo(int yearTo) {
		this.yearTo = yearTo;
	}

	public String createPermanentLink() {

		// UidGenerator generator = new UidGenerator();
		// String uid = generator.generate();

		Configuration conf = new Configuration();
		conf.addEntry("permlinkEstimates", new EstimatesSelection(this));
		conf.save();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		messageBar.setMessage(request.getRequestURL() + "?permlink=" + conf.getId());
		messageBar.setRendered(true);

		return null;
	}

	public MessageBarComponent getMessageBar() {
		return messageBar;
	}

	public void setMessageBar(MessageBarComponent messageBar) {
		this.messageBar = messageBar;
	}

	/**
	 * Ugly trick (because of limitations of JSF). This is called every time the
	 * page is reloaded (because it is bound to a textbox). Calls
	 * {@link #restorePermlinkIfAny()}.
	 * 
	 * @return
	 */
	public String getFakeHiddenForPermlinkRestore() {

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			Map params = context.getExternalContext().getRequestParameterMap();
			if (!params.containsKey("permlink"))
				return null;

			String permlink = (String) params.get("permlink");
			if (StringUtils.isNullOrEmpty(permlink))
				return null;

			Configuration conf = Configuration.loadConfiguration(permlink);
			if (conf == null)
				return null;

			if (conf.getEntry("permlinkEstimates") != null) {
				EstimatesSelection selection = (EstimatesSelection) conf.getEntry("permlinkEstimates");
				this.selectedExpRegionIds = selection.selectedExpRegionIds;
				this.selectedImpAreaIds = selection.selectedImpAreaIds;
				this.selectedImpRegionIds = selection.selectedImpRegionIds;
				this.selectedNationIds = selection.selectedNationIds;
				this.yearFrom = selection.yearFrom;
				this.yearTo = selection.yearTo;
				if (selection.selectedTab != null) {
					this.selectedTab = selection.selectedTab;
				}
				this.restoreConditions(session);
				this.updateSelectionInfo();
			}
		} finally {
			t.commit();
			session.close();
		}
		return null;
	}

	/**
	 * Ugly trick (because of limitations of JSF). See
	 * {@link #getFakeHiddenForPermlinkRestore()}.
	 * 
	 * @return
	 */
	public void setFakeHiddenForPermlinkRestore(String value) {
	}

	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

}