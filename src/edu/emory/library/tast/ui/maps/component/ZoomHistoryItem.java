package edu.emory.library.tast.ui.maps.component;

import java.io.Serializable;

public class ZoomHistoryItem implements Serializable
{
	
	private static final long serialVersionUID = -435520343082690652L;
	
	private int scale;
	private double centerX;
	private double centerY;
	
	public ZoomHistoryItem()
	{
		this.scale = 1;
		this.centerX = 0;
		this.centerY = 0;
	}

	public ZoomHistoryItem(int scale, double centerX, double centerY)
	{
		this.scale = scale;
		this.centerX = centerX;
		this.centerY = centerY;
	}

	public double getCenterX()
	{
		return centerX;
	}
	
	public void setCenterX(double centerX)
	{
		this.centerX = centerX;
	}
	
	public double getCenterY()
	{
		return centerY;
	}
	
	public void setCenterY(double centerY)
	{
		this.centerY = centerY;
	}
	
	public int getScale()
	{
		return scale;
	}
	
	public void setScale(int scale)
	{
		this.scale = scale;
	}

}
