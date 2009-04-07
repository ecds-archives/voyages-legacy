package edu.emory.library.tast.maps.component;

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
	
	public String getCssClassForMapControl()
	{
		switch (position)
		{
			case POSITION_TOP_LEFT: return "minimap-control-nw";
			case POSITION_TOP_RIGHT: return "minimap-control-ne";
			case POSITION_BOTTOM_LEFT: return "minimap-control-sw";
			case POSITION_BOTTOM_RIGHT: return "minimap-control-se";
			default: return null;
		}
	}
	
	public String getCssClassForToggleButton(boolean visible)
	{
		if (visible)
		{
			switch (position)
			{
				case POSITION_TOP_LEFT: return "minimap-toggle-nw-expanded";
				case POSITION_TOP_RIGHT: return "minimap-toggle-ne-expanded";
				case POSITION_BOTTOM_LEFT: return "minimap-toggle-sw-expanded";
				case POSITION_BOTTOM_RIGHT: return "minimap-toggle-se-expanded";
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
	
	public static MiniMapPosition parse(String str)
	{
		if (str == null) return MiniMapPosition.BottomRight;
		str = str.toLowerCase();
		boolean top = str.indexOf("top") != -1;
		boolean left = str.indexOf("left") != -1;
		if (top)
		{
			if (left) return MiniMapPosition.TopLeft;
			else return MiniMapPosition.TopRight; 
		}
		else
		{
			if (left) return MiniMapPosition.BottomLeft;
			else return MiniMapPosition.BottomRight; 
		}
	}

}