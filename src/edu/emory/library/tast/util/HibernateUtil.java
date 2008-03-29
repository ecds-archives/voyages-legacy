package edu.emory.library.tast.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Class helper to operate with Hibernate.
 * @author Pawel Jurczyk
 *
 */
public class HibernateUtil {
	
	/**
	 * Used session factory.
	 */
    private static SessionFactory sessionFactory = null;
    
    private static void ensureSessionFactory()
    {
    	if (sessionFactory == null)
    	{
	        try
	        {
	            // Create the SessionFactory from hibernate.cfg.xml
	            sessionFactory = new Configuration().configure().buildSessionFactory();
	            
	        } catch (Throwable ex)
	        {
	            // Make sure you log the exception, as it might be swallowed
	            System.err.println("Initial SessionFactory creation failed." + ex);
	            throw new ExceptionInInitializerError(ex);
	        }
    	}
    }

    /**
     * Gets session that can be used.
     * @return
     */
    public synchronized static Session getSession()
    {
    	ensureSessionFactory();
    	return sessionFactory.openSession();
    }
    
	/**
     * Gets session factory.
     * @return
     */
    public static SessionFactory getSessionFactory()
    {
    	ensureSessionFactory();
        return sessionFactory;
    }

    
}