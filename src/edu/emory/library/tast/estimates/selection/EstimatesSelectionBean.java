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
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.ui.SelectItem;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesSelectionBean
{
	
	private Conditions conditions;

	private String[] checkedNations;
	private String[] checkedExpRegions;
	private String[] checkedImpRegions;
	private String[] expandedExpRegions;
	private String[] expandedImpRegions;
	
	private Set selectedNationIds;
	private Set selectedExpRegionIds;
	private Set selectedImpRegionIds;
	
	public EstimatesSelectionBean()
	{
		selectAllNationsAndRegions();
	}
	
	private void selectAllNationsAndRegions()
	{
		
		int i;
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		List allNations = loadAllNations(sess);
		List allExpRegions = loadAllRegions(sess, false);
		List allImpRegions = loadAllRegions(sess, true);
		
		i = 0;
		selectedNationIds = new HashSet();
		checkedNations = new String[allNations.size()];
		for (Iterator iter = allNations.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			selectedNationIds.add(new Long(nation.getId()));
			checkedNations[i++] = String.valueOf(nation.getId());
		}
		
		i = 0;
		selectedExpRegionIds = new HashSet();
		checkedExpRegions = new String[allExpRegions.size()];
		Set expandedExpAreas = new HashSet();
		for (Iterator iter = allExpRegions.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			selectedExpRegionIds.add(region.getId());
			checkedExpRegions[i] = String.valueOf(region.getId());
			expandedExpAreas.add("area" + region.getArea().getId());
			i++;
		}
		
		expandedExpRegions = new String[expandedExpAreas.size()];
		expandedExpAreas.toArray(expandedExpRegions);
		
		i = 0;
		selectedImpRegionIds = new HashSet();
		checkedImpRegions = new String[allImpRegions.size()];
		Set expandedImpAreas = new HashSet();
		for (Iterator iter = allImpRegions.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			selectedImpRegionIds.add(region.getId());
			checkedImpRegions[i] = String.valueOf(region.getId());
			expandedImpAreas.add("area" + region.getArea().getId());
			i++;
		}
		
		expandedImpRegions = new String[expandedImpAreas.size()];
		expandedImpAreas.toArray(expandedImpRegions);

		transaction.commit();
		sess.close();
		
		changeSelection();
		
	}
	
	public List loadSelectedNations(Session session)
	{
		
		Conditions cond = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = selectedNationIds.iterator(); iter.hasNext();)
			cond.addCondition(Nation.getAttribute("id"),
					iter.next(), Conditions.OP_EQUALS);
		
		QueryValue query = new QueryValue("edu.emory.library.tast.dm.Nation", cond);
		query.setOrderBy(new Attribute[] {Nation.getAttribute("name")});
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
		query.setOrderBy(new Attribute[] {Nation.getAttribute("name")});
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
		query.setOrderBy(new Attribute[] {Nation.getAttribute("name")});
		query.setOrder(QueryValue.ORDER_ASC);
		
		return query.executeQueryList(session);
		
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

	private List loadAllRegions(Session session, boolean america)
	{
		
		Attribute areaAmericaAtrribute = new SequenceAttribute(new Attribute[] {
				Region.getAttribute("area"),
				Area.getAttribute("america")});
		
//		Attribute areaNameAtrribute = new SequenceAttribute(new Attribute[] {
//				Region.getAttribute("area"),
//				Area.getAttribute("name")});

		Conditions cond = new Conditions();
		cond.addCondition(areaAmericaAtrribute,
				new Boolean(america), Conditions.OP_EQUALS);
		
		QueryValue query = new QueryValue("Region", cond);
		query.setOrder(QueryValue.ORDER_ASC);
		query.setOrderBy(new Attribute[] {
				Region.getAttribute("name")});
		
		return Region.sortRegionsByArea(query.executeQueryList(session));

	}
	
	private SelectItem[] loadAllRegionsToUi(boolean america)
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		List regionsDb = loadAllRegions(sess, america); 
		
		int lastAreaId = -1;
		int areasCount = 0;
		for (Iterator iter = regionsDb.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			int areaId = region.getArea().getId();
			if (lastAreaId != areaId)
			{
				lastAreaId = areaId;
				areasCount++;
			}
		}

		SelectItem[] areasUi = new SelectItem[areasCount];
		List regionsTemp = new ArrayList();

		lastAreaId = -1;
		int areaIndex = -1;
		for (Iterator iter = regionsDb.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			int areaId = region.getArea().getId();
			
			regionsTemp.add(new SelectItem(
					region.getName(),
					String.valueOf(region.getId())));

			if (lastAreaId != areaId)
			{
				
				if (areaIndex != -1)
				{
					SelectItem[] regionsUi = new SelectItem[regionsTemp.size()];
					regionsTemp.toArray(regionsUi);
					areasUi[areaIndex].setSubItems(regionsUi);
				}
				
				areaIndex++;
				lastAreaId = areaId;
				regionsTemp.clear();
				
				SelectItem areaItem = new SelectItem(
						region.getArea().getName(),
						"area" + region.getArea().getId());

				areaItem.setSelectable(false);
				areasUi[areaIndex] = areaItem;
				
			}
		
		}
		
		if (areaIndex != -1)
		{
			SelectItem[] regionsUi = new SelectItem[regionsTemp.size()];
			regionsTemp.toArray(regionsUi);
			areasUi[areaIndex].setSubItems(regionsUi);
		}

		transaction.commit();
		sess.close();
		
		return areasUi; 

	}
	
	public String changeSelection()
	{
		
		Conditions conditionNations = new Conditions(Conditions.JOIN_OR);
		Conditions conditionExpRegions = new Conditions(Conditions.JOIN_OR);
		Conditions conditionImpRegions = new Conditions(Conditions.JOIN_OR);
		
		conditions = new Conditions(Conditions.JOIN_AND);
		conditions.addCondition(conditionNations);
		conditions.addCondition(conditionExpRegions);
		conditions.addCondition(conditionImpRegions);

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
			Long regionId = new Long(checkedImpRegions[i]);
			selectedImpRegionIds.add(regionId);
			conditionImpRegions.addCondition(regionImpIdAttr, regionId, Conditions.OP_EQUALS);
		}

		return null;

	}
	
	public Conditions getConditions()
	{
		return conditions;
	}

	public SelectItem[] getAllExpRegions()
	{
		return loadAllRegionsToUi(false);
	}

	public SelectItem[] getAllImpRegions()
	{
		return loadAllRegionsToUi(true);
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

}
