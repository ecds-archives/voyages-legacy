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
package edu.emory.library.tast.faq;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.FAQ;
import edu.emory.library.tast.dm.FAQCategory;
import edu.emory.library.tast.util.StringUtils;

public class FAQBean
{
	
	private String searchTerm;
	
	public String search()
	{
		return null;
	}
	
	private static void createHqlWhere(StringBuffer hql, String hqlFAQAlias, String[] keywords)
	{
		if (keywords.length > 0)
		{
			hql.append(" where ");
			hql.append("(");
			for (int i = 0; i < keywords.length; i++)
			{
				if (i > 0) hql.append(" and ");
				hql.append("remove_accents(upper(").append(hqlFAQAlias).append(".question))");
				hql.append(" like ");
				hql.append("'%").append(keywords[i]).append("%'");
			}
			hql.append(") or (");
			for (int i = 0; i < keywords.length; i++)
			{
				if (i > 0) hql.append(" and ");
				hql.append("remove_accents(upper(").append(hqlFAQAlias).append(".answer))");
				hql.append(" like ");
				hql.append("'%").append(keywords[i]).append("%'");
			}
			hql.append(")");
		}
	}
	
	public FAQList getFAQList()
	{
		
		// parse keywords
		String[] keywords = StringUtils.extractQueryKeywords(searchTerm, StringUtils.UPPER_CASE);
		
		// collection which we are supposed to return
		FAQList faqList = new FAQList();
		faqList.setKeywords(keywords);
		
		// open connection
		Session sess = HibernateConn.getSession();
		Transaction transaction = sess.beginTransaction();

		// create main query
		StringBuffer hql = new StringBuffer();
		hql.append("from FAQ f left join f.category c ");
		createHqlWhere(hql, "f", keywords);
		hql.append(" order by c.order asc, f.id asc");
		Query query = sess.createQuery(hql.toString());
		
		// execute query
		List faqs = query.list();
		
		// move data to UI
		FAQCategory cat = null;
		FAQListCategory listCat = null;
		for (Iterator faqIt = faqs.iterator(); faqIt.hasNext();)
		{
			Object[] row = (Object[]) faqIt.next();
			FAQ faq = (FAQ)row[0];
			FAQCategory thisCat = (FAQCategory)row[1];
			if (cat == null || !thisCat.equals(cat))
			{
				cat = thisCat;
				listCat = faqList.addCategory(thisCat.getName());
			}
			listCat.addQuestion(faq.getQuestion(), faq.getAnswer());
		}

		// done with db
		transaction.commit();
		sess.close();
	
		// return the list of terms
		return faqList;
		
	}
	
	public String getSearchTerm()
	{
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm)
	{
		this.searchTerm = searchTerm;
	}

}
