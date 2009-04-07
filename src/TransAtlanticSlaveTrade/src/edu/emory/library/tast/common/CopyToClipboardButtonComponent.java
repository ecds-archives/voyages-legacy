package edu.emory.library.tast.common;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.BrowserInfo;
import edu.emory.library.tast.util.JsfUtils;

public class CopyToClipboardButtonComponent extends UIComponentBase
{
	
	private boolean dataSet = false;
	private String data;
	
	private boolean textSet = false;
	private String text;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = data;
		values[2] = text;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		data = (String) values[1];
		text = (String) values[2];
	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// render only for IE
		if (!BrowserInfo.isInternetExplorer(context))
			return;
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();

		// copy data
		String onClick =
			"if (window.clipboardData && window.clipboardData.setData) " +
				"window.clipboardData.setData('Text'," +
					"'" + JsfUtils.escapeStringForJS(getData()) + "')";

		// button
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", getText(), null);
		writer.writeAttribute("onclick", onClick, null);
		writer.endElement("input");
		
	}

	public String getData()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"data", dataSet, data);
	}

	public void setData(String data)
	{
		dataSet = true;
		this.data = data;
	}

	public String getText()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"text", textSet, text);
	}

	public void setText(String text)
	{
		textSet = true;
		this.text = text;
	}

}
