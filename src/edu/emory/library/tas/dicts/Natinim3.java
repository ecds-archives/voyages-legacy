package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Natinim3 extends Dictionary {
	public static final Integer TYPE = new Integer(27);
	public static final String NAME = "Natinim3";
	
	public Natinim3() {
		setType(TYPE);
	}
	
	public static Natinim3 loadNatinim3(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Natinim3)dicts[0];
		} else {
			return null;
		}
		
	}
}
