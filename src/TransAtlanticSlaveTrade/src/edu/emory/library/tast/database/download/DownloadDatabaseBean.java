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
package edu.emory.library.tast.database.download;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Revision;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.util.CSVUtils;

public class DownloadDatabaseBean {
	
	private String revision;
	private Boolean codes = new Boolean(true);
	private static SelectItem[] revisions;
	private static Object monitor = new Object();
	
	public SelectItem[] getRevisions() {
		synchronized (monitor) {
			if (revisions == null) {
				TastDbQuery qValue = new TastDbQuery("Revision");
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
		
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		
		TastDbQuery q = this.getQuery();
		CSVUtils.writeResponse(session, q, false, !codes.booleanValue(), "");	
		
		t.commit();
		session.close();
		return null;
	}

	private TastDbQuery getQuery() {
		
		TastDbConditions c = new TastDbConditions();
		c.addCondition(Voyage.getAttribute("revision"), new Integer(revision), TastDbConditions.OP_EQUALS);
		
		TastDbQuery q = new TastDbQuery("Voyage", c);
		String[] attrs = Voyage.getAllAttrNames();
		for (int i = 0; i < attrs.length; i++) {
			q.addPopulatedAttribute(Voyage.getAttribute(attrs[i]));
		}
		return q;
	}
	
	
	
}
