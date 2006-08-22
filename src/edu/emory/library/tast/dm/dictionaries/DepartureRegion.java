package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class DepartureRegion extends Dictionary {
	public static final Integer TYPE = new Integer(7);
	public static final String NAME = "DepartureRegion";
	
	public DepartureRegion() {
		setType(TYPE);
	}
	
	public static DepartureRegion loadDepartureRegion(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (DepartureRegion)dicts[0];
		} else {
			return null;
		}
		
	}
}
