package edu.emory.library.tast.dm;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class LocationWithImages extends Location
{
	
	private SortedSet images;

	public SortedSet getImages()
	{
		return images;
	}
	
	public SortedSet getReadyToGoImages() {
		SortedSet set = new TreeSet();
		for (Iterator iter = images.iterator(); iter.hasNext();) {
			Image element = (Image) iter.next();
			if (element.isReadyToGo()) {
				set.add(element);
			}
		}
		return set;
	}

	public void setImages(SortedSet images)
	{
		this.images = images;
	}

}
