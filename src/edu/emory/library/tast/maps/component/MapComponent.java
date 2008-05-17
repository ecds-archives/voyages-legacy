package edu.emory.library.tast.maps.component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class MapComponent extends UIComponentBase
{
	
	private static final int ZOOM_SLIDER_SLOT_WIDTH = 24;
	
	private boolean x1Set = false;
	private boolean x2Set = false;
	private boolean y1Set = false;
	private boolean y2Set = false;

	private double x1 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_X1);
	private double y1 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_Y1);
	private double x2 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_X2);
	private double y2 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_Y2);

	private boolean zoomLevelsSet = false;
	private ZoomLevel[] zoomLevels = null;

	private final static MapSize defaultMapSize = new MapSize(420, 420);

	private boolean mapSizesSet = false;
	private MapSize[] mapSizes = new MapSize[] {
		new MapSize(420, 420),	
		new MapSize(650, 600),	
		new MapSize(1024, 768)
	};
	
	private boolean mapSizeSet = false;
	private MapSize mapSize = (MapSize) defaultMapSize.clone();

	private boolean pointsOfInterestSet = false;
	private PointOfInterest[] pointsOfInterest = null;

	private boolean linesSet = false;
	private Line[] lines = null;

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

	private boolean zoomHistorySet = false;
	private ZoomHistory zoomHistory = new ZoomHistory();
	
	private MouseMode mouseMode = MouseMode.Pan;
	
	private boolean pointsSelectIdSet = false;
	private String pointsSelectId;
	
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
			new Double(x1),
			new Double(y1),
			new Double(x2),
			new Double(y2),
			mouseMode,
			mapSizes,
			mapSize,
			zoomHistory,
			new Boolean(miniMap),
			miniMapZoomLevel,
			new Boolean(miniMapVisible),
			miniMapPosition,
			new Integer(miniMapWidth),
			new Integer(miniMapHeight),
			pointsSelectId};
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		int i = 0;
		Object[] values = (Object[]) state;
		super.restoreState(context, values[i++]);
		zoomLevels = (ZoomLevel[]) values[i++];
		x1 = ((Double)values[i++]).doubleValue();
		y1 = ((Double)values[i++]).doubleValue();
		x2 = ((Double)values[i++]).doubleValue();
		y2 = ((Double)values[i++]).doubleValue();
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
		pointsSelectId = (String) values[i++];
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

	private String getHiddenFieldNameForActionName(FacesContext context)
	{
		return getClientId(context) + "_action";
	}

	private String getHiddenFieldNameForActionParam(FacesContext context)
	{
		return getClientId(context) + "_param";
	}

	public void decode(FacesContext context)
	{
		Map params = context.getExternalContext().getRequestParameterMap();
		
		x1 = JsfUtils.getParamDouble(params, getHiddenFieldNameForX1(context), x1);
		y1 = JsfUtils.getParamDouble(params, getHiddenFieldNameForY1(context), y1);
		x2 = JsfUtils.getParamDouble(params, getHiddenFieldNameForX2(context), x2);
		y2 = JsfUtils.getParamDouble(params, getHiddenFieldNameForY2(context), y2);
		
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
	
	public void processUpdates(FacesContext context)
	{
		
		ValueBinding vbZoomHistory = getValueBinding("zoomHistory");
		if (vbZoomHistory != null) vbZoomHistory.setValue(context, zoomHistory);
		
		ValueBinding vbX1 = getValueBinding("x1");
		if (vbX1 != null) vbX1.setValue(context, new Double(x1));
		
		ValueBinding vbX2 = getValueBinding("x2");
		if (vbX2 != null) vbX2.setValue(context, new Double(x2));

		ValueBinding vbY1 = getValueBinding("y1");
		if (vbY1 != null) vbY1.setValue(context, new Double(y1));
		
		ValueBinding vbY2 = getValueBinding("y2");
		if (vbY2 != null) vbY2.setValue(context, new Double(y2));

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
	
	private void encodeTool(FacesContext context, ResponseWriter writer, String id, String className, String tooltip) throws IOException
	{
		writer.startElement("td", this);
		writer.writeAttribute("title", tooltip, null);
		writer.writeAttribute("id", id, null);
		writer.writeAttribute("class", className, null);
		writer.endElement("td");
	}

	private void encodeToolZoomSlider(FacesContext context, ResponseWriter writer, String contId, String knobId, String tooltip) throws IOException
	{
		
		if (this.zoomLevels == null || this.zoomLevels.length <= 1)
			return;
		
		int n = this.zoomLevels.length;
		
		writer.startElement("td", this);
		writer.writeAttribute("title", tooltip, null);
		writer.writeAttribute("id", contId, null);
		writer.writeAttribute("class", "map-zoom-slider", null);
		writer.writeAttribute("style", "width: " + (n*ZOOM_SLIDER_SLOT_WIDTH) + "px;", null);
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "map-zoom-slider-cont", null);
		writer.writeAttribute("style", "width: " + (n*ZOOM_SLIDER_SLOT_WIDTH) + "px;", null);

		for (int i = 0; i < n; i++)
		{
			writer.startElement("div", this);
			if (i == 0)
			{
				writer.writeAttribute("class", "map-zoom-slider-left", null);
			}
			else if (i == this.zoomLevels.length - 1)
			{
				writer.writeAttribute("class", "map-zoom-slider-right", null);
			}
			else
			{
				writer.writeAttribute("class", "map-zoom-slider-middle", null);
			}
			writer.writeAttribute("style", "left: " + (i*ZOOM_SLIDER_SLOT_WIDTH) + "px;", null);
			writer.endElement("div");
		}

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
		writer.writeAttribute("title", "Enable/disable mini-map", null);
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
		
		String mapId = getClientId(context);
		String mapControlId = getClientId(context) + "_control";
		String tilesContainerId = getClientId(context) + "_tiles_container";
		
		String toolsBackId = getClientId(context) + "_back";
		String toolsForwardId = getClientId(context) + "_forward";
		String toolsZoomPlusId = getClientId(context) + "_zoom_plus";
		String toolsZoomMinusId = getClientId(context) + "_zoom_minus";
		String toolsSliderContId = getClientId(context) + "_zoom_slider_cont";
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

		String hiddenFieldNameX1 = getHiddenFieldNameForX1(context);
		String hiddenFieldNameY1 = getHiddenFieldNameForY1(context);
		String hiddenFieldNameX2 = getHiddenFieldNameForX2(context);
		String hiddenFieldNameY2 = getHiddenFieldNameForY2(context);
		String hiddenFieldNameForMouseMode = getHiddenFieldNameForMouseMode(context);
		String hiddenFieldNameForZoomHistory = getHiddenFieldNameForZoomHistory(context);
		String hiddenFieldNameForMapSize = getHiddenFieldNameForMapSize(context);
		String hiddenFieldNameForMiniMapVisibility = getHiddenFieldNameForMiniMapVisibility(context);
		
		String mapAssetsBaseUrl = AppConfig.getConfiguration().getString(AppConfig.MAP_URL);
		
		// from beans or prev values
		zoomLevels = getZoomLevels();
		mapSize = getMapSize();
		mapSizes = getMapSizes();
		PointOfInterest[] pointsOfInterest = getPointsOfInterest();
		Line[] lines = getLines();
		miniMap = isMiniMap();
		miniMapZoomLevel = getMiniMapZoomLevel();
		miniMapPosition = getMiniMapPosition();
		miniMapWidth = getMiniMapWidth();
		miniMapHeight = getMiniMapHeight();
		pointsSelectId = getPointsSelectId();
		x1 = getX1();
		y1 = getY1();
		x2 = getX2();
		y2 = getY2();

		// sort points of interest
		Arrays.sort(pointsOfInterest, new Comparator() {
			public int compare(Object arg0, Object arg1)
			{
				PointOfInterest pnt0 = (PointOfInterest) arg0;
				PointOfInterest pnt1 = (PointOfInterest) arg1;
				// return pnt0.getShowAtZoom() == pnt1.getShowAtZoom() ? 0 : pnt0.getShowAtZoom() < pnt1.getShowAtZoom() ? 1 : -1;
				return pnt0.getLabel().compareTo(pnt1.getLabel());
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
		
		// custom action (not used and not fully implemented now)
		jsRegister.append("'").append(getHiddenFieldNameForActionName(context)).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(getHiddenFieldNameForActionParam(context)).append("'");
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
			if (i > 0) jsRegister.append(", ");
			jsRegister.append("new MapZoomLevel(");
			jsRegister.append(zoomLevel.getTileWidth()).append(", ");
			jsRegister.append(zoomLevel.getTileHeight()).append(", ");
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
		jsRegister.append("'").append(hiddenFieldNameX1).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(hiddenFieldNameY1).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(hiddenFieldNameX2).append("'");
		jsRegister.append(", ");
		jsRegister.append("'").append(hiddenFieldNameY2).append("'");
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
		jsRegister.append(ZOOM_SLIDER_SLOT_WIDTH);
		jsRegister.append(", ");
		jsRegister.append("'").append(toolsSliderContId).append("'");
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
					jsRegister.append("'").append(mapAssetsBaseUrl).append(symbol.getUrl()).append("'");
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
		
		// lines
		if (lines != null)
		{
			jsRegister.append("[");
			for (int i = 0; i < lines.length; i++)
			{
				Line line = lines[i];
				Symbol symbol = Symbol.get(line.getSymbol());
				if (i > 0) jsRegister.append(", ");
				jsRegister.append("new MapLine(");
				jsRegister.append(line.getX1());
				jsRegister.append(", ");
				jsRegister.append(line.getY1());
				jsRegister.append(", ");
				jsRegister.append(line.getX2());
				jsRegister.append(", ");
				jsRegister.append(line.getY2());
				jsRegister.append(", ");
				jsRegister.append(line.getSymbolSpacing());
				jsRegister.append(", ");
				jsRegister.append("new MapSymbol(");
				jsRegister.append("'").append(symbol.getName()).append("'");
				jsRegister.append(", ");
				jsRegister.append("'").append(mapAssetsBaseUrl).append(symbol.getUrl()).append("'");
				jsRegister.append(", ");
				jsRegister.append(symbol.getWidth());
				jsRegister.append(", ");
				jsRegister.append(symbol.getHeight());
				jsRegister.append(", ");
				jsRegister.append(symbol.getCenterX());
				jsRegister.append(", ");
				jsRegister.append(symbol.getCenterY());
				jsRegister.append(")");
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
			jsRegister.append(miniMapZoomLevel.getTileWidth()).append(", ");
			jsRegister.append(miniMapZoomLevel.getTileHeight()).append(", ");
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
		
		// select with the list of places
		jsRegister.append(", ");
		if (pointsSelectId != null)
			jsRegister.append("'").append(pointsSelectId).append("'");
		else
			jsRegister.append("null");
		
		// end of init JS
		jsRegister.append(");");
		
		// render JS
		JsfUtils.encodeJavaScriptStart(this, writer);
		writer.write(jsRegister.toString());
		JsfUtils.encodeJavaScriptEnd(this, writer);
		
		// hidden fields for extend
		JsfUtils.encodeHiddenInput(this, writer, hiddenFieldNameX1, String.valueOf(x1));
		JsfUtils.encodeHiddenInput(this, writer, hiddenFieldNameY1, String.valueOf(y1));
		JsfUtils.encodeHiddenInput(this, writer, hiddenFieldNameX2, String.valueOf(x2));
		JsfUtils.encodeHiddenInput(this, writer, hiddenFieldNameY2, String.valueOf(y2));
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
		encodeTool(context, writer, toolsBackId, zoomHistory.canGoBack() ? "map-icon-back" : "map-icon-back-off", "Previous view/zoom");
		encodeToolSepearator(context, writer);
		encodeTool(context, writer, toolsForwardId, zoomHistory.canGoForward() ? "map-icon-forward" : "map-icon-forward-off", "Next view/zoom");
		encodeToolSepearator(context, writer);
		
		// icons: zoom / pan
		encodeTool(context, writer, toolsPanId, mouseMode.isPan() ? "map-icon-pan" : "map-icon-pan-off", "Pan view");
		encodeTool(context, writer, toolsZoomId, mouseMode.isZoom() ? "map-icon-zoom" : "map-icon-zoom-off", "Zoom view by rectangle");
		encodeToolSepearator(context, writer);
		
		// icons: zoom + / zoom -
		encodeTool(context, writer, toolsZoomMinusId, "map-icon-zoom-minus", "Zoom out");
		encodeToolSepearator(context, writer);
		encodeToolZoomSlider(context, writer, toolsSliderContId, toolsSliderKnobId, "Adjust zoom");
		encodeToolSepearator(context, writer);
		encodeTool(context, writer, toolsZoomPlusId, "map-icon-zoom-plus", "Zoom in");
		encodeToolSepearator(context, writer);
		
		// icons: sizes
		for (int i = 0; i < mapSizes.length; i++)
		{
			MapSize ms = getMapSizes()[i];
			String className = "map-icon-size-" + i;
			if (!ms.equals(mapSize)) className += "-off";
			encodeTool(context, writer, getElementIdForMapSize(context, i), className, "Map size");
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
			//writer.writeAttribute("onmouseover", "myHint.show(" + 8 + ",this)", null);
			//writer.writeAttribute("onmouseout", "myHint.hide()", null);
			writer.endElement("div");
		
		}
		
		// bubble
		encodeBubble(context, writer, bubbleId, bubbleTextId);
		
		//end main div
		writer.endElement("div");
		
		/*
		writer.write("List of visible places: ");
		writer.startElement("select", this);
		writer.writeAttribute("id", this.getHiddenFieldNameForPlacesListBox(context), null);
		writer.endElement("select");
		StringBuffer buffer = new StringBuffer();
		buffer.append("MapsGlobal.clickedShowMap(");
		buffer.append("'").append(mapId).append("'");
		buffer.append("); return false;");
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", "Show place", null);
		writer.writeAttribute("onClick", buffer.toString(), null);
		writer.endElement("input");
		*/
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

	public void setLines(Line[] lines)
	{
		linesSet = true;
		this.lines = lines;
	}

	public Line[] getLines()
	{
		return (Line[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"lines", linesSet, lines);
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

	public String getPointsSelectId()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"pointsSelectId", pointsSelectIdSet, pointsSelectId);
	}

	public void setPointsSelectId(String placesSelectId)
	{
		pointsSelectIdSet = true;
		this.pointsSelectId = placesSelectId;
	}

	public ZoomHistory getZoomHistory()
	{
		return (ZoomHistory) JsfUtils.getCompPropObject(this, getFacesContext(),
				"zoomHistory", zoomHistorySet, zoomHistory);
	}

	public void setZoomHistory(ZoomHistory zoomHistory)
	{
		zoomHistorySet = true;
		this.zoomHistory = zoomHistory;
	}

	public double getX1()
	{
		return JsfUtils.getCompPropDouble(this, getFacesContext(),
				"x1", x1Set, x1);
	}

	public void setX1(double x1)
	{
		x1Set = true;
		this.x1 = x1;
	}

	public double getY1()
	{
		return JsfUtils.getCompPropDouble(this, getFacesContext(),
				"y1", y1Set, y1);
	}

	public void setY1(double y1)
	{
		y1Set = true;
		this.y1 = y1;
	}

	public double getX2()
	{
		return JsfUtils.getCompPropDouble(this, getFacesContext(),
				"x2", x2Set, x2);
	}

	public void setX2(double x2)
	{
		x2Set = true;
		this.x2 = x2;
	}

	public double getY2()
	{
		return JsfUtils.getCompPropDouble(this, getFacesContext(),
				"y2", y2Set, y2);
	}

	public void setY2(double y2)
	{
		y2Set = true;
		this.y2 = y2;
	}

}