package edu.emory.library.tast.ui.maps.component;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.ui.search.query.UtilsJSF;
import edu.emory.library.tast.util.StringUtils;

public class MapComponent extends UIComponentBase
{
	
	private final static MapSize defaultMapSize = new MapSize(480, 320);

	private boolean mapSizesSet = false;
	private MapSize[] mapSizes = new MapSize[] {
		new MapSize(480, 320),
		new MapSize(800, 600),
		new MapSize(1024, 768)
	};
	
	private boolean mapSizeSet = false;
	private MapSize mapSize = (MapSize) defaultMapSize.clone();

	private boolean mapFileSet = false;
	private String mapFile = null;

	private boolean serverBaseUrlSet = false;
	private String serverBaseUrl = null;

	private boolean pointsOfInterestSet = false;
	private PointOfInterest[] pointsOfInterest = null;

	private double x1 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_EXTENT_X_MIN);
	private double y1 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_EXTENT_Y_MIN);
	private double x2 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_EXTENT_X_MAX);
	private double y2 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_EXTENT_Y_MAX);
	
	private boolean miniMapSet = false;
	private boolean miniMap = true;
	private boolean miniMapVisible = true;
	
	private boolean miniMapPositionSet = false;
	private MiniMapPosition miniMapPosition = MiniMapPosition.BottomRight;

	private boolean miniMapWidthSet = false;
	private boolean miniMapHeightSet = false;
	private int miniMapWidth = 100;
	private int miniMapHeight = 100;

	private ZoomHistory zoomHistory = new ZoomHistory();
	private MouseMode mouseMode = MouseMode.Pan;
	
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
		Object[] values = new Object[16];
		values[0] = super.saveState(context);
		values[1] = mapFile;
		values[2] = serverBaseUrl;
		values[3] = new Double(x1);
		values[4] = new Double(y1);
		values[5] = new Double(x2);
		values[6] = new Double(y2);
		values[7] = mouseMode;
		values[8] = mapSizes;
		values[9] = mapSize;
		values[10] = zoomHistory;
		values[11] = new Boolean(miniMap);
		values[12] = new Boolean(miniMapVisible);
		values[13] = miniMapPosition;
		values[14] = new Integer(miniMapWidth);
		values[15] = new Integer(miniMapHeight);
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
		mouseMode = (MouseMode)values[7];
		mapSizes = (MapSize[]) values[8];
		mapSize = (MapSize) values[9];
		zoomHistory = (ZoomHistory) values[10];
		miniMap = ((Boolean)values[11]).booleanValue();
		miniMapVisible = ((Boolean)values[12]).booleanValue();
		miniMapPosition = (MiniMapPosition) values[13];
		miniMapWidth = ((Integer)values[14]).intValue();
		miniMapHeight = ((Integer)values[15]).intValue();
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

	private String getHiddenFieldNameForMouseMode(FacesContext context)
	{
		return getClientId(context) + "_mouse_mode";
	}

	private String getHiddenFieldNameForMapSize(FacesContext context)
	{
		return getClientId(context) + "_map_size";
	}

	private String getHiddenFieldNameForZoomHistory(FacesContext context)
	{
		return getClientId(context) + "_zoom_history";
	}

	private String getHiddenFieldNameForMiniMapVisibility(FacesContext context)
	{
		return getClientId(context) + "_minimap_visible";
	}

	public void decode(FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();
		
		x1 = UtilsJSF.getParamDouble(params, getHiddenFieldNameForX1(context), x1);
		y1 = UtilsJSF.getParamDouble(params, getHiddenFieldNameForY1(context), y1);
		x2 = UtilsJSF.getParamDouble(params, getHiddenFieldNameForX2(context), x2);
		y2 = UtilsJSF.getParamDouble(params, getHiddenFieldNameForY2(context), y2);
		
		String mouseModeStr = UtilsJSF.getParamString(params, getHiddenFieldNameForMouseMode(context));
		if (!StringUtils.isNullOrEmpty(mouseModeStr))
			mouseMode = MouseMode.parse(mouseModeStr);
		
		String zoomHistoryStr = UtilsJSF.getParamString(params, getHiddenFieldNameForZoomHistory(context));
		if (!StringUtils.isNullOrEmpty(zoomHistoryStr))
			zoomHistory = ZoomHistory.parse(zoomHistoryStr);
		
		String mapSizeStr = UtilsJSF.getParamString(params, getHiddenFieldNameForMapSize(context));
		if (!StringUtils.isNullOrEmpty(mapSizeStr))
			mapSize = MapSize.parse(mapSizeStr, true);
		
		miniMapVisible = UtilsJSF.getParamBoolean(params, getHiddenFieldNameForMiniMapVisibility(context), miniMapVisible);
		
	}
	
	private void encodeToolStart(ResponseWriter writer) throws IOException
	{
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "map-tools", null);
		writer.startElement("tr", this);
	}
	
	private void encodeTool(FacesContext context, ResponseWriter writer, String id, String className) throws IOException
	{
		writer.startElement("td", this);
		writer.writeAttribute("id", id, null);
		writer.writeAttribute("class", className, null);
		writer.endElement("td");
	}

	private void encodeToolZoomSlider(FacesContext context, ResponseWriter writer, String bgId, String knobId) throws IOException
	{
		writer.startElement("td", this);
		writer.writeAttribute("id", bgId, null);
		writer.writeAttribute("class", "map-zoom-slider-bg", null);
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "map-zoom-slider-bg", null);
		
		writer.startElement("div", this);
		writer.writeAttribute("id", knobId, null);
		writer.writeAttribute("class", "map-zoom-slider-knob", null);
		writer.writeAttribute("style", "display: none", null);
		writer.endElement("div");
		
		writer.endElement("div");
		
		writer.endElement("td");
	}

	private void encodeToolSepearator(FacesContext context, ResponseWriter writer) throws IOException
	{
		writer.startElement("td", this);
		writer.writeAttribute("class", "map-tools-separator", null);
		writer.endElement("td");
	}

	private void encodeToolEnd(ResponseWriter writer) throws IOException
	{
		writer.endElement("tr");
		writer.endElement("table");
	}
	
	private void encodeBubble(FacesContext context, ResponseWriter writer, String bubbleId, String bubbleTextId) throws IOException
	{
		
		writer.startElement("table", this);
		writer.writeAttribute("id", bubbleId, null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "map-bubble", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "map-bubble-11", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "map-bubble-12", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "map-bubble-13", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");

		writer.endElement("tr");
		writer.startElement("tr", this);

		writer.startElement("td", this);
		writer.writeAttribute("class", "map-bubble-21", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("id", bubbleTextId, null);
		writer.writeAttribute("class", "map-bubble-22", null);
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "map-bubble-23", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");

		writer.endElement("tr");
		writer.startElement("tr", this);

		writer.startElement("td", this);
		writer.writeAttribute("class", "map-bubble-31", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "map-bubble-32", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "map-bubble-33", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
		
	}
	
	private void encodeScaleIndicator(FacesContext context, ResponseWriter writer, String scaleIndicatorTextId, String scaleIndicatorBarId) throws IOException
	{
		writer.startElement("div", this);
		writer.writeAttribute("class", "map-scale-indicator-container", null);

		writer.startElement("div", this);
		writer.writeAttribute("class", "map-scale-indicator-text", null);
		writer.writeAttribute("id", scaleIndicatorTextId, null);
		writer.endElement("div");
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "map-scale-indicator-bar", null);
		writer.writeAttribute("id", scaleIndicatorBarId, null);
		writer.endElement("div");

		writer.endElement("div");
	}

	private String getElementIdForMapSize(FacesContext context, int sizeIndex)
	{
		return getClientId(context) + "_size_" + sizeIndex; 
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
		
		String toolsBackId = getClientId(context) + "_back";
		String toolsForwardId = getClientId(context) + "_forward";
		String toolsZoomPlusId = getClientId(context) + "_zoom_plus";
		String toolsZoomMinusId = getClientId(context) + "_zoom_minus";
		String toolsSliderBgId = getClientId(context) + "_zoom_slider_bg";
		String toolsSliderKnobId = getClientId(context) + "_zoom_slider_knob";
		String toolsPanId = getClientId(context) + "_pan";
		String toolsZoomId = getClientId(context) + "_zoom";
		String bubbleId = getClientId(context) + "_bubble";
		String bubbleTextId = getClientId(context) + "_bubble_text";
		String scaleIndicatorTextId = getClientId(context) + "_scale_indicator_text";
		String scaleIndicatorBarId = getClientId(context) + "_scale_indicator_bar";
		String miniMapControlId = getClientId(context) + "_minimap_control";
		String miniMapFrameId = getClientId(context) + "_minimap_frame";
		String miniMapToggleId = getClientId(context) + "_minimap_toggle";

		String hiddenFieldNameForX1 = getHiddenFieldNameForX1(context);
		String hiddenFieldNameForY1 = getHiddenFieldNameForY1(context);
		String hiddenFieldNameForX2 = getHiddenFieldNameForX2(context);
		String hiddenFieldNameForY2 = getHiddenFieldNameForY2(context);
		String hiddenFieldNameForMouseMode = getHiddenFieldNameForMouseMode(context);
		String hiddenFieldNameForZoomHistory = getHiddenFieldNameForZoomHistory(context);
		String hiddenFieldNameForMapSize = getHiddenFieldNameForMapSize(context);
		String hiddenFieldNameForMiniMapVisibility = getHiddenFieldNameForMiniMapVisibility(context);
		
		// from beans or prev values
		mapSize = getMapSize();
		mapSizes = getMapSizes();
		PointOfInterest[] pointsOfInterest = getPointsOfInterest();
		miniMap = isMiniMap();
		miniMapPosition = getMiniMapPosition();
		miniMapWidth = getMiniMapWidth();
		miniMapHeight = getMiniMapHeight();
		
		// at least the default map size
		if (mapSizes == null || mapSizes.length == 0)
			mapSizes = new MapSize[] {defaultMapSize};

		// scale configuration and extent
		double scaleFactor = AppConfig.getConfiguration().getDouble(AppConfig.MAP_SCALE_FACTOR);
		int scaleMax = AppConfig.getConfiguration().getInt(AppConfig.MAP_MAX_MAGNIFICATION);
		
		// init JS
		StringBuffer jsRegister = new StringBuffer();
		
		// map id
		jsRegister.append("MapsGlobal.registerMap(");
		jsRegister.append("'").append(mapId).append("'");
		jsRegister.append(", ");
		
		// fixed size
		jsRegister.append("true");
		jsRegister.append(", ");
		
		// servlet
		jsRegister.append("'").append(serverBaseUrl).append("'");
		jsRegister.append(", ");

		// m parameter
		jsRegister.append("'").append(mapFile).append("'");
		jsRegister.append(", ");

		// scale factor (i.e. denominator)
		jsRegister.append("'").append(scaleFactor).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(scaleMax).append("'");
		jsRegister.append(", ");

		// main HTML elements
		jsRegister.append("'").append(mapControlId).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(mapFrameId).append("'");
		jsRegister.append(", ");

		// HTML elements for tools (not used now)
		jsRegister.append("null");
		jsRegister.append(", ");
		jsRegister.append("null");
		jsRegister.append(", ");
		jsRegister.append("null");
		jsRegister.append(", ");
		jsRegister.append("null");
		jsRegister.append(", ");
		
		// form name
		jsRegister.append("'").append(form.getId()).append("'");
		jsRegister.append(", ");
		
		// hidden fields: extends
		jsRegister.append("'").append(hiddenFieldNameForX1).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(hiddenFieldNameForY1).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(hiddenFieldNameForX2).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(hiddenFieldNameForY2).append("'");
		jsRegister.append(", ");
		
		// zoom history
		jsRegister.append("'").append(hiddenFieldNameForZoomHistory).append("'");
		jsRegister.append(", ");

		// back / forward
		jsRegister.append("'").append(toolsBackId).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(toolsForwardId).append("'");
		jsRegister.append(", ");

		// zoom + / -
		jsRegister.append("'").append(toolsZoomPlusId).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(toolsZoomMinusId).append("'");
		jsRegister.append(", ");
		
		// slider
		jsRegister.append("'").append(toolsSliderBgId).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(toolsSliderKnobId).append("'");
		jsRegister.append(", ");

		// pan / zoom tools
		jsRegister.append("'").append(toolsPanId).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(toolsZoomId).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(hiddenFieldNameForMouseMode).append("'");
		jsRegister.append(", ");
		
		// sizes
		jsRegister.append("[");
		for (int i = 0; i < mapSizes.length; i++)
		{
			MapSize ms = getMapSizes()[i];
			if (i > 0) jsRegister.append(", ");
			jsRegister.append("{");
			jsRegister.append("elementId: '").append(getElementIdForMapSize(context, i)).append("'");
			jsRegister.append(", ");
			jsRegister.append("width: ").append(ms.getWidth());
			jsRegister.append(", ");
			jsRegister.append("height: ").append(ms.getHeight());
			jsRegister.append("}");
		}
		jsRegister.append("]");
		jsRegister.append(", ");
		jsRegister.append("'").append(hiddenFieldNameForMapSize).append("'");
		jsRegister.append(", ");
		
		// points of interest
		if (pointsOfInterest != null)
		{
			jsRegister.append("[");
			for (int i = 0; i < pointsOfInterest.length; i++)
			{
				PointOfInterest pnt = pointsOfInterest[i];
				if (i > 0) jsRegister.append(", ");
				jsRegister.append("new PointOfInterest(");
				jsRegister.append(pnt.getX()).append(", ");
				jsRegister.append(pnt.getY()).append(", ");
				jsRegister.append("'").append(pnt.getNameJavaScriptSafe()).append("'").append(", ");
				jsRegister.append("'").append(pnt.getTextJavaScriptSafe()).append("'");
				jsRegister.append(")");
			}
			jsRegister.append("]");
		}
		else
		{
			jsRegister.append("null");
		}
		jsRegister.append(", ");
		jsRegister.append("'").append(bubbleId).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(bubbleTextId).append("'");
		jsRegister.append(", ");
		
		// scale indicator
		jsRegister.append("'").append(scaleIndicatorTextId).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(scaleIndicatorBarId).append("'");
		jsRegister.append(", ");
		
		// minimap
		if (miniMap)
		{
			jsRegister.append("'").append(miniMapControlId).append("'");
			jsRegister.append(", ");
			jsRegister.append("'").append(miniMapFrameId).append("'");
			jsRegister.append(", ");
			jsRegister.append("'").append(miniMapToggleId).append("'");
			jsRegister.append(", ");
			jsRegister.append("'").append(hiddenFieldNameForMiniMapVisibility).append("'");
			jsRegister.append(", ");
			jsRegister.append("'").append(miniMapPosition).append("'");
			jsRegister.append(", ");
			jsRegister.append(miniMapWidth);
			jsRegister.append(", ");
			jsRegister.append(miniMapHeight);
		}
		else
		{
			jsRegister.append("null");
			jsRegister.append(", ");
			jsRegister.append("null");
			jsRegister.append(", ");
			jsRegister.append("null");
			jsRegister.append(", ");
			jsRegister.append("null");
			jsRegister.append(", ");
			jsRegister.append("null");
			jsRegister.append(", ");
			jsRegister.append("null");
			jsRegister.append(", ");
			jsRegister.append("null");
		}
		
		// end of init JS
		jsRegister.append(");");
		
		// render JS
		UtilsJSF.encodeJavaScriptStart(this, writer);
		writer.write(jsRegister.toString());
		UtilsJSF.encodeJavaScriptEnd(this, writer);
		
		// hidden fields for extend
		UtilsJSF.encodeHiddenInput(this, writer, hiddenFieldNameForX1, String.valueOf(x1));
		UtilsJSF.encodeHiddenInput(this, writer, hiddenFieldNameForY1, String.valueOf(y1));
		UtilsJSF.encodeHiddenInput(this, writer, hiddenFieldNameForX2, String.valueOf(x2));
		UtilsJSF.encodeHiddenInput(this, writer, hiddenFieldNameForY2, String.valueOf(y2));
		UtilsJSF.encodeHiddenInput(this, writer, hiddenFieldNameForMiniMapVisibility, String.valueOf(miniMapVisible));
		
		// hidden field: mouse mod
		UtilsJSF.encodeHiddenInput(this, writer,
				hiddenFieldNameForMouseMode,
				mouseMode.toString());
		
		// hidden field: zoom history
		UtilsJSF.encodeHiddenInput(this, writer,
				hiddenFieldNameForZoomHistory,
				zoomHistory.toString());

		// hidden field: map size
		UtilsJSF.encodeHiddenInput(this, writer,
				hiddenFieldNameForMapSize,
				String.valueOf(mapSize.toString()));
		
		// main div style
		String mainDivStyle =
			"position: relative; " +
			"width: " + mapSize.getWidth() + "px; " +
			"height: " + mapSize.getHeight() + "px;";
		
		// frame div style
		String frameDivStyle = 
			"position: absolute; " +
			"cursor: " + mouseMode.getStyleCursor();
			
		// main div
		writer.startElement("div", this);
		writer.writeAttribute("id", mapControlId, null);
		writer.writeAttribute("style", mainDivStyle, null);
		writer.writeAttribute("class", "map-control", null);

		// map frame for tiles
		writer.startElement("div", this);
		writer.writeAttribute("id", mapFrameId, null);
		writer.writeAttribute("style", frameDivStyle, null);
		writer.endElement("div");
		
		// icon start
		encodeToolStart(writer);
		
		// icons: < / >
		encodeTool(context, writer, toolsBackId, zoomHistory.canGoBack() ? "map-icon-back" : "map-icon-back-off");
		encodeToolSepearator(context, writer);
		encodeTool(context, writer, toolsForwardId, zoomHistory.canGoForward() ? "map-icon-forward" : "map-icon-forward-off");
		encodeToolSepearator(context, writer);
		
		// icons: zoom / pan
		encodeTool(context, writer, toolsPanId, mouseMode.isPan() ? "map-icon-pan" : "map-icon-pan-off");
		encodeTool(context, writer, toolsZoomId, mouseMode.isZoom() ? "map-icon-zoom" : "map-icon-zoom-off");
		encodeToolSepearator(context, writer);
		
		// icons: zoom + / zoom -
		encodeTool(context, writer, toolsZoomMinusId, "map-icon-zoom-minus");
		encodeToolSepearator(context, writer);
		encodeToolZoomSlider(context, writer, toolsSliderBgId, toolsSliderKnobId);
		encodeToolSepearator(context, writer);
		encodeTool(context, writer, toolsZoomPlusId, "map-icon-zoom-plus");
		encodeToolSepearator(context, writer);
		
		// icons: sizes
		for (int i = 0; i < mapSizes.length; i++)
		{
			MapSize ms = getMapSizes()[i];
			String className = "map-icon-size-" + i;
			if (!ms.equals(mapSize)) className += "-off";
			encodeTool(context, writer, getElementIdForMapSize(context, i), className);
		}
		encodeToolEnd(writer);
		
		// scale indicator
		encodeScaleIndicator(context, writer, scaleIndicatorTextId, scaleIndicatorBarId);
		
		// minimap
		if (miniMap)
		{
			
			String miniMapControlStyle =
				"width: " + miniMapWidth + "px; " +
				"height: " + miniMapHeight + "px; " +
				"visibility: " + (!miniMapVisible ? "hidden" : "visible");

			String miniMapFrameStyle =
				"width: " + miniMapWidth + "px; " +
				"height: " + miniMapHeight + "px;";

			// main DIV and frame DIV
			writer.startElement("div", this);
			writer.writeAttribute("id", miniMapControlId, null);
			writer.writeAttribute("style", miniMapControlStyle, null);
			writer.writeAttribute("class", miniMapPosition.getCssClassForMapControl(), null);
			writer.startElement("div", this);
			writer.writeAttribute("id", miniMapFrameId, null);
			writer.writeAttribute("style", miniMapFrameStyle, null);
			writer.writeAttribute("class", "minimap-frame", null);
			writer.endElement("div");
			writer.endElement("div");

			// toggle button
			writer.startElement("div", this);
			writer.writeAttribute("id", miniMapToggleId, null);
			writer.writeAttribute("class", miniMapPosition.getCssClassForToggleButton(miniMapVisible), null);
			writer.endElement("div");
		
		}
		
		// bubble
		encodeBubble(context, writer, bubbleId, bubbleTextId);
		
		// end main div
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
        return (String) vb.getValue(getFacesContext());
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
        return (String) vb.getValue(getFacesContext());
	}

	public void setServerBaseUrl(String serverBaseUrl)
	{
		serverBaseUrlSet = true;
		this.serverBaseUrl = serverBaseUrl;
	}

	public void setMapSizes(MapSize[] mapSizes)
	{
		mapSizesSet = true; 
		this.mapSizes = mapSizes;
	}

	public MapSize[] getMapSizes()
	{
        if (mapSizesSet) return mapSizes;
        ValueBinding vb = getValueBinding("mapSizes");
        if (vb == null) return mapSizes;
        return (MapSize[]) vb.getValue(getFacesContext());
	}

	public void setMapSize(MapSize mapSize)
	{
		mapSizeSet = true;
		this.mapSize = mapSize;
	}

	public MapSize getMapSize()
	{
        if (mapSizeSet) return mapSize;
        ValueBinding vb = getValueBinding("mapSize");
        if (vb == null) return mapSize;
        return (MapSize) vb.getValue(getFacesContext());
	}

	public void setPointsOfInterest(PointOfInterest[] pointsOfInterest)
	{
		pointsOfInterestSet = true;
		this.pointsOfInterest = pointsOfInterest;
	}

	public PointOfInterest[] getPointsOfInterest()
	{
        if (pointsOfInterestSet) return pointsOfInterest;
        ValueBinding vb = getValueBinding("pointsOfInterest");
        if (vb == null) return pointsOfInterest;
        return (PointOfInterest[]) vb.getValue(getFacesContext());
	}

	public void setMiniMap(boolean miniMap)
	{
		miniMapSet = true;
		this.miniMap = miniMap;
	}

	public boolean isMiniMap()
	{
        if (miniMapSet) return miniMap;
        ValueBinding vb = getValueBinding("miniMap");
        if (vb == null) return miniMap;
        return ((Boolean) vb.getValue(getFacesContext())).booleanValue();
	}

	public void setMiniMapPosition(MiniMapPosition miniMapPosition)
	{
		miniMapPositionSet = true;
		this.miniMapPosition = miniMapPosition;
	}

	public MiniMapPosition getMiniMapPosition()
	{
        if (miniMapPositionSet) return miniMapPosition;
        ValueBinding vb = getValueBinding("miniMapPosition");
        if (vb == null) return miniMapPosition;
        return (MiniMapPosition) vb.getValue(getFacesContext());
	}

	public void setMiniMapWidth(int miniMapWidth)
	{
		miniMapWidthSet = true;
		this.miniMapWidth = miniMapWidth;
	}

	public int getMiniMapWidth()
	{
		return UtilsJSF.getComponentInt(this, getFacesContext(),
				miniMapWidthSet, miniMapWidth, "miniMapWidth");
	}

	public void setMiniMapHeight(int miniMapHeight)
	{
		miniMapHeightSet = true;
		this.miniMapHeight = miniMapHeight;
	}

	public int getMiniMapHeight()
	{
		return UtilsJSF.getComponentInt(this, getFacesContext(),
				miniMapHeightSet, miniMapHeight, "miniMapHeight");
	}

}
