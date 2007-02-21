package edu.emory.library.tast.estimates.selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesExportRegion;
import edu.emory.library.tast.dm.EstimatesImportArea;
import edu.emory.library.tast.dm.EstimatesImportRegion;
import edu.emory.library.tast.dm.EstimatesNation;
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
		
		totalNationsCount = nations.size();
		checkedNations = new String[nations.size()];
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			EstimatesNation nation = (EstimatesNation) iter.next();
			checkedNations[i++] = String.valueOf(nation.getId());
		}

	}

	private void selectExportRegions(List regions)
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

	private void selectImportRegions(List regions)
	{

		int i = 0;
		int regionsCount = regions.size();
		
		List tempIds = new ArrayList();

		while(i < regionsCount)
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
				tempIds.add(
						"A" + area.getId());
			}
			else
			{
				tempIds.add(
						"A" + area.getId() + "_" +
						"R" + lastRegionId);
			}
			
		}

		checkedImpRegions = new String[tempIds.size()];
		tempIds.toArray(checkedImpRegions);

	}

	private void selectAllNationsAndRegions(Session sess)
	{
		
		List allNations = EstimatesNation.loadAll(sess);
		List allExpRegions = EstimatesExportRegion.loadAll(sess);
		List allImpRegions = EstimatesImportRegion.loadAll(sess);
		
		totalNationsCount = allNations.size(); 
		totalExpRegionsCount = allExpRegions.size(); 
		totalImpRegionsCount = allImpRegions.size();
		
		selectNations(allNations);
		selectExportRegions(allExpRegions);
		selectImportRegions(allImpRegions);
		
	}

	public List loadSelectedNations(Session session)
	{
		
		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedNationIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesNation.getAttribute("id"),
					iter.next(), Conditions.OP_EQUALS);
		
		QueryValue query = new QueryValue("edu.emory.library.tast.dm.EstimatesNation", cond);
		query.setOrderBy(new Attribute[] {EstimatesNation.getAttribute("order")});
		query.setOrder(QueryValue.ORDER_ASC);
		
		return query.executeQueryList(session);
	
	}
	
	public List loadSelectedExpRegions(Session session)
	{

		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedExpRegionIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesExportRegion.getAttribute("id"),
					iter.next(), Conditions.OP_EQUALS);
		
		QueryValue query = new QueryValue("edu.emory.library.tast.dm.EstimatesExportRegion", cond);
		query.setOrderBy(new Attribute[] {EstimatesExportRegion.getAttribute("order")});
		query.setOrder(QueryValue.ORDER_ASC);
		
		return query.executeQueryList(session);

	}

	public List loadSelectedImpRegions(Session session)
	{
		
		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedImpRegionIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesImportRegion.getAttribute("id"),
					iter.next(), Conditions.OP_EQUALS);
		
		QueryValue query = new QueryValue("edu.emory.library.tast.dm.EstimatesImportRegion", cond);
		query.setOrderBy(new Attribute[] {EstimatesImportRegion.getAttribute("order")});
		query.setOrder(QueryValue.ORDER_ASC);
		
		return query.executeQueryList(session);

	}

	public List loadSelectedImpAreas(Session session)
	{
		
		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedImpAreaIds.iterator(); iter.hasNext();)
			cond.addCondition(EstimatesImportArea.getAttribute("id"),
					iter.next(), Conditions.OP_EQUALS);
		
		QueryValue query = new QueryValue("edu.emory.library.tast.dm.EstimatesImportArea", cond);
		query.setOrderBy(new Attribute[] {EstimatesImportRegion.getAttribute("order")});
		query.setOrder(QueryValue.ORDER_ASC);
		
		return query.executeQueryList(session);

	}
	
	public boolean isAllNationsSelected()
	{
		return totalNationsCount == selectedNationIds.size();
	}

	public boolean isAllExpRegionsSelected()
	{
		return totalExpRegionsCount == selectedExpRegionIds.size();
	}

	public boolean isAllImpRegionsSelected()
	{
		return totalImpRegionsCount == selectedImpRegionIds.size();
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

	private SelectItem[] loadAllNationsToUi()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		List nationsDb = EstimatesNation.loadAll(sess);
		SelectItem[] nationsUi = new SelectItem[nationsDb.size()];

		int i = 0;
		for (Iterator iter = nationsDb.iterator(); iter.hasNext();)
		{
			EstimatesNation nation = (EstimatesNation) iter.next();
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
		
		List regionsDb = EstimatesExportRegion.loadAll(sess); 
		SelectItem[] regionsUi = new SelectItem[regionsDb.size()];
		
		int i = 0;
		for (Iterator iter = regionsDb.iterator(); iter.hasNext();)
		{
			EstimatesExportRegion region = (EstimatesExportRegion) iter.next();
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
		
		List regionsDb = EstimatesImportRegion.loadAll(sess);
		
		List areasTemp = new ArrayList();
		List regionsTemp = new ArrayList();

		int i = 0;
		int regionsCount = regionsDb.size();

		while(i < regionsCount)
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
			
			SelectItemWithImage areaItem = new SelectItemWithImage(
					area.getName(),
					null,
					"regions/area-" + area.getId() + ".png");

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
				areaItem.setValue("A" + area.getId() + "_" + singleRegion.getValue());
			}
			
		}

		transaction.commit();
		sess.close();
		
		SelectItemWithImage[] areasUi = new SelectItemWithImage[areasTemp.size()];
		areasTemp.toArray(areasUi);

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
		selectedImpAreaIds = new HashSet();
		
		Attribute nationIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("nation"),
				EstimatesNation.getAttribute("id")});
		
		for (int i = 0; i < checkedNations.length; i++)
		{
			Long nationId = new Long(checkedNations[i]);
			selectedNationIds.add(nationId);
			conditionNations.addCondition(nationIdAttr, nationId, Conditions.OP_EQUALS);
		}
		
		Attribute regionExpIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("expRegion"),
				EstimatesExportRegion.getAttribute("id")});

		for (int i = 0; i < checkedExpRegions.length; i++)
		{
			Long regionId = new Long(checkedExpRegions[i]);
			selectedExpRegionIds.add(regionId);
			conditionExpRegions.addCondition(regionExpIdAttr, regionId, Conditions.OP_EQUALS);
		}

		Attribute regionImpIdAttr = new SequenceAttribute(new Attribute[] {
				Estimate.getAttribute("impRegion"),
				EstimatesImportRegion.getAttribute("id")});

		for (int i = 0; i < checkedImpRegions.length; i++)
		{
			String[] ids = checkedImpRegions[i].split("_");
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

					region = (EstimatesImportRegion) iter.next();
					
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
	
	public String resetSelection()
	{
		initDefaultValues();
		return null;
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