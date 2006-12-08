package edu.emory.library.tast.estimates.selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.ui.SelectItem;
import edu.emory.library.tast.ui.SelectItemWithImage;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesSelectionBean
{
	
	private static final int TIME_SPAN_INITIAL_TO = 1900;
	private static final int TIME_SPAN_INITIAL_FROM = 1500;
	
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
	
	private String selectedNationsAsText;
	private String selectedExpRegionsAsText;
	private String selectedImpRegionsAsText;
	
	private int yearFrom = TIME_SPAN_INITIAL_FROM;
	private int yearTo = TIME_SPAN_INITIAL_TO;

	public EstimatesSelectionBean()
	{
		initDefaultValues();
	}
	
	private void initDefaultValues()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		selectAllNationsAndRegions(sess);
		initTimeFrame(sess);
		
		transaction.commit();
		sess.close();

		changeGeographicSelection();
		changeTimeFrameSelection();

	}
	
	private void initTimeFrame(Session sess)
	{

		QueryValue query = new QueryValue("edu.emory.library.tast.dm.Estimate");
		
		query.addPopulatedAttribute(
				new FunctionAttribute("min",
						new Attribute[] {Estimate.getAttribute("year")}));

		query.addPopulatedAttribute(
				new FunctionAttribute("max",
						new Attribute[] {Estimate.getAttribute("year")}));
		
		Object[] result = query.executeQuery();
		Object[] firsrRow = (Object[]) result[0]; 
			
		yearFrom = ((Integer) firsrRow[0]).intValue();
		yearTo = ((Integer) firsrRow[1]).intValue();
	
	}
	
	private void selectNations(List nations)
	{
		
		selectedNationIds = new HashSet();
		totalNationsCount = nations.size();
		checkedNations = new String[nations.size()];
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			selectedNationIds.add(nation.getId());
			checkedNations[i++] = String.valueOf(nation.getId());
		}

	}

	private void selectExportRegions(List regions)
	{

		selectedExpRegionIds = new HashSet();
		totalExpRegionsCount = regions.size();
		checkedExpRegions = new String[regions.size()];
		
		int i = 0;
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			selectedExpRegionIds.add(region.getId());
			checkedExpRegions[i] = String.valueOf(region.getId());
			i++;
		}

	}

	private void selectImportRegions(List regions)
	{
		
		selectedImpRegionIds = new HashSet();
		totalImpRegionsCount = regions.size();
		
		Set impAreaIds = new HashSet();
		Set impAllIds = new HashSet();
		
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			String regionIdStr = String.valueOf(region.getId());
			String areaIdStr = "area" + region.getArea().getId();
			selectedImpRegionIds.add(region.getId());
			if (impAreaIds.add(areaIdStr)) impAllIds.add(areaIdStr);
			impAllIds.add(regionIdStr);
		}
		
		checkedImpRegions = new String[impAllIds.size()];
		impAllIds.toArray(checkedImpRegions);

		expandedImpRegions = new String[impAreaIds.size()];
		impAreaIds.toArray(expandedImpRegions);

	}

	private void selectAllNationsAndRegions(Session sess)
	{
		
		List allNations = loadAllNations(sess);
		List allExpRegions = loadAllRegions(sess, true);
		List allImpRegions = loadAllRegions(sess, false);
		
		selectNations(allNations);
		selectExportRegions(allExpRegions);
		selectImportRegions(allImpRegions);
		
	}

	public List loadSelectedNations(Session session)
	{
		
		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedNationIds.iterator(); iter.hasNext();)
			cond.addCondition(Nation.getAttribute("id"),
					iter.next(), Conditions.OP_EQUALS);
		
		QueryValue query = new QueryValue("edu.emory.library.tast.dm.Nation", cond);
		query.setOrderBy(new Attribute[] {Nation.getAttribute("order")});
		query.setOrder(QueryValue.ORDER_ASC);
		
		return query.executeQueryList(session);
	
	}
	
	private List loadSelectedRegions(Session session, boolean america)
	{
		
		Conditions cond = new Conditions(Conditions.JOIN_OR);
		Set selectedIds = america ?  selectedImpRegionIds : selectedExpRegionIds;
		
		for (Iterator iter = selectedIds.iterator(); iter.hasNext();)
		{
			Long regionId = (Long) iter.next();
			cond.addCondition(Region.getAttribute("id"), regionId, Conditions.OP_EQUALS);
		}
		
		QueryValue query = new QueryValue("edu.emory.library.tast.dm.Region", cond);
		query.setOrderBy(new Attribute[] {Nation.getAttribute("order")});
		query.setOrder(QueryValue.ORDER_ASC);
		
		return query.executeQueryList(session);
	
	}

	public List loadSelectedExpRegions(Session session)
	{
		return loadSelectedRegions(session, false);
	}

	public List loadSelectedImpRegions(Session session)
	{
		return loadSelectedRegions(session, true);
	}

	public List loadAllNations(Session session)
	{
		
		QueryValue query = new QueryValue("edu.emory.library.tast.dm.Nation");
		query.setOrderBy(new Attribute[] {Nation.getAttribute("order")});
		query.setOrder(QueryValue.ORDER_ASC);
		
		return query.executeQueryList(session);
		
	}

	private List loadAllRegions(Session session, boolean onlyAfrican)
	{
		
		Conditions cond = new Conditions();
		if (onlyAfrican)
			cond.addCondition(new SequenceAttribute(new Attribute[] {
					Region.getAttribute("area"),
					Area.getAttribute("america")}),
					new Boolean(false), Conditions.OP_EQUALS);

		cond.addCondition(new SequenceAttribute(new Attribute[] {
				Region.getAttribute("area"),
				Area.getAttribute("id")}),
				null, Conditions.OP_NOT_EQUALS);
		
		QueryValue query = new QueryValue(
				"Region", cond);

		query.setOrder(QueryValue.ORDER_ASC);
		query.setOrderBy(new Attribute[] {
				Region.getAttribute("order")});
		
		return query.executeQueryList(session);

	}
	
	public List loadAllExportRegions(Session session)
	{
		return loadAllRegions(session, true);
	}

	public List loadAllImportRegions(Session session)
	{
		return loadAllRegions(session, false);
	}

	private SelectItem[] loadAllNationsToUi()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		List nationsDb = loadAllNations(sess);
		SelectItem[] nationsUi = new SelectItem[nationsDb.size()];

		int i = 0;
		for (Iterator iter = nationsDb.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			nationsUi[i++] = new SelectItem(
					nation.getName(),
					String.valueOf(nation.getId()));
		}
		
		transaction.commit();
		sess.close();
		
		return nationsUi; 
		
	}
	
	private SelectItem[] loadExportRegionsToUi()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		List regionsDb = loadAllRegions(sess, true); 
		SelectItem[] regionsUi = new SelectItem[regionsDb.size()];
		
		int i = 0;
		for (Iterator iter = regionsDb.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			regionsUi[i++] = new SelectItem(
					region.getName(),
					String.valueOf(region.getId()));
		}

		transaction.commit();
		sess.close();
		
		return regionsUi; 
		
	}
	
	private SelectItem[] loadImportRegionsToUi()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		List regionsDb = loadAllRegions(sess, false); 
		
		int lastAreaId = -1;
		int areasCount = 0;
		for (Iterator iter = regionsDb.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			int areaId = region.getArea().getId().intValue();
			if (lastAreaId != areaId)
			{
				lastAreaId = areaId;
				areasCount++;
			}
		}

		SelectItemWithImage[] areasUi = new SelectItemWithImage[areasCount];
		List regionsTemp = new ArrayList();

		lastAreaId = -1;
		int areaIndex = -1;
		for (Iterator iter = regionsDb.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			int areaId = region.getArea().getId().intValue();
			
			if (lastAreaId != areaId)
			{
				
				if (areaIndex != -1)
				{
					SelectItemWithImage[] regionsUi = new SelectItemWithImage[regionsTemp.size()];
					regionsTemp.toArray(regionsUi);
					areasUi[areaIndex].setSubItems(regionsUi);
				}
				
				areaIndex++;
				lastAreaId = areaId;
				regionsTemp.clear();
				
				SelectItemWithImage areaItem = new SelectItemWithImage(
						region.getArea().getName(),
						"area" + areaId,
						"regions/area-" + areaId + ".png");

				areaItem.setSelectable(true);
				areasUi[areaIndex] = areaItem;
				
			}
		
			regionsTemp.add(new SelectItemWithImage(
					region.getName(),
					String.valueOf(region.getId()),
					"regions/region-" + region.getId() + ".png"));

		}
		
		if (areaIndex != -1)
		{
			SelectItemWithImage[] regionsUi = new SelectItemWithImage[regionsTemp.size()];
			regionsTemp.toArray(regionsUi);
			areasUi[areaIndex].setSubItems(regionsUi);
		}

		transaction.commit();
		sess.close();
		
		return areasUi; 

	}
	
	public String changeGeographicSelection()
	{
		
		Conditions conditionNations = new Conditions(Conditions.JOIN_OR);
		Conditions conditionExpRegions = new Conditions(Conditions.JOIN_OR);
		Conditions conditionImpRegions = new Conditions(Conditions.JOIN_OR);
		
		geographicConditions = new Conditions(Conditions.JOIN_AND);
		geographicConditions.addCondition(conditionNations);
		geographicConditions.addCondition(conditionExpRegions);
		geographicConditions.addCondition(conditionImpRegions);

		selectedNationIds = new HashSet();
		selectedExpRegionIds = new HashSet();
		selectedImpRegionIds = new HashSet();
		
		Attribute nationIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("nation"),
				Nation.getAttribute("id")});
		
		for (int i = 0; i < checkedNations.length; i++)
		{
			Long nationId = new Long(checkedNations[i]);
			selectedNationIds.add(nationId);
			conditionNations.addCondition(nationIdAttr, nationId, Conditions.OP_EQUALS);
		}
		
		Attribute regionExpIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("expRegion"),
				Region.getAttribute("id")});

		for (int i = 0; i < checkedExpRegions.length; i++)
		{
			Long regionId = new Long(checkedExpRegions[i]);
			selectedExpRegionIds.add(regionId);
			conditionExpRegions.addCondition(regionExpIdAttr, regionId, Conditions.OP_EQUALS);
		}

		Attribute regionImpIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("impRegion"),
				Region.getAttribute("id")});

		for (int i = 0; i < checkedImpRegions.length; i++)
		{
			if (!checkedImpRegions[i].startsWith("area"))
			{
				Long regionId = new Long(checkedImpRegions[i]);
				selectedImpRegionIds.add(regionId);
				conditionImpRegions.addCondition(regionImpIdAttr, regionId, Conditions.OP_EQUALS);
			}
		}

		updateSelectionInfo();

		return null;

	}

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

	private void updateSelectedNationsInfo(Session sess)
	{
		
		StringBuffer selectedNationsBuff = new StringBuffer(); 

		if (totalNationsCount > selectedNationIds.size())
		{
			int i = 0;
			List selectedNations = loadSelectedNations(sess);
			for (Iterator iter = selectedNations.iterator(); iter.hasNext();)
			{
				Nation nation = (Nation) iter.next();
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

	private void updateSelectedExpRegionsInfo(Session sess)
	{

		StringBuffer selectedExpRegionsBuff = new StringBuffer(); 

		if (totalExpRegionsCount > selectedExpRegionIds.size())
		{
			int i = 0;
			List selectedExpRegions = loadSelectedExpRegions(sess);
			for (Iterator iter = selectedExpRegions.iterator(); iter.hasNext();)
			{
				Region region = (Region) iter.next();
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

	private void updateSelectedImpRegionsInfo(Session sess)
	{

		StringBuffer selectedImpRegionsBuff = new StringBuffer();
		
		if (totalImpRegionsCount > selectedImpRegionIds.size())
		{
			
			int i = 0;
			
			List selectedImpRegions = loadAllImportRegions(sess);
			
			Iterator iter = selectedImpRegions.iterator();
			Region region = (Region) iter.next();
			int lastAreaId = region.getArea().getId().intValue();
			String lastAreaName = region.getArea().getName();
			
			StringBuffer selectedImpRegionsInAreaBuff = new StringBuffer(); 
			
			while (iter.hasNext())
			{

				int j = 0;
				boolean allSelected = true;
				boolean noSelected = true;
				
				lastAreaName = region.getArea().getName();

				selectedImpRegionsInAreaBuff.setLength(0);
				
				while (iter.hasNext())
				{

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

					region = (Region) iter.next();
					
					int areaId = region.getArea().getId().intValue();
					if (lastAreaId != areaId)
					{
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
		}
		else
		{
			selectedImpRegionsBuff.append("<i>all</i>");
		}
		
		selectedImpRegionsAsText = selectedImpRegionsBuff.toString();

	}
	
	public String changeTimeFrameSelection()
	{

		timeFrameConditions = new Conditions(Conditions.JOIN_AND);
		
		timeFrameConditions.addCondition(
				Estimate.getAttribute("year"),
				new Integer(yearFrom),
				Conditions.OP_GREATER_OR_EQUAL);
		
		timeFrameConditions.addCondition(
				Estimate.getAttribute("year"),
				new Integer(yearTo),
				Conditions.OP_SMALLER_OR_EQUAL);
		
		return null;

	}
	
	public Conditions getTimeFrameConditions()
	{
		return timeFrameConditions;
	}

	public Conditions getGeographicConditions()
	{
		return geographicConditions;
	}

	public Conditions getConditions()
	{
		Conditions conds = new Conditions(Conditions.JOIN_AND);
		conds.addCondition(geographicConditions);
		conds.addCondition(timeFrameConditions);
		return conds;
	}

	public SelectItem[] getAllExpRegions()
	{
		return loadExportRegionsToUi();
	}

	public SelectItem[] getAllImpRegions()
	{
		return loadImportRegionsToUi();
	}
	
	public SelectItem[] getAllNations()
	{
		return loadAllNationsToUi();
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

	public String getSelectedExpRegionsAsText()
	{
		return selectedExpRegionsAsText;
	}

	public void setSelectedExpRegionsAsText(String selectedExpRegionsAsText)
	{
		this.selectedExpRegionsAsText = selectedExpRegionsAsText;
	}

	public String getSelectedImpRegionsAsText()
	{
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

	public int getYearFrom()
	{
		return yearFrom;
	}

	public void setYearFrom(int yearFrom)
	{
		this.yearFrom = yearFrom;
	}

	public int getYearTo()
	{
		return yearTo;
	}

	public void setYearTo(int yearTo)
	{
		this.yearTo = yearTo;
	}

}