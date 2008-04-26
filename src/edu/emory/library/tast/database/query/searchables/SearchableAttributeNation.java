package edu.emory.library.tast.database.query.searchables;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.database.query.QueryConditionList;
import edu.emory.library.tast.database.query.QueryConditionListItem;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeNation extends SearchableAttributeSimple implements ListItemsSource
{
	
	public SearchableAttributeNation(String id, String userLabel, UserCategories userCategories, Attribute[] attributes, String spssName, String listDescription, boolean inEstimates)
	{
		super(id, userLabel, userCategories, attributes, spssName, listDescription, inEstimates);
	}

	public boolean addToConditions(boolean markErrors, Conditions conditions, QueryCondition queryCondition)
	{

		if (!(queryCondition instanceof QueryConditionList))
			throw new IllegalArgumentException("expected QueryConditionList"); 
		
		QueryConditionList queryConditionList =
			(QueryConditionList) queryCondition;

		if (queryConditionList.getSelectedIdsCount() == 0)
			return true;
		
		Conditions subCond = new Conditions(Conditions.JOIN_OR);
		
		for (Iterator iter = queryConditionList.getSelectedIds().iterator(); iter.hasNext();)
		{

			Long id = new Long((String) iter.next());

			Attribute[] attributes = getAttributes();
			for (int i = 0; i < attributes.length; i++)
			{
				DictionaryAttribute attribute = (DictionaryAttribute) attributes[i];
				NumericAttribute idAttr = attribute.getItAttribute();
				subCond.addCondition(new SequenceAttribute(new Attribute[] {attribute, idAttr}), id, Conditions.OP_EQUALS);
			}
			
		}
		conditions.addCondition(subCond);
		
		return true;
	}

	public QueryCondition createQueryCondition()
	{
		return new QueryConditionList(getId());
	}
	
	public QueryConditionListItem[] getAvailableItems(Session session)
	{
		
		QueryConditionListItem[] items =
			ListItemsCache.getCachedListItems(getId());
		
		if (items == null)
		{
		
			DictionaryAttribute firstAttr = (DictionaryAttribute)(getAttributes()[0]);
	
			String hsql =
				"from edu.emory.library.tast.dm.Nation p " +
				"where p in (select v." + firstAttr.getName() + " from Voyage v) ";
			
			Query query = session.createQuery(hsql);
			List dictItems = query.list();
			
			items = new QueryConditionListItem[dictItems.size()];
			
			int i = 0;
			for (Iterator iter = dictItems.iterator(); iter.hasNext();)
			{
				Dictionary dictItem = (Dictionary) iter.next();
				items[i++] = new QueryConditionListItem(
						dictItem.getId().toString(),
						dictItem.getName());
			}
			
			ListItemsCache.setCachedListItems(getId(), items);
			
		}

		return items;

	}

	public QueryConditionListItem getItemByFullId(Session session, String id)
	{

		DictionaryAttribute firstAttr = (DictionaryAttribute)(getAttributes()[0]);
		Dictionary dictItem = firstAttr.loadObjectById(session, Long.parseLong(id));

		return new QueryConditionListItem(
				dictItem.getId().toString(),
				dictItem.getName());

	}
	
}