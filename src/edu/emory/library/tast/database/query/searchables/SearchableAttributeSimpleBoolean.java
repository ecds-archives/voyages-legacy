package edu.emory.library.tast.database.query.searchables;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.database.query.QueryConditionBoolean;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.TastDbConditions;

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

	private void addForOneAttribute(QueryConditionBoolean queryConditionBoolean, Attribute attribute, TastDbConditions cond)
	{
		if (queryConditionBoolean.isYesChecked() && queryConditionBoolean.isNoChecked())
		{
			TastDbConditions orCond = new TastDbConditions(TastDbConditions.OR);
			orCond.addCondition(attribute, new Boolean(true), TastDbConditions.OP_EQUALS);
			orCond.addCondition(attribute, new Boolean(false), TastDbConditions.OP_EQUALS);
			cond.addCondition(orCond);
		}
		else if (queryConditionBoolean.isYesChecked())
		{
			cond.addCondition(attribute, new Boolean(true), TastDbConditions.OP_EQUALS);
		}
		else if (queryConditionBoolean.isNoChecked())
		{
			cond.addCondition(attribute, new Boolean(false), TastDbConditions.OP_EQUALS);
		}
	}
	
	public boolean addToConditions(boolean markErrors, TastDbConditions conditions, QueryCondition queryCondition)
	{
		
		// check
		if (!(queryCondition instanceof QueryConditionBoolean))
			throw new IllegalArgumentException("expected QueryConditionBoolean"); 
		
		// cast
		QueryConditionBoolean queryConditionBoolean =
			(QueryConditionBoolean) queryCondition;
		
		// nothing to do
		if (!queryConditionBoolean.isYesChecked() && !queryConditionBoolean.isNoChecked())
			return true;

		// create db conditions
		Attribute[] attributes = getAttributes();
		if (attributes.length == 1)
		{
			addForOneAttribute(queryConditionBoolean, attributes[0], conditions);
		}
		else
		{
			TastDbConditions orCond = new TastDbConditions(TastDbConditions.OR);
			conditions.addCondition(orCond);
			for (int i = 0; i < attributes.length; i++)
				addForOneAttribute(queryConditionBoolean, attributes[i], orCond);
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
		
		queryCondition.setYesChecked(
				"present".equalsIgnoreCase(urlValue) ||
				"yes".equalsIgnoreCase(urlValue) ||
				"true".equalsIgnoreCase(urlValue) ||
				"ok".equalsIgnoreCase(urlValue) ||
				"on".equalsIgnoreCase(urlValue) ||
				"1".equalsIgnoreCase(urlValue));
		
		queryCondition.setNoChecked(
				"present".equalsIgnoreCase(urlValue) ||
				"no".equalsIgnoreCase(urlValue) ||
				"false".equalsIgnoreCase(urlValue) ||
				"off".equalsIgnoreCase(urlValue) ||
				"0".equalsIgnoreCase(urlValue));

		return queryCondition;

	}

}