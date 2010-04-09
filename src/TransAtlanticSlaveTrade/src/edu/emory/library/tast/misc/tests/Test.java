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
package edu.emory.library.tast.misc.tests;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

public class Test
{
	
	public static void main(String[] args)
	{
		
		Session session = HibernateConn.getSession();
		Transaction trans = session.beginTransaction();
		
		//order by v.mjbyptimp.order
		
		String hsql;

		hsql =
			"select distinct v.mjbyptimp " +
			"from Voyage v " +
			"order by v.mjbyptimp.order.region.area.order, v.mjbyptimp.region.order, v.mjbyptimp.order";

		hsql =
			"select distinct v.mjbyptimp, v.mjbyptimp.region, v.mjbyptimp.order.region.area " +
			"from Voyage v " +
			"order by v.mjbyptimp.order.region.area.order, v.mjbyptimp.region.order, v.mjbyptimp.order";
		
		hsql =
			"select v.mjbyptimp.id " +
			"from Voyage v " +
			"group by v.mjbyptimp";

		hsql =
			"from Port p " +
			"where p in (select v.mjbyptimp from Voyage v) or (select v.mjbyptimp from Voyage v) " +
			"order by p.region.area.order, p.region.order, p.order";

		TastDbQuery qv = new TastDbQuery("Voyage");
		qv.setDistinct(true);

		qv.addPopulatedAttribute(Voyage.getAttribute("mjbyptimp"));
		qv.addPopulatedAttribute(new SequenceAttribute( new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("region")}));
		qv.addPopulatedAttribute(new SequenceAttribute( new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("region"), Region.getAttribute("area")}));

		qv.setOrder(TastDbQuery.ORDER_ASC);
		qv.setOrderBy(new Attribute[] {
				new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("region"), Region.getAttribute("area"), Area.getAttribute("order")}),
				new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("region"), Region.getAttribute("order")}),
				new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("order")})});
		
		Query query = session.createQuery(hsql);
		List list = query.list();
		//List List = qv.executeQueryList(session); 
		
		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			//Port port = (Port) iter.next();
			//System.out.println(port.getOrder() + " " + port.getName() + " " + port.getRegion().getName() + " " + port.getRegion().getArea().getName());
		}
		
		trans.commit();
		session.close();
		
	}

}
