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
