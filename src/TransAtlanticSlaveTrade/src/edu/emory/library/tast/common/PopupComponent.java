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
package edu.emory.library.tast.common;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class PopupComponent extends UIComponentBase
{
	
	private boolean display = false;
	
	private boolean widthSet = false;
	private int width = 200;
	
	private boolean heightSet = false;
	private int height = 160;

	public String getFamily()
	{
		return null;
	}

	public boolean getRendersChildren()
	{
		return true;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = new Integer(width);
		values[2] = new Integer(height);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		width = ((Integer) values[1]).intValue();
		height = ((Integer) values[2]).intValue();
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// displayed only once
		if (!this.display)
			return;
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();

		// id's
		String mainId = getClientId(context);
		String blackoutElementId = getClientId(context) + "_blackout";
		String contentElementId = getClientId(context) + "_content";
	
		// JS registration
		StringBuffer regJS = new StringBuffer();
		regJS.append("Popups.registerPopup(new Popup(");
		
		// main id
		regJS.append("'").append(mainId).append("'");
		regJS.append(", ");
		
		// blackoutElementId
		regJS.append("'").append(blackoutElementId).append("'");
		regJS.append(", ");

		// contentElementId
		regJS.append("'").append(contentElementId).append("'");
		regJS.append(", ");

		// width & height
		regJS.append(this.width);
		regJS.append(", ");
		regJS.append(this.height);

		// render JS
		regJS.append("));");
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);
		
		// blackout
		writer.startElement("div", this);
		writer.writeAttribute("id", blackoutElementId, null);
		writer.writeAttribute("class", "popup-blackout", null);
		writer.writeAttribute("style", "display: none", null);
		
		// content
		writer.startElement("div", this);
		writer.writeAttribute("id", contentElementId, null);
		writer.writeAttribute("class", "popup-content", null);
		writer.writeAttribute("style", "display: none", null);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{

		// displayed only once
		if (this.display)
			JsfUtils.renderChildren(context, this);
		
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
		
		// displayed only once
		if (!this.display)
			return;

		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// close blackout & content
		writer.endElement("div");
		writer.endElement("div");
		
	}

	public String createCloseJavaScript()
	{
		return "Popups.close('" + getClientId(getFacesContext()) + "')";
	}

	public void display()
	{
		this.display = true;
	}

	public int getWidth()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"width", widthSet, width);
	}

	public void setWidth(int width)
	{
		this.widthSet = true;
		this.width = width;
	}

	public int getHeight()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"height", heightSet, height);
	}

	public void setHeight(int height)
	{
		this.heightSet = true;
		this.height = height;
	}

}