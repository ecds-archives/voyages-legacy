package edu.emory.library.tast.master;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

/***
 * I've always tried to keep components simple in the sense that
 * a component should always have a single purpose. However,
 * for the site main header it seems that the most efficient
 * solution is to pack everything in one huge component. Then it's
 * easy to use it because it's represented by just a single tag.
 * I'll try to separate the logical pieces into reasonable chunks
 * at least.
 *  
 * @author Jan Zich
 *
 */

public class SiteHeaderComponent extends UIComponentBase
{
	
	private static final String SITE_INDEX_PAGE = "index.faces";

	private static final String LOGO_SRC = "images/main-menu/logo.png";
	private static final int LOGO_HEIGHT = 60;
	private static final int LOGO_WIDTH = 240;

	private static final String BREADCRUMB_SEPARATOR_SRC = "images/main-menu/breadcrumb-separator.png";
	private static final int BREADCRUMB_SEPARATOR_WIDTH = 10;
	private static final int BREADCRUMB_SEPARATOR_HEIGHT = 10;
	
	private boolean activeSectionIdSet = false;
	private String activeSectionId;
	
	private static HelpLink[] helpLinks = {
		new HelpLink("Sitemap", "help/sitemap.faces"),
		new HelpLink("FAQs", "help/faq.faces"),
		new HelpLink("Demos", "help/demo-overview.faces"),
		new HelpLink("Glossary", "help/glossary.faces") };

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
		Object[] values = new Object[2];
		values[0] = super.saveState(context); 
		values[1] = activeSectionId; 
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		activeSectionId = (String) values[1];
	}
	
	private void encodeMenuImage(ResponseWriter writer, MainMenuBarSectionItem section, boolean sectionActive, String baseUrl, String sectionImgId, String onMouseOver, String onMouseOut) throws IOException
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
	
	private void encodeMenuPopupBox(ResponseWriter writer, MainMenuBarSectionItem section, boolean sectionActive, String baseUrl, String popupBoxId, String onMouseOver, String onMouseOut) throws IOException
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

	private void encodeMenuItem(ResponseWriter writer, FacesContext context, String baseUrl, MainMenuBarSectionItem section) throws IOException
	{
		
		boolean sectionActive = section.getId().equals(activeSectionId);
		
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

		encodeMenuImage(
				writer,
				section,
				sectionActive,
				baseUrl,
				sectionImgId,
				onMouseOver,
				onMouseOut);
		
		encodeMenuPopupBox(
				writer,
				section,
				sectionActive,
				baseUrl,
				popupBoxId,
				onMouseOver,
				onMouseOut);

		writer.endElement("td");

	}
	
	public void encodeSiteMenu(FacesContext context, ResponseWriter writer, String baseUrl) throws IOException
	{
		
		// get menu from a bean
		activeSectionId = getActiveSectionId();
		
		// main table
		writer.startElement("table", this);
		writer.writeAttribute("class", "main-menu", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);
		
		// sections
		MainMenuBarSectionItem[] menuItems = MainMenu.getMainMenu();
		for (int i = 0; i < menuItems.length; i++)
			encodeMenuItem(writer, context, baseUrl, menuItems[i]);
		
		// main table
		writer.endElement("tr");
		writer.endElement("table");
		
	}
	
	public void encodeSiteLogo(FacesContext context, ResponseWriter writer, String baseUrl) throws IOException
	{
		
		writer.startElement("a", this);
		writer.writeAttribute("href", baseUrl + "/" + SITE_INDEX_PAGE, null);

		writer.startElement("img", this);
		writer.writeAttribute("src", baseUrl + "/" + LOGO_SRC, null);
		writer.writeAttribute("width", String.valueOf(LOGO_WIDTH), null);
		writer.writeAttribute("height", String.valueOf(LOGO_HEIGHT), null);
		writer.writeAttribute("border", "0", null);
		writer.endElement("img");

		writer.endElement("a");
	
	}
	
	private void encodeMainBar(FacesContext context, ResponseWriter writer, String baseUrl) throws IOException
	{
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "top-bar", null);
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "top-bar", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "top-bar-logo", null);
		encodeSiteLogo(context, writer, baseUrl);
		writer.endElement("td");
	
		writer.startElement("td", this);
		writer.writeAttribute("class", "top-bar-menu", null);
		encodeSiteMenu(context, writer, baseUrl);
		writer.endElement("td");
		
		writer.endElement("tr");
		writer.endElement("table");
		
		writer.endElement("div");

	}
	
	private void encodeBreadcrumbBar(FacesContext context, ResponseWriter writer, String baseUrl) throws IOException
	{
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "breadcrumb", null);
		writer.startElement("tr", this);
		
		int pathPartIdx = 0;
		for (Iterator iter = getChildren().iterator(); iter.hasNext();)
		{
			UIComponent cmp = (UIComponent) iter.next();
			
			if (pathPartIdx > 0)
			{
				writer.startElement("td", this);
				writer.writeAttribute("class", "breadcrumb-separator", null);
				writer.startElement("img", this);
				writer.writeAttribute("src", baseUrl + "/" + BREADCRUMB_SEPARATOR_SRC, null);
				writer.writeAttribute("width", String.valueOf(BREADCRUMB_SEPARATOR_WIDTH), null);
				writer.writeAttribute("height", String.valueOf(BREADCRUMB_SEPARATOR_HEIGHT), null);
				writer.writeAttribute("border", "0", null);
				writer.endElement("img");
				writer.endElement("td");
			}
			
			writer.startElement("td", this);
			JsfUtils.renderChild(context, cmp);
			writer.endElement("td");
			
			pathPartIdx++;
		}
		
		writer.endElement("tr");
		writer.endElement("table");
		
	}
	
	private void encodeHelpLinks(FacesContext context, ResponseWriter writer, String baseUrl) throws IOException
	{
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "secondary-bar-help-links", null);
		writer.startElement("tr", this);
		
		for (int i = 0; i < helpLinks.length; i++)
		{
			
			writer.startElement("td", this);
			writer.writeAttribute("class", i == 0 ? "secondary-bar-help-link-first" : "secondary-bar-help-link", null);
			
			writer.startElement("a", this);
			writer.writeAttribute("href", "javascript:openPopup('" + baseUrl + "/" + helpLinks[i].getHref() + "')", null);
			writer.write(helpLinks[i].getLabel());
			writer.endElement("a");
			
			writer.endElement("td");
			
		}
		
		writer.endElement("tr");
		writer.endElement("table");
		
	}

	private void encodeSecondaryBar(FacesContext context, ResponseWriter writer, String baseUrl) throws IOException
	{
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "secondary-bar", null);
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "secondary-bar", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "secondary-bar-breadcrumb", null);
		encodeBreadcrumbBar(context, writer, baseUrl);
		writer.endElement("td");
	
		writer.startElement("td", this);
		writer.writeAttribute("class", "secondary-bar-help-links", null);
		encodeHelpLinks(context, writer, baseUrl);
		writer.endElement("td");
		
		writer.endElement("tr");
		writer.endElement("table");
		
		writer.endElement("div");
		
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		String baseUrl = context.getExternalContext().getRequestContextPath();
		ResponseWriter writer = context.getResponseWriter();
		
		encodeMainBar(context, writer, baseUrl);
		encodeSecondaryBar(context, writer, baseUrl);

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
