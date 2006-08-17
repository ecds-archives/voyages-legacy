package edu.emory.library.tast.dm;

import java.util.List;
import java.util.Set;

import edu.emory.library.tast.util.query.QueryValue;

public class Region extends Location
{
	
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
		qValue.setOrderBy(new String[] {"name"});
		qValue.setCacheable(true);
		return qValue.executeQueryList();
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