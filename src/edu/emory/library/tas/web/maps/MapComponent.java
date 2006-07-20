package edu.emory.library.tas.web.maps;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tas.web.UtilsJSF;

public class MapComponent extends UIComponentBase
{
	
	private String mapFile = null;
	private boolean mapFileSet = false;

	private String serverBaseUrl = null;
	private boolean serverBaseUrlSet = false;

	private double x1 = -180;
	private double y1 = -90;
	private double x2 = 180;
	private double y2 = 90;

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
		Object[] values = new Object[7];
		values[0] = super.saveState(context);
		values[1] = mapFile;
		values[2] = serverBaseUrl;
		values[3] = new Double(x1);
		values[4] = new Double(y1);
		values[5] = new Double(x2);
		values[6] = new Double(y2);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		mapFile = (String) values[1];
		serverBaseUrl = (String) values[2];
		x1 = ((Double)values[3]).doubleValue();
		y1 = ((Double)values[4]).doubleValue();
		x2 = ((Double)values[5]).doubleValue();
		y2 = ((Double)values[6]).doubleValue();
	}
	
	private String getHiddenFieldNameForX1(FacesContext context)
	{
		return getClientId(context) + "_x1";
	}
	
	private String getHiddenFieldNameForY1(FacesContext context)
	{
		return getClientId(context) + "_y1";
	}

	private String getHiddenFieldNameForX2(FacesContext context)
	{
		return getClientId(context) + "_x2";
	}

	private String getHiddenFieldNameForY2(FacesContext context)
	{
		return getClientId(context) + "_y2";
	}

	public void decode(FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();
		
		x1 = UtilsJSF.getParamDouble(params, getHiddenFieldNameForX1(context), x1);
		y1 = UtilsJSF.getParamDouble(params, getHiddenFieldNameForY1(context), y1);
		x2 = UtilsJSF.getParamDouble(params, getHiddenFieldNameForX2(context), x2);
		y2 = UtilsJSF.getParamDouble(params, getHiddenFieldNameForY2(context), y2);
		
	}
	
	private void encodeToolStart(ResponseWriter writer) throws IOException
	{
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "5", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);
	}
	
	private void encodeTool(FacesContext context, ResponseWriter writer, String label, String js) throws IOException
	{
		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", label, null);
		writer.writeAttribute("onclick", js, null);
		writer.endElement("input");
		writer.endElement("td");
	}

	private void encodeToolEnd(ResponseWriter writer) throws IOException
	{
		writer.endElement("tr");
		writer.endElement("table");
	}
	
	private void encodeTopTools(FacesContext context, ResponseWriter writer, String mapId) throws IOException
	{

		encodeToolStart(writer);
		
		encodeTool(context, writer, "<", "MapsGlobal.zoomGoBack('" + mapId + "')");
		encodeTool(context, writer, ">", "MapsGlobal.zoomGoForward('" + mapId + "')");
		encodeTool(context, writer, "+", "MapsGlobal.zoomPlus('" + mapId + "')");
		encodeTool(context, writer, "-", "MapsGlobal.zoomMinus('" + mapId + "')");

		encodeTool(context, writer, "Pan", "MapsGlobal.setMouseModeToPan('" + mapId + "')");
		encodeTool(context, writer, "Zoom", "MapsGlobal.setMouseModeToZoom('" + mapId + "')");

		encodeTool(context, writer, "S", "MapsGlobal.setSize('" + mapId + "', 480, 320)");
		encodeTool(context, writer, "M", "MapsGlobal.setSize('" + mapId + "', 800, 600)");
		encodeTool(context, writer, "L", "MapsGlobal.setSize('" + mapId + "', 1024, 768)");
		
		encodeToolEnd(writer);
		
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = UtilsJSF.getForm(this, context);
		
		String mapFile = getMapFile();
		String serverBaseUrl = getServerBaseUrl();
		
		String mapId = getClientId(context);
		String mapControlId = getClientId(context) + "_contol";
		String mapFrameId = getClientId(context) + "_frame";
		String mapTopToolsId = getClientId(context) + "_top_tools";
		
		String hiddenFieldNameForX1 = getHiddenFieldNameForX1(context);
		String hiddenFieldNameForY1 = getHiddenFieldNameForY1(context);
		String hiddenFieldNameForX2 = getHiddenFieldNameForX2(context);
		String hiddenFieldNameForY2 = getHiddenFieldNameForY2(context);
		
		StringBuffer jsRegister = new StringBuffer();
		jsRegister.append("MapsGlobal.registerMap(");
		jsRegister.append("'").append(mapId).append("', ");
		jsRegister.append("true").append(", ");
		jsRegister.append("'").append(serverBaseUrl).append("', ");
		jsRegister.append("'").append(mapFile).append("', ");
		jsRegister.append("'").append(mapControlId).append("', ");
		jsRegister.append("'").append(mapFrameId).append("', ");
		jsRegister.append("'").append(mapTopToolsId).append("', ");
		jsRegister.append("null").append(", ");
		jsRegister.append("null").append(", ");
		jsRegister.append("null").append(", ");
		jsRegister.append("'").append(form.getId()).append("', ");
		jsRegister.append("'").append(hiddenFieldNameForX1).append("', ");
		jsRegister.append("'").append(hiddenFieldNameForY1).append("', ");
		jsRegister.append("'").append(hiddenFieldNameForX2).append("', ");
		jsRegister.append("'").append(hiddenFieldNameForY2).append("'");
		jsRegister.append(")");
		
		UtilsJSF.encodeJavaScriptStart(this, writer);
		writer.write(jsRegister.toString());
		UtilsJSF.encodeJavaScriptEnd(this, writer);
		
		UtilsJSF.encodeHiddenInput(this, writer, hiddenFieldNameForX1, String.valueOf(x1));
		UtilsJSF.encodeHiddenInput(this, writer, hiddenFieldNameForY1, String.valueOf(y1));
		UtilsJSF.encodeHiddenInput(this, writer, hiddenFieldNameForX2, String.valueOf(x2));
		UtilsJSF.encodeHiddenInput(this, writer, hiddenFieldNameForY2, String.valueOf(y2));
		
		writer.startElement("div", this);
		writer.writeAttribute("id", mapControlId, null);
		writer.writeAttribute("style", "position: relative; width: 800px; height: 600px;", null);

		writer.startElement("div", this);
		writer.writeAttribute("id", mapFrameId, null);
		writer.writeAttribute("style", "position: absolute", null);
		writer.endElement("div");
		
		writer.startElement("div", this);
		writer.writeAttribute("id", mapTopToolsId, null);
		encodeTopTools(context, writer, mapId);
		writer.endElement("div");

		writer.endElement("div");
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public String getMapFile()
	{
        if (mapFileSet) return mapFile;
        ValueBinding vb = getValueBinding("mapFile");
        if (vb == null) return mapFile;
        return (String) getValueBinding("mapFile").getValue(getFacesContext());
	}

	public void setMapFile(String mapFile)
	{
		mapFileSet = true;
		this.mapFile = mapFile;
	}

	public String getServerBaseUrl()
	{
        if (serverBaseUrlSet) return serverBaseUrl;
        ValueBinding vb = getValueBinding("serverBaseUrl");
        if (vb == null) return serverBaseUrl;
        return (String) getValueBinding("serverBaseUrl").getValue(getFacesContext());
	}

	public void setServerBaseUrl(String serverBaseUrl)
	{
		serverBaseUrlSet = true;
		this.serverBaseUrl = serverBaseUrl;
	}

}
