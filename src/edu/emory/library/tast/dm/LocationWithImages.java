package edu.emory.library.tast.dm;

import java.util.Set;
import java.util.SortedSet;

public class LocationWithImages extends Location
{
	
	private SortedSet images;

	public SortedSet getImages()
	{
		return images;
	}

	public void setImages(SortedSet images)
	{
		this.images = images;
	}

}
