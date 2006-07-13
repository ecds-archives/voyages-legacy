package edu.emory.library.tas.util;

import org.hibernate.*;
import org.hibernate.cfg.*;

/**
 * Class helper to operate with Hibernate.
 * @author Pawel Jurczyk
 *
 */
public class HibernateUtil {

	/**
	 * Used session factory.
	 */
    private static final SessionFactory sessionFactory;
    
    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
            
            
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Gets session that can be used.
     * @return
     */
    public synchronized static Session getSession() {
    	return sessionFactory.openSession();
    }
    
    /**
     * Gets session factory.
     * @return
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    
}