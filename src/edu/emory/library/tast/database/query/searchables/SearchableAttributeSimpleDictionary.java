package edu.emory.library.tast.database.query.searchables;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.SimpleCache;
import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.database.query.QueryConditionList;
import edu.emory.library.tast.database.query.QueryConditionListItem;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.StringUtils;

public class SearchableAttributeSimpleDictionary extends SearchableAttributeSimple implements ListItemsSource
{
	
	private static final boolean USE_SQL_IN = true;

	public SearchableAttributeSimpleDictionary(String id, String userLabel, UserCategories userCategories, Attribute[] attributes, String spssName, String listDescription, boolean inEstimates)
	{
		super(id, userLabel, userCategories, attributes, spssName, listDescription, inEstimates);
	}

	public boolean addToConditions(boolean markErrors, TastDbConditions conditions, QueryCondition queryCondition)
	{

		if (!(queryCondition instanceof QueryConditionList))
			throw new IllegalArgumentException("expected QueryConditionList"); 
		
		QueryConditionList queryConditionList =
			(QueryConditionList) queryCondition;

		if (queryConditionList.getSelectedIdsCount() == 0)
			return true;
		
		Long[] idsArray = StringUtils.toLongArray(queryConditionList.getSelectedIds());
		if (idsArray == null)
			return false;
		
		// Jan Zich, November 15, 2008
		// This is to experiment with "... column IN (1, 2, 3) ... " versus
		// "... column = 1 OR column = 2 OR column = 3 ...". By some rudimentary
		// testing, I found that there is almost no difference. But I would like
		// to keep it here just for record.

		if (USE_SQL_IN)
		{
		
			Attribute[] attributes = getAttributes();
			for (int i = 0; i < attributes.length; i++)
			{
				DictionaryAttribute attribute = (DictionaryAttribute) attributes[i];
				NumericAttribute idAttr = attribute.getIdAttribute();
				conditions.addCondition(
						new SequenceAttribute(new Attribute[] {attribute, idAttr}),
						idsArray, TastDbConditions.OP_IN);
			}
		
		}
		else
		{
		
			TastDbConditions subCond = new TastDbConditions(TastDbConditions.OR);
			for (int j = 0; j < idsArray.length; j++)
			{
	
				Attribute[] attributes = getAttributes();
				for (int i = 0; i < attributes.length; i++)
				{
					DictionaryAttribute attribute = (DictionaryAttribute) attributes[i];
					NumericAttribute idAttr = attribute.getIdAttribute();
					subCond.addCondition(
							new SequenceAttribute(new Attribute[] {attribute, idAttr}),
							idsArray[j], TastDbConditions.OP_EQUALS);
				}
				
			}
			conditions.addCondition(subCond);
			
		}
		
		return true;
	}

	public QueryCondition createQueryCondition()
	{
		return new QueryConditionList(getId());
	}
	
	public QueryConditionListItem[] getAvailableItems(Session session)
	{
		
		QueryConditionListItem[] queryConditionItems =
			(QueryConditionListItem[]) SimpleCache.get(
					SimpleCache.VOYAGES_PREFIX + getId());
		
		if (queryConditionItems == null)
		{
		
			Attribute[] attributes = (Attribute[]) getAttributes();
			DictionaryAttribute firstAttr = (DictionaryAttribute) attributes[0];
			
			StringBuffer hql = new StringBuffer();
			
			hql.append("from " + firstAttr.getDictionayClass().getCanonicalName() + " d ");
			hql.append("where ");
			hql.append("(");
			for (int i = 0; i < attributes.length; i++)
			{
				if (i > 0) hql.append(" or ");
				hql.append("d in (select v.");
				hql.append(attributes[i].getName());
				hql.append(" from Voyage v)");
			}
			hql.append(") ");
			hql.append("order by d.id");
			
			List dictItems = session.createQuery(hql.toString()).list();
			queryConditionItems = new QueryConditionListItem[dictItems.size()];
	
			int i = 0;
			for (Iterator iter = dictItems.iterator(); iter.hasNext();)
			{
				Dictionary dictItem = (Dictionary) iter.next();
				queryConditionItems[i++] = new QueryConditionListItem(
						dictItem.getId().toString(),
						dictItem.getName());
			}
			
			SimpleCache.set(
					SimpleCache.VOYAGES_PREFIX + getId(),
					queryConditionItems);
		
		}

		return queryConditionItems;

	}

	public QueryConditionListItem getItemByFullId(Session session, String id)
	{

		DictionaryAttribute firstAttr = (DictionaryAttribute)(getAttributes()[0]);
		Dictionary dictItem = firstAttr.loadObjectById(session, Long.parseLong(id));

		return new QueryConditionListItem(
				dictItem.getId().toString(),
				dictItem.getName());

	}

	public QueryCondition restoreFromUrl(Session session, Map params)
	{
		
		String urlValue = StringUtils.getFirstElement((String[]) params.get(getId()));
		if (StringUtils.isNullOrEmpty(urlValue))
			return null;
		
		QueryConditionList queryCondition = new QueryConditionList(getId());
		
		// I know this is not very nice, but we have to be sure that
		// pass inside only numerical values.
		String[] idsStrArr = urlValue.split("\\s*[,\\.]\\s*");
		for (int i = 0; i < idsStrArr.length; i++)
		{
			try
			{
				long id = Long.parseLong(idsStrArr[i]);
				queryCondition.addId(String.valueOf(id));
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		 
		return queryCondition;
	}

	public Long getItemRealId(String id)
	{
		return new Long(id);
	}
	
}