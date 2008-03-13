package edu.emory.library.tast.common.voyage;


public class VoyageRoutePlace
{
	
	private long id;
	private String placeName;
	private String eventDescription;
	private double longitude;
	private double latitude;
	private VoyageRouteSymbol symbol;
	
	public VoyageRoutePlace(long id, String name, String text, double longitude, double latitude, VoyageRouteSymbol symbol)
	{
		this.id = id;
		this.placeName = name;
		this.eventDescription = text;
		this.longitude = longitude;
		this.latitude = latitude;
		this.symbol = symbol;
	}
	public long getId()
	{
		return id;
	}

	public String getPlaceName()
	{
		return placeName;
	}
	
	public String getEventDescription()
	{
		return eventDescription;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public VoyageRouteSymbol getSymbol()
	{
		return symbol;
	}

}