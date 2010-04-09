/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.images;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * Tag used for gallery component.
 * Follows general structure for JSF components tag.
 *
 */
public class GalleryTag extends UIComponentTag
{
	
	private String thumbnailWidth;
	private String thumbnailHeight;
	private String columnsCount;
	private String images;
	private String action;
	private String selectedImageId;
	private String showLabels;

	public String getComponentType()
	{
		return "Gallery";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		GalleryComponent gallery = (GalleryComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (thumbnailWidth != null && isValueReference(thumbnailWidth))
		{
			ValueBinding vb = app.createValueBinding(thumbnailWidth);
			gallery.setValueBinding("thumbnailWidth", vb);
		}
		else
		{
			gallery.setThumbnailWidth(Integer.parseInt(thumbnailWidth));
		}

		if (thumbnailHeight != null && isValueReference(thumbnailHeight))
		{
			ValueBinding vb = app.createValueBinding(thumbnailHeight);
			gallery.setValueBinding("thumbnailHeight", vb);
		}
		else
		{
			gallery.setThumbnailHeight(Integer.parseInt(thumbnailHeight));
		}
		
		if (columnsCount != null && isValueReference(columnsCount))
		{
			ValueBinding vb = app.createValueBinding(columnsCount);
			gallery.setValueBinding("columnsCount", vb);
		}
		else
		{
			gallery.setColumnsCount(Integer.parseInt(columnsCount));
		}

		if (images != null && isValueReference(images))
		{
			ValueBinding vb = app.createValueBinding(images);
			gallery.setValueBinding("images", vb);
		}
		if (action != null && isValueReference(images))
		{
			gallery.setAction(action);
		}
		
		if (selectedImageId != null && isValueReference(selectedImageId))
		{
			ValueBinding vb = app.createValueBinding(selectedImageId);
			gallery.setValueBinding("selectedImageId", vb);
		}
		
		if (showLabels != null && isValueReference(showLabels))
		{
			ValueBinding vb = app.createValueBinding(showLabels);
			gallery.setValueBinding("showLabels", vb);
		}
		else
		{
			gallery.setShowLabels(Boolean.parseBoolean(showLabels));
		}

	}

	public String getColumnsCount()
	{
		return columnsCount;
	}

	public void setColumnsCount(String columnsCount)
	{
		this.columnsCount = columnsCount;
	}

	public String getImages()
	{
		return images;
	}

	public void setImages(String images)
	{
		this.images = images;
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

	public void setThumbnailWidth(String thumbnailWidth)
	{
		this.thumbnailWidth = thumbnailWidth;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSelectedImageId() {
		return selectedImageId;
	}

	public void setSelectedImageId(String selectedImageId) {
		this.selectedImageId = selectedImageId;
	}

	public String getShowLabels()
	{
		return showLabels;
	}

	public void setShowLabels(String showLabels)
	{
		this.showLabels = showLabels;
	}

}
