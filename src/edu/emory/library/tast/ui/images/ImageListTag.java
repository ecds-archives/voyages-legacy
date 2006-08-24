package edu.emory.library.tast.ui.images;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

public class ImageListTag extends UIComponentTagBase
{
	
	private String images;
	private String listStyle;
	private String onImageSelected;
	private String action;
	private String thumbnailWidth;
	private String thumbnailHeight;

	public String getComponentType()
	{
		return "ImageList";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		ImageListComponent imageList = (ImageListComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (images != null && isValueReference(images))
		{
			ValueBinding vb = app.createValueBinding(images);
			component.setValueBinding("images", vb);
		}

		if (listStyle != null && isValueReference(listStyle))
		{
			ValueBinding vb = app.createValueBinding(listStyle);
			component.setValueBinding("listStyle", vb);
		}
		else
		{
			imageList.setListStyle(ImageListStyle.parse(listStyle));
		}
		
		if (onImageSelected != null && isValueReference(onImageSelected))
		{
			MethodBinding mb = app.createMethodBinding(onImageSelected, new Class[] {ImageSelectedEvent.class});
			imageList.setOnImageSelected(mb);
		}
		
		if (action != null && isValueReference(action))
		{
			MethodBinding mb = app.createMethodBinding(action, null);
			imageList.setAction(mb);
		}

		if (thumbnailWidth != null && isValueReference(thumbnailWidth))
		{
			ValueBinding vb = app.createValueBinding(thumbnailWidth);
			component.setValueBinding("thumbnailWidth", vb);
		}
		else
		{
			imageList.setThumbnailWidth(Integer.parseInt(thumbnailWidth));
		}
		
		if (thumbnailHeight != null && isValueReference(thumbnailHeight))
		{
			ValueBinding vb = app.createValueBinding(thumbnailHeight);
			component.setValueBinding("thumbnailHeight", vb);
		}
		else
		{
			imageList.setThumbnailWidth(Integer.parseInt(thumbnailHeight));
		}

	}

	public String getListStyle()
	{
		return listStyle;
	}

	public void setListStyle(String listStyle)
	{
		this.listStyle = listStyle;
	}

	public String getOnImageSelected()
	{
		return onImageSelected;
	}

	public void setOnImageSelected(String onImageSelected)
	{
		this.onImageSelected = onImageSelected;
	}

	public String getImages()
	{
		return images;
	}

	public void setImages(String images)
	{
		this.images = images;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getThumbnailHeight()
	{
		return thumbnailHeight;
	}

	public void setThumbnailHeight(String thumbnailHeight)
	{
		this.thumbnailHeight = thumbnailHeight;
	}

	public String getThumbnailWidth()
	{
		return thumbnailWidth;
	}

	public void setThumbnailWidth(String listThumbnailWidth)
	{
		this.thumbnailWidth = listThumbnailWidth;
	}

}
