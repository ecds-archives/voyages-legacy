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