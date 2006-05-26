package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Gc extends Dictionary {
	private static final Integer TYPE = new Integer(17);
	private static final String NAME = "Gc";
	
	public Gc() {
		setType(TYPE);
	}
	
	public static Gc loadGc(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Gc)dicts[0];
		} else {
			return null;
		}
		
	}
}
