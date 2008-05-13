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
import edu.emory.library.tast.util.query.TastDbConditions;
import edu.emory.library.tast.util.query.TastDbQuery;


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

	private TastDbConditions timeFrameConditions;
	private TastDbConditions geographicConditions;

	private String[] checkedNations;
	private String[] checkedExpRegions;
	private String[] checkedImpRegions;
	private String[] expandedExpRegions;
	private String[] expandedImpRegions;

	private Set selectedNationIds;
	private Set selectedExpRegionIds;
	private Set selectedImpRegionIds;
	private Set selectedImpAreaIds;
	private String selectedNationsAsText;
	private String selectedExpRegionsAsText;
	private String selectedImpRegionsAsText;
	private boolean allImpRegionseSelected = false;

	private String yearFrom = String.valueOf(TIME_SPAN_INITIAL_FROM);
	private String yearTo = String.valueOf(TIME_SPAN_INITIAL_TO);

	private String selectedTab = "table";
	
	private PopupComponent permlinkPopup = null;
	private String lastPermLink = null;

	private String minYear;

	private String maxYear;

	public EstimatesSelectionBean()
	{
		initDefaultValues(true);
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
	private void initDefaultValues(boolean firstTime)
	{

		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		checkAllNationsAndRegions(sess);
		initDefaultTimeFrame(sess, firstTime);

		transaction.commit();
		sess.close();

		changeSelection();

	}

	/**
	 * Used only during initialisation and when user presses the Reset button.
	 * 
	 * @param sess
	 */
	private void initDefaultTimeFrame(Session sess, boolean firstTime)
	{
		
		if (firstTime)
		{

			TastDbQuery query = new TastDbQuery("edu.emory.library.tast.dm.Estimate");
	
			query.addPopulatedAttribute(new FunctionAttribute("min", new Attribute[] { Estimate.getAttribute("year") }));
			query.addPopulatedAttribute(new FunctionAttribute("max", new Attribute[] { Estimate.getAttribute("year") }));
	
			Object[] result = query.executeQuery();
			Object[] firsrRow = (Object[]) result[0];
			
			minYear = firsrRow[0] != null ? firsrRow[0].toString() : "";
			maxYear = firsrRow[1] != null ? firsrRow[1].toString() : "";
		
		}

		yearFrom = minYear;
		yearTo = maxYear;

	}

	/**
	 * General method, which changes {@link #checkedNations} by the given list
	 * of nations from the database. Now used only during initialisation and
	 * when user presses the Reset button.
	 * 
	 * @param nations
	 *            List of nations.
	 */
	private void checkNations(List nations)
	{

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
				tempIds.add(createImpRegionOnlyId(lastRegionId.toString()));
				regionsInArea++;
				if (++i == regionsCount) break;
				region = (EstimatesImportRegion) regions.get(i);
			}

			if (regionsInArea > 1)
			{
				tempIds.add(createImpAreaOnlyId(area.getId().toString()));
			}
			else
			{
				tempIds.add(createImpAreaRegionId(area.getId().toString(), lastRegionId.toString()));
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

		TastDbConditions cond = new TastDbConditions(TastDbConditions.OR);
		for (Iterator iter = selectedNationIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesNation.getAttribute("id"), iter.next(), TastDbConditions.OP_EQUALS);

		TastDbQuery query = new TastDbQuery("edu.emory.library.tast.dm.EstimatesNation", cond);
		query.setOrderBy(new Attribute[] { EstimatesNation.getAttribute("order") });
		query.setOrder(TastDbQuery.ORDER_ASC);

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

		TastDbConditions cond = new TastDbConditions(TastDbConditions.OR);
		for (Iterator iter = selectedExpRegionIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesExportRegion.getAttribute("id"), iter.next(), TastDbConditions.OP_EQUALS);

		TastDbQuery query = new TastDbQuery("edu.emory.library.tast.dm.EstimatesExportRegion", cond);
		query.setOrderBy(new Attribute[] { EstimatesExportRegion.getAttribute("order") });
		query.setOrder(TastDbQuery.ORDER_ASC);

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

		TastDbConditions cond = new TastDbConditions(TastDbConditions.OR);
		for (Iterator iter = selectedImpRegionIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesImportRegion.getAttribute("id"), iter.next(), TastDbConditions.OP_EQUALS);

		TastDbQuery query = new TastDbQuery("edu.emory.library.tast.dm.EstimatesImportRegion", cond);
		query.setOrderBy(new Attribute[] { EstimatesImportRegion.getAttribute("order") });
		query.setOrder(TastDbQuery.ORDER_ASC);

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

		TastDbConditions cond = new TastDbConditions(TastDbConditions.OR);
		for (Iterator iter = selectedImpAreaIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesImportArea.getAttribute("id"), iter.next(), TastDbConditions.OP_EQUALS);

		TastDbQuery query = new TastDbQuery("edu.emory.library.tast.dm.EstimatesImportArea", cond);
		query.setOrderBy(new Attribute[] { EstimatesImportRegion.getAttribute("order") });
		query.setOrder(TastDbQuery.ORDER_ASC);

		return query.executeQueryList(session);

	}

	public String changeSelection()
	{
		createConditions();
		updateSelectionInfo();
		return null;
	}
	
	public String restoreDefaultTimeFrameExtent()
	{
		
		yearFrom = minYear;
		yearTo = maxYear;
		
		return null;
		
	}

	private void createConditions()
	{
		
		Integer yearFromInt = ConversionUtils.toInteger(yearFrom);
		Integer yearToInt = ConversionUtils.toInteger(yearTo);

		timeFrameConditions = new TastDbConditions(TastDbConditions.AND);
		
		if (yearFromInt == null || yearToInt == null || yearFromInt.compareTo(yearToInt) <= 0)
		{

			if (yearFromInt != null)
				timeFrameConditions.addCondition(
						Estimate.getAttribute("year"),
						yearFromInt,
						TastDbConditions.OP_GREATER_OR_EQUAL);
			
			if (yearToInt != null)
				timeFrameConditions.addCondition(
						Estimate.getAttribute("year"),
						yearToInt,
						TastDbConditions.OP_SMALLER_OR_EQUAL);
		}

		TastDbConditions conditionNations = new TastDbConditions(TastDbConditions.OR);
		TastDbConditions conditionExpRegions = new TastDbConditions(TastDbConditions.OR);
		TastDbConditions conditionImpRegions = new TastDbConditions(TastDbConditions.OR);
		TastDbConditions conditionRegions = new TastDbConditions(TastDbConditions.AND);

		geographicConditions = new TastDbConditions(TastDbConditions.AND);
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
			conditionNations.addCondition(nationIdAttr, nationId, TastDbConditions.OP_EQUALS);
		}

		Attribute regionExpIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("expRegion"),
				EstimatesExportRegion.getAttribute("id") });

		for (int i = 0; i < checkedExpRegions.length; i++)
		{
			Long regionId = new Long(checkedExpRegions[i]);
			selectedExpRegionIds.add(regionId);
			conditionExpRegions.addCondition(regionExpIdAttr, regionId, TastDbConditions.OP_EQUALS);
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
					conditionImpRegions.addCondition(regionImpIdAttr, id, TastDbConditions.OP_EQUALS);
				}
			}
		}
	}

	/**
	 * Recalculates {@link #selectedNationsAsText},
	 * {@link #selectedExpRegionsAsText} and {@link #selectedImpRegionsAsText}
	 * based on {@link #selectedExpRegionIds}, {@link #selectedExpRegionIds},
	 * {@link #selectedImpRegionIds} and {@link #selectedImpAreaIds}.
	 */
	private void updateSelectionInfo()
	{
		updateSelectedNationsInfo();
		updateSelectedExpRegionsInfo();
		updateSelectedImpRegionsInfo();
	}

	/**
	 * Part of {@link #updateSelectionInfo()}. Split for clarity.
	 * 
	 * @param sess
	 */
	private void updateSelectedNationsInfo()
	{

		StringBuffer selectedNationsBuff = new StringBuffer();

		if (selectedNationIds.size() < getTotalNationsCount())
		{
			int i = 0;
			SelectItem nations[] = getAllNations();
			for (int j = 0; j < nations.length; j++)
			{
				SelectItem nation = nations[j];
				if (selectedNationIds.contains(new Long(nation.getValue())))
				{
					if (i > 0) selectedNationsBuff.append(", ");
					selectedNationsBuff.append(nation.getText());
					i++;
				}
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
	private void updateSelectedExpRegionsInfo()
	{

		StringBuffer selectedExpRegionsBuff = new StringBuffer();

		if (selectedExpRegionIds.size() < getTotalExpRegionsCount())
		{
			int i = 0;
			SelectItem expRegions[] = getAllExpRegions();
			for (int j = 0; j < expRegions.length; j++)
			{
				SelectItem region = expRegions[j];
				if (selectedExpRegionIds.contains(new Long(region.getValue())))
				{
					if (i > 0) selectedExpRegionsBuff.append(", ");
					selectedExpRegionsBuff.append(region.getText());
					i++;
				}
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
	private void updateSelectedImpRegionsInfo()
	{

		StringBuffer selectedImpRegionsBuff = new StringBuffer();
		SelectItem impRegions[] = getAllImpRegions();
		
		allImpRegionseSelected = true;
		
		for (int i = 0; i < impRegions.length; i++)
		{
			SelectItem area = impRegions[i];
			
			if (!area.hasSubItems())
			{
				
				String ids[] = area.getValue().split(IMP_REGIONS_ID_SEPATATOR);
				if (selectedImpRegionIds.contains(new Long((ids[1].substring(1)))))
				{
					if (selectedImpRegionsBuff.length() > 0) selectedImpRegionsBuff.append("<br>");
					selectedImpRegionsBuff.append("<i>");
					selectedImpRegionsBuff.append(area.getText());					
					selectedImpRegionsBuff.append("</i>");
				}
				else
				{
					allImpRegionseSelected = false;
				}
				
			}
			else
			{
				
				int selectedCount = 0;
				SelectItem[] regions = area.getSubItems();
				for (int k = 0; k < regions.length; k++)
				{
					String regionId = regions[k].getValue().substring(1);
					if (selectedImpRegionIds.contains(new Long(regionId))) selectedCount++;
				}
				
				if (selectedCount < regions.length)
					allImpRegionseSelected = false;
				
				if (0 < selectedCount)
				{
					if (selectedImpRegionsBuff.length() > 0) selectedImpRegionsBuff.append("<br>");
					selectedImpRegionsBuff.append("<i>");
					selectedImpRegionsBuff.append(area.getText());					
					selectedImpRegionsBuff.append("</i>");
					selectedImpRegionsBuff.append(": ");
				}
				
				if (selectedCount == regions.length)
				{
					selectedImpRegionsBuff.append("all regions");
				}
				
				else if (0 < selectedCount)
				{
					int l = 0;
					for (int k = 0; k < regions.length; k++)
					{
						SelectItem region = regions[k];
						String regionId = region.getValue().substring(1);
						if (selectedImpRegionIds.contains(new Long(regionId)))
						{
							if (l > 0) selectedImpRegionsBuff.append(", ");
							selectedImpRegionsBuff.append(region.getText());
							l++;
						}
					}
				}
				
			}
		}
		
		if (allImpRegionseSelected)
		{
			selectedImpRegionsBuff.setLength(0);
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
		initDefaultValues(false);
		return null;
	}

	/**
	 * Used by the other beans.
	 * 
	 * @return
	 */
	public TastDbConditions getTimeFrameConditions()
	{
		return timeFrameConditions;
	}

	/**
	 * Used by the other beans.
	 * 
	 * @return
	 */
	public TastDbConditions getGeographicConditions()
	{
		return geographicConditions;
	}

	/**
	 * Used by the other beans.
	 * 
	 * @return
	 */
	public TastDbConditions getConditions()
	{
		TastDbConditions conds = new TastDbConditions(TastDbConditions.AND);
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
				Long lastRegionId = region.getId();
				EstimatesImportArea area = region.getArea();
	
				regionsTemp.clear();
	
				while (area.equals(region.getArea()))
				{
					regionsTemp.add(new SelectItemWithImage(
							region.getName(),
							createImpRegionOnlyId(region.getId().toString()),
							null));
					
					if (++i == regionsCount) break;
					lastRegionId = region.getId();
					region = (EstimatesImportRegion) regionsDb.get(i);
				}
	
				SelectItemWithImage areaItem =
					new SelectItemWithImage(area.getName(), null, null);
	
				areaItem.setSelectable(true);
				areasTemp.add(areaItem);
	
				if (regionsTemp.size() > 1)
				{
					SelectItem[] regionsUi = new SelectItemWithImage[regionsTemp.size()];
					regionsTemp.toArray(regionsUi);
					areaItem.setSubItems(regionsUi);
					areaItem.setValue(
							createImpAreaOnlyId(
									area.getId().toString()));
				}
				else
				{
					areaItem.setValue(
							createImpAreaRegionId(
									area.getId().toString(),
									lastRegionId.toString()));
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
	
	private boolean restoreNationsFromUrl(Map params)
	{
		
		String nationsFromUrl = 
			StringUtils.getFirstElement(
				(String[]) params.get("flag"));
		
		if (nationsFromUrl == null)
			return false;
		
		Set nationsFromUrlSet = 
			StringUtils.toStringSet(
					nationsFromUrl.split("\\."));
		
		List newCheckedNations = new ArrayList();
		
		SelectItem[] nations = getAllNations();
		for (int i = 0; i < nations.length; i++)
		{
			String nationId = nations[i].getValue();
			if (nationsFromUrlSet.contains(nationId))
			{
				newCheckedNations.add(nationId);
			}
		}
		
		if (newCheckedNations.size() == 0)
			return false;
		
		checkedNations = new String[newCheckedNations.size()];
		newCheckedNations.toArray(checkedNations);

		return true;
		
	}
	
	private boolean restoreExpRegionsFromUrl(Map params)
	{
		
		String expRegionsFromUrl = 
			StringUtils.getFirstElement(
				(String[]) params.get("embarkation"));
		
		if (expRegionsFromUrl == null)
			return false;
		
		Set expRegionsFromUrlSet = 
			StringUtils.toStringSet(
					expRegionsFromUrl.split("\\."));
		
		List newCheckedExpRegions = new ArrayList();
		
		SelectItem[] expRegions = getAllExpRegions();
		for (int i = 0; i < expRegions.length; i++)
		{
			String regionId = expRegions[i].getValue();
			if (expRegionsFromUrlSet.contains(regionId))
			{
				newCheckedExpRegions.add(regionId);
			}
		}
		
		if (newCheckedExpRegions.size() == 0)
			return false;
		
		checkedExpRegions = new String[newCheckedExpRegions.size()];
		newCheckedExpRegions.toArray(checkedExpRegions);

		return true;
		
	}
	
	private boolean restoreImpRegionsFromUrl(Map params)
	{
		
		String impRegionsFromUrl = 
			StringUtils.getFirstElement(
				(String[]) params.get("disembarkation"));
		
		if (impRegionsFromUrl == null)
			return false;
		
		Set impRegionsFromUrlSet = 
			StringUtils.toStringSet(
					impRegionsFromUrl.split("\\."));
		
		List newCheckedImpRegions = new ArrayList();
		
		SelectItem[] impAreas = getAllImpRegions();
		for (int i = 0; i < impAreas.length; i++)
		{
			SelectItem area = impAreas[i];
			if (!area.hasSubItems())
			{
				String ids[] = area.getValue().split(IMP_REGIONS_ID_SEPATATOR);
				if (impRegionsFromUrlSet.contains(ids[1].substring(1)))
				{
					newCheckedImpRegions.add(area.getValue());
				}
			}
			else
			{
				boolean selectedAllRegions = true;
				SelectItem[] impRegions = area.getSubItems();
				for (int k = 0; k < impRegions.length; k++)
				{
					String regionId = impRegions[k].getValue();
					if (impRegionsFromUrlSet.contains(regionId.substring(1)))
					{
						newCheckedImpRegions.add(regionId);
					}
					else
					{
						selectedAllRegions = false;
					}
				}
				if (selectedAllRegions)
				{
					newCheckedImpRegions.add(area.getValue());
				}
			}
		}
		
		if (newCheckedImpRegions.size() == 0)
			return false;
		
		checkedImpRegions = new String[newCheckedImpRegions.size()];
		newCheckedImpRegions.toArray(checkedImpRegions);
		
		return true;
		
	}

	public boolean restoreQueryFromUrl(Map params)
	{
		
		initDefaultValues(false);
		
		yearFrom =
			StringUtils.getFirstElement(
				(String[]) params.get("yearFrom"));
		
		yearTo =
			StringUtils.getFirstElement(
				(String[]) params.get("yearTo"));
		
		boolean nationsOk = restoreNationsFromUrl(params);
		boolean expRegionsOk = restoreExpRegionsFromUrl(params);
		boolean impRegionsOk = restoreImpRegionsFromUrl(params);
		
		if (yearFrom == null && yearTo == null &&
				!nationsOk && !expRegionsOk &&!impRegionsOk)
			return false;
		
		changeSelection();
		
		return true;
		
	}
	
	private static String createImpRegionOnlyId(String regionId)
	{
		return "R" + regionId; 
	}

	private static String createImpAreaOnlyId(String areaId)
	{
		return "A" + areaId; 
	}

	private static String createImpAreaRegionId(String areaId, String regionId)
	{
		return "A" + areaId + IMP_REGIONS_ID_SEPATATOR + "R" + regionId; 
	}
	
	public int getTotalNationsCount()
	{
		SelectItem[] nations = getAllNations(); 
		return nations == null ? 0 : nations.length;
	}
	
	public int getTotalExpRegionsCount()
	{
		SelectItem[] expRegions = getAllExpRegions(); 
		return expRegions == null ? 0 : expRegions.length;
	}

	public int getTotalImpRegionsCount()
	{
		SelectItem[] impRegions = getAllImpRegions(); 
		return impRegions == null ? 0 : impRegions.length;
	}

	public boolean isAllNationsSelected()
	{
		return getTotalNationsCount() == selectedNationIds.size();
	}

	public boolean isNoNationSelected()
	{
		return 0 == selectedNationIds.size();
	}

	public boolean isAllExpRegionsSelected()
	{
		return getTotalExpRegionsCount() == selectedExpRegionIds.size();
	}

	public boolean isNoExpRegionSelected()
	{
		return 0 == selectedExpRegionIds.size();
	}

	public boolean isAllImpRegionsSelected()
	{
		return allImpRegionseSelected;
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
		this.yearFrom = yearFrom;
	}

	public String getYearTo()
	{
		return yearTo;
	}

	public void setYearTo(String yearTo)
	{
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
	
	public String getTimeFrameExtentHint()
	{
		return
			"Note: " +
			"The full extent of time covered by estimates is " +
			minYear + " &ndash; " + maxYear;
	}

}