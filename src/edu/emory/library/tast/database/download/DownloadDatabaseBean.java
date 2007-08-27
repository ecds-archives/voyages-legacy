package edu.emory.library.tast.database.download;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.util.CSVUtils;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.QueryValue;

public class DownloadDatabaseBean {
	
	public String getFileAllData() {
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		QueryValue q = this.getQuery();
		CSVUtils.writeResponse(session, q);	
		
		t.commit();
		session.close();
		return null;
	}

	private QueryValue getQuery() {
		QueryValue q = new QueryValue("Voyage");
		String[] attrs = Voyage.getAllAttrNames();
		for (int i = 0; i < attrs.length; i++) {
			q.addPopulatedAttribute(Voyage.getAttribute(attrs[i]));
		}
		return q;
	}
	
	
	
}
