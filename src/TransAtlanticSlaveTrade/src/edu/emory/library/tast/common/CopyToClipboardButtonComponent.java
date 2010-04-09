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
