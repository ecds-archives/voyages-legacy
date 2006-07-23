package edu.emory.library.tas.web.maps;

import java.io.Serializable;

public class MouseMode implements Serializable
{

	private static final long serialVersionUID = 2445965207252391047L;
	
	private static final int MODE_PAN = 1;
	private static final int MODE_ZOOM = 2;
	
	public static final MouseMode Zoom = new MouseMode(MODE_ZOOM); 
	public static final MouseMode Pan = new MouseMode(MODE_PAN); 
	
	private int mode = 0;

	private MouseMode(int mode)
	{
		this.mode = mode;
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof MouseMode)
		{
			return ((MouseMode)obj).mode == mode;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isZoom()
	{
		return mode == MODE_ZOOM;
	}
	
	public boolean isPan()
	{
		return mode == MODE_PAN;
	}
	
	public String toString()
	{
		switch (mode)
		{
			case MODE_PAN: return "pan";
			case MODE_ZOOM: return "zoom";
			default: return "";
		}
	}
	
	public static MouseMode parse(String value)
	{
		if (value == null || value.equals("pan"))
			return Pan;
		else
			return Zoom;
	}
	
}