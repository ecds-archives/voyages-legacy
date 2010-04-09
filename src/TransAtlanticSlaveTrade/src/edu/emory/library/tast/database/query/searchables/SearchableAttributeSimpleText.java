/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.database.query.searchables;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.database.query.QueryConditionText;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.StringUtils;

public class SearchableAttributeSimpleText extends SearchableAttributeSimple
{
	
	private String textIndexColumn;
	
	public SearchableAttributeSimpleText(String id, String userLabel, UserCategories userCategories, Attribute[] attributes, String spssName, String listDescription, boolean inEstimates, String textIndexColumn)
	{
		super(id, userLabel, userCategories, attributes, spssName, listDescription, inEstimates);
		this.textIndexColumn = textIndexColumn;
	}

	public QueryCondition createQueryCondition()
	{
		return new QueryConditionText(getId());
	}

	public boolean addToConditions(boolean markErrors, TastDbConditions conditions, QueryCondition queryCondition)
	{
		
		// check
		if (!(queryCondition instanceof QueryConditionText))
			throw new IllegalArgumentException("expected QueryConditionText"); 
		
		// cast
		QueryConditionText queryConditionText =
			(QueryConditionText) queryCondition;

		// is empty -> no db condition
		if (!queryConditionText.isNonEmpty())
			return true;
		
		// consider only alfanum and digits
		String[] keywords = StringUtils.extractQueryKeywords(queryConditionText.getValue(), StringUtils.LOWER_CASE);
		if (keywords.length == 0)
			return false;
	
		if (AppConfig.getConfiguration().getBoolean(AppConfig.DATABASE_USE_SQL) && textIndexColumn != null)
		{
			
			conditions.addCondition(
					Voyage.getAttribute(textIndexColumn),
					StringUtils.joinNonEmpty(" & ", keywords),
					TastDbConditions.OP_TEXT_SEARCH);
			
		}
		else
		{
		
			// add keywords to query
			TastDbConditions attrCond = new TastDbConditions(TastDbConditions.OR);
			Attribute[] attributes = getAttributes();
			for (int j = 0; j < attributes.length; j++)
			{
				
				TastDbConditions keywordCond = new TastDbConditions(TastDbConditions.AND);
			
				for (int i = 0; i < keywords.length; i++)
				{
					String keyword = "%" + keywords[i] + "%";
					
					FunctionAttribute attr =
						new FunctionAttribute("remove_accents", new Attribute[] {
									new FunctionAttribute("upper", new Attribute[]{attributes[j]})});
					
					keywordCond.addCondition(attr, keyword, TastDbConditions.OP_LIKE);
	
				}
				
				attrCond.addCondition(keywordCond);
	
			}
			conditions.addCondition(attrCond);
		
		}
		
		// all OK
		return true;
	
	}

	public QueryCondition restoreFromUrl(Session session, Map params)
	{
		
		String value = StringUtils.getFirstElement((String[]) params.get(getId()));
		if (StringUtils.isNullOrEmpty(value))
			return null;
		
		QueryConditionText queryCondition = new QueryConditionText(getId());
		queryCondition.setValue(value);
		return queryCondition;

	}

}
