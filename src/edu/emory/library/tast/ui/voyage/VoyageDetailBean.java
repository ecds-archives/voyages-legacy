package edu.emory.library.tast.ui.voyage;

public class VoyageDetailBean
{
	
	private int voyageId;
	
	public void openVoyage(int voyageId)
	{
		this.voyageId = voyageId;
	}
	
	public int getTestValue()
	{
		return voyageId;
	}

}