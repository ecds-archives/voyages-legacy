package edu.emory.library.tast.database.download;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Revision;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.util.CSVUtils;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class DownloadDatabaseBean {
	
	private String revision;
	private Boolean codes = new Boolean(true);
	private static SelectItem[] revisions;
	private static Object monitor = new Object();
	
	public SelectItem[] getRevisions() {
		synchronized (monitor) {
			if (revisions == null) {
				QueryValue qValue = new QueryValue("Revision");
				Object[] revs = qValue.executeQuery();
				revisions = new SelectItem[revs.length];
				for (int i = 0; i < revs.length; i++) {
					revisions[i] = new SelectItem(((Revision)revs[i]).getId().toString(), ((Revision)revs[i]).getName());
				}
			}
		}
		return revisions;
	}
	
	public String getRevision() {
		return revision;
	}
	
	public void setRevision(String revision) {
		this.revision = revision;
	}
	
	public Boolean getCodes() {
		return codes ;
	}
	
	public void setCodes(Boolean codes) {
		this.codes = codes;
	}
	
	public String getFileAllData() {
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		QueryValue q = this.getQuery();
		CSVUtils.writeResponse(session, q, !codes.booleanValue(), "");	
		
		t.commit();
		session.close();
		return null;
	}

	private QueryValue getQuery() {
		
		Conditions c = new Conditions();
		c.addCondition(Voyage.getAttribute("revision"), new Integer(revision), Conditions.OP_EQUALS);
		
		QueryValue q = new QueryValue("Voyage", c);
		String[] attrs = Voyage.getAllAttrNames();
		for (int i = 0; i < attrs.length; i++) {
			q.addPopulatedAttribute(Voyage.getAttribute(attrs[i]));
		}
		return q;
	}
	
	
	
}
