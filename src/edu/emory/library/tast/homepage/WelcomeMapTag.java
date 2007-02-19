package edu.emory.library.tast.homepage;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class WelcomeMapTag extends UIComponentTag
{
	
	private String imageUrl;
	private String imageWidth;
	private String imageHeight;
	private String places;

	public String getComponentType()
	{
		return "WelcomeMap";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		WelcomeMapComponent welcomeMap = (WelcomeMapComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (imageUrl != null && isValueReference(imageUrl))
		{
			ValueBinding vb = app.createValueBinding(imageUrl);
			welcomeMap.setValueBinding("imageUrl", vb);
		}
		else
		{
			welcomeMap.setImageUrl(imageUrl);
		}

		if (imageWidth != null && isValueReference(imageWidth))
		{
			ValueBinding vb = app.createValueBinding(imageWidth);
			welcomeMap.setValueBinding("imageWidth", vb);
		}
		else
		{
			welcomeMap.setImageWidth(Integer.parseInt(imageWidth));
		}
		
		if (imageHeight != null && isValueReference(imageHeight))
		{
			ValueBinding vb = app.createValueBinding(imageHeight);
			welcomeMap.setValueBinding("imageHeight", vb);
		}
		else
		{
			welcomeMap.setImageHeight(Integer.parseInt(imageHeight));
		}

		if (places != null && isValueReference(places))
		{
			ValueBinding vb = app.createValueBinding(places);
			welcomeMap.setValueBinding("places", vb);
		}

	}

	public String getImageHeight()
	{
		return imageHeight;
	}

	public void setImageHeight(String imageHeight)
	{
		this.imageHeight = imageHeight;
	}

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public String getImageWidth()
	{
		return imageWidth;
	}

	public void setImageWidth(String imageWidth)
	{
		this.imageWidth = imageWidth;
	}

	public String getPlaces()
	{
		return places;
	}

	public void setPlaces(String places)
	{
		this.places = places;
	}

}
