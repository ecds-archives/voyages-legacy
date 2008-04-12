package edu.emory.library.tast.database;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.SimpleTableCell;
import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.dm.attributes.Group;
import edu.emory.library.tast.util.HibernateUtil;

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
		
		Session sess = HibernateUtil.getSession();
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
					new SimpleTableCell(group.getUserLabel(), null, "header-group", 1, 7)};

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
