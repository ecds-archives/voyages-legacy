package edu.emory.library.tast.util;

import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.util.query.QueryValue;

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
	 * @param p_firstResult TODO
	 * @param p_option
	 *            option
	 * @return Query object
	 */
	private Query getVoyageIndexByVoyageQuery(Session session, Voyage p_voyage,
			int p_firstResult ,int p_fetchSize, int p_option) {

		StringBuffer where = new StringBuffer("");
		boolean first = true;
		
		// Check if condition on ID is needed
		if (p_voyage != null) {
			if (p_fetchSize == -1) {
				where.append("where ");
				where.append("voyageIndex.voyage.voyageId = ");
				where.append(p_voyage.getVoyageid());
				where.append(" ");
				first = false;
			} else {
				where.append("where ");
				where.append("voyageIndex.voyage.voyageId >= ");
				where.append(p_voyage.getVoyageid());
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
			//String oldWhere = where.toString();
			if (!first) {
				where.append("and ");
			} else {
				where.append("where ");
			}
			first = false;
			if ((p_option & 6) == (APPROVED_AND_NOT_APPROVED & 6)) {
				where.append("latest=1 ");
			} else {
				where.append("latest_approved=1 ");
			}
		}

		// Create query
		Query query = session.createQuery("from VoyageIndex as voyageIndex left join fetch voyageIndex.voyage " + where
				+ " order by voyageIndex.voyageId"
				);

		if (p_firstResult != -1) {
			query.setFirstResult(p_firstResult);
		}

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
		VoyageIndex[] res = getVoyageIndexByVoyage(session, p_voyage, p_option);
		transaction.commit();
		session.close();
		return res;
	}

	/**
	 * Gets VoyageIndex object with voyage id as id in provided Voyage.
	 * 
	 * @param p_session
	 * 			  Session object
	 * @param p_voyage
	 *            Voyage with ID (null if all voyages should be returned)
	 * @param p_option
	 *            option
	 * @return table of VoyageIndex objects
	 */
	public VoyageIndex[] getVoyageIndexByVoyage(Session p_session, Voyage p_voyage, int p_option) {

		//Transaction transaction = p_session.beginTransaction();
		Query query = this.getVoyageIndexByVoyageQuery(p_session, p_voyage,
				 -1, -1, p_option);
		List list = query.list();
		//transaction.commit();

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
		ScrollableResults res = scrollVoyageIndexByVoyage(session, p_voyage, p_option);
		transaction.commit();
		session.close();
		return res;
	}
	
	/**
	 * Gets Scrollable result of VoyageIndex objects with voyage id as id in
	 * provided Voyage.
	 * 
	 * @param p_session session context
	 * @param p_voyage
	 *            Voyage with ID (null if all voyages should be returned)
	 * @param p_option
	 *            option
	 * @return table of VoyageIndex objects
	 */
	public ScrollableResults scrollVoyageIndexByVoyage(Session p_session, Voyage p_voyage,
			int p_option) {

		//Transaction transaction = p_session.beginTransaction();
		Query query = this.getVoyageIndexByVoyageQuery(p_session, p_voyage,
				-1, -1, p_option);
		ScrollableResults scroll = query.scroll();
		//transaction.commit();

		return scroll;
	}

	/**
	 * Creates new VoyageIndex - saves it into DB.
	 * @param p_voyage voyage index object
	 */
	public void createVoyage(VoyageIndex p_voyage) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		createVoyage(session, p_voyage);
		transaction.commit();
		session.close();
	}
	
	/**
	 * Creates new VoyageIndex - saves it into DB.
	 * @param p_session session context
	 * @param p_voyage voyage index object
	 */
	public void createVoyage(Session p_session, VoyageIndex p_voyage) {

		
		//Transaction transaction = p_session.beginTransaction();
		
		p_voyage.setLatest(new Integer(1));
		p_voyage.setLatest_approved(new Integer(0));
		
		// Check if we really create new row
		List tmpResponse = p_session.createQuery(
			"from VoyageIndex where vid=:vid and latest=1")
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
			p_session.save(iter.next());
		}

		// Create Voyage
		p_session.save(p_voyage.getVoyage());

		// Create Voyage Index
		p_session.save(p_voyage);

		// Commit changes
		//transaction.commit();
	}

	/**
	 * Updates given voyage index in DB
	 * @param p_voyage voyage index object
	 */
	public void updateVoyage(VoyageIndex p_voyage) {
		Session session = HibernateUtil.getSession();
		updateVoyage(session, p_voyage);
		session.close();
	}
	
	/**
	 * Updates given voyage index in DB
	 * @param p_session session context
	 * @param p_voyage voyage index object
	 */
	public void updateVoyage(Session p_session, VoyageIndex p_voyage) {

		/*
		
		//Transaction transaction = p_session.beginTransaction();

		// check for last revision ID
		List tmpResponse = p_session.createQuery(
				"from VoyageIndex where vid=:vid and latest=1")
				.setParameter("vid", p_voyage.getVoyageId().toString()).list();		

		p_voyage.setLatest(new Integer(1));
		p_voyage.setLatest_approved(new Integer(0));
		
		// Increment revision ID
		if (tmpResponse.size() == 0) {
			throw new InvalidParameterException(
					"Cannot update - no record in DB. Call createVoyage()");
		} else {
			Long max = ((VoyageIndex) tmpResponse.get(tmpResponse.size() - 1))
					.getRevisionId();
			p_voyage.setRevisionId(new Long(max.longValue() + 1));
		}


		if (p_voyage.getVoyage().getModified() == Voyage.UPDATED
				|| p_voyage.getVoyage().wereSlavesModified() || slaveSaved) {
			// Update slaves list
			p_session.createQuery("update VoyageIndex set latest=0 where voyageId=" + p_voyage.getVoyageId().toString()).executeUpdate();
			
			p_voyage.setSlaves(newSlaves);

			// Modify voyage if needed
			if (p_voyage.getVoyage().getModified() == Voyage.UPDATED) {
				Voyage newVoyage = (Voyage) p_voyage.getVoyage().clone();
				p_voyage.setVoyage(newVoyage);
				p_session.save(newVoyage);
			}

			// Save new revision of record
			p_session.save(p_voyage);
		}

		// Commit changes
		//transaction.commit();

		 */

	}

	/**
	 * Approves given voyage.
	 * Sets approved flag to 1.
	 * TODO implement it!
	 * @param p_voyage voyage index to approve
	 */
	public void approveVoyage(VoyageIndex p_voyage) {

	}
	
	/**
	 * Load given object type that satisfies set of conditions
	 * @param p_objType object type name
	 * @param params list of attributes
	 * @param values list of desired values of attributes
	 * @param strings list of trues/falses - true if any of params above is string type
	 * @return list of objects
	 */
	public Object[] loadObjects(String p_objType, String[] params, String[] values, boolean[] strings) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		Object[] ret = loadObjects(session, p_objType, params, values, strings);
		transaction.commit();
		session.close();
		return ret;
	}
	
	/**
	 * Load given object type that satisfies set of conditions
	 * @param p_session session context
	 * @param p_objType object type name
	 * @param params list of attributes
	 * @param values list of desired values of attributes
	 * @param strings list of trues/falses - true if any of params above is string type
	 * @return list of objects
	 */
	public Object[] loadObjects(Session p_session, String p_objType, String[] params, String[] values, boolean[] strings) {
		
		//Prepare where string
		StringBuffer where = params.length != 0 ? new StringBuffer("where "):new StringBuffer("");
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
		
		//lock transaction
		Transaction transaction = p_session.beginTransaction();
		
		//Exec query
		Query query = p_session.createQuery("from " + p_objType + " " + where.toString());
		List list = query.list();
		
		transaction.commit();
		
		//Return results
		if (list.size() != 0) {
			return list.toArray();
		} else {
			return new Object[] {};
		}
	}
	
	/**
	 * Loads object for given QueryValue.
	 * @param p_query QueryValue object
	 * @return results of execution of QueryValue
	 */
	public Object[] loadObjects(QueryValue p_query) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		Object[] ret = loadObjects(session, p_query);
		transaction.commit();
		session.close();
		return ret;
	}
	
	/**
	 * Loads object for given QueryValue.
	 * @param p_query QueryValue object
	 * @return results of execution of QueryValue
	 */
	public List loadObjectList(QueryValue p_query) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		List list = loadObjectList(session, p_query);
		transaction.commit();
		session.close();
		return list;
	}

	/**
	 * Loads object for given QueryValue.
	 * @param p_session session context
	 * @param p_query QueryValue object
	 * @return results of execution of QueryValue
	 */
	public Object[] loadObjects(Session p_session, QueryValue p_query)
	{
		//Transaction transaction = p_session.beginTransaction();
		List list = p_query.getQuery(p_session).list();
		//transaction.commit();
		
		if (list.size() != 0) {
			return list.toArray();
		} else {
			return new Object[] {};
		}
	}
	
	/**
	 * Loads object for given QueryValue.
	 * @param p_session session context
	 * @param p_query QueryValue object
	 * @return results of execution of QueryValue
	 */
	public List loadObjectList(Session p_session, QueryValue p_query)
	{
		//Transaction transaction = p_session.beginTransaction();
		List list = p_query.getQuery(p_session).list();
		//transaction.commit();
		return list;
	}

	/**
	 * Saves given object to DB.
	 * @param obj object to save
	 */
	public void saveObject(Object obj) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		session.save(obj);
		transaction.commit();
	}
	
	/**
	 * Saves given object into DB.
	 * @param session session context
	 * @param obj object to save
	 */
	public void saveObject(Session session, Object obj) {
		//Transaction transaction = session.beginTransaction();
		session.save(obj);
		//transaction.commit();
	}
	
	/**
	 * Updates object in DB.
	 * @param obj object to update.
	 */
	public void updateObject(Object obj) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		session.update(obj);
		transaction.commit();
	}
	
	/**
	 * Updates object in DB.
	 * @param session session context
	 * @param obj object to update.
	 */
	public void updateObject(Session session, Object obj) {
		//Transaction transaction = session.beginTransaction();
		session.update(obj);
		//transaction.commit();
	}
	
	/**
	 * Saves object or updates it (if it exists in DB).
	 * @param obj object to save/update
	 */
	public void saveOrUpdateObject(Object obj) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(obj);
		transaction.commit();
	}
	
	/**
	 * Saves object or updates it (if it exists in DB).
	 * @param session session context
	 * @param obj object to save/update
	 */
	public void saveOrUpdateObject(Session session, Object obj) {
		//Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(obj);
		//transaction.commit();
	}
	
	/**
	 * Deletes object from DB.
	 * @param obj object to delete
	 */
	public void deleteObject(Object obj) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		session.delete(obj);
		transaction.commit();
	}

	/**
	 * Deletes object from DB.
	 * @param session session context
	 * @param obj object to delete
	 */
	public void deleteObject(Session session, Object obj) {
		//Transaction transaction = session.beginTransaction();
		session.delete(obj);
		//transaction.commit();
	}
	
	/**
	 * Gets VoyagesIndex object from db.
	 * @param p_voyage Voyage (provides voyageID)
	 * @param p_fetchSize max number of results
	 * @param p_option option
	 * @return
	 */
	public VoyageIndex[] getVoyagesIndexSet(Voyage p_voyage, int p_fetchSize, int p_option) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		VoyageIndex[] ret = getVoyagesIndexSet(session, p_voyage, p_fetchSize, p_option);
		transaction.commit();
		session.close();
		return ret;
	}
	
	/**
	 * Gets VoyagesIndex object from db.
	 * @param p_session session context
	 * @param p_voyage Voyage (provides voyageID)
	 * @param p_fetchSize max number of results
	 * @param p_option option
	 * @return
	 */
	public VoyageIndex[] getVoyagesIndexSet(Session p_session, Voyage p_voyage, int p_fetchSize, int p_option) {		
		//Transaction transaction = p_session.beginTransaction();
		Query query = this.getVoyageIndexByVoyageQuery(p_session, p_voyage,
				-1, p_fetchSize, p_option);
		List list = query.list();
		//transaction.commit();

		return this.prepareResponse(list, p_option);
	}

	/**
	 * Gets a set of voyages from db.
	 * @param p_firstResult
	 * @param p_fetchSize
	 * @param p_option
	 * @return
	 */
	public VoyageIndex[] getVoyagesIndexSet(int p_firstResult, int p_fetchSize, int p_option) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		VoyageIndex[] ret = getVoyagesIndexSet(session, p_firstResult, p_fetchSize, p_option);
		transaction.commit();
		
		session.close();
		return ret;
	}
	
	/**
	 * Gets a set of voyages from db.
	 * @param p_session
	 * @param p_firstResult
	 * @param p_fetchSize
	 * @param p_option
	 * @return
	 */
	public VoyageIndex[] getVoyagesIndexSet(Session p_session, int p_firstResult, int p_fetchSize, int p_option) {			
		//Transaction transaction = p_session.beginTransaction();
		Query query = this.getVoyageIndexByVoyageQuery(p_session, null,
				p_firstResult, p_fetchSize, p_option);
		List list = query.list();
		//transaction.commit();

		return this.prepareResponse(list, p_option);
	}

}
