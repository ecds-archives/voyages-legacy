package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionBoolean;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeSimpleBoolean extends SearchableAttributeSimple
{

	public SearchableAttributeSimpleBoolean(String id, String userLabel, UserCategories userCategories, Attribute[] attributes)
	{
		super(id, userLabel, userCategories, attributes);
	}

	public QueryCondition createQueryCondition()
	{
		return new QueryConditionBoolean(getId());
	}

	public boolean addToConditions(boolean markErrors, Conditions conditions, QueryCondition queryCondition)
	{
		
		// check
		if (!(queryCondition instanceof QueryConditionBoolean))
			throw new IllegalArgumentException("expected QueryConditionBoolean"); 
		
		// cast
		QueryConditionBoolean queryConditionBoolean =
			(QueryConditionBoolean) queryCondition;

		// create db conditions
		Attribute[] attributes = getAttributes();
		if (attributes.length == 1)
		{
			conditions.addCondition(attributes[0],
					new Boolean(queryConditionBoolean.isChecked()),
					Conditions.OP_EQUALS);
		}
		else
		{
			Conditions orCond = new Conditions(Conditions.JOIN_OR);
			conditions.addCondition(orCond);
			for (int i = 0; i < attributes.length; i++)
				orCond.addCondition(attributes[i],
						new Boolean(queryConditionBoolean.isChecked()),
						Conditions.OP_EQUALS);
		}
		
		// all OK
		return true;
	
	}

}