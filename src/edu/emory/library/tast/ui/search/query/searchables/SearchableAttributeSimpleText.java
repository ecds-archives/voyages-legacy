package edu.emory.library.tast.ui.search.query.searchables;

import java.util.regex.Pattern;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionText;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeSimpleText extends SearchableAttributeSimple
{
	
	private final static Pattern sepRegex = Pattern.compile("[^a-zA-Z_0-9]+");

	public SearchableAttributeSimpleText(String id, String userLabel, UserCategories userCategories, Attribute[] attributes)
	{
		super(id, userLabel, userCategories, attributes);
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
		
		// consider only alfanum and digits
		String value = queryConditionText.getValue().toUpperCase();
		String[] keywords = sepRegex.split(value);
		if (keywords.length == 0)
			return false;

		// add keywords to query
		Conditions subCond = new Conditions(Conditions.JOIN_AND);
		Attribute[] attributes = getAttributes();
		for (int i = 0; i < keywords.length; i++)
		{
			Conditions kewordCond = new Conditions(Conditions.JOIN_OR);
			for (int j = 0; j < attributes.length; j++)
			{
				kewordCond.addCondition(
						new FunctionAttribute("upper", new Attribute[]{attributes[j]}),
						"%" + keywords[i] + "%",
						Conditions.OP_LIKE);
			}
			subCond.addCondition(kewordCond);
		}
		conditions.addCondition(subCond);
		
		// all OK
		return true;
	
	}

}
