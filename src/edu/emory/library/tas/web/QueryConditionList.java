package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.util.query.Conditions;

public class QueryConditionList extends QueryCondition
{
	
	private List values = new ArrayList();

	public QueryConditionList(String attributeName)
	{
		super(attributeName);
	}

	public void addToConditions(Conditions conditions) throws QueryInvalidValueException
	{
		Conditions subCondition = new Conditions(Conditions.JOIN_OR);
		SchemaColumn col = Voyage.getSchemaColumn(getAttributeName());
		for (Iterator iterId = values.iterator(); iterId.hasNext();)
		{
			String id = (String) iterId.next();
			Dictionary[] dicts = Dictionary.loadDictionary(col.getDictinaory(), new Integer(id));
			if (dicts.length > 0)
				subCondition.addCondition(getAttributeName(), dicts[0], Conditions.OP_EQUALS);
		}
		conditions.addCondition(subCondition);
	}

	public List getValues()
	{
		return values;
	}

	public void setValues(List values)
	{
		this.values = values;
	}

}
