/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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

	public PointOfInterest(double x, double y, String[] symbols)
	{
		this.x = x;
		this.y = y;
		this.symbols = symbols;
	}

	public PointOfInterest(double x, double y, String[] symbols, String label, String text)
	{
		this.x = x;
		this.y = y;
		this.label = label;
		this.text = text;
		this.symbols = symbols;
	}

	public PointOfInterest(double x, double y, String[] symbols, String label)
	{
		this.x = x;
		this.y = y;
		this.label = label;
		this.symbols = symbols;
	}

	public PointOfInterest(double x, double y, String[] symbols, int showAtZoom)
	{
		this.x = x;
		this.y = y;
		this.showAtZoom = showAtZoom;
		this.symbols = symbols;
	}

	public PointOfInterest(double x, double y, String[] symbols, String label, String text, int showAtZoom)
	{
		this.x = x;
		this.y = y;
		this.label = label;
		this.text = text;
		this.showAtZoom = showAtZoom;
		this.symbols = symbols;
	}

	public PointOfInterest(double x, double y, String[] symbols, String label, int showAtZoom)
	{
		this.x = x;
		this.y = y;
		this.label = label;
		this.showAtZoom = showAtZoom;
		this.symbols = symbols;
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
