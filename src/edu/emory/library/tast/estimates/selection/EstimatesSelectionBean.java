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
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesArea;
import edu.emory.library.tast.dm.EstimatesExportRegion;
import edu.emory.library.tast.dm.EstimatesImportArea;
import edu.emory.library.tast.dm.EstimatesImportRegion;
import edu.emory.library.tast.dm.EstimatesNation;
import edu.emory.library.tast.dm.EstimatesRegion;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.ConversionUtils;
import edu.emory.library.tast.util.StringUtils;


/**
 * This bean is responsible for managing the query on the left hand side in the
 * estimates. It does not do the actual searching, it is only connected to the
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

	private static final String PAGE_URL = "/assessment/estimates.faces";
	private static final String URL_IMP_REGIONS_PARAM_NAME = "embarkation";
	private static final String URL_EXP_REGIONS_PARAM_NAME = "disembarkation";

	private static final String REGIONS_ID_SEPATATOR = "_";
	private static final String IMP_REGIONS_CACHE_ID = "impRegions";
	private static final String EXP_REGIONS_CACHE_ID = "expRegions";
	
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
	private Set selectedExpAreaIds;
	private Set selectedExpRegionIds;
	private Set selectedImpAreaIds;
	private Set selectedImpRegionIds;
	private String selectedNationsAsText;
	private String selectedExpRegionsAsText;
	private String selectedImpRegionsAsText;
	private boolean allImpRegionseSelected = false;
	private boolean allExpRegionseSelected = false;

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

		Session sess = HibernateConn.getSession();
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
	 * General method, which changes {@link #checkedImpRegions} by the given
	 * list of regions from the database. Now used only during initialisation
	 * and when user presses the Reset button.
	 * 
	 * @param nations
	 *            List of nations.
	 */
	private void checkRegions(List regions, boolean isImportRegions)
	{

		int i = 0;
		int regionsCount = regions.size();

		List tempIds = new ArrayList();
		
		while (i < regionsCount)
		{

			EstimatesRegion region = (EstimatesRegion) regions.get(i);
			EstimatesArea area = region.getAbstractArea();
			Long lastRegionId = region.getId();

			int regionsInArea = 0;
			while (area.equals(region.getAbstractArea()))
			{
				lastRegionId = region.getId();
				tempIds.add(createRegionOnlyId(lastRegionId.toString()));
				regionsInArea++;
				if (++i == regionsCount) break;
				region = (EstimatesRegion) regions.get(i);
			}

			if (regionsInArea > 1)
			{
				tempIds.add(createAreaOnlyId(area.getId().toString()));
			}
			else
			{
				tempIds.add(createAreaRegionId(area.getId().toString(), lastRegionId.toString()));
			}

		}

		String[] checkedRregions = new String[tempIds.size()];;
		tempIds.toArray(checkedRregions);
		
		if (isImportRegions)
			checkedImpRegions = checkedRregions;
		else
			checkedExpRegions = checkedRregions;
		
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
		checkRegions(allExpRegions, false);
		checkRegions(allImpRegions, true);

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
		selectedExpAreaIds = new HashSet();
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
			String[] ids = checkedExpRegions[i].split(REGIONS_ID_SEPATATOR);
			for (int j = 0; j < ids.length; j++)
			{
				Long id = new Long(ids[j].substring(1));
				if (ids[j].startsWith("A"))
				{
					selectedExpAreaIds.add(id);
				}
				else
				{
					selectedExpRegionIds.add(id);
					conditionExpRegions.addCondition(regionExpIdAttr, id, TastDbConditions.OP_EQUALS);
				}
			}
		}
		
		Attribute regionImpIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("impRegion"),
				EstimatesImportRegion.getAttribute("id") });

		for (int i = 0; i < checkedImpRegions.length; i++)
		{
			String[] ids = checkedImpRegions[i].split(REGIONS_ID_SEPATATOR);
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
		updateSelectedRegionsInfo(true);
		updateSelectedRegionsInfo(false);
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

	private void updateSelectedRegionsInfo(boolean isImportRegions)
	{

		StringBuffer selectedRegionsBuff = new StringBuffer();
		SelectItem impRegions[] = getAllRegions(isImportRegions);
		Set selectedRegionIds = isImportRegions ? selectedImpRegionIds : selectedExpRegionIds;
		
		boolean allRegionsSelected = true;
		
		for (int i = 0; i < impRegions.length; i++)
		{
			SelectItem area = impRegions[i];
			
			if (!area.hasSubItems())
			{
				
				String ids[] = area.getValue().split(REGIONS_ID_SEPATATOR);
				if (selectedRegionIds.contains(new Long((ids[1].substring(1)))))
				{
					if (selectedRegionsBuff.length() > 0) selectedRegionsBuff.append("<br>");
					selectedRegionsBuff.append("<i>");
					selectedRegionsBuff.append(area.getText());					
					selectedRegionsBuff.append("</i>");
				}
				else
				{
					allRegionsSelected = false;
				}
				
			}
			else
			{
				
				int selectedCount = 0;
				SelectItem[] regions = area.getSubItems();
				for (int k = 0; k < regions.length; k++)
				{
					String regionId = regions[k].getValue().substring(1);
					if (selectedRegionIds.contains(new Long(regionId))) selectedCount++;
				}
				
				if (selectedCount < regions.length)
					allRegionsSelected = false;
				
				if (0 < selectedCount)
				{
					if (selectedRegionsBuff.length() > 0) selectedRegionsBuff.append("<br>");
					selectedRegionsBuff.append("<i>");
					selectedRegionsBuff.append(area.getText());					
					selectedRegionsBuff.append("</i>");
					selectedRegionsBuff.append(": ");
				}
				
				if (selectedCount == regions.length)
				{
					selectedRegionsBuff.append("all regions");
				}
				
				else if (0 < selectedCount)
				{
					int l = 0;
					for (int k = 0; k < regions.length; k++)
					{
						SelectItem region = regions[k];
						String regionId = region.getValue().substring(1);
						if (selectedRegionIds.contains(new Long(regionId)))
						{
							if (l > 0) selectedRegionsBuff.append(", ");
							selectedRegionsBuff.append(region.getText());
							l++;
						}
					}
				}
				
			}
		}
		
		if (allRegionsSelected)
		{
			selectedRegionsBuff.setLength(0);
			selectedRegionsBuff.append("<i>all</i>");
		}

		if (isImportRegions)
		{
			selectedImpRegionsAsText = selectedRegionsBuff.toString();
			allImpRegionseSelected = allRegionsSelected;
		}
		else
		{
			selectedExpRegionsAsText = selectedRegionsBuff.toString();
			allExpRegionseSelected = allRegionsSelected;
		}
		
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
	
	private SelectItem[] getAllRegions(boolean isImpRegions)
	{
		
		String cacheId = SimpleCache.ESTIMATES_PREFIX + (
				isImpRegions ? IMP_REGIONS_CACHE_ID : EXP_REGIONS_CACHE_ID);
		
		SelectItemWithImage[] areasUi = (SelectItemWithImage[]) SimpleCache.get(cacheId);
		
		if (areasUi == null)
		{
		
			Session sess = HibernateConn.getSession();
			Transaction transaction = sess.beginTransaction();
	
			List regionsDb = isImpRegions ?
					EstimatesImportRegion.loadAll(sess) :
						EstimatesExportRegion.loadAll(sess);
	
			List areasTemp = new ArrayList();
			List regionsTemp = new ArrayList();
	
			int i = 0;
			int regionsCount = regionsDb.size();
	
			while (i < regionsCount)
			{
	
				EstimatesRegion region = (EstimatesRegion) regionsDb.get(i);
				Long lastRegionId = region.getId();
				EstimatesArea area = region.getAbstractArea();
	
				regionsTemp.clear();
	
				while (area.equals(region.getAbstractArea()))
				{
					regionsTemp.add(new SelectItemWithImage(
							region.getName(),
							createRegionOnlyId(region.getId().toString()),
							null));
					
					if (++i == regionsCount) break;
					lastRegionId = region.getId();
					region = (EstimatesRegion) regionsDb.get(i);
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
							createAreaOnlyId(
									area.getId().toString()));
				}
				else
				{
					areaItem.setValue(
							createAreaRegionId(
									area.getId().toString(),
									lastRegionId.toString()));
				}
	
			}
	
			transaction.commit();
			sess.close();
			
			areasUi = new SelectItemWithImage[areasTemp.size()];
			areasTemp.toArray(areasUi);
			
			SimpleCache.set(cacheId, areasUi);
			
		}

		return areasUi;
		
	}
	
	public SelectItem[] getAllExpRegions()
	{
		return getAllRegions(false);
	}

	public SelectItem[] getAllImpRegions()
	{
		return getAllRegions(true);
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
		
			Session sess = HibernateConn.getSession();
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
			url.append(URL_IMP_REGIONS_PARAM_NAME).append("=");
			url.append(StringUtils.join(".", selectedExpRegionIds));
			queryStringParams++;
		}
		
		if (!isNoImpRegionSelected() && !isAllImpRegionsSelected())
		{
			if (queryStringParams > 0) url.append("&");
			url.append(URL_EXP_REGIONS_PARAM_NAME).append("=");
			url.append(StringUtils.join(".", selectedImpRegionIds));
		}
		
		lastPermLink =
			AppConfig.getConfiguration().getString(AppConfig.SITE_URL) +
			PAGE_URL + (queryStringParams > 0 ? url.toString() : "");
		
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
	
	private boolean restoreRegionsFromUrl(Map params, boolean isImportRegions)
	{
		
		String regionsFromUrl = 
			StringUtils.getFirstElement(
				(String[]) params.get(isImportRegions ?
						URL_IMP_REGIONS_PARAM_NAME : URL_EXP_REGIONS_PARAM_NAME));
		
		if (regionsFromUrl == null)
			return false;
		
		Set regionsFromUrlSet = 
			StringUtils.toStringSet(
					regionsFromUrl.split("\\."));
		
		List newCheckedRegions = new ArrayList();
		
		SelectItem[] areas = getAllRegions(isImportRegions);
		for (int i = 0; i < areas.length; i++)
		{
			SelectItem area = areas[i];
			if (!area.hasSubItems())
			{
				String ids[] = area.getValue().split(REGIONS_ID_SEPATATOR);
				if (regionsFromUrlSet.contains(ids[1].substring(1)))
				{
					newCheckedRegions.add(area.getValue());
				}
			}
			else
			{
				boolean selectedAllRegions = true;
				SelectItem[] impRegions = area.getSubItems();
				for (int k = 0; k < impRegions.length; k++)
				{
					String regionId = impRegions[k].getValue();
					if (regionsFromUrlSet.contains(regionId.substring(1)))
					{
						newCheckedRegions.add(regionId);
					}
					else
					{
						selectedAllRegions = false;
					}
				}
				if (selectedAllRegions)
				{
					newCheckedRegions.add(area.getValue());
				}
			}
		}
		
		if (newCheckedRegions.size() == 0)
			return false;
		
		String[] checkedRegionsArr = new String[newCheckedRegions.size()];
		newCheckedRegions.toArray(checkedRegionsArr);
		
		if (isImportRegions)
			checkedImpRegions = checkedRegionsArr;
		else
			checkedExpRegions = checkedRegionsArr;
		
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
		boolean expRegionsOk = restoreRegionsFromUrl(params, false);
		boolean impRegionsOk = restoreRegionsFromUrl(params, true);
		
		if (yearFrom == null && yearTo == null &&
				!nationsOk && !expRegionsOk &&!impRegionsOk)
			return false;
		
		changeSelection();
		
		return true;
		
	}
	
	private static String createRegionOnlyId(String regionId)
	{
		return "R" + regionId; 
	}

	private static String createAreaOnlyId(String areaId)
	{
		return "A" + areaId; 
	}

	private static String createAreaRegionId(String areaId, String regionId)
	{
		return "A" + areaId + REGIONS_ID_SEPATATOR + "R" + regionId; 
	}
	
	public int getTotalNationsCount()
	{
		SelectItem[] nations = getAllNations(); 
		return nations == null ? 0 : nations.length;
	}
	
	public int getTotalExpRegionsCount()
	{
		SelectItem[] expRegions = getAllRegions(false); 
		return expRegions == null ? 0 : expRegions.length;
	}

	public int getTotalImpRegionsCount()
	{
		SelectItem[] impRegions = getAllRegions(true); 
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
		return allExpRegionseSelected;
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
			"The full extent of time covered by estimates is " +
			minYear + " &ndash; " + maxYear;
	}

}