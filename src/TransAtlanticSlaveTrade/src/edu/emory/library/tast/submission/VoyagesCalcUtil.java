package edu.emory.library.tast.submission;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Voyage;

public class VoyagesCalcUtil {
	private Voyage voyage;
	private Session session;
	private Transaction tran;
	
	public Voyage getVoyage() {
		return voyage;
	}

	public void setVoyage(Voyage voyage) {
		this.voyage = voyage;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public VoyagesCalcUtil(Voyage voyage, Session session) {
		this.voyage = voyage;
		if (session == null){
			initiateSession();
		}		
	}
	
	//TODO added hibernate only for testing purposes
	public void initiateSession() {
		session = HibernateConn.getSession();
		tran = session.beginTransaction();
	}
	
	public void saveSession(){
		tran.commit();
		session.close();
	}
}
