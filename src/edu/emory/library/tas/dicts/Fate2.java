package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Fate2 extends Dictionary {
	public static final Integer TYPE = new Integer(11);
	public static final String NAME = "Fate2";
	
	public Fate2() {
		setType(TYPE);
	}
	
	public static Fate2 loadFate2(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Fate2)dicts[0];
		} else {
			return null;
		}
		
	}
}
