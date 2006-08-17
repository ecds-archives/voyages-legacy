package edu.emory.library.tast.ui;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

public class TimelineComponent extends UIComponentBase
{
	
	private boolean markersSet = false;
	private String[] markers;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[2];
		values[0] = super.saveState(context); 
		values[1] = markers; 
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		markers = (String[]) values[1];
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		String[] markers = getMarkers();

		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "map-tools", null);
		writer.startElement("tr", this);
		
		writer.endElement("tr");
		writer.endElement("table");
		
	}
	
	public void encodeChildren(FacesContext arg0) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext arg0) throws IOException
	{
	}

	public void setMarkers(String[] markers)
	{
		markersSet = true;
		this.markers = markers;
	}

	public String[] getMarkers()
	{
        if (markersSet) return markers;
        ValueBinding vb = getValueBinding("markers");
        if (vb == null) return markers;
        return (String[]) vb.getValue(getFacesContext());
	}

}
