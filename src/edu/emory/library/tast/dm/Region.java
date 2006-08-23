package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.query.QueryValue;

public class Region extends Location
{
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new StringAttribute("id", "Region"));
		attributes.put("name", new StringAttribute("name", "Region"));
		attributes.put("x", new NumericAttribute("x", "Region"));
		attributes.put("y", new NumericAttribute("y", "Region"));
		attributes.put("ports", new NumericAttribute("ports", "Region"));
	}
	
	private Set ports;

	public Set getPorts()
	{
		return ports;
	}

	public void setPorts(Set ports)
	{
		this.ports = ports;
	}
	
	public static Region[] getRegionsArray()
	{
		List list = getRegionsList();
		Region[] regions = new Region[list.size()];
		list.toArray(regions);
		return regions;
	}
	
	public static List getRegionsList()
	{
		QueryValue qValue = new QueryValue("Region");
		qValue.setOrderBy(new Attribute[] {Region.getAttribute("name")});
		qValue.setCacheable(true);
		return qValue.executeQueryList();
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
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