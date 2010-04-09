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

import edu.emory.library.tast.util.StringUtils;

/**
 * Just class which represents image.
 * It has a bunch of properties and getters/setters.
 *
 */
public class GalleryImage
{
	
	private String id;
	private String imageName;
	private String label;
	private String description;
	
	public GalleryImage(String id, String imageName, String label, String description)
	{
		this.imageName = imageName;
		this.label = label;
		this.description = description;
		this.id = id;
	}

	public GalleryImage(String id, String imageName, String label)
	{
		this.imageName = imageName;
		this.label = label;
		this.id = id;
	}

	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public boolean hasDescription()
	{
		return !StringUtils.isNullOrEmpty(description);
	}

	public String getImageName()
	{
		return imageName;
	}
	
	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}