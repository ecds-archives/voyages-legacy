package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionText;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeSimpleText extends SearchableAttributeSimple
{

	public SearchableAttributeSimpleText(String id, String userLabel, UserCategory userCategory, Attribute[] attributes)
	{
		super(id, userLabel, userCategory, attributes);
	}

	public QueryCondition createQueryCondition()
	{
		return new QueryConditionText(getId());
	}

	public boolean addToConditions(boolean markErrors, Conditions conditions, QueryCondition queryCondition)
	{
		
		// check
		if (!(queryCondition instanceof QueryConditionText))
			throw new IllegalArgumentException("expected QueryConditionText"); 
		
		// retype
		QueryConditionText queryConditionText =
			(QueryConditionText) queryCondition;

		// is empty -> no db condition
		if (!queryConditionText.isNonEmpty())
			return true;
		
		// trivial preprocessing
		String value = queryConditionText.getValue().trim();
		if (!value.endsWith("%")) value += "%";

		// create db conditions
		Attribute[] attributes = getAttributes();
		if (attributes.length == 1)
		{
			conditions.addCondition(attributes[0].getName(),
					value, Conditions.OP_LIKE);
		}
		else
		{
			Conditions orCond = new Conditions(Conditions.JOIN_OR);
			conditions.addCondition(orCond);
			for (int i = 0; i < attributes.length; i++)
				conditions.addCondition(attributes[i].getName(),
						value, Conditions.OP_LIKE);
		}
		
		// all OK
		return true;
	
	}

}
