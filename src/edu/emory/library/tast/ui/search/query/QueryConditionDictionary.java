package edu.emory.library.tast.ui.search.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.AbstractAttribute;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.CompoundAttribute;
import edu.emory.library.tast.util.query.Conditions;

public class QueryConditionDictionary extends QueryCondition
{
	
	private static final long serialVersionUID = 6147345036427086382L;

	private Map dictionaries = new HashMap();
	private boolean edit = false;
	
	public QueryConditionDictionary(AbstractAttribute attribute)
	{
		super(attribute);
	}

	public void addSingleAttributeToConditions(Attribute attribute, Conditions conditions)
	{
		Conditions subCondition = new Conditions(Conditions.JOIN_OR);
		for (Iterator dictIter = dictionaries.entrySet().iterator(); dictIter.hasNext();)
		{
			Dictionary dict = (Dictionary) ((Map.Entry)dictIter.next()).getValue();
			subCondition.addCondition(attribute.getName(), dict, Conditions.OP_EQUALS);
		}
		conditions.addCondition(subCondition);
	}

	public boolean addToConditions(Conditions conditions, boolean markErrors)
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

	public Map getDictionaries()
	{
		return dictionaries;
	}

	public void setDictionaries(Map values)
	{
		this.dictionaries = values;
	}
	
	public boolean containsDictionary(Long id)
	{
		if (dictionaries == null) return false;
		return dictionaries.containsKey(id);
	}

	public boolean containsDictionary(long id)
	{
		return containsDictionary(new Long(id));
	}

	public boolean containsDictionary(Dictionary dict)
	{
		return containsDictionary(dict.getId());
	}

	public void addDictionary(Dictionary dict)
	{
		if (dictionaries == null) dictionaries = new HashMap();
		if (dict != null) dictionaries.put(dict.getId(), dict);
	}

	public void addDictionary(long id)
	{
		addDictionary(new Long(id));
	}

	public void addDictionary(Long id)
	{
		String dictName = getAttribute().getDictionary();
		Dictionary dict = Dictionary.loadDictionaryById(dictName, id);
		if (dict != null) addDictionary(dict);
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj)) return false;
		if (obj instanceof QueryConditionDictionary)
		{
			QueryConditionDictionary queryConditionDictionary = (QueryConditionDictionary) obj;
			Map theOtherDicts = queryConditionDictionary.getDictionaries();

			if (dictionaries.size() != theOtherDicts.size()) return false;
			
			for (Iterator iter = dictionaries.keySet().iterator(); iter.hasNext();)
			{
				Integer remoteId = (Integer) iter.next();
				if (!theOtherDicts.containsKey(remoteId)) return false;
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
		for (Iterator iterDict = dictionaries.keySet().iterator(); iterDict.hasNext();)
		{
			Long id = (Long) iterDict.next();
			newQueryCondition.addDictionary(id);
		}
		return newQueryCondition;
	}

	public boolean isEdit()
	{
		return edit;
	}

	public void setEdit(boolean edit)
	{
		this.edit = edit;
	}

}
