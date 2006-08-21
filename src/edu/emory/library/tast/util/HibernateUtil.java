package edu.emory.library.tast.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.*;
import org.hibernate.cfg.*;

import edu.emory.library.tast.AppConfig;

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
            prepareDatabase();
            
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
    
    private static void prepareDatabase() {
    	
    	String stmtDir = AppConfig.getConfiguration().getString("sql.scriptdir");
		Session session = getSession();
		try {
			Statement statement = session.connection().createStatement();
			System.out.println("Will execute SQL scripts from directory: " + stmtDir);
			
			File file = new File(stmtDir);
			if (!file.exists() || !file.canRead()) {
				System.err.println("Insufficient privilidges to directory or directory does not exist: " + stmtDir);
				return;
			}
			String[] files = file.list();
			for (int i = 0; i < files.length; i++) {
				File script = new File(files[i]);
				if (script.isDirectory()) {
					continue;
				}
				
				System.out.print("   Execution of: " + files[i] + "... ");
				
				if (!file.canRead()) {
					System.err.println("Insufficient privilidges to script: " + files[i] + " - skpipped!");
					return;
				}
				
				BufferedReader reader = new BufferedReader(new FileReader(stmtDir + script));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				statement.execute(buffer.toString());
				System.out.println("OK");
			}
			statement.close();
			session.flush();
			session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
     * Gets session factory.
     * @return
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    
}