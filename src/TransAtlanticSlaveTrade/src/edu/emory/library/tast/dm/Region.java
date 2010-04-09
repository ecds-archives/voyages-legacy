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
package edu.emory.library.tast.dm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.attributes.AreaAttribute;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Region extends LocationWithImages
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Region", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Region"));
		attributes.put("x", new NumericAttribute("x", "Region", NumericAttribute.TYPE_FLOAT));
		attributes.put("y", new NumericAttribute("y", "Region", NumericAttribute.TYPE_FLOAT));
		attributes.put("latitude", new NumericAttribute("latitude", "Region", NumericAttribute.TYPE_FLOAT));
		attributes.put("longitude", new NumericAttribute("longitude", "Region", NumericAttribute.TYPE_FLOAT));
		attributes.put("ports", new NumericAttribute("ports", "Region", NumericAttribute.TYPE_LONG));
		attributes.put("area", new AreaAttribute("area", "Region"));
		attributes.put("order", new NumericAttribute("order", "Region", NumericAttribute.TYPE_INTEGER));
		attributes.put("showAtZoom", new NumericAttribute("showAtZoom", "Region", NumericAttribute.TYPE_INTEGER));
		attributes.put("showOnMap", new BooleanAttribute("showOnMap", "Region"));
	}
	
	private Set ports;
	private Area area;
	private int order;
	private boolean showOnMap;
	
	public Area getArea()
	{
		return area;
	}

	public void setArea(Area area)
	{
		this.area = area;
	}

	public Set getPorts()
	{
		return ports;
	}

	public void setPorts(Set ports)
	{
		this.ports = ports;
	}
	
	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(Region.class, sess, "order");
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(Region.class, sess, orderBy);
	}

	public static Region loadById(Session sess, long portId)
	{
		return (Region) Dictionary.loadById(Region.class, sess, portId);
	}
	
	public static Region loadById(Session sess, String portId)
	{
		return (Region) Dictionary.loadById(Region.class, sess, portId);
	}

	public static Attribute getAttribute(String name)
	{
		Attribute attribute = (Attribute)attributes.get(name);
		if (attribute == null) throw new RuntimeException("invalid region attribute '" + name + "'");
		return attribute;
	}

	public static String[] regionNamesToArray(List regions)
	{
		String[] names = new String[regions.size()];
		
		int i = 0;
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			names[i++] = region.getName();
		}
		
		return names;
	}
	
	public static Map createIdIndexMap(List regions)
	{
		Map map = new HashMap();
		
		int i = 0;
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			map.put(region.getId(), new Integer(i++));
		}
		
		return map;
	}

	public static List sortRegionsByArea(List regions)
	{
		Region regionsArray[] = new Region[regions.size()];
		regions.toArray(regionsArray);
		Arrays.sort(regionsArray, new Comparator() {
			public int compare(Object obj0, Object obj1)
			{
				Area a0 = ((Region) obj0).getArea();
				Area a1 = ((Region) obj1).getArea();
				if (a0 == null) return -1;
				if (a1 == null) return 1;
				return a0.getName().compareTo(a1.getName());
			}});
		List newList = new ArrayList();
		for (int i = 0; i < regionsArray.length; i++) newList.add(regionsArray[i]);
		return newList;
	}
	
	public static void main(String[] args)
	{
		
		Session sess = HibernateConn.getSession();
		Transaction transaction = sess.beginTransaction();
		
		Region africa = Region.loadById(sess, 60000);
		
		for (Iterator iter = africa.getReadyToGoImages().iterator(); iter.hasNext();)
		{
			Image image = (Image) iter.next();
			System.out.println(image.getTitle());
		}
		
		transaction.commit();
		sess.close();
		
	}

	public boolean isShowOnMap()
	{
		return showOnMap;
	}

	public void setShowOnMap(boolean showOnMap)
	{
		this.showOnMap = showOnMap;
	}
	
//	public static void main(String[] args)
//	{
//		
//		Region[] regions = getRegions();
//		for (int i = 0; i < regions.length; i++)
//		{
//			Region region = regions[i];
//			System.out.println(region.getId() + " : " + region.getName());
//			for (Iterator iterPort = region.getPorts().iterator(); iterPort.hasNext();)
//			{
//				Port port = (Port) iterPort.next();
//				System.out.println(port.getId() + " : " + port.getName());
//			}
//			System.out.println("============================================");
//		}
//		
//	}

}