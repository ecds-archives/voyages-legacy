package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.dm.attributes.Attribute;

public class SearchableAttributeLocation extends SearchableAttribute
{

	private Location[] locations;
	
	public class Location
	{
		
		private Attribute port;
		private Attribute region;
		
		public Location(Attribute port, Attribute region)
		{
			super();
			this.port = port;
			this.region = region;
		}

		public Attribute getPort()
		{
			return port;
		}

		public Attribute getRegion()
		{
			return region;
		}
		
	}

	public SearchableAttributeLocation(String userLabel, Location[] locations)
	{
		super(userLabel);
		this.locations = locations != null ? locations : new Location[0];
	}

	public int getLocationsCount()
	{
		return locations.length;
	}

	public Location[] getLocations()
	{
		return locations;
	}

}
