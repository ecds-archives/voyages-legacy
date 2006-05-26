package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class PlaceB extends Dictionary {

	public static final Integer TYPE = new Integer(35);
	public static final String NAME = "PlaceB";
	
	public PlaceB() {
		setType(TYPE);
	}
	
	public static PlaceB loadPortLocation(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (PlaceB)dicts[0];
		} else {
			return null;
		}
		
	}
}
