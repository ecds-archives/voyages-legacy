package edu.emory.library.tast.db;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;

/**
 * Class helper to operate with Hibernate.
 * @author Pawel Jurczyk
 *
 */
public class HibernateConn {
	
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

    public static String getColumnName(String className, String property)
    {
    	ensureSessionFactory();
    	for (Iterator iterator = configuration.getClassMapping(className).getProperty(property).getColumnIterator(); iterator.hasNext();)
		{
    		return ((Column)iterator.next()).getName();
		}
    	return null;
    }
    
    public static String getColumnName(Class clazz, String property)
    {
    	return getColumnName(clazz.getCanonicalName(), property);
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