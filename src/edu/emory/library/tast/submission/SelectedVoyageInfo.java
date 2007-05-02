package edu.emory.library.tast.submission;

public class SelectedVoyageInfo
{
	
	private long voyageId;
	private String captain;
	private String shipname;
	private String year;
	
	public SelectedVoyageInfo(long voyageId, String captain, String shipname, String year)
	{
		this.voyageId = voyageId;
		this.captain = captain;
		this.shipname = shipname;
		this.year = year;
	}

	public String getCaptain()
	{
		return captain;
	}
	
	public String getShipname()
	{
		return shipname;
	}
	
	public long getVoyageId()
	{
		return voyageId;
	}
	
	public String getYear()
	{
		return year;
	}

}