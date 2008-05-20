package edu.emory.library.tast.faq;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.FAQ;
import edu.emory.library.tast.dm.FAQCategory;
import edu.emory.library.tast.util.HibernateUtil;
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
		String[] keywords = StringUtils.extractQueryKeywords(searchTerm, true);
		
		// collection which we are supposed to return
		FAQList faqList = new FAQList();
		faqList.setKeywords(keywords);
		
		// open connection
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		// create main query
		StringBuffer hql = new StringBuffer();
		hql.append("from FAQ f left join f.category c ");
		createHqlWhere(hql, "f", keywords);
		hql.append(" order by c.name asc, c.id asc");
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
