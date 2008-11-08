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
    private static Configuration configuration = null;
    
    private static void ensureSessionFactory()
    {
    	if (sessionFactory == null)
    	{
	        try
	        {
	            // Create the SessionFactory from hibernate.cfg.xml
	        	configuration = new Configuration().configure();
	            sessionFactory = configuration.buildSessionFactory();
	            
	        } catch (Throwable ex)
	        {
	            // Make sure you log the exception, as it might be swallowed
	            System.err.println("Initial SessionFactory creation failed." + ex);
	            throw new ExceptionInInitializerError(ex);
	        }
    	}
    }
    
    public synchronized static Configuration getConfiguration()
    {
    	ensureSessionFactory();
    	return configuration;
    }
    
    public static String getTableName(String className)
    {
    	ensureSessionFactory();
    	return configuration.getClassMapping(className).getTable().getName();
    }

    public static String getTableName(Class clazz)
    {
    	return getTableName(clazz.getCanonicalName());
    }

    public synchronized static Session getSession()
    {
    	ensureSessionFactory();
    	return sessionFactory.openSession();
    }
    
    public static SessionFactory getSessionFactory()
    {
    	ensureSessionFactory();
        return sessionFactory;
    }

    
}