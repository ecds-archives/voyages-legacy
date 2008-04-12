package edu.emory.library.tast.master;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class SecondaryMenuComponent extends UIComponentBase
{
	
	private boolean activeMenuItemIdSet = false;
	private String activeMenuItemId;

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
			
			String menuItemCssClass = "secondary-menu-item-" + level;
			if (activeMenuItemId != null && activeMenuItemId.equals(menuItem.getMenuId()))
				menuItemCssClass += "-selected";
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "secondary-menu-item-" + level, null);
			writer.startElement("a", this);
			writer.writeAttribute("href", menuItem.getHref(), null);
			writer.write(menuItem.getLabel());
			writer.endElement("a");
			writer.endElement("div");
			
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
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		activeMenuItemId = getActiveMenuItemId();
		encodeItems(context, writer, getChildren(), 0, activeMenuItemId);
	}

	public String getActiveMenuItemId()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"activeMenuItemId", activeMenuItemIdSet, activeMenuItemId);
	}

	public void setActiveMenuItemId(String activeMenuItemId)
	{
		activeMenuItemIdSet = true;
		this.activeMenuItemId = activeMenuItemId;
	}

}