package edu.emory.library.tast.ui.images.admin;

import java.io.Serializable;

public class ImageListColumn implements Serializable
{
	
	private static final long serialVersionUID = 977215386719459666L;
	
	private String name;
	
	public ImageListColumn(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
