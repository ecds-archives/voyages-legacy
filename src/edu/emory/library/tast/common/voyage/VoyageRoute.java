package edu.emory.library.tast.common.voyage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tast.maps.component.Line;
import edu.emory.library.tast.maps.component.PointOfInterest;

public class VoyageRoute
{
	
	private static final int ROUTE_DOTS_SPACING = 20;

	private ArrayList legs = new ArrayList();
	
	public VoyageRoute()
	{
	}
	
	public void addLeg(VoyageRouteLeg leg)
	{
		legs.add(leg);
	}
	
	public ArrayList getLegs()
	{
		return legs;
	}

	public PointOfInterest[] createMapPointsOfInterest()
	{
		
		int pointIdx = 0;
		HashMap placeIdPointIdx = new HashMap();
		List pointsList = new ArrayList();
		
		for (Iterator legIter = legs.iterator(); legIter.hasNext();)
		{
			VoyageRouteLeg leg = (VoyageRouteLeg) legIter.next();
			
			for (Iterator placeIter = leg.getPlaces().iterator(); placeIter.hasNext();)
			{
				
				VoyageRoutePlace place = (VoyageRoutePlace) placeIter.next();
				Long placeIdLong = new Long(place.getId());

				Integer currPointIdx = (Integer) placeIdPointIdx.get(placeIdLong);
				ArrayList placeDescs;
				if (currPointIdx != null)
				{
					placeDescs = (ArrayList) pointsList.get(currPointIdx.intValue());
				}
				else
				{
					placeDescs = new ArrayList(); 
					pointsList.add(placeDescs);
					placeIdPointIdx.put(placeIdLong, new Integer(pointIdx));
					pointIdx++;
				}
				
				placeDescs.add(place);
				
			}
		}
		
		
		StringBuffer text = new StringBuffer(); 
		PointOfInterest[] points = new PointOfInterest[pointsList.size()];
		for (int i = 0; i < pointsList.size(); i++)
		{
			
			ArrayList places = (ArrayList) pointsList.get(i);
			VoyageRoutePlace firstPlace = (VoyageRoutePlace) places.get(0);
			
			ArrayList symbolsList = new ArrayList();

			text.setLength(0);
			text.append("<div class=\"voyage-route-popup-place-name\">");
			text.append(firstPlace.getName());
			text.append("</div>");
			
			for (int j = 0; j < places.size(); j++)
			{
				VoyageRoutePlace place = (VoyageRoutePlace) places.get(j);
				
				String[] placeSymbols = places.size() > 1 ?
					place.getSymbol().getNthSymbol(j) :
					place.getSymbol().getSimpleSymbol();

				for (int k = 0; k < placeSymbols.length; k++)
					symbolsList.add(placeSymbols[k]);
				
				text.append("<div class=\"voyage-route-popup-event\">");
				text.append(place.getPurpose());
				text.append("</div>");
			}
			
			String[] symbols = new String[symbolsList.size()];
			symbolsList.toArray(symbols);

			points[i] = new PointOfInterest(
					firstPlace.getLongitude(),
					firstPlace.getLatitude(),
					symbols,
					firstPlace.getName(),
					text.toString());				
		
		}

		return points;

	}
	
	public Line[] createMapLines()
	{
		
		ArrayList linesList = new ArrayList();
		VoyageRoutePlace prevPlace = null;
			
		for (Iterator legIter = legs.iterator(); legIter.hasNext();)
		{
			VoyageRouteLeg leg = (VoyageRouteLeg) legIter.next();
			for (Iterator placeIter = leg.getPlaces().iterator(); placeIter.hasNext();)
			{
				VoyageRoutePlace place = (VoyageRoutePlace) placeIter.next();
				if (prevPlace != null && place.getId() != prevPlace.getId())
				{
					linesList.add(new Line(
							prevPlace.getLongitude(),
							prevPlace.getLatitude(),
							place.getLongitude(),
							place.getLatitude(),
							"path-dot",
							ROUTE_DOTS_SPACING));
				}
				prevPlace = place;
			}
		}
		
		Line[] lines = new Line[linesList.size()];
		linesList.toArray(lines);

		return lines;
		
	}

}
