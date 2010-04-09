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
package edu.emory.library.tast.master;

public class HelpLink
{
	
	private String label;
	private String href;
	private boolean openInNewWindow;
	
	public HelpLink(String label, String href, boolean openInNewWindow)
	{
		this.label = label;
		this.href = href;
		this.openInNewWindow = openInNewWindow;
	}

	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public boolean isOpenInNewWindow()
	{
		return openInNewWindow;
	}

	public void setOpenInNewWindow(boolean openInNewWindow)
	{
		this.openInNewWindow = openInNewWindow;
	}

}
