package edu.emory.library.tast.database.query.searchables;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.database.query.QueryConditionBoolean;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeSimpleBoolean extends SearchableAttributeSimple
{

	public SearchableAttributeSimpleBoolean(String id, String userLabel, UserCategories userCategories, Attribute[] attributes, String spssName, String listDescription, boolean inEstimates)
	{
		super(id, userLabel, userCategories, attributes, spssName, listDescription, inEstimates);
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

	public QueryCondition restoreFromUrl(Session session, Map params)
	{
		
		String urlValue = StringUtils.getFirstElement((String[]) params.get(getId()));
		if (StringUtils.isNullOrEmpty(urlValue))
			return null;
		
		QueryConditionBoolean queryCondition = new QueryConditionBoolean(getId());
		queryCondition.setChecked(
				"yes".equalsIgnoreCase(urlValue) ||
				"true".equalsIgnoreCase(urlValue) ||
				"ok".equalsIgnoreCase(urlValue) ||
				"on".equalsIgnoreCase(urlValue) ||
				"1".equalsIgnoreCase(urlValue));
		
		return queryCondition;

	}

}