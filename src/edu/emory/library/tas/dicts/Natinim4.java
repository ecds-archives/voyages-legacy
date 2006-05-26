package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Natinim4 extends Dictionary {
	private static final Integer TYPE = new Integer(28);
	private static final String NAME = "Natinim3";
	
	public Natinim4() {
		setType(TYPE);
	}
	
	public static Natinim4 loadNatinim4(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Natinim4)dicts[0];
		} else {
			return null;
		}
		
	}
}
