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
package edu.emory.library.tast.database;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.common.SimpleTableCell;
import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.attributes.Group;

public class VariableListBean
{
	
	private static final String CHECK_SYMBOL = "&bull;";
	private static Map nonNullVoyages = null; 

	private synchronized Map getNumberOfNonNullVoyages()
	{
		
		if (nonNullVoyages != null)
			return nonNullVoyages;
		
		nonNullVoyages = new HashMap();
		
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT ");
		
		Group[] groups = Group.getGroups();
		
		int k = 0;
		for (int i = 0; i < groups.length; i++)
		{
			SearchableAttribute[] attributes = groups[i].getAllSearchableAttributes();
			for (int j = 0; j < attributes.length; j++)
			{
				if (k > 0) hql.append(", ");
				hql.append("COUNT(");
				hql.append(attributes[j].getNonNullSqlQuerySelectPart("v"));
				hql.append(")");
				hql.append(" AS ").append("count").append(k++);
			}
		}
		
		hql.append(" FROM voyages v");
		hql.append(" WHERE v.revision = ");
		hql.append(AppConfig.getConfiguration().getString(AppConfig.DEFAULT_REVISION));
		
		Session sess = HibernateConn.getSession();
		Transaction transaction = sess.beginTransaction();
		
		List result = sess.createSQLQuery(hql.toString()).list();
		Object[] firstRow = (Object[]) result.get(0);
		
		k = 0;
		for (int i = 0; i < groups.length; i++)
		{
			SearchableAttribute[] attributes = groups[i].getAllSearchableAttributes();
			for (int j = 0; j < attributes.length; j++)
			{
				nonNullVoyages.put(attributes[j].getId(), firstRow[k++]);
			}
		}
		
		transaction.commit();
		sess.close();
		
		return nonNullVoyages;
		
	}

	public SimpleTableCell[][] getTable()
	{
		
		MessageFormat voyagesCountFormat = new MessageFormat("{0,number,#,###,###}");
		
		Map nonNullVoyages = getNumberOfNonNullVoyages();
		
		Group[] groups = Group.getGroups();
		
		int searchableAttrsCount = 0;
		for (int i = 0; i < groups.length; i++)
			searchableAttrsCount += groups[i].getNoOfAllSearchableAttributes();
		
		SimpleTableCell[][] tableRows = new SimpleTableCell[searchableAttrsCount + 2 * groups.length][];
		
		int rowIndex = 0;
		for (int i = 0; i < groups.length; i++)
		{
			Group group = groups[i];
			SearchableAttribute[] attributes = group.getAllSearchableAttributes();

			tableRows[rowIndex] = new SimpleTableCell[7];
			
			tableRows[rowIndex++] = new SimpleTableCell[] {
					new SimpleTableCell(group.getUserLabel(), null, i == 0 ? "header-group-first" : "header-group", 1, 7)};

			tableRows[rowIndex++] = new SimpleTableCell[] {
					new SimpleTableCell("Variables", "header"),
					new SimpleTableCell("SPSS", "header"),
					new SimpleTableCell("Voyages", "header"),
					new SimpleTableCell("Estimates", "header"),
					new SimpleTableCell("Basic", "header"),
					new SimpleTableCell("General", "header"),
					new SimpleTableCell("Note", "header")};

			for (int j = 0; j < attributes.length; j++)
			{
				SearchableAttribute attr = attributes[j];
				
				String cssCell =
					j == 0 ? "first" : j % 2 == 0 ? "odd" : "even";
				
				tableRows[rowIndex++] = new SimpleTableCell[] {
					new SimpleTableCell(attr.getUserLabel(), cssCell),
					new SimpleTableCell(attr.getSpssName(), cssCell),
					new SimpleTableCell(voyagesCountFormat.format(new Object[]{nonNullVoyages.get(attr.getId())}), cssCell + " number"),
					new SimpleTableCell(attr.isInEstimates() ? CHECK_SYMBOL : "", cssCell + " check"),
					new SimpleTableCell(attr.getUserCategories().isIn(UserCategory.Beginners) ? CHECK_SYMBOL : "", cssCell + " check"),
					new SimpleTableCell(attr.getUserCategories().isIn(UserCategory.General) ? CHECK_SYMBOL : "", cssCell + " check"),
					new SimpleTableCell(attr.getListDescription(), cssCell)};

			}
		
		}
		
		return tableRows;
		
	}
	
}
