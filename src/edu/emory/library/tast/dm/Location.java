package edu.emory.library.tast.dm;


public abstract class Location extends Dictionary
{
	
	private double latitude;
	private double longitude;

	public double getLatitude()
	{
		return latitude;
	}
	
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public double getX()
	{
		return latitude;
	}
	
	public void setX(double x)
	{
		this.latitude = x;
	}
	
	public double getY()
	{
		return longitude;
	}
	
	public void setY(double y)
	{
		this.longitude = y;
	}

}
