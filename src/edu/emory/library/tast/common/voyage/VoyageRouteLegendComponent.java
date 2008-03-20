package edu.emory.library.tast.common.voyage;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.maps.component.Symbol;
import edu.emory.library.tast.util.JsfUtils;

public class VoyageRouteLegendComponent extends UIComponentBase
{
	
	public String getFamily()
	{
		return null;
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		String mapAssetsBaseUrl = AppConfig.getConfiguration().getString(AppConfig.MAP_URL);
		
		// get data
		VoyageRoute route = getRoute();
		
		// main frame
		writer.startElement("div", this);
		writer.writeAttribute("class", "voyage-route-legend", null);

		// for each leg
		for (Iterator legIter = route.getLegs().iterator(); legIter.hasNext();)
		{
			VoyageRouteLeg leg = (VoyageRouteLeg) legIter.next();
			
			// leg frame
			writer.startElement("div", this);
			writer.writeAttribute("class", "voyage-route-legend-leg", null);
			
			// for each place
			for (Iterator placeIter = leg.getPlaces().iterator(); placeIter.hasNext();)
			{
				VoyageRoutePlace place = (VoyageRoutePlace) placeIter.next();
				
				Symbol symbol = Symbol.get(place.getSymbol().getLegendSymbol()); 
				
				String cssStyle =
					"background-image: " +
					"url(" + mapAssetsBaseUrl + symbol.getUrl() + ")";
				
				// place
				writer.startElement("div", this);
				writer.writeAttribute("class", "voyage-route-legend-place", null);
				writer.writeAttribute("style", cssStyle, null);
				
				// place name
				writer.startElement("div", this);
				writer.writeAttribute("class", "voyage-route-legend-place-name", null);
				writer.write(place.getName());
				writer.endElement("div");

				// purpose
				writer.startElement("div", this);
				writer.writeAttribute("class", "voyage-route-legend-place-purpose", null);
				writer.write(place.getPurpose());
				writer.endElement("div");
				
				// info lines
				for (Iterator iterator = place.getInfoLines().iterator(); iterator.hasNext();)
				{
					VoyageRouteInfoLine info = (VoyageRouteInfoLine) iterator.next();
					writer.startElement("div", this);
					writer.startElement("span", this);
					writer.writeAttribute("class", "voyage-route-legend-info-line-name", null);
					writer.write(info.getName());
					writer.endElement("span");
					writer.write(": ");
					writer.startElement("span", this);
					writer.writeAttribute("class", "voyage-route-legend-info-line-value", null);
					writer.write(info.getValue());
					writer.endElement("span");
					writer.endElement("div");
				}

				// place
				writer.endElement("div");
				
			}

			// leg frame
			writer.endElement("div");
		
		}
		
		// main frame
		writer.endElement("tr");
		
	}

	public VoyageRoute getRoute()
	{
		return (VoyageRoute) JsfUtils.getCompPropObject(this, getFacesContext(),
				"route", false, null);
	}

}
