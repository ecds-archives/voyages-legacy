package edu.emory.library.tast.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.MethodAppendix;
import edu.emory.library.tast.util.HibernateUtil;




public class MethodAppendixBean
{
	private List datalist;
	
	public MethodAppendixBean(){
	}
	 
	public List getDatalist()
	{		
		 
		System.out.println("---------------------begin---------------------");
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT * from method_appendix");
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		List result = sess.createSQLQuery(hql.toString()).list();
		
		System.out.println("------------------------size-------------");
		System.out.println(result.size());
		datalist=new ArrayList();
		
		for (Iterator dataIt = result.iterator(); dataIt.hasNext();)
		{
			MethodAppendix item=(MethodAppendix)dataIt.next();
			datalist.add(new MethodAppendix(item.getGroup(),item.getAve_em(),item.getNumber_em(),item.getSd_em(),
											item.getAve_disem(),item.getNumber_disem(),item.getSd_disem()));
			
		}
			
		
		transaction.commit();
		sess.close();
		return datalist;
	}
}
