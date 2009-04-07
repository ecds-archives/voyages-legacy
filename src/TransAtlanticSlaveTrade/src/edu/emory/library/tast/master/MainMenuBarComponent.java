package edu.emory.library.tast.master;

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
	
	private void encodeMainImage(ResponseWriter writer, MainMenuBarSectionItem section, boolean sectionActive, String baseUrl, String sectionImgId, String onMouseOver, String onMouseOut) throws IOException
	{
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "main-menu-section-image", null);
		
		if (section.getUrl() != null)
		{
			writer.startElement("a", this);
			writer.writeAttribute("href", baseUrl + "/" + section.getUrl(), null);
		}

		writer.startElement("img", this);
		writer.writeAttribute("id", sectionImgId, null);
		writer.writeAttribute("width", String.valueOf(section.getImageWidth()), null);
		writer.writeAttribute("height", String.valueOf(section.getImageHeight()), null);
		writer.writeAttribute("class", "main-menu-section-image", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("onmouseover", onMouseOver, null);
		writer.writeAttribute("onmouseout", onMouseOut, null);
		if (!sectionActive)
		{
			writer.writeAttribute("src", baseUrl + "/" + section.getImageUrlNormal(), null);
		}
		else
		{
			writer.writeAttribute("src", baseUrl + "/" + section.getImageUrlActive(), null);
		}
		writer.endElement("img");
		
		if (section.getUrl() != null)
		{
			writer.endElement("a");
		}

		writer.endElement("div");
		
	}
	
	private void encodePopupBox(ResponseWriter writer, MainMenuBarSectionItem section, boolean sectionActive, String baseUrl, String popupBoxId, String onMouseOver, String onMouseOut) throws IOException
	{
		
		String boxCssClass = "main-menu-box";
		if (section.getBoxCssClass() != null)
			boxCssClass += " " + section.getBoxCssClass();  
		
		writer.startElement("div", this);
		writer.writeAttribute("id", popupBoxId, null);
		writer.writeAttribute("class", boxCssClass, null);
		writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("onmouseover", onMouseOver, null);
		writer.writeAttribute("onmouseout", onMouseOut, null);
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "main-menu-box", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-box-top-left", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-box-top", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-box-top-right", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");

		writer.endElement("tr");
		writer.startElement("tr", this);

		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-box-left", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-box-center", null);
		
		MainMenuBarPageItem[] pages = section.getSubItems(); 
		for (int i = 0; i < pages.length; i++)
		{
			MainMenuBarPageItem page = pages[i];
			writer.startElement("div", this);
			writer.writeAttribute("class", "main-menu-box-link", null);
			writer.startElement("a", this);
			writer.writeAttribute("href", baseUrl + "/" + page.getUrl(), null);
			writer.write(page.getLabel());
			writer.endElement("a");
			writer.endElement("div");
		}
		
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-box-right", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");

		writer.endElement("tr");
		writer.startElement("tr", this);

		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-box-bottom-left", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-box-bottom", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-box-bottom-right", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
			
		writer.endElement("div");
	
	}

	private void encodeSection(ResponseWriter writer, FacesContext context, MainMenuBarSectionItem section) throws IOException
	{
		
		boolean sectionActive = section.getId().equals(activeSectionId);
		String baseUrl = context.getExternalContext().getRequestContextPath();
		
		String sectionImgId = getClientId(context) + "_img_" + section.getId(); 
		String popupBoxId = getClientId(context) + "_box_" + section.getId(); 
		
		String onMouseOver = "MainMenuBar.mouseOver(" +
				"'" + sectionImgId + "', " +
				"'" + popupBoxId + "', " +
				"'" + baseUrl + "/" + section.getImageUrlHighlighted() + "')";

		String onMouseOut = "MainMenuBar.mouseOut(" +
				"'" + sectionImgId + "', " +
				"'" + popupBoxId + "', " +
				"'" + baseUrl + "/" + (!sectionActive ? section.getImageUrlNormal() : section.getImageUrlActive()) + "')";
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "main-menu-section", null);

		encodeMainImage(
				writer,
				section,
				sectionActive,
				baseUrl,
				sectionImgId,
				onMouseOver,
				onMouseOut);
		
		encodePopupBox(
				writer,
				section,
				sectionActive,
				baseUrl,
				popupBoxId,
				onMouseOver,
				onMouseOut);

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
