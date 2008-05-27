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
	private MethodAppendix[] datalist;
	 
	public MethodAppendix[] getDatalist()
	{		
		 
		System.out.println("---------------------begin---------------------");
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT * from method_appendix");
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		List result = sess.createSQLQuery(hql.toString()).list();
		
		System.out.println("------------------------size-------------");
		System.out.println(result.size());
		datalist=new MethodAppendix[result.size()];
		int rows=0;
		for (Iterator dataIt = result.iterator(); dataIt.hasNext();)
		{
			MethodAppendix item=(MethodAppendix)dataIt.next();
			datalist[rows]=new MethodAppendix(item.getGroup(),item.getAve_em(),item.getNumber_em(),item.getSd_em(),
											item.getAve_disem(),item.getNumber_disem(),item.getSd_disem());
			rows++;
		}
			
		/*
		SimpleTableCell[][] datas = new SimpleTableCell[result.size()][];

		System.out.println("------------------------size-------------");
		System.out.println(result.size());
		int rowIndex = 0;
		for (Iterator dataIt = result.iterator(); dataIt.hasNext();)
		{	
			MethodAppendix data=(MethodAppendix)dataIt.next();
			System.out.println("-------------------group----------------------  "+data.getGroup());
			datas[rowIndex++] = new SimpleTableCell[] {
					new SimpleTableCell(data.getGroup())
					};

		}*/
		transaction.commit();
		sess.close();
		return datalist;
	}
}
