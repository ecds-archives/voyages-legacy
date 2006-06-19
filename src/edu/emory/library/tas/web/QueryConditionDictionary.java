package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.util.query.Conditions;

public class QueryConditionDictionary extends QueryCondition
{
	
	private List dictionaries = new ArrayList();

	public QueryConditionDictionary(String attributeName)
	{
		super(attributeName);
	}

	public boolean addToConditions(Conditions conditions)
	{
		if (dictionaries.size() > 0)
		{
			Conditions subCondition = new Conditions(Conditions.JOIN_OR);
			for (Iterator dictIter = dictionaries.iterator(); dictIter.hasNext();)
			{
				Dictionary dict = (Dictionary) dictIter.next();
				subCondition.addCondition(getAttributeName(), dict, Conditions.OP_EQUALS);
			}
			conditions.addCondition(subCondition);
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
		SchemaColumn col = Voyage.getSchemaColumn(getAttributeName());
		Dictionary[] dicts = Dictionary.loadDictionary(col.getDictinaory(), new Integer(value));
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
		QueryConditionDictionary newQueryCondition = new QueryConditionDictionary(getAttributeName());
		SchemaColumn col = Voyage.getSchemaColumn(getAttributeName());
		for (Iterator iterDict = dictionaries.iterator(); iterDict.hasNext();)
		{
			Dictionary dict = (Dictionary) iterDict.next();
			Dictionary[] dicts = Dictionary.loadDictionary(col.getDictinaory(), dict.getRemoteId());
			if (dicts.length > 0) newQueryCondition.addDictionary(dicts[0]);
		}
		return newQueryCondition;
	}

}
