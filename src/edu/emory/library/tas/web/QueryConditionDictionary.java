package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.util.query.Conditions;

public class QueryConditionDictionary extends QueryCondition
{
	
	private List dictionaries = new ArrayList();
	
	public QueryConditionDictionary(AbstractAttribute attribute)
	{
		super(attribute);
	}

	public void addSingleAttributeToConditions(Attribute attribute, Conditions conditions)
	{
		Conditions subCondition = new Conditions(Conditions.JOIN_OR);
		for (Iterator dictIter = dictionaries.iterator(); dictIter.hasNext();)
		{
			Dictionary dict = (Dictionary) dictIter.next();
			subCondition.addCondition(attribute.getName(), dict, Conditions.OP_EQUALS);
		}
		conditions.addCondition(subCondition);
	}

	public boolean addToConditions(Conditions conditions)
	{
		if (dictionaries.size() == 0)
			return true;
		
		if (isOnAttribute())
		{
			Attribute attr = (Attribute) getAttribute();
			addSingleAttributeToConditions(attr, conditions);
		}
		else if (isOnCompoundAttribute())
		{
			CompoundAttribute compAttr = (CompoundAttribute) getAttribute();
			Conditions orCond = new Conditions(Conditions.JOIN_OR);
			conditions.addCondition(orCond);
			for (Iterator iterAttr = compAttr.getAttributes().iterator(); iterAttr.hasNext();)
			{
				Attribute attr = (Attribute) iterAttr.next();
				addSingleAttributeToConditions(attr, orCond);
			}
		}
		
		return true;
	}

	public List getDictionaries()
	{
		return dictionaries;
	}

	public void setDictionaries(List values)
	{
		this.dictionaries = values;
	}

	public void addDictionary(Dictionary dict)
	{
		if (dictionaries == null) dictionaries = new ArrayList();
		dictionaries.add(dict);
	}

	public void addDictionary(int value)
	{
		String dictName = getAttribute().getDictionary();
		Dictionary[] dicts = Dictionary.loadDictionary(dictName, new Integer(value));
		if (dicts.length > 0) dictionaries.add(dicts[0]);
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj)) return false;
		if (obj instanceof QueryConditionDictionary)
		{
			QueryConditionDictionary queryConditionDictionary = (QueryConditionDictionary) obj;
			List theOtherDicts = queryConditionDictionary.getDictionaries();

			if (dictionaries.size() != theOtherDicts.size()) return false;
			
			for (int i = 0; i < dictionaries.size(); i++)
			{
				Dictionary dict1 = (Dictionary)dictionaries.get(i); 
				Dictionary dict2 = (Dictionary)theOtherDicts.get(i);
				if ((dict1 != null && dict2 == null) || (dict1 == null && dict2 != null) || !dict1.equals(dict2))
					return false;
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected Object clone()
	{
		QueryConditionDictionary newQueryCondition = new QueryConditionDictionary(getAttribute());
		String dictName = getAttribute().getDictionary();
		for (Iterator iterDict = dictionaries.iterator(); iterDict.hasNext();)
		{
			Dictionary dict = (Dictionary) iterDict.next();
			Dictionary[] dicts = Dictionary.loadDictionary(dictName, dict.getRemoteId());
			if (dicts.length > 0) newQueryCondition.addDictionary(dicts[0]);
		}
		return newQueryCondition;
	}

}
