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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class SecondaryMenuComponent extends UIComponentBase
{
	
	private boolean activeItemIdSet = false;
	private String activeItemId;

	public String getFamily()
	{
		return null;
	}
	
	public boolean getRendersChildren()
	{
		return true;
	}
	
	private void encodeItems(FacesContext context, ResponseWriter writer, List menuItems, int level, String activeMenuItemId) throws IOException
	{
		
		for (Iterator iterator = menuItems.iterator(); iterator.hasNext();)
		{
			SecondaryMenuItemComponent menuItem = (SecondaryMenuItemComponent) iterator.next();

			boolean hasLink = !StringUtils.isNullOrEmpty(menuItem.getHref());
			
			String menuItemCssClass = "secondary-menu-item-" + level;
			
			if (activeMenuItemId != null && activeMenuItemId.equals(menuItem.getMenuId()))
				menuItemCssClass += "-selected";

			writer.startElement("div", this);
			writer.writeAttribute("class", menuItemCssClass, null);
			if (hasLink)
			{
				writer.startElement("a", this);
				writer.writeAttribute("href", menuItem.getHref(), null);
			}
			writer.write(menuItem.getLabel());
			if (hasLink)
			{
				writer.endElement("a");
			}
			writer.endElement("div");
			
			if (menuItem.isExpanded())
			{
				List subItems = menuItem.getChildren();
				if (!subItems.isEmpty())
				{
					writer.startElement("div", this);
					writer.writeAttribute("class", "secondary-menu-subitems-" + level, null);
					encodeItems(context, writer, subItems, level + 1, activeMenuItemId);
					writer.endElement("div");
				}
			}

		}
		
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		activeItemId = getActiveItemId();
		encodeItems(context, writer, getChildren(), 0, activeItemId);
	}

	public String getActiveItemId()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"activeItemId", activeItemIdSet, activeItemId);
	}

	public void setActiveItemId(String activeMenuItemId)
	{
		activeItemIdSet = true;
		this.activeItemId = activeMenuItemId;
	}

}