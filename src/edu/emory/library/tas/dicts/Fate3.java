package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Fate3 extends Dictionary {
	private static final Integer TYPE = new Integer(12);
	private static final String NAME = "Fate3";
	
	public Fate3() {
		setType(TYPE);
	}
	
	public static Fate3 loadFate3(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Fate3)dicts[0];
		} else {
			return null;
		}
		
	}
}
