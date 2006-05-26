package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Fate4 extends Dictionary {
	private static final Integer TYPE = new Integer(13);
	private static final String NAME = "Fate4";
	
	public Fate4() {
		setType(TYPE);
	}
	
	public static Fate4 loadFate4(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Fate4)dicts[0];
		} else {
			return null;
		}
		
	}
}
