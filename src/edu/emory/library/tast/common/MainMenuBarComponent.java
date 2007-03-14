package edu.emory.library.tast.common;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class MainMenuBarComponent extends UIComponentBase
{
	
	private boolean menuItemsSet = false;
	private MainMenuBarSectionItem[] menuItems;
	
	private boolean activeSectionIdSet;
	private String activeSectionId;

	private boolean activePageIdSet;
	private String activePageId;

	public String getFamily()
	{
		return null;
	}
	
	public MainMenuBarComponent()
	{
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[3];
		values[0] = super.saveState(context); 
		values[1] = activeSectionId; 
		values[2] = activePageId; 
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		activeSectionId = (String) values[1];
		activePageId = (String) values[2];
	}
	
	private void encodePageLink(ResponseWriter writer, FacesContext context, MainMenuBarPageItem page, boolean sectionActive, String onMouseOver, String onMouseOut) throws IOException
	{
		
		boolean pageActive = sectionActive && page.getId().equals(activePageId);
		String linkClass = pageActive ? "main-menu-page-active" : "main-menu-page-normal"; 
		
		writer.startElement("div", this);
		writer.writeAttribute("class", linkClass, null);
		if (!sectionActive)
		{
			writer.writeAttribute("onmouseover", onMouseOver, null);
			writer.writeAttribute("onmouseout", onMouseOut, null);
		}
		
		writer.startElement("a", this);
		writer.writeAttribute("href", context.getExternalContext().getRequestContextPath() + "/" + page.getUrl(), null);
		writer.write(page.getLabel());
		writer.endElement("a");
		
		writer.endElement("div");

	}

	private void encodeSection(ResponseWriter writer, FacesContext context, MainMenuBarSectionItem section) throws IOException
	{
		
		boolean sectionActive = section.getId().equals(activeSectionId);
		String baseUrl = context.getExternalContext().getRequestContextPath();
		
		String sectionImgId = getClientId(context) + "_img_" + section.getId(); 
		String pagesImgId = getClientId(context) + "_pages_" + section.getId(); 
		
		String onMouseOver = "MainMenuBar.mouseOver(" +
				"'" + sectionImgId + "', " +
				"'" + pagesImgId + "', " +
				"'" + baseUrl + "/" + section.getImageUrlActive() + "')";

		String onMouseOut = "MainMenuBar.mouseOut(" +
				"'" + sectionImgId + "', " +
				"'" + pagesImgId + "', " +
				"'" + baseUrl + "/" + section.getImageUrlNormal() + "')";
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-section", null);

		writer.startElement("div", this);
		writer.writeAttribute("class", "main-menu-section-image", null);
		
		writer.startElement("a", this);
		writer.writeAttribute("href", baseUrl + "/" + section.getUrl(), null);

		writer.startElement("img", this);
		writer.writeAttribute("id", sectionImgId, null);
		writer.writeAttribute("width", String.valueOf(section.getImageWidth()), null);
		writer.writeAttribute("height", String.valueOf(section.getImageHeight()), null);
		writer.writeAttribute("class", "main-menu-section-image", null);
		writer.writeAttribute("border", "0", null);
		if (!sectionActive)
		{
			writer.writeAttribute("onmouseover", onMouseOver, null);
			writer.writeAttribute("onmouseout", onMouseOut, null);
			writer.writeAttribute("src", baseUrl + "/" + section.getImageUrlNormal(), null);
		}
		else
		{
			writer.writeAttribute("src", baseUrl + "/" + section.getImageUrlActive(), null);
		}
		writer.endElement("img");
		
		writer.endElement("a");

		writer.endElement("div");
		
		writer.startElement("div", this);
		writer.writeAttribute("id", pagesImgId, null);
		writer.writeAttribute("class", "main-menu-pages", null);
		if (!sectionActive) writer.writeAttribute("style", "display: none", null);
		writer.startElement("div", this);
		
		MainMenuBarPageItem[] pages = section.getSubItems(); 
		for (int i = 0; i < pages.length; i++)
			encodePageLink(writer, context, pages[i], sectionActive, onMouseOver, onMouseOut);
			
		writer.endElement("div");
		writer.endElement("div");

		writer.endElement("td");

	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// general stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// get menu from a bean
		menuItems = getMenuItems();
		activeSectionId = getActiveSectionId();
		activePageId = getActivePageId();
		
		// main table
		writer.startElement("table", this);
		writer.writeAttribute("class", "main-menu", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);
		
		// sections
		for (int i = 0; i < menuItems.length; i++)
			encodeSection(writer, context, menuItems[i]);
		
		// main table
		writer.endElement("tr");
		writer.endElement("table");
		
	}

	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public MainMenuBarSectionItem[] getMenuItems()
	{
		return (MainMenuBarSectionItem[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"menuItems", menuItemsSet, menuItems);
	}

	public void setMenuItems(MainMenuBarSectionItem[] menuItems)
	{
		menuItemsSet = true;
		this.menuItems = menuItems;
	}

	public String getActivePageId()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"activePageId", activePageIdSet, activePageId);
	}

	public void setActivePageId(String activePageId)
	{
		activePageIdSet = true;
		this.activePageId = activePageId;
	}

	public String getActiveSectionId()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"activeSectionId", activeSectionIdSet, activeSectionId);
	}

	public void setActiveSectionId(String activeSectionId)
	{
		activeSectionIdSet = true;
		this.activeSectionId = activeSectionId;
	}

}
