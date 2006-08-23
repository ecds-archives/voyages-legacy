package edu.emory.library.tast.ui.images;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ImageSelectedEvent extends FacesEvent
{
	
	private int imageId;

	public ImageSelectedEvent(UIComponent component, int imageId)
	{
		super(component);
		this.imageId = imageId;
	}

	private static final long serialVersionUID = -6270202691622194726L;

	public boolean isAppropriateListener(FacesListener arg0)
	{
		return false;
	}

	public void processListener(FacesListener arg0)
	{
	}

	public int getImageId()
	{
		return imageId;
	}

	public void setImageId(int imageId)
	{
		this.imageId = imageId;
	}

}
