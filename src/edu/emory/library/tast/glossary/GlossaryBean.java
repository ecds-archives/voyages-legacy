package edu.emory.library.tast.glossary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.GlossaryTerm;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;

public class GlossaryBean
{
	
	private String searchTerm;
	
	public String search()
	{
		return null;
	}
	
	private static void createHqlWhere(StringBuffer hql, String hqlTermAlias, String[] keywords)
	{
		if (keywords.length > 0)
		{
			hql.append(" where ");
			hql.append("(");
			for (int i = 0; i < keywords.length; i++)
			{
				if (i > 0) hql.append(" and ");
				hql.append("remove_accents(upper(").append(hqlTermAlias).append(".term))");
				hql.append(" like ");
				hql.append("'%").append(keywords[i]).append("%'");
			}
			hql.append(") or (");
			for (int i = 0; i < keywords.length; i++)
			{
				if (i > 0) hql.append(" and ");
				hql.append("remove_accents(upper(").append(hqlTermAlias).append(".description))");
				hql.append(" like ");
				hql.append("'%").append(keywords[i]).append("%'");
			}
			hql.append(")");
		}
	}
	
	public GlossaryList getTerms()
	{

		// parse keywords
		String[] keywords = StringUtils.extractQueryKeywords(searchTerm, StringUtils.UPPER_CASE);
		
		// collection which we are supposed to return
		GlossaryList terms = new GlossaryList();
		terms.setListingAll(keywords.length == 0);
		terms.setKeywords(keywords);

		// open connection
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// create main query
		StringBuffer hqlMain = new StringBuffer();
		hqlMain.append("from GlossaryTerm t ");
		createHqlWhere(hqlMain, "t", keywords);
		hqlMain.append(" order by t.term");
		Query queryMain = sess.createQuery(hqlMain.toString());
		
		// map: char -> GlossaryListLetter
		HashMap charToLetter = new HashMap();
		
		// load terms
		GlossaryListLetter currLetter = null;
		for (Iterator termIt = queryMain.list().iterator(); termIt.hasNext();)
		{
			GlossaryTerm dbTerm = (GlossaryTerm) termIt.next();
			char letterChar = Character.toUpperCase(dbTerm.getTerm().charAt(0));
			
			// new letter
			if (currLetter == null || currLetter.getLetter() != letterChar)
			{
				currLetter = terms.addLetter(letterChar);
				charToLetter.put(new Character(letterChar), currLetter);
			}

			// add the term
			currLetter.addTerm(
					dbTerm.getTerm(),
					dbTerm.getDescription());
		}
		
		// create totals query
		StringBuffer hqlTotals = new StringBuffer();
		hqlTotals.append("select upper(substr(t.term, 1, 1)), count(t) ");
		hqlTotals.append("from GlossaryTerm t ");
		hqlTotals.append("group by upper(substr(t.term, 1, 1))");
		Query queryTotals = sess.createQuery(hqlTotals.toString());

		// load total counts
		for (Iterator it = queryTotals.list().iterator(); it.hasNext();)
		{
			Object[] object = (Object[]) it.next();
			char letterChar = ((String)object[0]).charAt(0);
			int totalTerms = ((Long)object[1]).intValue();
			
			// did we previously load the letter?
			GlossaryListLetter letter =
				(GlossaryListLetter) charToLetter.get(new Character(letterChar));
		
			// yes, we did
			if (letter != null)
				letter.setTotalTerms(totalTerms);
		}

		// done with db
		transaction.commit();
		sess.close();
	
		// return the list of terms
		return terms;

	}
	
	public GlossaryLetter[] getLetters()
	{
		
		// parse keywords
		String[] keywords = StringUtils.extractQueryKeywords(searchTerm, StringUtils.UPPER_CASE);
		
		// open connection
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// create query for the all
		StringBuffer hqlAll = new StringBuffer();
		hqlAll.append("select upper(substr(t.term, 1, 1)) ");
		hqlAll.append("from GlossaryTerm t ");
		Query queryAll = sess.createQuery(hqlAll.toString());
		
		// default set: all letters
		TreeMap letters = new TreeMap();
		for (Iterator it = queryAll.list().iterator(); it.hasNext();)
		{
			char letterChar = ((String)it.next()).charAt(0);
			letters.put(new Character(letterChar), new Boolean(false));
		}

		// create query for the search
		StringBuffer hqlMatched = new StringBuffer();
		hqlMatched.append("select upper(substr(t.term, 1, 1)) ");
		hqlMatched.append("from GlossaryTerm t ");
		createHqlWhere(hqlMatched, "t", keywords);
		hqlMatched.append(" order by t.term");
		Query query = sess.createQuery(hqlMatched.toString());
		
		// highlight covered letters
		for (Iterator it = query.list().iterator(); it.hasNext();)
		{
			char letterChar = ((String)it.next()).charAt(0);
			letters.put(new Character(letterChar), new Boolean(true));
		}
		
		// done with db
		transaction.commit();
		sess.close();
		
		// return the tree as an array
		int letterIdx = 0;
		GlossaryLetter[] lettersArr = new GlossaryLetter[letters.size()];
		for (Iterator it = letters.keySet().iterator(); it.hasNext();)
		{
			Character letterChar = (Character) it.next();
			lettersArr[letterIdx++] = new GlossaryLetter(
					letterChar.charValue(),
					((Boolean)letters.get(letterChar)).booleanValue());
		}
		return lettersArr;
		
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
