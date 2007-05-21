package edu.emory.library.tast.maps.component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class MapComponent extends UIComponentBase
{
	
	private final static MapSize defaultMapSize = new MapSize(480, 320);

	private boolean zoomLevelsSet = false;
	private ZoomLevel[] zoomLevels = null;

	private boolean mapSizesSet = false;
	private MapSize[] mapSizes = new MapSize[] {
		new MapSize(480, 320),
		new MapSize(800, 600),
		new MapSize(1024, 768)
	};
	
	private boolean mapSizeSet = false;
	private MapSize mapSize = (MapSize) defaultMapSize.clone();

	private boolean pointsOfInterestSet = false;
	private PointOfInterest[] pointsOfInterest = null;

	private double centerX = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_CENTER_X);
	private double centerY = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_CENTER_Y);
	private int zoomLevel = 0;
	
	private boolean miniMapZoomLevelSet = false;
	private ZoomLevel miniMapZoomLevel = null;
	
	private boolean miniMapSet = false;
	private boolean miniMap = true;
	
	private boolean miniMapVisible = true;
	
	private boolean miniMapPositionSet = false;
	private MiniMapPosition miniMapPosition = MiniMapPosition.BottomRight;

	private boolean miniMapWidthSet = false;
	private int miniMapWidth = 100;

	private boolean miniMapHeightSet = false;
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
		return new Object[] {
			super.saveState(context),
			zoomLevels,
			new Double(centerX),
			new Double(centerY),
			new Integer(zoomLevel),
			mouseMode,
			mapSizes,
			mapSize,
			zoomHistory,
			new Boolean(miniMap),
			miniMapZoomLevel,
			new Boolean(miniMapVisible),
			miniMapPosition,
			new Integer(miniMapWidth),
			new Integer(miniMapHeight) };
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		int i = 0;
		Object[] values = (Object[]) state;
		super.restoreState(context, values[i++]);
		zoomLevels = (ZoomLevel[]) values[i++];
		centerX = ((Double)values[i++]).doubleValue();
		centerY = ((Double)values[i++]).doubleValue();
		zoomLevel = ((Integer)values[i++]).intValue();
		mouseMode = (MouseMode)values[i++];
		mapSizes = (MapSize[]) values[i++];
		mapSize = (MapSize) values[i++];
		zoomHistory = (ZoomHistory) values[i++];
		miniMap = ((Boolean)values[i++]).booleanValue();
		miniMapZoomLevel = (ZoomLevel)values[i++];
		miniMapVisible = ((Boolean)values[i++]).booleanValue();
		miniMapPosition = (MiniMapPosition) values[i++];
		miniMapWidth = ((Integer)values[i++]).intValue();
		miniMapHeight = ((Integer)values[i++]).intValue();
	}
	
	private String getHiddenFieldNameForCenterX(FacesContext context)
	{
		return getClientId(context) + "_center_x";
	}
	
	private String getHiddenFieldNameForCenterY(FacesContext context)
	{
		return getClientId(context) + "_center_y";
	}

	private String getHiddenFieldNameForZoomLevel(FacesContext context)
	{
		return getClientId(context) + "_zoom_level";
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
		
		centerX = JsfUtils.getParamDouble(params, getHiddenFieldNameForCenterX(context), centerX);
		centerY = JsfUtils.getParamDouble(params, getHiddenFieldNameForCenterY(context), centerY);
		zoomLevel = JsfUtils.getParamInt(params, getHiddenFieldNameForZoomLevel(context), zoomLevel);
		
		String mouseModeStr = JsfUtils.getParamString(params, getHiddenFieldNameForMouseMode(context));
		if (!StringUtils.isNullOrEmpty(mouseModeStr))
			mouseMode = MouseMode.parse(mouseModeStr);
		
		String zoomHistoryStr = JsfUtils.getParamString(params, getHiddenFieldNameForZoomHistory(context));
		if (!StringUtils.isNullOrEmpty(zoomHistoryStr))
			zoomHistory = ZoomHistory.parse(zoomHistoryStr);
		
		String mapSizeStr = JsfUtils.getParamString(params, getHiddenFieldNameForMapSize(context));
		if (!StringUtils.isNullOrEmpty(mapSizeStr))
			mapSize = MapSize.parse(mapSizeStr, true);
		
		miniMapVisible = JsfUtils.getParamBoolean(params, getHiddenFieldNameForMiniMapVisibility(context), miniMapVisible);
		
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
		UIForm form = JsfUtils.getForm(this, context);
		
		ZoomLevel[] zoomLevels = getZoomLevels();
		
		String mapId = getClientId(context);
		String mapControlId = getClientId(context) + "_control";
		String tilesContainerId = getClientId(context) + "_tiles_container";
		
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

		String hiddenFieldNameCenterX = getHiddenFieldNameForCenterX(context);
		String hiddenFieldNameCenterY = getHiddenFieldNameForCenterY(context);
		String hiddenFieldNameZoomLevel = getHiddenFieldNameForZoomLevel(context);
		String hiddenFieldNameForMouseMode = getHiddenFieldNameForMouseMode(context);
		String hiddenFieldNameForZoomHistory = getHiddenFieldNameForZoomHistory(context);
		String hiddenFieldNameForMapSize = getHiddenFieldNameForMapSize(context);
		String hiddenFieldNameForMiniMapVisibility = getHiddenFieldNameForMiniMapVisibility(context);
		
		// from beans or prev values
		mapSize = getMapSize();
		mapSizes = getMapSizes();
		PointOfInterest[] pointsOfInterest = getPointsOfInterest();
		miniMap = isMiniMap();
		miniMapZoomLevel = getMiniMapZoomLevel();
		miniMapPosition = getMiniMapPosition();
		miniMapWidth = getMiniMapWidth();
		miniMapHeight = getMiniMapHeight();

		// sort points of interest
		Arrays.sort(pointsOfInterest, new Comparator() {
			public int compare(Object arg0, Object arg1)
			{
				PointOfInterest pnt0 = (PointOfInterest) arg0;
				PointOfInterest pnt1 = (PointOfInterest) arg1;
				return pnt0.getShowAtZoom() == pnt1.getShowAtZoom() ? 0 : pnt0.getShowAtZoom() < pnt1.getShowAtZoom() ? 1 : -1;
			}});

		// at least the default map size
		if (mapSizes == null || mapSizes.length == 0)
			mapSizes = new MapSize[] {defaultMapSize};

		// init JS
		StringBuffer jsRegister = new StringBuffer();
		
		// map id
		jsRegister.append("MapsGlobal.registerMap(");
		jsRegister.append("'").append(mapId).append("'");
		jsRegister.append(", ");
		
		// fixed size
		jsRegister.append("true");
		jsRegister.append(", ");
		
		// form name
		jsRegister.append("'").append(form.getId()).append("'");
		jsRegister.append(", ");
		
		// main HTML elements
		jsRegister.append("'").append(mapControlId).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(tilesContainerId).append("'");
		jsRegister.append(", ");
		
		// zoom levels
		jsRegister.append("[");
		for (int i = 0; i < zoomLevels.length; i++)
		{
			ZoomLevel zoomLevel = zoomLevels[i];
			jsRegister.append("new MapZoomLevel(");
			jsRegister.append(zoomLevel.getBottomLeftTileX()).append(", ");
			jsRegister.append(zoomLevel.getBottomLeftTileY()).append(", ");
			jsRegister.append(zoomLevel.getTilesNumX()).append(", ");
			jsRegister.append(zoomLevel.getTilesNumY()).append(", ");
			jsRegister.append(zoomLevel.getScale()).append(", ");
			jsRegister.append("'").append(zoomLevel.getTilesDir()).append("'");
			jsRegister.append(")");
		}
		jsRegister.append("]");
		jsRegister.append(", ");

		// hidden fields: extends
		jsRegister.append("'").append(hiddenFieldNameCenterX).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(hiddenFieldNameCenterY).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(hiddenFieldNameZoomLevel).append("'");
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
				String[] symbols = pnt.getSymbols();
				if (i > 0) jsRegister.append(", ");
				jsRegister.append("new PointOfInterest(");
				jsRegister.append(pnt.getX());
				jsRegister.append(", ");
				jsRegister.append(pnt.getY());
				jsRegister.append(", ");
				jsRegister.append(pnt.getShowAtZoom());
				jsRegister.append(", ");
				jsRegister.append("'").append(pnt.getLabelJavaScriptSafe()).append("'");
				jsRegister.append(", ");
				jsRegister.append("'").append(pnt.getTextJavaScriptSafe());
				jsRegister.append("', [");
				for (int j = 0; j < symbols.length; j++)
				{
					Symbol symbol = Symbol.get(symbols[j]);
					if (j > 0) jsRegister.append(", ");
					jsRegister.append("new MapSymbol(");
					jsRegister.append("'").append(symbol.getName()).append("'");
					jsRegister.append(", ");
					jsRegister.append("'").append(symbol.getUrl()).append("'");
					jsRegister.append(", ");
					jsRegister.append(symbol.getWidth());
					jsRegister.append(", ");
					jsRegister.append(symbol.getHeight());
					jsRegister.append(", ");
					jsRegister.append(symbol.getCenterX());
					jsRegister.append(", ");
					jsRegister.append(symbol.getCenterY());
					jsRegister.append(")");
				}
				jsRegister.append("]");
				jsRegister.append(")");
			}
			jsRegister.append("]");
		}
		else
		{
			jsRegister.append("null");
		}
		jsRegister.append(", ");
		
		// bubble
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
			jsRegister.append("new MapZoomLevel(");
			jsRegister.append(miniMapZoomLevel.getBottomLeftTileX()).append(", ");
			jsRegister.append(miniMapZoomLevel.getBottomLeftTileY()).append(", ");
			jsRegister.append(miniMapZoomLevel.getTilesNumX()).append(", ");
			jsRegister.append(miniMapZoomLevel.getTilesNumY()).append(", ");
			jsRegister.append(miniMapZoomLevel.getScale()).append(", ");
			jsRegister.append("'").append(miniMapZoomLevel.getTilesDir()).append("'");
			jsRegister.append(")");
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
			jsRegister.append(", ");
			jsRegister.append("null");
		}
		
		// end of init JS
		jsRegister.append(");");
		
		// render JS
		JsfUtils.encodeJavaScriptStart(this, writer);
		writer.write(jsRegister.toString());
		JsfUtils.encodeJavaScriptEnd(this, writer);
		
		// hidden fields for extend
		JsfUtils.encodeHiddenInput(this, writer, hiddenFieldNameCenterX, String.valueOf(centerX));
		JsfUtils.encodeHiddenInput(this, writer, hiddenFieldNameCenterY, String.valueOf(centerY));
		JsfUtils.encodeHiddenInput(this, writer, hiddenFieldNameZoomLevel, String.valueOf(zoomLevel));
		JsfUtils.encodeHiddenInput(this, writer, hiddenFieldNameForMiniMapVisibility, String.valueOf(miniMapVisible));
		
		// hidden field: mouse mod
		JsfUtils.encodeHiddenInput(this, writer,
				hiddenFieldNameForMouseMode,
				mouseMode.toString());
		
		// hidden field: zoom history
		JsfUtils.encodeHiddenInput(this, writer,
				hiddenFieldNameForZoomHistory,
				zoomHistory.toString());

		// hidden field: map size
		JsfUtils.encodeHiddenInput(this, writer,
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

		// container for tiles
		writer.startElement("div", this);
		writer.writeAttribute("id", tilesContainerId, null);
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

	public void setMapSizes(MapSize[] mapSizes)
	{
		mapSizesSet = true; 
		this.mapSizes = mapSizes;
	}

	public MapSize[] getMapSizes()
	{
		return (MapSize[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"mapSizes", mapSizesSet, mapSizes);
	}

	public void setMapSize(MapSize mapSize)
	{
		mapSizeSet = true;
		this.mapSize = mapSize;
	}

	public MapSize getMapSize()
	{
		return (MapSize) JsfUtils.getCompPropObject(this, getFacesContext(),
				"mapSize", mapSizeSet, mapSize);
	}

	public void setPointsOfInterest(PointOfInterest[] pointsOfInterest)
	{
		pointsOfInterestSet = true;
		this.pointsOfInterest = pointsOfInterest;
	}

	public PointOfInterest[] getPointsOfInterest()
	{
		return (PointOfInterest[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"pointsOfInterest", pointsOfInterestSet, pointsOfInterest);
	}

	public void setMiniMap(boolean miniMap)
	{
		miniMapSet = true;
		this.miniMap = miniMap;
	}

	public boolean isMiniMap()
	{
		return JsfUtils.getCompPropBoolean(this, getFacesContext(),
				"miniMap", miniMapSet, miniMap);
	}

	public void setMiniMapPosition(MiniMapPosition miniMapPosition)
	{
		miniMapPositionSet = true;
		this.miniMapPosition = miniMapPosition;
	}

	public MiniMapPosition getMiniMapPosition()
	{
		return (MiniMapPosition) JsfUtils.getCompPropObject(this, getFacesContext(),
				"miniMapPosition", miniMapPositionSet, miniMapPosition);
	}

	public void setMiniMapWidth(int miniMapWidth)
	{
		miniMapWidthSet = true;
		this.miniMapWidth = miniMapWidth;
	}

	public int getMiniMapWidth()
	{
		return JsfUtils.getComponentInt(this, getFacesContext(),
				miniMapWidthSet, miniMapWidth, "miniMapWidth");
	}

	public void setMiniMapHeight(int miniMapHeight)
	{
		miniMapHeightSet = true;
		this.miniMapHeight = miniMapHeight;
	}

	public int getMiniMapHeight()
	{
		return JsfUtils.getComponentInt(this, getFacesContext(),
				miniMapHeightSet, miniMapHeight, "miniMapHeight");
	}

	public ZoomLevel[] getZoomLevels()
	{
		return (ZoomLevel[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"zoomLevels", zoomLevelsSet, zoomLevels);
	}

	public void setZoomLevels(ZoomLevel[] zoomLevels)
	{
		zoomLevelsSet = true;
		this.zoomLevels = zoomLevels;
	}

	public ZoomLevel getMiniMapZoomLevel()
	{
		return (ZoomLevel) JsfUtils.getCompPropObject(this, getFacesContext(),
				"miniMapZoomLevel", miniMapZoomLevelSet, miniMapZoomLevel);
	}

	public void setMiniMapZoomLevel(ZoomLevel miniMapZoomLevel)
	{
		miniMapZoomLevelSet = true;
		this.miniMapZoomLevel = miniMapZoomLevel;
	}

}