package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Yearches extends Dictionary {
	public static final Integer TYPE = new Integer(57);
	public static final String NAME = "Yearches";
	
	public Yearches() {
		setType(TYPE);
	}
	
	public static Yearches loadYearches(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Yearches)dicts[0];
		} else {
			return null;
		}
		
	}
}
