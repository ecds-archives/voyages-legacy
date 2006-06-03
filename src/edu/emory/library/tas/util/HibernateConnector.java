package edu.emory.library.tas.util;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tas.Slave;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.util.query.QueryValue;

public class HibernateConnector {

	private static HibernateConnector singleton = null;

	public static final int WITH_HISTORY = 7;

	public static final int WITHOUT_HISTORY = 6;

	public static final int APPROVED = 5;

	public static final int NOT_APPROVED = 3;

	public static final int APPROVED_AND_NOT_APPROVED = 1;

	static {
		HibernateUtil.getSessionFactory();
	}
	
	private HibernateConnector() {

	}

	/**
	 * Gets instance of HibernateConnector
	 * 
	 * @return HibernateConnector object
	 */
	public static synchronized HibernateConnector getConnector() {
		if (singleton == null) {
			singleton = new HibernateConnector();
		}
		return singleton;
	}

	/**
	 * Prepares query for voyage.
	 * 
	 * @param session
	 *            Hibernate session
	 * @param p_voyage
	 *            Voyage providing voyage id
	 * @param p_option
	 *            option
	 * @return Query object
	 */
	private Query getVoyageIndexByVoyageQuery(Session session, Voyage p_voyage,
			int p_fetchSize ,int p_option) {

		StringBuffer where = new StringBuffer("");
		boolean first = true;
		
		// Check if condition on ID is needed
		if (p_voyage != null) {
			if (p_fetchSize == -1) {
				where.append("where ");
				where.append("vid=");
				where.append(p_voyage.getVoyageId());
				where.append(" ");
				first = false;
			} else {
				where.append("where ");
				where.append("vid >= ");
				where.append(p_voyage.getVoyageId());
				where.append(" ");
				first = false;
			}
		}

		// Recognize APPROVED/NOT APPROVED
		if ((p_option & 6) != (APPROVED_AND_NOT_APPROVED & 6)) {

			int flag;
			if ((p_option & 6) == (APPROVED & 6)) {
				flag = 1;
			} else {
				flag = 0;
			}
			if (!first) {
				where.append("and ");
			} else {
				where.append("where ");
			}
			first = false;
			where.append("flag=");
			where.append(flag);
			where.append(" ");
		}

		// Recognize history/no history
		if ((p_option & 1) == (WITHOUT_HISTORY & 1)) {
			String oldWhere = where.toString();
			if (!first) {
				where.append("and ");
			} else {
				where.append("where ");
			}
			first = false;
			where.append(" (vid, global_rev_id) = some"
					+ " (select voyageId, max(revisionId) from VoyageIndex "
					+ oldWhere + " group by vid)");
		}

		// Create query
		Query query = session.createQuery("from VoyageIndex " + where
				+ " order by vid, global_rev_id ");

		if (p_fetchSize != -1) {
			query.setMaxResults(p_fetchSize);
		}
		
		return query;
	}

	/**
	 * Prepares response - parses list of objects
	 * 
	 * @param list
	 *            list of objects
	 * @param p_option
	 *            option
	 * @return table of objects that should be returned
	 */
	public VoyageIndex[] prepareResponse(List list, int p_option) {
		return (VoyageIndex[]) list.toArray(new VoyageIndex[] {});
	}

	/**
	 * Gets VoyageIndex object with voyage id as id in provided Voyage.
	 * 
	 * @param p_voyage
	 *            Voyage with ID (null if all voyages should be returned)
	 * @param p_option
	 *            option
	 * @return table of VoyageIndex objects
	 */
	public VoyageIndex[] getVoyageIndexByVoyage(Voyage p_voyage, int p_option) {

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		Query query = this.getVoyageIndexByVoyageQuery(session, p_voyage,
				 -1, p_option);
		List list = query.list();
		transaction.commit();

		return this.prepareResponse(list, p_option);
	}

	/**
	 * Gets Scrollable result of VoyageIndex objects with voyage id as id in
	 * provided Voyage.
	 * 
	 * @param p_voyage
	 *            Voyage with ID (null if all voyages should be returned)
	 * @param p_option
	 *            option
	 * @return table of VoyageIndex objects
	 */
	public ScrollableResults scrollVoyageIndexByVoyage(Voyage p_voyage,
			int p_option) {

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		Query query = this.getVoyageIndexByVoyageQuery(session, p_voyage,
				-1, p_option);
		ScrollableResults scroll = query.scroll();
		transaction.commit();

		return scroll;
	}

	/**
	 * Gets VoyageIndex of object having given slave as child.
	 * 
	 * @param p_slave
	 *            Slave object
	 * @return VoyageIndex object
	 */
	public VoyageIndex getVoyageIndexBySlave(Slave p_slave) {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	/**
	 * 
	 * @param p_voyage
	 */
	public void createVoyage(VoyageIndex p_voyage) {

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		// Check if we really create new row
		List tmpResponse = session.createQuery(
				"from VoyageIndex where vid=:vid order by global_rev_id")
				.setParameter("vid", p_voyage.getVoyageId().toString()).list();
		if (tmpResponse.size() == 0) {
			p_voyage.setRevisionId(new Long(0));
		} else {
			throw new InvalidParameterException(
					"Cannot create - record already in DB!. Call updateVoyage()");
		}

		// Create slaves
		Set slaves = p_voyage.getSlaves();
		for (Iterator iter = slaves.iterator(); iter.hasNext();) {
			session.save(iter.next());
		}

		// Create Voyage
		session.save(p_voyage.getVoyage());

		// Create Voyage Index
		session.save(p_voyage);

		// Commit changes
		transaction.commit();
	}

	public void updateVoyage(VoyageIndex p_voyage) {

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		// check for last revision ID
		List tmpResponse = session.createQuery(
				"from VoyageIndex where vid=:vid order by global_rev_id")
				.setParameter("vid", p_voyage.getVoyageId().toString()).list();

		// Increment revision ID
		if (tmpResponse.size() == 0) {
			throw new InvalidParameterException(
					"Cannot update - no record in DB. Call createVoyage()");
		} else {
			Long max = ((VoyageIndex) tmpResponse.get(tmpResponse.size() - 1))
					.getRevisionId();
			p_voyage.setRevisionId(new Long(max.longValue() + 1));
		}

		// Modify slaves
		Set slaves = p_voyage.getSlaves();
		Set newSlaves = new HashSet();
		boolean slaveSaved = false;
		for (Iterator iter = slaves.iterator(); iter.hasNext();) {
			// Create new records for changed/created slaves
			Slave slave = (Slave) iter.next();
			if (slave.getModified() == Slave.UPDATED) {
				Slave newSlave = (Slave) slave.clone();
				newSlaves.add(newSlave);
				slaveSaved = true;
				session.save(slave);
				
			} else {
				newSlaves.add(slave);
			}

		}

		if (p_voyage.getVoyage().getModified() == Voyage.UPDATED
				|| p_voyage.getVoyage().wereSlavesModified() || slaveSaved) {
			// Update slaves list
			p_voyage.setSlaves(newSlaves);

			// Modify voyage if needed
			if (p_voyage.getVoyage().getModified() == Voyage.UPDATED) {
				Voyage newVoyage = (Voyage) p_voyage.getVoyage().clone();
				p_voyage.setVoyage(newVoyage);
				session.save(newVoyage);
			}

			// Save new revision of record
			session.save(p_voyage);
		}

		// Commit changes
		transaction.commit();
	}

	public void approveVoyage(VoyageIndex p_voyage) {

	}
	
	public Object[] loadObjects(String p_objType, String[] params, String[] values, boolean[] strings) {
		
		StringBuffer where = params.length != 0 ? new StringBuffer("where "):new StringBuffer("");
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		for (int i = 0; i < params.length; i++) {
			if (i != 0) {
				where.append("and ");
			}
			where.append(params[i]).append(" = ");
			if (strings[i]) {
				where.append("\"");
			}
			where.append(values[i]);
			if (strings[i]) {
				where.append("\" ");
			}
		}
		
		Query query = session.createQuery("from " + p_objType + " " + where.toString());
		List list = query.list();
		
		transaction.commit();
		
		if (list.size() != 0) {
			return list.toArray();
		} else {
			return new Object[] {};
		}
	}
	
	public Object[] loadObjects(QueryValue p_query) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		System.out.println("My query: " + p_query.toString());
		List list = p_query.getQuery(session).list();
		transaction.commit();
		
		if (list.size() != 0) {
			return list.toArray();
		} else {
			return new Object[] {};
		}
	}
	
	public void saveObject(Object obj) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		session.save(obj);
		transaction.commit();
	}
	
	public void updateObject(Object obj) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		session.update(obj);
		transaction.commit();
	}
	
	public void saveOrUpdateObject(Object obj) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(obj);
		transaction.commit();
	}
	
	public void deleteObject(Object obj) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		session.delete(obj);
		transaction.commit();
	}

	public VoyageIndex[] getVoyagesIndexSet(Voyage p_voyage, int p_fetchSize, int p_option) {
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		Query query = this.getVoyageIndexByVoyageQuery(session, p_voyage,
				p_fetchSize, p_option);
		

		List list = query.list();
		transaction.commit();

		return this.prepareResponse(list, p_option);
	}
}
