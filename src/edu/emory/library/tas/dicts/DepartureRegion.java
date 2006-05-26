package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class DepartureRegion extends Dictionary {
	public static final Integer TYPE = new Integer(7);
	public static final String NAME = "DepartureRegion";
	
	public DepartureRegion() {
		setType(TYPE);
	}
	
	public static DepartureRegion loadDepartureRegion(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (DepartureRegion)dicts[0];
		} else {
			return null;
		}
		
	}
}
