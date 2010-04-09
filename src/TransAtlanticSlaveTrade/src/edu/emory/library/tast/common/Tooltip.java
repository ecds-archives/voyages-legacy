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

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

public class Tooltip
{
	
	private String id;
	private String text;
	
	public Tooltip(String id, String text)
	{
		this.id = id;
		this.text = text;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public void renderTooltip(ResponseWriter writer, UIComponent component) throws IOException
	{
		writer.startElement("div", component);
		writer.writeAttribute("id", id, null);
		writer.writeAttribute("class", "tooltip", null);
		writer.writeAttribute("style", "position: absolute; visibility: hidden;", null);
		writer.write(text);
		writer.endElement("div");
	}

	public void renderMouseOverEffects(ResponseWriter writer) throws IOException
	{
		writer.writeAttribute("onmouseover", "tooltipShow(event, '" + id + "')", null);
		writer.writeAttribute("onmouseout", "tooltipHide(event, '" + id + "')", null);
		writer.writeAttribute("onmousemove", "tooltipMove(event, '" + id + "')", null);
	}

}
