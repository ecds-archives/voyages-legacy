package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Year25 extends Dictionary {
	public static final Integer TYPE = new Integer(55);
	public static final String NAME = "Year25";
	
	public Year25() {
		setType(TYPE);
	}
	
	public static Year25 loadYear25(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Year25)dicts[0];
		} else {
			return null;
		}
		
	}
}
