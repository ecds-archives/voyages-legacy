package edu.emory.library.tast.dm;

public abstract class EstimatesRegion extends Location
{
	
	private boolean showOnMap;
	
	public abstract EstimatesArea getAbstractArea();

	public boolean isShowOnMap()
	{
		return showOnMap;
	}

	public void setShowOnMap(boolean showOnMap)
	{
		this.showOnMap = showOnMap;
	}
	
}
