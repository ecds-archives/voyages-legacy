package edu.emory.library.tast.dm;

import java.util.Set;

public class EstimatesArea extends Location
{
	
	private Set regions;
	private boolean showOnMap;
	
	public boolean isShowOnMap()
	{
		return showOnMap;
	}

	public void setShowOnMap(boolean showOnMap)
	{
		this.showOnMap = showOnMap;
	}

	public Set getRegions()
	{
		return regions;
	}

	public void setRegions(Set regions)
	{
		this.regions = regions;
	}

}
