package edu.emory.library.tas;

public class PlaceA extends Dictionary {

	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "PlaceA";
	
	public PlaceA() {
		setType(TYPE);
	}
	
	public static PlaceA loadPlaceA(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (PlaceA)dicts[0];
		} else {
			return null;
		}
		
	}
}
