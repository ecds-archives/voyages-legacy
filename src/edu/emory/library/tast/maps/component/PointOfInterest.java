package edu.emory.library.tast.maps.component;

import edu.emory.library.tast.dm.Location;
import edu.emory.library.tast.util.JsfUtils;

public class PointOfInterest
{
	
	private double x;
	private double y;
	private String text;
	private String label;
	private String[] symbols;
	private int showAtZoom = -1;
	
	public PointOfInterest(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public PointOfInterest(double x, double y, String label, String text)
	{
		this.x = x;
		this.y = y;
		this.label = label;
		this.text = text;
	}

	public PointOfInterest(double x, double y, String label)
	{
		this.x = x;
		this.y = y;
		this.label = label;
	}

	public PointOfInterest(double x, double y, int showAtZoom)
	{
		this.x = x;
		this.y = y;
		this.showAtZoom = showAtZoom;
	}

	public PointOfInterest(double x, double y, String label, String text, int showAtZoom)
	{
		this.x = x;
		this.y = y;
		this.label = label;
		this.text = text;
		this.showAtZoom = showAtZoom;
	}

	public PointOfInterest(double x, double y, String label, int showAtZoom)
	{
		this.x = x;
		this.y = y;
		this.label = label;
		this.showAtZoom = showAtZoom;
	}

	public String getLabelJavaScriptSafe()
	{
		return JsfUtils.escapeStringForJS(label);
	}

	public String getTextJavaScriptSafe()
	{
		return JsfUtils.escapeStringForJS(text);
	}

	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
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
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String[] getSymbols() {
		return symbols;
	}

	public void setSymbols(String[] symbols) {
		this.symbols = symbols;
	}

	public int getShowAtZoom()
	{
		return showAtZoom;
	}

	public void setShowAtZoom(int showAtZoom)
	{
		this.showAtZoom = showAtZoom;
	}

	public boolean equals(Object o) {
		if (o instanceof PointOfInterest) {
			PointOfInterest that = (PointOfInterest)o;
			return this.x == that.x && this.y == that.y;
		} else if (o instanceof Location) {
			Location that = (Location)o;
			return this.x == that.getX() && this.y == that.getY();
		}
		return false;
	}

}
