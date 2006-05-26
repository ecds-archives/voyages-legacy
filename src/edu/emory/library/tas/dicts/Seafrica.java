package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Seafrica extends Dictionary {
	private static final Integer TYPE = new Integer(40);
	private static final String NAME = "Seafrica";
	
	public Seafrica() {
		setType(TYPE);
	}
	
	public static Seafrica loadSeafrica(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Seafrica)dicts[0];
		} else {
			return null;
		}
		
	}
}
