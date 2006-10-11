package edu.emory.library.tast.estimates;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.ui.SelectItem;
import edu.emory.library.tast.util.HibernateUtil;

public class EstimatesBean
{
	
	private String[] selectedNations;
	private String[] selectedAfricanRegions;
	private String[] selectedAmericanRegions;
	
	public SelectItem[] getListOfNations()
	{
		
		List nationsDb = Nation.loadAllNations();
		SelectItem[] nationsUi = new SelectItem[nationsDb.size()];
		
		int i = 0;
		for (Iterator iter = nationsDb.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			nationsUi[i++] = new SelectItem(
					nation.getName(),
					String.valueOf(nation.getId()));
		}
		
		return nationsUi;

	}
	
	private SelectItem[] loadRegions(boolean america)
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		Criteria crit = sess.createCriteria(Region.class)
		.addOrder(Order.asc("name"))
		.createCriteria("area")
		.add(Restrictions.eq("america", new Boolean(america)));
		
		List regionsDb = crit.list();
		
		transaction.commit();
		sess.close();
		
		SelectItem[] regionsUi = new SelectItem[regionsDb.size()];
		
		int i = 0;
		for (Iterator iter = regionsDb.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			regionsUi[i++] = new SelectItem(
					region.getName(),
					String.valueOf(region.getId()));
		}
		
		return regionsUi;
	}
	
	public SelectItem[] getListOfAfricanRegions()
	{
		return loadRegions(false);
	}

	public SelectItem[] getListOfAmericanRagions()
	{
		return loadRegions(true);
	}

	public String[] getSelectedAfricanRegions()
	{
		return selectedAfricanRegions;
	}

	public void setSelectedAfricanRegions(String[] selectedAfricanRegions)
	{
		this.selectedAfricanRegions = selectedAfricanRegions;
	}

	public String[] getSelectedAmericanRegions()
	{
		return selectedAmericanRegions;
	}

	public void setSelectedAmericanRegions(String[] selectedAmericanRegions)
	{
		this.selectedAmericanRegions = selectedAmericanRegions;
	}

	public String[] getSelectedNations()
	{
		return selectedNations;
	}

	public void setSelectedNations(String[] selectedNations)
	{
		this.selectedNations = selectedNations;
	}

}