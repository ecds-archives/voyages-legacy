package edu.emory.library.tast.database;

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
	
	private Map getNumberOfNonNullVoyages()
	{
		
		Map nonNullVoyages = new HashMap();
		
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
		
		Map nonNullVoyages = getNumberOfNonNullVoyages();
		
		Group[] groups = Group.getGroups();
		
		int searchableAttrsCount = 0;
		for (int i = 0; i < groups.length; i++)
			searchableAttrsCount += groups[i].getNoOfAllSearchableAttributes();
		
		int headerRowsCount = 1;
		SimpleTableCell[][] tableRows = new SimpleTableCell[searchableAttrsCount + headerRowsCount][];
		
		tableRows[0] = new SimpleTableCell[] {
			new SimpleTableCell("Categories", "header"),
			new SimpleTableCell("Variables", "header"),
			new SimpleTableCell("SPSS variable name", "header"),
			new SimpleTableCell("Number of voyages with information on variable", "header"),
			new SimpleTableCell("Estimates", "header"),
			new SimpleTableCell("Basic selection", "header"),
			new SimpleTableCell("General selection", "header"),
			new SimpleTableCell("Method of imputation", "header")};

		int rowIndex = headerRowsCount;
		for (int i = 0; i < groups.length; i++)
		{
			Group group = groups[i];
			SearchableAttribute[] attributes = group.getAllSearchableAttributes();

			tableRows[rowIndex] = new SimpleTableCell[8];
			
			tableRows[rowIndex][0] = new SimpleTableCell(
					group.getUserLabel(),
					group.getNoOfAllSearchableAttributes(), 1);

			for (int j = 0; j < attributes.length; j++)
			{
				SearchableAttribute attr = attributes[j];
				
				int cellOffset;
				if (j == 0)
				{
					cellOffset = 1;
				}
				else
				{
					cellOffset = 0;
					tableRows[rowIndex] = new SimpleTableCell[7];
				}
				
				tableRows[rowIndex][cellOffset+0] = new SimpleTableCell(attr.getUserLabel());
				tableRows[rowIndex][cellOffset+1] = new SimpleTableCell(attr.getSpssName());
				tableRows[rowIndex][cellOffset+2] = new SimpleTableCell(String.valueOf(nonNullVoyages.get(attr.getId())));
				tableRows[rowIndex][cellOffset+3] = new SimpleTableCell(attr.isInEstimates() ? "yes" : "");
				tableRows[rowIndex][cellOffset+4] = new SimpleTableCell(attr.getUserCategories().isIn(UserCategory.Beginners) ? "yes" : "");
				tableRows[rowIndex][cellOffset+5] = new SimpleTableCell(attr.getUserCategories().isIn(UserCategory.General) ? "yes" : "");
				tableRows[rowIndex][cellOffset+6] = new SimpleTableCell(attr.getListDescription());
				
				rowIndex++;
			}
		
		}
		
		return tableRows;
		
	}
	
}
