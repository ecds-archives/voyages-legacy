package edu.emory.library.tas.web.maps;

import edu.emory.library.tas.GISPortLocation;
import edu.emory.library.tas.web.UtilsJSF;

public class PointOfInterest
{
	
	private double x;
	private double y;
	private String name;
	private String text;
	
	public PointOfInterest(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public PointOfInterest(double x, double y, String name, String text)
	{
		this.x = x;
		this.y = y;
		this.name = name;
		this.text = text;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getNameJavaScriptSafe()
	{
		return UtilsJSF.escapeStringForJS(name);
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public String getTextJavaScriptSafe()
	{
		return UtilsJSF.escapeStringForJS(text);
	}
	
	public double getX()
	{
		return x;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
	public boolean equals(Object o) {
		if (o instanceof PointOfInterest) {
			PointOfInterest that = (PointOfInterest)o;
			return this.x == that.x && this.y == that.y;
		} else if (o instanceof GISPortLocation) {
			GISPortLocation that = (GISPortLocation)o;
			return this.x == that.getX() && this.y == that.getY();
		}
		return false;
	}
	
}
