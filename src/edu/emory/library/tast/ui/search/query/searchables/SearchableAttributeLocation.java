package edu.emory.library.tast.ui.search.query.searchables;


public class SearchableAttributeLocation extends SearchableAttribute
{

	private Location[] locations;

	public SearchableAttributeLocation(String id, String userLabel, UserCategory userCategory, Location[] locations)
	{
		super(id, userLabel, userCategory);
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
