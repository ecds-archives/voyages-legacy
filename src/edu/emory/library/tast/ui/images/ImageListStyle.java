package edu.emory.library.tast.ui.images;

import java.io.Serializable;


public class ImageListStyle implements Serializable
{
	
	private static final long serialVersionUID = 6788716676709166618L;
	
	private static final int TABLE = 1;
	private static final int GALLERY = 2;
	
	public static ImageListStyle Table = new ImageListStyle(TABLE); 
	public static ImageListStyle Gallery = new ImageListStyle(GALLERY); 
	
	private int style;
	
	private ImageListStyle(int style)
	{
		this.style = style;
	}
	
	public boolean isList()
	{
		return style == TABLE; 
	}

	public boolean Thumbnails()
	{
		return style == GALLERY; 
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ImageListStyle)
		{
			return ((ImageListStyle)obj).style == style;
		}
		else
		{
			return false;
		}
	}
	
	public String toString()
	{
		switch (style)
		{
		case TABLE: return "table"; 
		case GALLERY: return "gallery";
		default: return ""; 
		}
	}
	
	public static ImageListStyle parse(String str)
	{
		if ("table".equalsIgnoreCase(str))
			return Table; 
		else
			return Gallery; 
	}

}