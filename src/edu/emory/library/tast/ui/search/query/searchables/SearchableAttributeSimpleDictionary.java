package edu.emory.library.tast.ui.search.query.searchables;

import java.util.Iterator;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionList;
import edu.emory.library.tast.ui.search.query.QueryConditionListItem;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeSimpleDictionary extends SearchableAttributeSimple implements ListItemsSource
{
	
	private String dictionary;

	public SearchableAttributeSimpleDictionary(String id, String userLabel, UserCategory userCategory, Attribute[] attributes)
	{
		super(id, userLabel, userCategory, attributes);
		this.dictionary = ((DictionaryAttribute)attributes[0]).getDictionary();
	}

	public void addSingleAttributeToConditions(QueryConditionList queryConditionList, Attribute attribute, Conditions conditions)
	{
		Conditions subCondition = new Conditions(Conditions.JOIN_OR);
		for (Iterator iter = queryConditionList.getSelectedIds().iterator(); iter.hasNext();)
		{
			String id = (String) iter.next();
			Dictionary dictItem = Dictionary.loadDictionaryById(dictionary, new Long(id));
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
		Dictionary[] dictItems = Dictionary.loadDictionary(dictionary);
		QueryConditionListItem items[] = new QueryConditionListItem[dictItems.length]; 
		for (int i = 0; i < dictItems.length; i++)
		{
			Dictionary dictItem = dictItems[i];
			items[i] = new QueryConditionListItem(
					dictItem.getId().toString(),
					dictItem.getName());
		}
		return items;
	}

	public QueryConditionListItem getItemByFullId(String id)
	{
		Dictionary dictItem = Dictionary.loadDictionaryById(dictionary, new Long(id));
		return new QueryConditionListItem(
				dictItem.getId().toString(),
				dictItem.getName());
	}
	
}