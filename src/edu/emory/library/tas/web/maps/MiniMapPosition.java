package edu.emory.library.tas.web.maps;

import java.io.Serializable;

public class MiniMapPosition implements Serializable
{

	private static final long serialVersionUID = 2882397267325572222L;

	private static final int POSITION_TOP_LEFT = 1;
	private static final int POSITION_TOP_RIGHT = 2;
	private static final int POSITION_BOTTOM_LEFT = 3;
	private static final int POSITION_BOTTOM_RIGHT = 4;
	
	public static final MiniMapPosition TopLeft = new MiniMapPosition(POSITION_TOP_LEFT); 
	public static final MiniMapPosition TopRight = new MiniMapPosition(POSITION_TOP_RIGHT); 
	public static final MiniMapPosition BottomLeft = new MiniMapPosition(POSITION_BOTTOM_LEFT); 
	public static final MiniMapPosition BottomRight = new MiniMapPosition(POSITION_BOTTOM_RIGHT); 

	private int position;
	
	private MiniMapPosition(int position)
	{
		this.position = position;
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof MiniMapPosition)
		{
			return ((MiniMapPosition)obj).position == position;
		}
		else
		{
			return false;
		}
	}

	public String toString()
	{
		switch (position)
		{
			case POSITION_TOP_LEFT: return "top left";
			case POSITION_TOP_RIGHT: return "top right";
			case POSITION_BOTTOM_LEFT: return "bottom left";
			case POSITION_BOTTOM_RIGHT: return "bottom right";
			default: return null;
		}
	}
	
	public String getCssClass(boolean collapsed)
	{
		if (!collapsed)
		{
			switch (position)
			{
				case POSITION_TOP_LEFT: return "minimap-toggle-nw";
				case POSITION_TOP_RIGHT: return "minimap-toggle-ne";
				case POSITION_BOTTOM_LEFT: return "minimap-toggle-sw";
				case POSITION_BOTTOM_RIGHT: return "minimap-toggle-se";
				default: return null;
			}
		}
		else
		{
			switch (position)
			{
				case POSITION_TOP_LEFT: return "minimap-toggle-nw-collapsed";
				case POSITION_TOP_RIGHT: return "minimap-toggle-ne-collapsed";
				case POSITION_BOTTOM_LEFT: return "minimap-toggle-sw-collapsed";
				case POSITION_BOTTOM_RIGHT: return "minimap-toggle-se-collapsed";
				default: return null;
			}
		}
	}
	
	public MiniMapPosition parse(String str)
	{
		if (str == null) return MiniMapPosition.BottomRight;
		str = str.toLowerCase();
		boolean top = str.indexOf("top") != -1;
		boolean left = str.indexOf("left") != -1;
		if (top)
		{
			if (left) return TopLeft;
			else return TopRight; 
		}
		else
		{
			if (left) return BottomLeft;
			else return BottomRight; 
		}
	}

}