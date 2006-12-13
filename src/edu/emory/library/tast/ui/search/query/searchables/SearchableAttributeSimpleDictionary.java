package edu.emory.library.tast.ui.search.query.searchables;

import java.util.Iterator;
import java.util.List;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionList;
import edu.emory.library.tast.ui.search.query.QueryConditionListItem;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeSimpleDictionary extends SearchableAttributeSimple implements ListItemsSource
{
	
	public SearchableAttributeSimpleDictionary(String id, String userLabel, UserCategory userCategory, Attribute[] attributes)
	{
		super(id, userLabel, userCategory, attributes);
	}

	public void addSingleAttributeToConditions(QueryConditionList queryConditionList, Attribute attribute, Conditions conditions)
	{
		
		DictionaryAttribute firstAttr = (DictionaryAttribute)(getAttributes()[0]);
		
		Conditions subCondition = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = queryConditionList.getSelectedIds().iterator(); iter.hasNext();)
		{
			String id = (String) iter.next();
			Dictionary dictItem = firstAttr.loadObjectById(null, Long.parseLong(id));
			subCondition.addCondition(attribute, dictItem, Conditions.OP_EQUALS);
		}
		conditions.addCondition(subCondition);

	}
	
	public boolean addToConditions(boolean markErrors, Conditions conditions, QueryCondition queryCondition)
	{

		if (!(queryCondition instanceof QueryConditionList))
			throw new IllegalArgumentException("expected QueryConditionList"); 
		
		QueryConditionList queryConditionList =
			(QueryConditionList) queryCondition;

		if (queryConditionList.getSelectedIdsCount() == 0)
			return true;
		
		Attribute[] attributes = getAttributes();
		if (attributes.length == 1)
		{
			addSingleAttributeToConditions(queryConditionList,
					attributes[0], conditions);
		}
		else
		{
			Conditions orCond = new Conditions(Conditions.JOIN_OR);
			conditions.addCondition(orCond);
			for (int i = 0; i < attributes.length; i++)
				addSingleAttributeToConditions(queryConditionList,
						attributes[i], orCond);
		}
		
		return true;
	}

	public QueryCondition createQueryCondition()
	{
		return new QueryConditionList(getId());
	}
	
	public QueryConditionListItem[] getAvailableItems()
	{
		
		DictionaryAttribute firstAttr = (DictionaryAttribute)(getAttributes()[0]);

		List dictItems = (firstAttr).loadAllObjects(null);
		QueryConditionListItem items[] = new QueryConditionListItem[dictItems.size()];
		
		int i = 0;
		for (Iterator iter = dictItems.iterator(); iter.hasNext();)
		{
			Dictionary dictItem = (Dictionary) iter.next();
			items[i++] = new QueryConditionListItem(
					dictItem.getId().toString(),
					dictItem.getName());
		}

		return items;

	}

	public QueryConditionListItem getItemByFullId(String id)
	{

		DictionaryAttribute firstAttr = (DictionaryAttribute)(getAttributes()[0]);
		Dictionary dictItem = firstAttr.loadObjectById(null, Long.parseLong(id));

		return new QueryConditionListItem(
				dictItem.getId().toString(),
				dictItem.getName());

	}
	
}