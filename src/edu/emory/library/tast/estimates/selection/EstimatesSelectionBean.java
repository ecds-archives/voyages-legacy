package edu.emory.library.tast.estimates.selection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.SimpleCache;
import edu.emory.library.tast.common.PopupComponent;
import edu.emory.library.tast.common.SelectItem;
import edu.emory.library.tast.common.SelectItemWithImage;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesExportRegion;
import edu.emory.library.tast.dm.EstimatesImportArea;
import edu.emory.library.tast.dm.EstimatesImportRegion;
import edu.emory.library.tast.dm.EstimatesNation;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.ConversionUtils;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
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
 * recomputed whenever the user presses the Change Selection button. The reason
 * for this splitting is that the timeline tab should not be restricted by the
 * choice of the time frame. So it gets only the geographical part of the query.
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
 * one regions appear as one non-expandable item, and we need to decode from it
 * (without going to the database) which region does it represent.
 */

public class EstimatesSelectionBean
{

	private static final String IMP_REGIONS_ID_SEPATATOR = "_";

	private static final int TIME_SPAN_INITIAL_FROM = 1500;
	private static final int TIME_SPAN_INITIAL_TO = 1900;

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

	private String yearFrom = String.valueOf(TIME_SPAN_INITIAL_FROM);
	private String yearTo = String.valueOf(TIME_SPAN_INITIAL_TO);

	private String selectedTab = "table";

	private boolean lockedYears = false;
	
	private PopupComponent permlinkPopup = null;
	private String lastPermLink = null;
	
	public EstimatesSelectionBean()
	{
		initDefaultValues();
	}

	/**
	 * Used only during initialisation and when user presses the Reset button.
	 * Load the default values, i.e. all nations and regions are selected and
	 * the time frame is set to the min and max year determined from the
	 * database. It uses {@link #checkAllNationsAndRegions} and
	 * {@link #initDefaultTimeFrame(Session)} to do it.
	 * 
	 * @param sess
	 */
	private void initDefaultValues()
	{

		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		checkAllNationsAndRegions(sess);
		initDefaultTimeFrame(sess);

		transaction.commit();
		sess.close();

		changeSelection();

	}

	/**
	 * Used only during initialisation and when user presses the Reset button.
	 * 
	 * @param sess
	 */
	private void initDefaultTimeFrame(Session sess)
	{

		QueryValue query = new QueryValue("edu.emory.library.tast.dm.Estimate");

		query.addPopulatedAttribute(new FunctionAttribute("min", new Attribute[] { Estimate.getAttribute("year") }));
		query.addPopulatedAttribute(new FunctionAttribute("max", new Attribute[] { Estimate.getAttribute("year") }));

		Object[] result = query.executeQuery();
		Object[] firsrRow = (Object[]) result[0];

		yearFrom = firsrRow[0] != null ? firsrRow[0].toString() : "";
		yearTo = firsrRow[1] != null ? firsrRow[1].toString() : "";

	}

	/**
	 * General method, which changes {@link #checkedNations} by the given list
	 * of nations from the database. Now used only during inicialization and
	 * when user presses the Reset button.
	 * 
	 * @param nations
	 *            List of nations.
	 */
	private void checkNations(List nations)
	{

		totalNationsCount = nations.size();
		checkedNations = new String[nations.size()];

		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
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
	private void checkExpRegions(List regions)
	{

		checkedExpRegions = new String[regions.size()];

		int i = 0;
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			EstimatesExportRegion region = (EstimatesExportRegion) iter.next();
			checkedExpRegions[i] = String.valueOf(region.getId());
			i++;
		}

	}

	/**
	 * General method, which changes {@link #checkedImpRegions} by the given
	 * list of regions from the database. Now used only during initialisation
	 * and when user presses the Reset button.
	 * 
	 * @param nations
	 *            List of nations.
	 */
	private void checkImpRegions(List regions)
	{

		int i = 0;
		int regionsCount = regions.size();

		List tempIds = new ArrayList();
		
		while (i < regionsCount)
		{

			EstimatesImportRegion region = (EstimatesImportRegion) regions.get(i);
			EstimatesImportArea area = region.getArea();
			Long lastRegionId = region.getId();

			int regionsInArea = 0;
			while (area.equals(region.getArea()))
			{
				lastRegionId = region.getId();
				tempIds.add("R" + lastRegionId);
				regionsInArea++;
				if (++i == regionsCount) break;
				region = (EstimatesImportRegion) regions.get(i);
			}

			if (regionsInArea > 1)
			{
				tempIds.add("A" + area.getId());
			}
			else
			{
				tempIds.add("A" + area.getId() + IMP_REGIONS_ID_SEPATATOR + "R" + lastRegionId);
			}

		}

		checkedImpRegions = new String[tempIds.size()];
		tempIds.toArray(checkedImpRegions);
	
	}

	/**
	 * Used only during initialisation and when user presses the Reset button.
	 * 
	 * @param sess
	 */
	private void checkAllNationsAndRegions(Session sess)
	{

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
	public List loadSelectedNations(Session session)
	{

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
	public List loadSelectedExpRegions(Session session)
	{

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
	public List loadSelectedImpRegions(Session session)
	{

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
	public List loadSelectedImpAreas(Session session)
	{

		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedImpAreaIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesImportArea.getAttribute("id"), iter.next(), Conditions.OP_EQUALS);

		QueryValue query = new QueryValue("edu.emory.library.tast.dm.EstimatesImportArea", cond);
		query.setOrderBy(new Attribute[] { EstimatesImportRegion.getAttribute("order") });
		query.setOrder(QueryValue.ORDER_ASC);

		return query.executeQueryList(session);

	}

	/**
	 * Bound to the Change Selection buttons. Cales {@link #createConditions()}
	 * and {@link #updateSelectionInfo()}.
	 * 
	 * @return
	 */
	public String changeSelection()
	{
		createConditions();
		updateSelectionInfo();
		return null;
	}

	/**
	 * Uses {@link #checkedNations}, {@link #checkedExpRegions},
	 * {@link #checkedImpRegions}, {@link #yearFrom} and {@link #yearTo} to
	 * create the current query.
	 */
	private void createConditions()
	{
		
		Integer yearFromInt = ConversionUtils.toInteger(yearFrom);
		Integer yearToInt = ConversionUtils.toInteger(yearTo);

		timeFrameConditions = new Conditions(Conditions.JOIN_AND);
		
		if (yearFromInt == null || yearToInt == null || yearFromInt.compareTo(yearToInt) < 0)
		{

			if (yearFromInt != null)
				timeFrameConditions.addCondition(
						Estimate.getAttribute("year"),
						yearFromInt,
						Conditions.OP_GREATER_OR_EQUAL);
			
			if (yearToInt != null)
				timeFrameConditions.addCondition(
						Estimate.getAttribute("year"),
						yearToInt,
						Conditions.OP_SMALLER_OR_EQUAL);
		}

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

		Attribute nationIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("nation"),
				EstimatesNation.getAttribute("id") });

		for (int i = 0; i < checkedNations.length; i++)
		{
			Long nationId = new Long(checkedNations[i]);
			selectedNationIds.add(nationId);
			conditionNations.addCondition(nationIdAttr, nationId, Conditions.OP_EQUALS);
		}

		Attribute regionExpIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("expRegion"),
				EstimatesExportRegion.getAttribute("id") });

		for (int i = 0; i < checkedExpRegions.length; i++)
		{
			Long regionId = new Long(checkedExpRegions[i]);
			selectedExpRegionIds.add(regionId);
			conditionExpRegions.addCondition(regionExpIdAttr, regionId, Conditions.OP_EQUALS);
		}

		Attribute regionImpIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("impRegion"),
				EstimatesImportRegion.getAttribute("id") });

		for (int i = 0; i < checkedImpRegions.length; i++)
		{
			String[] ids = checkedImpRegions[i].split(IMP_REGIONS_ID_SEPATATOR);
			for (int j = 0; j < ids.length; j++)
			{
				Long id = new Long(ids[j].substring(1));
				if (ids[j].startsWith("A"))
				{
					selectedImpAreaIds.add(id);
				}
				else
				{
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
	/*
	public void restoreConditions(Session session)
	{
		
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
	*/

	/**
	 * Recalculates {@link #selectedNationsAsText},
	 * {@link #selectedExpRegionsAsText} and {@link #selectedImpRegionsAsText}
	 * based on {@link #selectedExpRegionIds}, {@link #selectedExpRegionIds},
	 * {@link #selectedImpRegionIds} and {@link #selectedImpAreaIds}.
	 */
	private void updateSelectionInfo()
	{

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
	private void updateSelectedNationsInfo(Session sess)
	{

		StringBuffer selectedNationsBuff = new StringBuffer();

		if (totalNationsCount > selectedNationIds.size())
		{
			int i = 0;
			List selectedNations = loadSelectedNations(sess);
			for (Iterator iter = selectedNations.iterator(); iter.hasNext();)
			{
				EstimatesNation nation = (EstimatesNation) iter.next();
				if (i > 0) selectedNationsBuff.append(", ");
				selectedNationsBuff.append(nation.getName());
				i++;
			}
		}
		else
		{
			selectedNationsBuff.append("<i>all</i>");
		}

		selectedNationsAsText = selectedNationsBuff.toString();

	}

	/**
	 * Part of {@link #updateSelectionInfo()}. Split for clarity.
	 * 
	 * @param sess
	 */
	private void updateSelectedExpRegionsInfo(Session sess)
	{

		StringBuffer selectedExpRegionsBuff = new StringBuffer();

		if (totalExpRegionsCount > selectedExpRegionIds.size())
		{
			int i = 0;
			List selectedExpRegions = loadSelectedExpRegions(sess);
			for (Iterator iter = selectedExpRegions.iterator(); iter.hasNext();)
			{
				EstimatesExportRegion region = (EstimatesExportRegion) iter.next();
				if (i > 0) selectedExpRegionsBuff.append(", ");
				selectedExpRegionsBuff.append(region.getName());
				i++;
			}
		}
		else
		{
			selectedExpRegionsBuff.append("<i>all</i>");
		}

		selectedExpRegionsAsText = selectedExpRegionsBuff.toString();

	}

	/**
	 * Part of {@link #updateSelectionInfo()}. Split for clarity.
	 * 
	 * @param sess
	 */
	private void updateSelectedImpRegionsInfo(Session sess)
	{

		StringBuffer selectedImpRegionsBuff = new StringBuffer();
		
		if (totalImpRegionsCount > selectedImpRegionIds.size())
		{

			int i = 0;

			List selectedImpRegions = EstimatesImportRegion.loadAll(sess);

			Iterator iter = selectedImpRegions.iterator();
			EstimatesImportRegion region = (EstimatesImportRegion) iter.next();
			int lastAreaId = region.getArea().getId().intValue();
			String lastAreaName = region.getArea().getName();

			StringBuffer selectedImpRegionsInAreaBuff = new StringBuffer();
			boolean allSelected;
			boolean noSelected;
			int j = 0;
			
			while (iter.hasNext()) {
				
				j=0;
				allSelected = true;
				noSelected = true;

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
				
				if (allSelected || !noSelected)
				{

					if (i > 0) selectedImpRegionsBuff.append("<br>");
					selectedImpRegionsBuff.append("<i>");
					selectedImpRegionsBuff.append(lastAreaName);					
					selectedImpRegionsBuff.append("</i>: ");

					if (allSelected)
					{
						selectedImpRegionsBuff.append("all regions");
					}
					else
					{
						selectedImpRegionsBuff.append(selectedImpRegionsInAreaBuff);
					}

					i++;

				}
			}
				
			allSelected = true;
			noSelected = true;
			lastAreaName = region.getArea().getName();
			if (selectedImpRegionIds.contains(region.getId()))
			{
				if (j > 0) selectedImpRegionsInAreaBuff.append(", ");
				selectedImpRegionsInAreaBuff.append(region.getName());
				noSelected = false;
				j++;
			}
			else
			{
				allSelected = false;
			}
			
			if (allSelected || !noSelected)
			{

				if (i > 0) selectedImpRegionsBuff.append("<br>");

				selectedImpRegionsBuff.append("<i>");
				selectedImpRegionsBuff.append(lastAreaName);					
				selectedImpRegionsBuff.append("</i>: ");

				if (allSelected)
				{
					selectedImpRegionsBuff.append("all regions");
				}
				else
				{
					selectedImpRegionsBuff.append(selectedImpRegionsInAreaBuff);
				}
				
				i++;
			}

		}
		else
		{
			selectedImpRegionsBuff.append("<i>all</i>");
		}

		selectedImpRegionsAsText = selectedImpRegionsBuff.toString();

	}

	/**
	 * Bound to the reset button.
	 * 
	 * @return null
	 */
	public String resetSelection()
	{
		initDefaultValues();
		return null;
	}

	/**
	 * Used by the other beans.
	 * 
	 * @return
	 */
	public Conditions getTimeFrameConditions()
	{
		return timeFrameConditions;
	}

	/**
	 * Used by the other beans.
	 * 
	 * @return
	 */
	public Conditions getGeographicConditions()
	{
		return geographicConditions;
	}

	/**
	 * Used by the other beans.
	 * 
	 * @return
	 */
	public Conditions getConditions()
	{
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
	public SelectItem[] getAllExpRegions()
	{
		
		SelectItem[] regionsUi = (SelectItem[]) SimpleCache.get(
				SimpleCache.ESTIMATES_PREFIX + "expRegions");
		
		if (regionsUi == null)
		{
		
			Session sess = HibernateUtil.getSession();
			Transaction transaction = sess.beginTransaction();
	
			List regionsDb = EstimatesExportRegion.loadAll(sess);
			regionsUi = new SelectItem[regionsDb.size()];
	
			int i = 0;
			for (Iterator iter = regionsDb.iterator(); iter.hasNext();)
			{
				EstimatesExportRegion region = (EstimatesExportRegion) iter.next();
				regionsUi[i++] = new SelectItem(region.getName(), String.valueOf(region.getId()));
			}
	
			transaction.commit();
			sess.close();
			
			SimpleCache.set(
					SimpleCache.ESTIMATES_PREFIX + "expRegions",
					regionsUi);
			
		}

		return regionsUi;
		
	}

	/**
	 * Bound to UI. It loads all import regions from the database and convert
	 * them to an array or {@link #edu.emory.library.tast.common.SelectItem}.
	 * See {@link EstimatesSelectionBean} for more information about encoding
	 * of ids.
	 * 
	 * @return
	 */
	public SelectItem[] getAllImpRegions()
	{
		
		SelectItemWithImage[] areasUi = (SelectItemWithImage[]) SimpleCache.get(
				SimpleCache.ESTIMATES_PREFIX + "impRegions");
		
		if (areasUi == null)
		{
		
			Session sess = HibernateUtil.getSession();
			Transaction transaction = sess.beginTransaction();
	
			List regionsDb = EstimatesImportRegion.loadAll(sess);
	
			List areasTemp = new ArrayList();
			List regionsTemp = new ArrayList();
	
			int i = 0;
			int regionsCount = regionsDb.size();
	
			while (i < regionsCount)
			{
	
				EstimatesImportRegion region = (EstimatesImportRegion) regionsDb.get(i);
				EstimatesImportArea area = region.getArea();
	
				regionsTemp.clear();
	
				while (area.equals(region.getArea()))
				{
					regionsTemp.add(new SelectItemWithImage(
							region.getName(),
							"R" + region.getId(),
							"regions/region-" + region.getId() + ".png"));
					if (++i == regionsCount) break;
					region = (EstimatesImportRegion) regionsDb.get(i);
				}
	
				SelectItemWithImage areaItem = new SelectItemWithImage(area.getName(), null, "regions/area-" + area.getId() + ".png");
	
				areaItem.setSelectable(true);
				areasTemp.add(areaItem);
	
				if (regionsTemp.size() > 1)
				{
					SelectItemWithImage[] regionsUi = new SelectItemWithImage[regionsTemp.size()];
					regionsTemp.toArray(regionsUi);
					areaItem.setSubItems(regionsUi);
					areaItem.setValue("A" + area.getId());
				}
				else
				{
					SelectItemWithImage singleRegion = (SelectItemWithImage) regionsTemp.get(0);
					areaItem.setValue(
							"A" + area.getId() +
							IMP_REGIONS_ID_SEPATATOR +
							singleRegion.getValue());
				}
	
			}
	
			transaction.commit();
			sess.close();
	
			areasUi = new SelectItemWithImage[areasTemp.size()];
			areasTemp.toArray(areasUi);
			
			SimpleCache.set(
					SimpleCache.ESTIMATES_PREFIX + "impRegions",
					areasUi);
			
		}

		return areasUi;
		
	}

	/**
	 * Bound to UI. It loads all nations from the database and convert them to
	 * an array or {@link #edu.emory.library.tast.common.SelectItem}.
	 * 
	 * @return
	 */
	public SelectItem[] getAllNations()
	{
		
		SelectItem[] nationsUi = (SelectItem[]) SimpleCache.get(
				SimpleCache.ESTIMATES_PREFIX + "nations");
		
		if (nationsUi == null)
		{
		
			Session sess = HibernateUtil.getSession();
			Transaction transaction = sess.beginTransaction();
	
			List nationsDb = EstimatesNation.loadAll(sess);
			nationsUi = new SelectItem[nationsDb.size()];
	
			int i = 0;
			for (Iterator iter = nationsDb.iterator(); iter.hasNext();)
			{
				EstimatesNation nation = (EstimatesNation) iter.next();
				nationsUi[i++] = new SelectItem(nation.getName(), String.valueOf(nation.getId()));
			}
	
			transaction.commit();
			sess.close();
			
			SimpleCache.set(
					SimpleCache.ESTIMATES_PREFIX + "nations",
					nationsUi);
			
		}

		return nationsUi;
		
	}
	
	public String createPermlink() throws UnsupportedEncodingException
	{
		
		StringBuffer url = new StringBuffer();
		url.append("?");

		int queryStringParams = 0;
		
		if (!StringUtils.isNullOrEmpty(yearFrom))
		{
			if (queryStringParams > 0) url.append("&");
			url.append("yearFrom");
			url.append("=");
			url.append(URLEncoder.encode(yearFrom, "UTF-8"));
			queryStringParams++;
		}

		if (!StringUtils.isNullOrEmpty(yearTo))
		{
			if (queryStringParams > 0) url.append("&");
			url.append("yearTo");
			url.append("=");
			url.append(URLEncoder.encode(yearTo, "UTF-8"));
			queryStringParams++;
		}
		
		if (!isNoNationSelected() && !isAllNationsSelected())
		{
			if (queryStringParams > 0) url.append("&");
			url.append("flag").append("=");
			url.append(StringUtils.join(".", selectedNationIds));
			queryStringParams++;
		}
		
		if (!isNoExpRegionSelected() && !isAllExpRegionsSelected())
		{
			if (queryStringParams > 0) url.append("&");
			url.append("embarkation").append("=");
			url.append(StringUtils.join(".", selectedExpRegionIds));
			queryStringParams++;
		}
		
		if (!isNoImpRegionSelected() && !isAllImpRegionsSelected())
		{
			if (queryStringParams > 0) url.append("&");
			url.append("disembarkation").append("=");
			url.append(StringUtils.join(".", selectedImpRegionIds));
		}
		
		lastPermLink =
			AppConfig.getConfiguration().getString(AppConfig.SITE_URL) +
			"/assessment/estimates.faces" + (
					queryStringParams > 0 ? url.toString() : "");
		
		this.permlinkPopup.display();
		return null;
		
	}

	public boolean restoreQueryFromUrl(Map params)
	{
		
		initDefaultValues();
		
		yearFrom =
			StringUtils.getFirstElement(
				(String[]) params.get("yearFrom"));
		
		yearTo =
			StringUtils.getFirstElement(
				(String[]) params.get("yearTo"));
		
		String selectedNationIdsStr =
			StringUtils.getFirstElement(
				(String[]) params.get("flag"));
		
		if (selectedNationIdsStr != null)
			checkedNations = selectedNationIdsStr.split("\\.");
		
		String selectedExpRegionIdsStr =
			StringUtils.getFirstElement(
				(String[]) params.get("embarkation"));
		
		if (selectedExpRegionIdsStr != null)
			checkedExpRegions = selectedExpRegionIdsStr.split("\\.");

		/*
		String selectedImpRegionIdsStr =
			StringUtils.getFirstElement(
				(String[]) params.get("disembarkation"));

		if (selectedImpRegionIdsStr != null)
			checkedImpRegions = selectedImpRegionIdsStr.split("\\.");
		*/
		
		changeSelection();
		
		return true;
		
	}
	
	public boolean isAllNationsSelected()
	{
		return totalNationsCount == selectedNationIds.size();
	}

	public boolean isNoNationSelected()
	{
		return 0 == selectedNationIds.size();
	}

	public boolean isAllExpRegionsSelected()
	{
		return totalExpRegionsCount == selectedExpRegionIds.size();
	}

	public boolean isNoExpRegionSelected()
	{
		return 0 == selectedExpRegionIds.size();
	}

	public boolean isAllImpRegionsSelected()
	{
		return totalImpRegionsCount == selectedImpRegionIds.size();
	}

	public boolean isNoImpRegionSelected()
	{
		return 0 == selectedImpRegionIds.size();
	}

	public Set getSelectedExpRegionIds()
	{
		return selectedExpRegionIds == null ? new HashSet() : selectedExpRegionIds;
	}

	public Set getSelectedImpAreaIds()
	{
		return selectedImpAreaIds == null ? new HashSet() : selectedImpRegionIds;
	}

	public Set getSelectedImpRegionIds()
	{
		return selectedImpRegionIds == null ? new HashSet() : selectedImpRegionIds;
	}

	public Set getSelectedNationIds()
	{
		return selectedNationIds == null ? new HashSet() : selectedNationIds;
	}

	public String[] getCheckedExpRegions()
	{
		return checkedExpRegions;
	}

	public void setCheckedExpRegions(String[] checkedExpRegionValues)
	{
		this.checkedExpRegions = checkedExpRegionValues;
	}

	public String[] getCheckedImpRegions()
	{
		return checkedImpRegions;
	}

	public void setCheckedImpRegions(String[] checkedImpRegionValues)
	{
		this.checkedImpRegions = checkedImpRegionValues;
	}
	
	public String[] getCheckedNations()
	{
		return checkedNations;
	}

	public void setCheckedNations(String[] checkedNationValues)
	{
		this.checkedNations = checkedNationValues;
	}

	public String[] getExpandedExpRegions()
	{
		return expandedExpRegions;
	}

	public void setExpandedExpRegions(String[] expandedExpRegions)
	{
		this.expandedExpRegions = expandedExpRegions;
	}

	public String[] getExpandedImpRegions()
	{
		return expandedImpRegions;
	}

	public void setExpandedImpRegions(String[] expandedImpRegions)
	{
		this.expandedImpRegions = expandedImpRegions;
	}

	public String getSelectedExpRegionsAsText() {
		return selectedExpRegionsAsText;
	}

	public void setSelectedExpRegionsAsText(String selectedExpRegionsAsText) {
		this.selectedExpRegionsAsText = selectedExpRegionsAsText;
	}

	public String getSelectedImpRegionsAsText() {
		return selectedImpRegionsAsText;
	}

	public void setSelectedImpRegionsAsText(String selectedImpRegionsAsText)
	{
		this.selectedImpRegionsAsText = selectedImpRegionsAsText;
	}

	public String getSelectedNationsAsText()
	{
		return selectedNationsAsText;
	}

	public void setSelectedNationsAsText(String selectedNationsAsText)
	{
		this.selectedNationsAsText = selectedNationsAsText;
	}

	public String getYearFrom()
	{
		return yearFrom;
	}

	public void setYearFrom(String yearFrom)
	{
		if (lockedYears) return;
		this.yearFrom = yearFrom;
	}

	public String getYearTo()
	{
		return yearTo;
	}

	public void setYearTo(String yearTo)
	{
		if (lockedYears) return;
		this.yearTo = yearTo;
	}

	public String getSelectedTab()
	{
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab)
	{
		this.selectedTab = selectedTab;
	}

	public void lockYears(boolean b)
	{
		lockedYears  = b;
	}
	
	public PopupComponent getPermlinkPopup()
	{
		return permlinkPopup;
	}

	public void setPermlinkPopup(PopupComponent permlinkPopup)
	{
		this.permlinkPopup = permlinkPopup;
	}

	public String getPermLink()
	{
		return lastPermLink;
	}

}