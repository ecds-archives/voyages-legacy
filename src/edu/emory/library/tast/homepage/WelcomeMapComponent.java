package edu.emory.library.tast.homepage;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class WelcomeMapComponent extends UIComponentBase
{
	
	private boolean imageUrlSet = false;
	private String imageUrl;
	
	private boolean imageWidthSet = false;
	private int imageWidth;

	private boolean imageHeightSet = false;
	private int imageHeight;

	private boolean placesSet = false;
	private WelcomeMapPlace[] places;

	private boolean initialTextSet = false;
	private String initialText;

	public String getFamily()
	{
		return null;
	}
	
	private String getImageElementId(FacesContext context, int imageIndex)
	{
		return getClientId(context) + "_image_" + imageIndex;
	}
	
	private void encodePlace(FacesContext context, ResponseWriter writer, String textElementId, int imageIndex) throws IOException
	{
		
		WelcomeMapPlace place = places[imageIndex];
		String imageId = getImageElementId(context, imageIndex);
		
		String cssStyle = 
			"top: " + place.getY() + "px; " +
			"left: " + place.getX() + "px; " +
			"position: absolute;";
		
		String onMouseOver = "WelcomeMapGlobals.showPlace(" +
			"'" + getClientId(context) + "', " +
			"'" + imageIndex + "')";
		
		String onMouseOut = "WelcomeMapGlobals.hidePlace(" +
			"'" + getClientId(context) + "', " +
			"'" + imageIndex + "')";

		writer.startElement("img", this);
		writer.writeAttribute("src", place.getImageUrl(), null);
		writer.writeAttribute("width", String.valueOf(place.getImageWidth()), null);
		writer.writeAttribute("height", String.valueOf(place.getImageHeight()), null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("class", "welcome-map-place", null);
		writer.writeAttribute("style", cssStyle, null);
		writer.writeAttribute("id", imageId, null);
		writer.writeAttribute("onmouseover", onMouseOver, null);
		writer.writeAttribute("onmouseout", onMouseOut, null);
		writer.endElement("img");

	}

	/*
	private void encodePlaces(FacesContext context, ResponseWriter writer, String textElementId) throws IOException
	{
		
		String cssStyle =
			"position: absolute; " +
			"top: 0px; " +
			"left: 0px;" +
			"width: " + imageWidth + "px; " +
			"height: " + imageHeight + "px;";
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "welcome-map-places", null);
		writer.writeAttribute("style", cssStyle, null);
		
		for (int i = 0; i < places.length; i++)
			encodePlace(context, writer, textElementId, i);
		
		writer.endElement("div");
		
	}
	
	private void encodeTextContainer(FacesContext context, ResponseWriter writer, String textElementId) throws IOException
	{
		
		String cssStyleCont =
			"position: absolute; " +
			"top: 0px; " +
			"left: 0px;" +
			"width: " + imageWidth + "px; " +
			"height: " + imageHeight + "px;";
		
		String cssStyleText =
			"display: none; " +
			"position: absolute; " +
			"top: auto; " +
			"left: 0px; " +
			"bottom: 0px; " + 
			"width: " + imageWidth + "px;";

		writer.startElement("div", this);
		writer.writeAttribute("class", "welcome-map-text-container", null);
		writer.writeAttribute("style", cssStyleCont, null);
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "welcome-map-text", null);
		writer.writeAttribute("style", cssStyleText, null);
		writer.writeAttribute("id", textElementId, null);

		writer.startElement("div", this);
		writer.endElement("div");

		writer.endElement("div");

		writer.endElement("div");
		
	}
	*/

	private void encodeMap(FacesContext context, ResponseWriter writer, String textElementId) throws IOException
	{
		String cssStyleMainCell =
			"width: " + imageWidth + "px; " +
			"height: " + imageHeight + "px;";

		String cssStylePositionedCont =
			"position: relative; " +
			"width: " + imageWidth + "px; " +
			"height: " + imageHeight + "px; " + 
			"background-image: url('" + imageUrl + "')";

		writer.startElement("table", this);
		writer.writeAttribute("class", "welcome-map-frame", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "welcome-map-top-left", null);
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "welcome-map-top-middle", null);
		writer.endElement("td");

		writer.startElement("td", this);
		writer.writeAttribute("class", "welcome-map-top-right", null);
		writer.endElement("td");

		writer.endElement("tr");
		
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "welcome-map-middle-left", null);
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "welcome-map-image", null);
		writer.writeAttribute("style", cssStyleMainCell, null);
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "welcome-map-container", null);
		writer.writeAttribute("style", cssStylePositionedCont, null);
		
		for (int i = 0; i < places.length; i++)
			encodePlace(context, writer, textElementId, i);

		writer.endElement("div");

		writer.endElement("td");

		writer.startElement("td", this);
		writer.writeAttribute("class", "welcome-map-middle-right", null);
		writer.endElement("td");

		writer.endElement("tr");

		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "welcome-map-bottom-left", null);
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "welcome-map-bottom-middle", null);
		writer.endElement("td");

		writer.startElement("td", this);
		writer.writeAttribute("class", "welcome-map-bottom-right", null);
		writer.endElement("td");

		writer.endElement("tr");

		writer.endElement("table");

	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();

		String textElementId = getClientId(context);
		
		imageUrl = getImageUrl();
		imageWidth = getImageWidth();
		imageHeight = getImageHeight();
		places = getPlaces();
		initialText = getInitialText();
		
		StringBuffer regJS = new StringBuffer();
		regJS.append("WelcomeMapGlobals.register(new WelcomeMap(");
		regJS.append("'").append(getClientId(context)).append("'");
		regJS.append(", ");
		regJS.append("'").append(JsfUtils.escapeStringForJS(initialText)).append("'");
		regJS.append(", ");
		regJS.append("'").append(textElementId).append("'");
		regJS.append(", ");
		regJS.append("[");
		for (int i = 0; i < places.length; i++)
		{
			WelcomeMapPlace place = places[i];
			if (i > 0) regJS.append(", ");
			regJS.append("new WelcomeMapPlace(");
			regJS.append("'").append(getImageElementId(context, i)).append("'");
			regJS.append(", ");
			regJS.append("'").append(place.getImageUrl()).append("'");
			regJS.append(", ");
			regJS.append("'").append(place.getImageUrlSelected()).append("'");
			regJS.append(", ");
			regJS.append("'").append(JsfUtils.escapeStringForJS(place.getText())).append("'");
			regJS.append(")");
		}
		regJS.append("]));");
		
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);
		
		writer.startElement("table", this);
		writer.writeAttribute("class", "welcome-map", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		
		writer.startElement("tr", this);

		writer.startElement("div", this);
		writer.writeAttribute("class", "welcome-map", null);
		encodeMap(context, writer, textElementId);
		writer.endElement("div");
		
		writer.startElement("div", this);
		writer.writeAttribute("id", textElementId, null);
		writer.writeAttribute("class", "welcome-map-text", null);
		writer.write(initialText);
		writer.endElement("div");
		
		writer.endElement("tr");
		writer.endElement("table");

	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public int getImageHeight()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"imageHeight", imageHeightSet, imageHeight);
	}

	public void setImageHeight(int imageHeight)
	{
		imageHeightSet = true;
		this.imageHeight = imageHeight;
	}

	public String getImageUrl()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"imageUrl", imageUrlSet, imageUrl);
	}

	public void setImageUrl(String imageUrl)
	{
		imageUrlSet = true;
		this.imageUrl = imageUrl;
	}

	public int getImageWidth()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"imageWidth", imageWidthSet, imageWidth);
	}

	public void setImageWidth(int imageWidth)
	{
		imageWidthSet = true;
		this.imageWidth = imageWidth;
	}

	public WelcomeMapPlace[] getPlaces()
	{
		return (WelcomeMapPlace[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"places", placesSet, places);
	}

	public void setPlaces(WelcomeMapPlace[] places)
	{
		placesSet = true;
		this.places = places;
	}

	public String getInitialText()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"initialText", initialTextSet, initialText);
	}

	public void setInitialText(String initialText)
	{
		initialTextSet = true;
		this.initialText = initialText;
	}

}